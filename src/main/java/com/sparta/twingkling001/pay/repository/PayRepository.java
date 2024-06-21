package com.sparta.twingkling001.pay.repository;

import com.sparta.twingkling001.pay.entity.Pay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PayRepository extends JpaRepository<Pay, Long> {
}
