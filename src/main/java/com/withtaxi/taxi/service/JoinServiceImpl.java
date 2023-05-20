package com.withtaxi.taxi.service;

import com.withtaxi.taxi.model.User;
import com.withtaxi.taxi.model.dto.UserRequestDto;
import com.withtaxi.taxi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class JoinServiceImpl implements JoinService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(UserRequestDto user) {
        String rawPassword = user.getPassword();
        String encPassword = passwordEncoder.encode(rawPassword);
        user.setPassword(encPassword);
        user.setRole("USER");
        return userRepository.save(user.toEntity());
    }

    @Override
    public boolean checkUserIdDuplicate(String userId) {
        return userRepository.existsByUserId(userId);
    }

    @Override
    public boolean checkNickNameDuplicate(String nickName) {
        return userRepository.existsByNickName(nickName);
    }

    @Override
    public boolean checkEmailDuplicate(String email) {
        return userRepository.existsByEmail(email);
    }
}
