package tools.ariad.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tools.ariad.entity.PullRequest;
import java.util.UUID;
import java.util.Optional;

public interface PullRequestRepository extends JpaRepository<PullRequest, UUID> {
    Optional<PullRequest> findByRepositoryIdAndGithubPrNumber(UUID repositoryId, Long githubPrNumber);
}
