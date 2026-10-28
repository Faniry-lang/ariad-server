package tools.ariad.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tools.ariad.entity.GithubInstallation;
import java.util.UUID;
import java.util.List;
import java.util.Optional;

public interface GithubInstallationRepository extends JpaRepository<GithubInstallation, UUID> {
    Optional<GithubInstallation> findByGithubInstallationId(Long githubInstallationId);
    List<GithubInstallation> findByOrganizationId(UUID organizationId);
}
