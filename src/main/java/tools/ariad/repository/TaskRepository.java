package tools.ariad.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tools.ariad.entity.Task;
import java.util.UUID;
import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, UUID> {
    Optional<Task> findByOrganizationIdAndId(UUID organizationId, UUID id);
    Optional<Task> findByProjectIdAndNumber(UUID projectId, Long number);
    List<Task> findByOrganizationIdAndAssigneeId(UUID organizationId, UUID assigneeId);
}
