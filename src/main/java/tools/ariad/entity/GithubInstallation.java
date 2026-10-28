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
@Table(name = "github_installations")
public class GithubInstallation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "organization_id", nullable = false)
    private UUID organizationId;

    @Column(name = "github_installation_id", nullable = false)
    private Long githubInstallationId;

    @Column(name = "github_account_id", nullable = false)
    private Long githubAccountId;

    @Column(name = "account_login", nullable = false, columnDefinition = "text")
    private String accountLogin;

    @Column(name = "account_type", nullable = false, columnDefinition = "text")
    private String accountType;

    @Column(name = "repository_selection", nullable = false, columnDefinition = "text")
    private String repositorySelection;

    @Column(name = "suspended_at", nullable = true, columnDefinition = "timestamptz")
    private Instant suspendedAt;

    @Column(name = "disconnected_at", nullable = true, columnDefinition = "timestamptz")
    private Instant disconnectedAt;

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
