package com.pulserun.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByProviderAndProviderId(String Provider, String ProviderId);

    boolean existsByEmail(String email);

    boolean existsByProviderAndProviderId(String Provider, String ProviderId);

    boolean existsByNickname(String nickname);
}
