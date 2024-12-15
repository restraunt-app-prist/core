package kpi.fict.prist.core.cart.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import kpi.fict.prist.core.cart.entity.CartEntity;

import java.util.Optional;

public interface CartEntityRepository extends MongoRepository<CartEntity, String> {
    Optional<CartEntity> findByUserExternalId(String userExternalId);
}
