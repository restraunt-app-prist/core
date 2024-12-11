package kpi.fict.prist.core.config.security;

import static kpi.fict.prist.core.common.Constants.TOKEN_CACHE;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import kpi.fict.prist.core.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
public class OAuth2UserCreationFilter extends OncePerRequestFilter {

    private final CacheManager cacheManager;
    private final UserService userService;

    public OAuth2UserCreationFilter(CacheManager cacheManager, UserService userService) {
        this.cacheManager = cacheManager;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws IOException, ServletException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwtAuthToken) {
            Jwt jwt = jwtAuthToken.getToken();
            String tokenValue = jwtAuthToken.getToken().getTokenValue();
            String externalUserId = jwt.getSubject();

            Cache cache = cacheManager.getCache(TOKEN_CACHE);
            if (cache != null) {
                String cachedToken = cache.get(externalUserId, String.class);
                if (cachedToken == null || !cachedToken.equals(tokenValue)) {
                    if (userService.existsByExternalId(externalUserId)) {
                        userService.updateProfile(jwt);
                        log.info("updating user {}", externalUserId);
                    } else {
                        userService.createProfile(jwt);
                        log.info("creating user {}", externalUserId);
                    }
                    cache.put(externalUserId, tokenValue);
                }
            } else {
                log.warn("Cache 'token_cache' does not exist");
            }

        }
        filterChain.doFilter(request, response);
    }

}
