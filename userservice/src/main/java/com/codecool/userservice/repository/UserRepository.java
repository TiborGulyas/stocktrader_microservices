package com.codecool.userservice.repository;


import com.codecool.userservice.model.Trader;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<Trader, Long> {
    Trader findById(long id);
}
