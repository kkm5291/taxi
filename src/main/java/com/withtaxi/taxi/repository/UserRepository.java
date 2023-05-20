package com.withtaxi.taxi.repository;


import com.withtaxi.taxi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;



public interface UserRepository extends JpaRepository<User, String> {

    /***
     * userId 찾는 db쿼리
     * @param userId
     * @return
     */
    User findByUserId(String userId);

    User findByNameAndEmail(String name, String email);

    void deleteByUserId(String userId);

    boolean existsByUserId(String userId);

    boolean existsByNickName(String nickName);

    boolean existsByEmail(String email);
}

