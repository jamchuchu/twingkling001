package com.sparta.twingkling001.cart.repository;

import com.sparta.twingkling001.cart.entity.CartDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartDetailRepository extends JpaRepository<CartDetail, Long> {

    List<CartDetail> getCartDetailsByCartId(Long cartId);
    @Query("UPDATE CartDetail cd SET cd.quantity = :quantity WHERE cd.cartDetailId = :cartDetailId")
    void updateProductQuantity(Long cartDetailId, Long quantity);
    void deleteCartDetailByCartDetailId(Long cartDetailId);

    @Query("UPDATE CartDetail cd SET cd.presentSaleYn = :true WHERE cd.productId = :productId")
    void updateProductSateTrue(Long productId);
    @Query("UPDATE CartDetail cd SET cd.presentSaleYn = :false WHERE cd.productId = :productId")
    void updateProductSateFalse(Long productId);


}
