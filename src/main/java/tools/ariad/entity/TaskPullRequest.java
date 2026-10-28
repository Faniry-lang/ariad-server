package tools.ariad.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "task_pull_requests")
@IdClass(TaskPullRequestId.class)
public class TaskPullRequest {

    @Id
    @Column(name = "task_id", nullable = false)
    private UUID taskId;

    @Id
    @Column(name = "pull_request_id", nullable = false)
    private UUID pullRequestId;

    @Column(name = "project_id", nullable = false)
    private UUID projectId;

    @Column(name = "repository_id", nullable = false)
    private UUID repositoryId;

    @Column(name = "created_at", nullable = false, columnDefinition = "timestamptz", updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void initializeTimestamps() {
        Instant now = Instant.now();
        if (createdAt == null) createdAt = now;
    }
}
