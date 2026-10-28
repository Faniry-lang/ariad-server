package tools.ariad.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tools.ariad.entity.WebhookDelivery;
import java.util.UUID;
import java.util.Optional;

public interface WebhookDeliveryRepository extends JpaRepository<WebhookDelivery, UUID> {
    Optional<WebhookDelivery> findByGithubDeliveryId(UUID githubDeliveryId);
}
