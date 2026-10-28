package tools.ariad;

import jakarta.persistence.EntityManager;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tools.ariad.entity.*;
import tools.ariad.repository.TaskRepository;
import tools.ariad.repository.WorkSessionRepository;
import tools.ariad.repository.WebhookDeliveryRepository;
import tools.ariad.repository.TaskCommitRepository;
import tools.ariad.repository.TaskPullRequestRepository;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

/** Runs against a dedicated PostgreSQL database initialized with schema/ariad.sql. */
@SpringBootTest
@Transactional
class PersistenceTests {
    @PersistenceContext
    private EntityManager em;
    @Autowired
    private TaskRepository tasks;
    @Autowired
    private WorkSessionRepository sessions;
    @Autowired
    private WebhookDeliveryRepository deliveries;
    @Autowired
    private TaskCommitRepository taskCommits;
    @Autowired
    private TaskPullRequestRepository taskPullRequests;

    @Test
    void allEntitiesRoundTripIncludingCompositeIdsJsonAndUpstreamTimestamps() {
        Fixture f = fixture();

        GithubAccount account = new GithubAccount();
        account.setGithubUserId(100L);
        account.setLogin("contributor");
        account.setUserId(f.user.getId());
        insert(account);

        GithubInstallation installation = new GithubInstallation();
        installation.setOrganizationId(f.org.getId());
        installation.setGithubInstallationId(200L);
        installation.setGithubAccountId(300L);
        installation.setAccountLogin("team");
        installation.setAccountType("Organization");
        installation.setRepositorySelection("selected");
        insert(installation);

        GithubRepository repo = new GithubRepository();
        repo.setOrganizationId(f.org.getId());
        repo.setInstallationId(installation.getId());
        repo.setGithubRepoId(400L);
        repo.setOwner("team");
        repo.setName("app");
        insert(repo);

        ProjectRepository mapping = new ProjectRepository();
        mapping.setProjectId(f.project.getId());
        mapping.setRepositoryId(repo.getId());
        mapping.setOrganizationId(f.org.getId());
        insert(mapping);

        Instant upstreamTime = Instant.parse("2026-01-01T10:00:00Z");
        Commit commit = new Commit();
        commit.setRepositoryId(repo.getId());
        commit.setSha("a".repeat(40));
        commit.setMessage("APP-1 Implement persistence");
        commit.setAuthorId(account.getId());
        commit.setCommittedAt(upstreamTime);
        insert(commit);

        PullRequest pr = new PullRequest();
        pr.setRepositoryId(repo.getId());
        pr.setGithubPrNumber(1L);
        pr.setTitle("APP-1 Implement persistence");
        pr.setAuthorId(account.getId());
        pr.setState("open");
        pr.setHeadRef("feature/persistence");
        pr.setBaseRef("main");
        pr.setCreatedAt(upstreamTime);
        pr.setUpdatedAt(upstreamTime);
        insert(pr);

        TaskCommit taskCommit = new TaskCommit();
        taskCommit.setTaskId(f.task.getId());
        taskCommit.setCommitId(commit.getId());
        taskCommit.setProjectId(f.project.getId());
        taskCommit.setRepositoryId(repo.getId());
        taskCommits.saveAndFlush(taskCommit);

        TaskPullRequest taskPr = new TaskPullRequest();
        taskPr.setTaskId(f.task.getId());
        taskPr.setPullRequestId(pr.getId());
        taskPr.setProjectId(f.project.getId());
        taskPr.setRepositoryId(repo.getId());
        taskPullRequests.saveAndFlush(taskPr);

        WorkSession session = session(f);
        sessions.saveAndFlush(session);

        WebhookDelivery delivery = new WebhookDelivery();
        delivery.setGithubDeliveryId(UUID.randomUUID());
        delivery.setEventType("pull_request");
        delivery.setPayload("{\"action\":\"opened\",\"number\":1}");
        deliveries.saveAndFlush(delivery);
        em.clear();

        assertThat(tasks.findByOrganizationIdAndId(f.org.getId(), f.task.getId()))
                .hasValueSatisfying(task -> {
                    assertThat(task.getStatus()).isEqualTo("todo");
                    assertThat(task.getVersion()).isZero();
                    assertThat(task.getCreatedAt()).isNotNull();
                });
        assertThat(em.find(OrganizationMember.class,
                new OrganizationMemberId(f.org.getId(), f.user.getId())).getRole()).isEqualTo("member");
        assertThat(em.find(ProjectMember.class,
                new ProjectMemberId(f.project.getId(), f.user.getId()))).isNotNull();
        assertThat(em.find(ProjectRepository.class,
                new ProjectRepositoryId(f.project.getId(), repo.getId()))).isNotNull();
        assertThat(taskCommits.findById(new TaskCommitId(f.task.getId(), commit.getId()))).isPresent();
        assertThat(taskPullRequests.findById(new TaskPullRequestId(f.task.getId(), pr.getId()))).isPresent();
        assertThat(sessions.findByUserIdAndEndedAtIsNull(f.user.getId())).isPresent();
        assertThat(deliveries.findByGithubDeliveryId(delivery.getGithubDeliveryId())).isPresent();
        assertThat(em.createNativeQuery("select payload ->> 'action' from webhook_deliveries where id = :id", String.class)
                .setParameter("id", delivery.getId()).getSingleResult()).isEqualTo("opened");

        PullRequest loadedPr = em.find(PullRequest.class, pr.getId());
        loadedPr.setTitle("Updated title");
        em.flush();
        em.clear();
        assertThat(em.find(PullRequest.class, pr.getId()).getUpdatedAt()).isEqualTo(upstreamTime);
    }

    @Test
    void databaseRejectsTaskFromAnotherOrganization() {
        Fixture f = fixture();
        Organization other = new Organization();
        other.setName("Other");
        other.setSlug("other");
        insert(other);
        f.task.setOrganizationId(other.getId());
        f.task.setAssigneeId(null);
        assertThatThrownBy(em::flush).isInstanceOf(PersistenceException.class);
    }

    @Test
    void databaseRejectsTwoActiveTimersForOneUser() {
        Fixture f = fixture();
        insert(session(f));
        WorkSession second = session(f);
        em.persist(second);
        assertThatThrownBy(em::flush).isInstanceOf(PersistenceException.class);
    }

    @Test
    void taskVersionRejectsStaleUpdates() {
        Fixture f = fixture();
        em.createNativeQuery("update tasks set version = version + 1 where id = :id")
                .setParameter("id", f.task.getId()).executeUpdate();
        f.task.setTitle("Stale update");
        assertThatThrownBy(em::flush).isInstanceOf(OptimisticLockException.class);
    }

    @Test
    void sessionVersionRejectsStaleUpdates() {
        Fixture f = fixture();
        WorkSession session = session(f);
        insert(session);
        em.createNativeQuery("update work_sessions set version = version + 1 where id = :id")
                .setParameter("id", session.getId()).executeUpdate();
        session.setEndedAt(Instant.now());
        assertThatThrownBy(em::flush).isInstanceOf(OptimisticLockException.class);
    }

    private Fixture fixture() {
        User user = new User();
        user.setUsername("tester");
        user.setEmail("tester@example.com");
        insert(user);
        Organization org = new Organization();
        org.setName("Team");
        org.setSlug("team");
        insert(org);
        OrganizationMember member = new OrganizationMember();
        member.setOrganizationId(org.getId());
        member.setUserId(user.getId());
        insert(member);
        Project project = new Project();
        project.setOrganizationId(org.getId());
        project.setName("Application");
        project.setKey("APP");
        insert(project);
        ProjectMember projectMember = new ProjectMember();
        projectMember.setOrganizationId(org.getId());
        projectMember.setProjectId(project.getId());
        projectMember.setUserId(user.getId());
        insert(projectMember);
        Task task = new Task();
        task.setOrganizationId(org.getId());
        task.setProjectId(project.getId());
        task.setNumber(1L);
        task.setTitle("Implement persistence");
        task.setCreatedBy(user.getId());
        task.setAssigneeId(user.getId());
        tasks.saveAndFlush(task);
        return new Fixture(user, org, project, task);
    }

    private WorkSession session(Fixture f) {
        WorkSession session = new WorkSession();
        session.setOrganizationId(f.org.getId());
        session.setTaskId(f.task.getId());
        session.setUserId(f.user.getId());
        session.setSource("vscode");
        session.setIdempotencyKey(UUID.randomUUID());
        session.setStartedAt(Instant.now());
        return session;
    }

    private void insert(Object entity) {
        em.persist(entity);
        em.flush();
    }

    private record Fixture(User user, Organization org, Project project, Task task) {}
}
