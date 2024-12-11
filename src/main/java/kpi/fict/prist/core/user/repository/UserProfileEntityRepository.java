package kpi.fict.prist.core.user.repository;

import java.util.Optional;
import kpi.fict.prist.core.user.entity.UserProfileEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserProfileEntityRepository extends MongoRepository<UserProfileEntity, String> {

    Optional<UserProfileEntity> findByExternalId(String externalId);

    boolean existsByExternalId(String externalId);

}
