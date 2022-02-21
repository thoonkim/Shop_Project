package com.shop.dto;

import lombok.Getter;
import lombok.Setter;

/*장바구니에 들어있는 상품을 조회하는 기능 Dto*/
@Getter @Setter
public class CartDetailDto {
    private Long cartItemId; //장바구니 상품 아이디

    private String itemNm; //상품명

    private int price; //상품 금액

    private int count; //수량

    private String imgUrl; //상품 이미지 경로

    public CartDetailDto(Long cartItemId, String ItemNm, int price, int count, String imgUrl){//장바구니페이지에 전달할 데이터를 생성자 파라미터로 만듦
        this.cartItemId = cartItemId;
        this.itemNm = itemNm;
        this.price = price;
        this.count = count;
        this.imgUrl = imgUrl;
    }
}
