package com.shop.controller;

import com.shop.dto.ItemFormDto;
import com.shop.dto.ItemSearchDto;
import com.shop.entity.Item;
import com.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.jws.WebParam;
import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping(value = "/admin/item/new")
    public String itemForm(Model model){
        model.addAttribute("itemFormDto", new ItemFormDto());
        return "item/itemForm";
    }

    @PostMapping(value = "/admin/item/new")
    public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, Model model, @RequestParam("itemImgFile")
            List<MultipartFile> itemImgFileList){
        /*상품 등록 시 필수 값이 없다면 다시 상품 등록 페이지로 전환된다.*/
        if(bindingResult.hasErrors()){
            return "item/itemForm";
        }
        /*상품 등록 시 첫 번째 이미지가 없다면 에러 메시지와 함께 상품 등록 페이지로 전환된다. 상품의 첫 번째 임지ㅣ는 메인 페이지에서
        * 보여줄 상품 이미지로 사용하기 위해서 필수 값으로 지정한다.*/
        if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null){
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
            return "item/itemForm";
        }

        try {
            /*상품 저장 로직을 호출한다. 매개변수로 상품 정보와 상품 이미지 정보를 담고 있는  itemImgFileList를 넘겨준다.*/
            itemService.saveItem(itemFormDto, itemImgFileList);
        }catch (Exception e){
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }
        /*상품이 정상적으로 등록되었다면 메인 페이지로 이동한다.*/
        return "redirect:/";
    }

    @GetMapping(value = "/admin/item/{itemId}")
    /*상품수정 페이지로 진입을 위한*/
    public String itemDtl(@PathVariable("itemId") Long itemId, Model model){

        try {
            /*조회한 상품 데이터를 모델에 담아 뷰로 전달*/
            ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
            model.addAttribute("itemFormDto", itemFormDto);
            /*상품 엔티티가 존재하지 않을 경우 에러메시지를 담아 상품 등록 페이지로 이동함.*/
        }catch (EntityNotFoundException e){
            model.addAttribute("errorMessage", "존재하지 않는 상품 입니다.");
            model.addAttribute("itemFormDto", new ItemFormDto());
            return "item/itemForm";
        }

        return "item/itemForm";
    }

    /*상품을 수정하는 URL을 ItemController에 추가한다.*/
    @PostMapping(value = "/admin/item/{itemId}")
    public String itemUpdate(@Valid ItemFormDto itemFormDto, BindingResult bindingResult,
                             @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList, Model model){
        if(bindingResult.hasErrors()){
            return "item/itemForm";
        }

        if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null){
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
            return "item/itemForm";
        }

        try {
            /*상품 수정 로직을 호출함.*/
            itemService.updateItem(itemFormDto, itemImgFileList);
        } catch (Exception e){
            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }

        return "redirect:/";
    }

    /*상품 관리 화면 이동 및 조회한 상품 데이터를 화면에 전달하는 로직 : 현재 상품 데이터 3개만 보여주도록 한다.*/
    @GetMapping(value = {"/admin/items", "/admin/items/{page}"})
    public String itemManage(ItemSearchDto itemSearchDto, @PathVariable("page")Optional<Integer> page, Model model){
        /*페이징을 위해 PageRequest.of 메소드를 통해 Pageable 객체를 생성함*/
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3);
        /*조회 조건에 페이징 정보를 파라미터로 넘겨 객체를 반환 받음*/
        Page<Item> items = itemService.getAdminItemPage(itemSearchDto, pageable);
        /*조회한 상품 데이터 및 페이지 정보를 뷰에 전달*/
        model.addAttribute("items", items);
        /*기존검색유지코드*/
        model.addAttribute("itemSearchDto", itemSearchDto);
        /*최대이동 페이지*/
        model.addAttribute("maxPage", 5);
        return "item/itemMng";
    }
    /*제품 상세페이지로 이동시킬 컨트롤러 클래스*/
    @GetMapping(value = "/item/{itemId}")
    public  String itemDtl(Model model, @PathVariable("itemId") Long itemId){
        ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
        model.addAttribute("item", itemFormDto);
        return "item/itemDtl";
    }

}
