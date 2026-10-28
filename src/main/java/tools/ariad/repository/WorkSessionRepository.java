package tools.ariad.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tools.ariad.entity.WorkSession;
import java.util.UUID;
import java.util.List;
import java.util.Optional;

public interface WorkSessionRepository extends JpaRepository<WorkSession, UUID> {
    Optional<WorkSession> findByUserIdAndEndedAtIsNull(UUID userId);
    Optional<WorkSession> findByUserIdAndIdempotencyKey(UUID userId, UUID idempotencyKey);
    List<WorkSession> findByOrganizationIdAndTaskId(UUID organizationId, UUID taskId);
}
