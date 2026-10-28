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
@Table(name = "commits")
public class Commit {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "repository_id", nullable = false)
    private UUID repositoryId;

    @Column(name = "sha", nullable = false, columnDefinition = "text")
    private String sha;

    @Column(name = "message", nullable = false, columnDefinition = "text")
    private String message;

    @Column(name = "author_id", nullable = true)
    private UUID authorId;

    @Column(name = "author_name", nullable = true, columnDefinition = "text")
    private String authorName;

    @Column(name = "author_email", nullable = true, columnDefinition = "text")
    private String authorEmail;

    @Column(name = "authored_at", nullable = true, columnDefinition = "timestamptz")
    private Instant authoredAt;

    @Column(name = "committed_at", nullable = false, columnDefinition = "timestamptz")
    private Instant committedAt;

    @Column(name = "created_at", nullable = false, columnDefinition = "timestamptz", updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void initializeTimestamps() {
        Instant now = Instant.now();
        if (createdAt == null) createdAt = now;
    }
}
