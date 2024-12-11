package kpi.fict.prist.core.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArticlesController {

    @GetMapping("articles")
    public String[] getArticles(@AuthenticationPrincipal Jwt jwt) {
        return new String[]{"Article 1", "Article 2", "Article 3"};
    }

}
