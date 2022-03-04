package com.shop.controller;

import com.shop.entity.Board;
import com.shop.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller // 이 클래스가 컨트롤러라는 것을 알려주는 어노테이션
public class BoardController {

    @Autowired
    BoardService service;

    @GetMapping("/board") //클라이언트로부터의 요청 url이 baord이고 get방식이라면 이 메소드에서 요청을 처리함
    public ModelAndView boardList(){
        List<Board> list = service.findAll(); // 서비스에서 요청에 해당하는 처리를 한다.
        ModelAndView nextView = new ModelAndView("board/list"); // ModelAndView객체를 응답페이지의 위치를 지정해 생성한다.
        nextView.addObject("boardList", list); // 서비스에서 받아온 데이터 List를 ModelAndView객체에 넣는다.
        return nextView;// ModelAndView객체를 리턴한다.
    }
}