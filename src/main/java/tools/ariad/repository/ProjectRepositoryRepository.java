package tools.ariad.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tools.ariad.entity.ProjectRepository;
import tools.ariad.entity.ProjectRepositoryId;
import java.util.UUID;
import java.util.List;

public interface ProjectRepositoryRepository extends JpaRepository<ProjectRepository, ProjectRepositoryId> {
    List<ProjectRepository> findByProjectId(UUID projectId);
    List<ProjectRepository> findByRepositoryId(UUID repositoryId);
}
