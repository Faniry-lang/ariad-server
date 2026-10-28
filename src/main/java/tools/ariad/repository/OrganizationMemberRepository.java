package tools.ariad.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tools.ariad.entity.OrganizationMember;
import tools.ariad.entity.OrganizationMemberId;
import java.util.UUID;
import java.util.List;

public interface OrganizationMemberRepository extends JpaRepository<OrganizationMember, OrganizationMemberId> {
    List<OrganizationMember> findByUserId(UUID userId);
    List<OrganizationMember> findByOrganizationId(UUID organizationId);
}
