package com.Service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsernameEquals(String username);

    @Transactional
    @Modifying
    @Query("update User u set u.username = ?1 where u.username = ?2")
    int updateUsernameByUsernameEquals(String username, String username1);
    
}