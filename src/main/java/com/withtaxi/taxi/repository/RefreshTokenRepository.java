package com.withtaxi.taxi.repository;

import com.withtaxi.taxi.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    RefreshToken findByUserKey(String userKey);
}
