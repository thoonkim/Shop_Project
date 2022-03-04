package com.shop.service;

import com.shop.entity.Board;
import com.shop.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // 서비스 클래스임을 나타냄
public class BoardService {

    @Autowired // 스프링부트가 자동으로 객체를 주입해준다.
    BoardRepository boardRepo;

    public List<Board> findAll() {
        List<Board> list = boardRepo.findAll(); // findAll() 메소드로 테이블의 레코드 리스트를 가져온다.
        return list;
    }
}