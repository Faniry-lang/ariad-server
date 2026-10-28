package tools.ariad.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "webhook_deliveries")
public class WebhookDelivery {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "github_delivery_id", nullable = false)
    private UUID githubDeliveryId;

    @Column(name = "github_installation_id", nullable = true)
    private Long githubInstallationId;

    @Column(name = "github_repo_id", nullable = true)
    private Long githubRepoId;

    @Column(name = "event_type", nullable = false, columnDefinition = "text")
    private String eventType;

    @Column(name = "action", nullable = true, columnDefinition = "text")
    private String action;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "payload", nullable = false, columnDefinition = "jsonb")
    private String payload;

    @Column(name = "received_at", nullable = false, columnDefinition = "timestamptz")
    private Instant receivedAt;

    @Column(name = "status", nullable = false, columnDefinition = "text")
    private String status = "pending";

    @Column(name = "attempt_count", nullable = false)
    private Integer attemptCount = 0;

    @Column(name = "next_attempt_at", nullable = true, columnDefinition = "timestamptz")
    private Instant nextAttemptAt;

    @Column(name = "locked_at", nullable = true, columnDefinition = "timestamptz")
    private Instant lockedAt;

    @Column(name = "processed_at", nullable = true, columnDefinition = "timestamptz")
    private Instant processedAt;

    @Column(name = "last_error", nullable = true, columnDefinition = "text")
    private String lastError;

    @PrePersist
    protected void initializeTimestamps() {
        Instant now = Instant.now();
        if (receivedAt == null) receivedAt = now;
    }
}
