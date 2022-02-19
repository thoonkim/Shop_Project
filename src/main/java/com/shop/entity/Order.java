package com.shop.entity;

import com.shop.constant.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")//정렬할 때 사용하는 "order" 키워드가 있기 때문에 Order 엔티티에 매핑되는 테이블로 "orders"를 지정한다.
@Getter
@Setter
public class Order extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime orderDate; //주문일

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; //주문상태

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)//부모 엔티티의 영속성 상태 변화를 자식 엔티티에 모두 전이하는
    // CascadeTypeAll 옵션을 설정
    // orphanRemoval 고아 객체 제거 어노테이션 설정 사용
    private List<OrderItem> orderItems = new ArrayList<>();

//    private LocalDateTime regTime;
//
//    private LocalDateTime updateTime;

    /*주문 상ㅍ훔 객체 이용해 주문 객체 만드는 메소드*/
    public void addOrderItem(OrderItem orderItem){//items에 주문 상품 정보들을 담는다.
        orderItems.add(orderItem);
        orderItem.setOrder(this);//Order 와 OrderItem 엔티티가 양방향 참조 관계 이므로, orderItem 객체에도 order 객체를 세팅한다.
    }

    public static Order createOrder(Member member, List<OrderItem> orderItemList){
        Order order = new Order();
        order.setMember(member);//주문 회원정보세팅
        for(OrderItem orderItem : orderItemList){/*상품 페이지에선 1개의 상품을 주문하지만, 장바구니 페이지에서는 한번에 여러 개의 
        상품을 주문할 수 있다.따라서 여러 개의 주문 사품을 담을 수 있도록 리스트 형태로 파라미터 값을 받으며 주문 객체에 orderItem 객체를 추가한다.*/
            order.addOrderItem(orderItem);
        }
        order.setOrderStatus(OrderStatus.ORDER);//주문상태 ORDER세팅
        order.setOrderDate(LocalDateTime.now());//현재 시간을 주문시간으로 세팅
        return order;
    }

    public int getTotalPrice(){//총주문금액 메소드
        int totalPrice = 0;
        for(OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalprice();
        }
        return totalPrice;
    }
}
