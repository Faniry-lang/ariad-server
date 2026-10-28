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
@Table(name = "pull_requests")
public class PullRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "repository_id", nullable = false)
    private UUID repositoryId;

    @Column(name = "github_pr_number", nullable = false)
    private Long githubPrNumber;

    @Column(name = "title", nullable = false, columnDefinition = "text")
    private String title;

    @Column(name = "author_id", nullable = true)
    private UUID authorId;

    @Column(name = "state", nullable = false, columnDefinition = "text")
    private String state;

    @Column(name = "draft", nullable = false)
    private boolean draft = false;

    @Column(name = "head_ref", nullable = false, columnDefinition = "text")
    private String headRef;

    @Column(name = "base_ref", nullable = false, columnDefinition = "text")
    private String baseRef;

    @Column(name = "created_at", nullable = false, columnDefinition = "timestamptz")
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "timestamptz")
    private Instant updatedAt;

    @Column(name = "closed_at", nullable = true, columnDefinition = "timestamptz")
    private Instant closedAt;

    @Column(name = "merged_at", nullable = true, columnDefinition = "timestamptz")
    private Instant mergedAt;

    @Column(name = "synced_at", nullable = false, columnDefinition = "timestamptz")
    private Instant syncedAt;

    @PrePersist
    protected void initializeTimestamps() {
        Instant now = Instant.now();
        if (syncedAt == null) syncedAt = now;
    }
}
