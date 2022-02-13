package com.shop.repository;

import com.shop.entity.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/*상품의 이미지 정보를 저장하기 위한 JpaRepository를 상송받는 ItemImgRepository 인터페이스를 만든다.*/
public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {

    List<ItemImg> findByItemIdOrderByIdAsc(Long itemId);

}
