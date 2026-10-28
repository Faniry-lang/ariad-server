package tools.ariad.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tools.ariad.entity.Project;
import java.util.UUID;
import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, UUID> {
    Optional<Project> findByOrganizationIdAndKey(UUID organizationId, String key);
    List<Project> findByOrganizationId(UUID organizationId);
}
