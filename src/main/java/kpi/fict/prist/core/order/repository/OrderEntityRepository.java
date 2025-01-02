package kpi.fict.prist.core.order.repository;

import java.util.Optional;
import kpi.fict.prist.core.order.entity.OrderEntity;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderEntityRepository extends MongoRepository<OrderEntity, String> {

    List<OrderEntity> findByUserExternalId(String userExternalId);

    Optional<OrderEntity> findByPaymentId(String paymentId);

}
