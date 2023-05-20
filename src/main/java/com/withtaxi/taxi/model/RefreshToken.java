package com.withtaxi.taxi.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Table(name = "refreshToken")
@Getter
@NoArgsConstructor
public class RefreshToken {


    // 유저키는 사용자의 아이디로 정한다.
    @Id
    @Column(nullable = false)
    private String userKey;

    @Column(nullable = false)
    private String token;

    public RefreshToken updateToken(String token) {
        this.token = token;
        return this;
    }

    @Builder
    public RefreshToken(String userKey, String token) {
        this.userKey = userKey;
        this.token = token;
    }
}
