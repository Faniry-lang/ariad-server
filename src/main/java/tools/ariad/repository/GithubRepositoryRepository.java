package tools.ariad.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tools.ariad.entity.GithubRepository;
import java.util.UUID;
import java.util.List;
import java.util.Optional;

public interface GithubRepositoryRepository extends JpaRepository<GithubRepository, UUID> {
    Optional<GithubRepository> findByGithubRepoId(Long githubRepoId);
    List<GithubRepository> findByOrganizationId(UUID organizationId);
}
