package kpi.fict.prist.core.payment.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import kpi.fict.prist.core.payment.entity.PaymentEntity;

@Repository
public interface PaymentEntityRepository extends MongoRepository<PaymentEntity, String> {
    Optional<PaymentEntity> findByPaymentId(String paymentId);
}