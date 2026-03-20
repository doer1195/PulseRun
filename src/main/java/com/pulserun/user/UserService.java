package com.pulserun.user;

import com.pulserun.utils.JwtUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.Base64;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    @Value("${oauth2.google.client-id}")
    private String clientId;

    @Value("${oauth2.google.client-secret}")
    private String clientSecret;

    @Value("${oauth2.google.redirect-uri}")
    private String redirectUri;

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final RestClient restClient = RestClient.create();

    public String processSocialLogin(String provider, String code) {
        Map<String, Object> googleResponse = getGoogleTokenResponse(code);
        String idToken = (String) googleResponse.get("id_token");

        String[] jwtParts = idToken.split("\\.");
        String payloadBase64 = jwtParts[1];

        String decodedPayload = new String(Base64.getUrlDecoder().decode(payloadBase64));

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode payloadJson = objectMapper.readTree(decodedPayload);

            String providerId = payloadJson.get("sub").asString();
            String email = payloadJson.get("email").asString();
            String name = payloadJson.get("name").asString();

            log.info("고유번호: {}, 이메일: {}, 이름: {}", providerId, email, name);

            User user = userRepository.findByProviderAndProviderId(provider, providerId)
                    .orElseGet(() -> {
                        log.info("new user added");
                        User newUser = User.builder()
                                .provider(provider)
                                .providerId(providerId)
                                .email(email)
                                .nickname(name)
                                .build();
                        return userRepository.save(newUser);
                    });
            String ourToken = jwtUtil.createToken(user.getId());

            log.info("자체 JWT 발급 완료: {}", ourToken);

            return ourToken;

        } catch (Exception e) {
            throw new RuntimeException("token error", e);
        }
    }

    private Map<String, Object> getGoogleTokenResponse(String code) {
        String tokenUri = "https://oauth2.googleapis.com/token";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);

        return restClient.post()
                .uri(tokenUri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(params)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }
}




