package com.shop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "cart_item")
public class CartItem extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "cart_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;//하나의 장바구니에는 여러 개의 상품을 담을 수 있도록 ManyToOne 어노테이션으로 다대일 관계 매핑한다.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;//장바구니에 담을 상품정보를 알아야 하므로 상품 엔티티를 매핑해준다. 하나의 상품은 여러 장바구니의 상품품으로 담길 수
    //있으므로 마찬가지로 @ManyToOne 어노테이션 다대일 관계 매핑한다.

    private int count; //같은 상품을 장바구니에 몇 개 담을지 지정한다.

    public static CartItem createCartItem(Cart cart, Item item, int count){
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setItem(item);
        cartItem.setCount(count);
        return cartItem;
    }

    public void addCount(int count){//장바구니에 기존에 담겨 있는 상품인데 해당 상품을 추가로 장바구니에 담을 때 기존 수량에 현재 담을 수량을 더해줄 메소드
        this.count += count;
    }
}
