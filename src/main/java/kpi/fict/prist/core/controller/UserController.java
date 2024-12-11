package kpi.fict.prist.core.controller;

import kpi.fict.prist.core.user.dto.UserResponse;
import kpi.fict.prist.core.user.service.UserMapper;
import kpi.fict.prist.core.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("user")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("current")
    UserResponse currentUser(@AuthenticationPrincipal Jwt jwt) {
        String externalId = jwt.getSubject();
        return userService.findByExternalId(externalId)
            .map(userMapper::toUserResponse)
            .orElseThrow(() -> new IllegalStateException("User with externalId " + externalId + " not found"));
    }

}
