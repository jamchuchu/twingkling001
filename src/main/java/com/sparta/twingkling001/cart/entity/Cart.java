package com.sparta.twingkling001.cart.entity;

import com.sparta.twingkling001.cart.dto.response.CartDetailRespDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;
    private Long memberId;
    private Boolean deletedYn;
    @OneToMany
    @JoinColumn(name = "cart_id", referencedColumnName = "cartId")
    private List<CartDetail> details = new ArrayList<>();

    public void setDeletedYn(Boolean deletedYn) {
        this.deletedYn = deletedYn;
    }
}
