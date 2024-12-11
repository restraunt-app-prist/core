package kpi.fict.prist.core.user.service;

import java.util.Optional;
import kpi.fict.prist.core.user.entity.UserProfileEntity;
import kpi.fict.prist.core.user.repository.UserProfileEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserProfileEntityRepository userRepository;

    public boolean existsByExternalId(String externalId) {
        return userRepository.existsByExternalId(externalId);
    }

    public Optional<UserProfileEntity> findByExternalId(String externalId) {
        return userRepository.findByExternalId(externalId);
    }

    public UserProfileEntity updateProfile(Jwt jwt) {
        String externalId = jwt.getSubject();
        UserProfileEntity user = userRepository.findByExternalId(externalId)
            .orElseThrow(() -> new IllegalStateException("User with externalId " + externalId + " not found"));
        user.setFirstName(jwt.getClaimAsString("given_name"));
        user.setLastName(jwt.getClaimAsString("family_name"));
        user.setEmail(jwt.getClaimAsString("email"));
        user.setPictureUrl(jwt.getClaimAsString("picture"));
        return userRepository.save(user);
    }

    public UserProfileEntity createProfile(Jwt jwt) {
        UserProfileEntity userProfile = UserProfileEntity.builder()
            .externalId(jwt.getSubject())
            .firstName(jwt.getClaimAsString("given_name"))
            .lastName(jwt.getClaimAsString("family_name"))
            .email(jwt.getClaimAsString("email"))
            .pictureUrl(jwt.getClaimAsString("picture"))
            .build();
        return userRepository.save(userProfile);
    }

    public UserProfileEntity setPhoneNumber(String externalId, String phoneNumber) {
        UserProfileEntity userProfile = userRepository.findByExternalId(externalId)
            .orElseThrow(() -> new IllegalStateException("User with externalId " + externalId + " not found"));
        userProfile.setPhoneNumber(phoneNumber);
        return userRepository.save(userProfile);
    }
}
