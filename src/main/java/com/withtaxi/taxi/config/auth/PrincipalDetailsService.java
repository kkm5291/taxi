package com.withtaxi.taxi.config.auth;

import com.withtaxi.taxi.model.User;
import com.withtaxi.taxi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // 아이디 가져오는 것 (메서드 이름만 Username)

    /***
     * 로그인 메서드
     * @param username the username identifying the user whose data is required.
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userEntity = userRepository.findByUserId(username);
        if (userEntity != null) {
            return new PrincipalDetails(userEntity);
        }
        return null;
    }


}
