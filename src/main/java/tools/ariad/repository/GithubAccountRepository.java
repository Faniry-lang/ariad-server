package tools.ariad.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tools.ariad.entity.GithubAccount;
import java.util.UUID;
import java.util.Optional;

public interface GithubAccountRepository extends JpaRepository<GithubAccount, UUID> {
    Optional<GithubAccount> findByGithubUserId(Long githubUserId);
    Optional<GithubAccount> findByUserId(UUID userId);
}
