package com.sparta.twingkling001.cart.repository;

import com.sparta.twingkling001.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    boolean existsCartByMemberId(Long memberId);
    Cart findCartByMemberId(Long memberId);
}
