package com.withtaxi.taxi.config.oauth.provider;

public interface OAuth2UserInfo {
    /*
        구글의 경우 getAttributes에서 sub가 providerId
        페이스북은 getAttributes에서
     */

    String getProviderId();
    String getProvider();
    String getEmail();
    String getName();
}