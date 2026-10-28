package tools.ariad.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tools.ariad.entity.Commit;
import java.util.UUID;
import java.util.Optional;

public interface CommitRepository extends JpaRepository<Commit, UUID> {
    Optional<Commit> findByRepositoryIdAndSha(UUID repositoryId, String sha);
}
