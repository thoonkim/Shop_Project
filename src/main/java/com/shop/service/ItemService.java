package com.shop.service;

import com.shop.dto.ItemFormDto;
import com.shop.dto.ItemImgDto;
import com.shop.dto.ItemSearchDto;
import com.shop.entity.Item;
import com.shop.entity.ItemImg;
import com.shop.repository.ItemImgRepository;
import com.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

/*상품을 등록하는 클래스*/
@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {
    
    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;
    private final ItemImgRepository itemImgRepository;
    
    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception{
        /*상품 등록 폼으로부터 입력 받은 데이터를 이용해 item 객체를 생성한다.*/
        //상품 등록
        Item item = itemFormDto.createItem();
        /*상품 데이터를 저장한다.*/
        itemRepository.save(item);
        
        //이미지 등록
        for(int i = 0; i<itemImgFileList.size(); i++){
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);
            
            /*첫 번째 이미지 일 경우 대표 상품 이미지 여부 값을 Y로 세팅하고 나머지 상품은 N으로 설정한다.*/
            if(i == 0)
                itemImg.setRepimgYn("Y");
            else
                itemImg.setRepimgYn("N");
            /*상품이미지 정보를을 저장한다.*/
            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
        }
        
        return item.getId();
    }
    /*상품 데이터를 읽어오는 트랜잭션을 읽기 전용으로 설정함 JPA가 더티체킹(변경감지)를 수행하지 않아서 성능이 향상 됨*/
    @Transactional(readOnly = true)
    public ItemFormDto getItemDtl(Long itemId){

        /*해당 상품의 이미지를 조회하고 등록순으로 가지고 오기 위해 아이디 오름차순 함*/
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        List<ItemImgDto> itemImgDtoList = new ArrayList<>();
        /*조회한 itemImg엔티티를 itemImgDto 객체로 만들어 리스트에 추가함*/
        for (ItemImg itemImg : itemImgList) {
            ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
            itemImgDtoList.add(itemImgDto);
        }
        /*상품의 아이디를 통해 상품 엔티티를 조회한다. 존재하지 않을 때는 EntityNotFoundException 을 발생시킨*/
        Item item = itemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);
        ItemFormDto itemFormDto = ItemFormDto.of(item);
        itemFormDto.setItemImgDtoList(itemImgDtoList);
        return itemFormDto;
    }

    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception{

        //상품 수정
        Item item = itemRepository.findById(itemFormDto.getId())
                .orElseThrow(EntityNotFoundException::new);
        item.updateItem(itemFormDto);

        List<Long> itemImgIds = itemFormDto.getItemImgIds();

        //이미지 등록
        for (int i = 0; i<itemImgFileList.size(); i++){
            itemImgService.updateItemImg(itemImgIds.get(i),
                    itemImgFileList.get(i));
        }
        return item.getId();
    }
    
    /*ItemService 클래스에 상품 조회 조건과 페이지 정보를 파라미터로 받아 상품 데이터를 조회하는 getAdmin...메소드를 추가한다.
    * 데이터 수정이 일어나지 않으므로 최적화를위해 @Trasactional....어노테이션을 설정함*/
    @Transactional(readOnly = true)
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getAdminItemPage(itemSearchDto, pageable);
    }
}
