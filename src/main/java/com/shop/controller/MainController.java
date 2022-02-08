package com.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


//메인 페이지 등록된 상품 목록을 보여주도록 하는 것
@Controller
public class MainController {

    @GetMapping(value="/")
    public String main(){
        return "main";
    }
}
