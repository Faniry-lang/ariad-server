package tools.ariad.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tools.ariad.entity.ProjectMember;
import tools.ariad.entity.ProjectMemberId;
import java.util.UUID;
import java.util.List;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, ProjectMemberId> {
    List<ProjectMember> findByProjectId(UUID projectId);
    List<ProjectMember> findByOrganizationIdAndUserId(UUID organizationId, UUID userId);
}
