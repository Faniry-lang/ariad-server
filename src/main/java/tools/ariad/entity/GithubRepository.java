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
@Table(name = "repositories")
public class GithubRepository {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "organization_id", nullable = false)
    private UUID organizationId;

    @Column(name = "installation_id", nullable = false)
    private UUID installationId;

    @Column(name = "github_repo_id", nullable = false)
    private Long githubRepoId;

    @Column(name = "owner", nullable = false, columnDefinition = "text")
    private String owner;

    @Column(name = "name", nullable = false, columnDefinition = "text")
    private String name;

    @Column(name = "default_branch", nullable = true, columnDefinition = "text")
    private String defaultBranch;

    @Column(name = "disconnected_at", nullable = true, columnDefinition = "timestamptz")
    private Instant disconnectedAt;

    @Column(name = "last_synced_at", nullable = true, columnDefinition = "timestamptz")
    private Instant lastSyncedAt;

    @Column(name = "created_at", nullable = false, columnDefinition = "timestamptz", updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "timestamptz")
    private Instant updatedAt;

    @PrePersist
    protected void initializeTimestamps() {
        Instant now = Instant.now();
        if (createdAt == null) createdAt = now;
        if (updatedAt == null) updatedAt = now;
    }

    @PreUpdate
    protected void updateTimestamp() {
        updatedAt = Instant.now();
    }
}
