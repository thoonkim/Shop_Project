package com.shop.controller;

import com.shop.dto.OrderDto;
import com.shop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;


    @PostMapping(value = "/order")
    public @ResponseBody
    ResponseEntity order(@RequestBody @Valid OrderDto orderDto
            , BindingResult bindingResult, Principal principal){//스프링 비동기 REQUESTBODY 바디에 담긴 내용 자바 객체로 전달,
        // RESPONSEBODY 자바 객체를 HTTP 요청의 body로 전달사용해 전달

        if(bindingResult.hasErrors()){//주문 정보를 받는 orderDto 객체에 데이터 바인딩 시 에러가 있는지 검사
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();

            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage());
            }

            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);//에러정보를 ResponeEntity객체에 담아 반환
        }

        String email = principal.getName();//현재 로그인 유저의 정보를 얻기 위해 @Controller 어노테이션이 선언된 클래스에서메소드
        // 인자로 principer객체를 넘겨 줄 경우 해당 객체에 직접 접근할 수 있다. 현재 로그인한 회원 이메일 정보 조회.
        Long orderId;

        try {
            orderId = orderService.order(orderDto, email);//화면으로ㅜ부터 넘어오는 주문 정보와 회원의 이메일 정보를 이용하여 주문로직호출
        } catch(Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Long>(orderId, HttpStatus.OK);//결과값으로 생성된 주문 번호와 요청이 성공했다는 HTTP 응답 상태 코드를 반환
    }
}
