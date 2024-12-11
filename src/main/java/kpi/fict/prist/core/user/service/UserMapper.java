package kpi.fict.prist.core.user.service;

import kpi.fict.prist.core.user.dto.UserResponse;
import kpi.fict.prist.core.user.entity.UserProfileEntity;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {

    public UserResponse toUserResponse(UserProfileEntity entity) {
        return UserResponse.builder()
            .id(entity.getId())
            .externalId(entity.getExternalId())
            .firstName(entity.getFirstName())
            .lastName(entity.getLastName())
            .email(entity.getEmail())
            .pictureUrl(entity.getPictureUrl())
            .creationDate(entity.getCreationDate())
            .lastUpdateDate(entity.getLastUpdateDate())
            .build();
    }

}
