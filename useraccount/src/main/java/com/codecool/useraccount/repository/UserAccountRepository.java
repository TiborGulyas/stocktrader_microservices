package com.codecool.useraccount.repository;

import com.codecool.useraccount.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    UserAccount findByUsername(String userName);

    UserAccount findByNickName(String nickname);
}

