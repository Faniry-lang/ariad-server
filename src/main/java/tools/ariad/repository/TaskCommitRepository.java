package tools.ariad.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tools.ariad.entity.TaskCommit;
import tools.ariad.entity.TaskCommitId;
import java.util.UUID;
import java.util.List;

public interface TaskCommitRepository extends JpaRepository<TaskCommit, TaskCommitId> {
    List<TaskCommit> findByTaskId(UUID taskId);
}
