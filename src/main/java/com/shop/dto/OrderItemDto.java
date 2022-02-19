package com.shop.dto;

import com.shop.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;
/*주문 이력 조회하기용*/
@Getter @Setter
public class OrderItemDto {

    public OrderItemDto(OrderItem orderItem, String imgUrl){//OrderItemDtoi 클래스의 생성자로 orderitem객체와 이미지 경로를 파라미터로
        //받아와 멤버 변수 값을 세팅합니다.
        this.itemNm = orderItem.getItem().getItemNm();
        this.count = orderItem.getCount();
        this.orderPrice = orderItem.getOrderPrice();
        this.imgUrl = imgUrl;
    }

    private String itemNm; //상품명

    private int count; //주문수량

    private int orderPrice;//주문금액
    
    private String imgUrl;//상품이미지경로
}
