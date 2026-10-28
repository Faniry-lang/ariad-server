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
@Table(name = "organization_members")
@IdClass(OrganizationMemberId.class)
public class OrganizationMember {

    @Id
    @Column(name = "organization_id", nullable = false)
    private UUID organizationId;

    @Id
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "role", nullable = false, columnDefinition = "text")
    private String role = "member";

    @Column(name = "joined_at", nullable = false, columnDefinition = "timestamptz")
    private Instant joinedAt;

    @PrePersist
    protected void initializeTimestamps() {
        Instant now = Instant.now();
        if (joinedAt == null) joinedAt = now;
    }
}
