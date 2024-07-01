package com.sparta.twingkling001.cart.repository;

import com.sparta.twingkling001.cart.entity.CartDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartDetailRepository extends JpaRepository<CartDetail, Long> {

    List<CartDetail> getCartDetailsByCartId(Long cartId);
    void deleteCartDetailByCartDetailId(Long cartDetailId);
    List<CartDetail> getCartDetailByProductId(Long productId);

}
