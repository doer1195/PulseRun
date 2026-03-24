package com.pulserun.user.controller;

import com.pulserun.user.service.UserService;
import com.pulserun.user.dto.OauthLoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/login/{provider}")
    public ResponseEntity<?> socialLogin(
            @PathVariable String provider,
            @RequestBody OauthLoginRequest request
    ) {
        String token = userService.processSocialLogin(provider, request.code());
        return ResponseEntity.ok(token);
    }
}
