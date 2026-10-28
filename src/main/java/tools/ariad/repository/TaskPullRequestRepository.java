package tools.ariad.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tools.ariad.entity.TaskPullRequest;
import tools.ariad.entity.TaskPullRequestId;
import java.util.UUID;
import java.util.List;

public interface TaskPullRequestRepository extends JpaRepository<TaskPullRequest, TaskPullRequestId> {
    List<TaskPullRequest> findByTaskId(UUID taskId);
}
