package com.shop.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.constant.ItemSellStatus;
import com.shop.dto.ItemSearchDto;
import com.shop.dto.MainItemDto;
import com.shop.dto.QMainItemDto;
import com.shop.entity.Item;
import com.shop.entity.QItem;
import com.shop.entity.QItemImg;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

/*2. querydsl을 spring data jpa와 함께 사용하기위한 사용자 정의 인터페이스를 구현한다.*/
/*ItemRepositoryCustom을 상속받음*/
public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {
    /*동적으로 쿼리를 생성하기 위해 JPAQueryFactory 클래스를 사용*/
    private JPAQueryFactory queryFactory;

    /*JPAQueryFactory의 생성자로 EntityManager 객체를 넣는다.*/
    public ItemRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }
    /*상품 판매 상태 저건ㅌ이 전체(null)일 경우는 null을 리턴한다. 결과값이 null이면 where
    * 절에서 해당 조건은 무시된다. 상품 판매 상태 조건이 null이 아니라 판매중 or 품절 상태라면
    * 해당 조건의 상품만 조회한다.*/
    private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus) {
        /*searchDate Type의 값에 따라 dateTime의 값을 이전 시간의 값으로 세팅 후 해당 시간
        * 이후로등록된 상품만 조회한다. 예를들어 serchdatetype 값이 1m인 경우 dateTime의
        * 시간을 한 달 전으로세팅 후 최근 한 달동안 등록된 상품만 조회하도록 조건값을 반환한다.*/
        return searchSellStatus == null ? null : QItem.item.itemSellStatus.eq(searchSellStatus);
    }

    private BooleanExpression regDtsAfter(String searchDateType) {

        LocalDateTime dateTime = LocalDateTime.now();

        if (StringUtils.equals("all", searchDateType) || searchDateType == null) {
            return null;
        } else if (StringUtils.equals("1d", searchDateType)) {
            dateTime = dateTime.minusDays(1);
        } else if (StringUtils.equals("1w", searchDateType)) {
            dateTime = dateTime.minusWeeks(1);
        } else if (StringUtils.equals("1m", searchDateType)) {
            dateTime = dateTime.minusMonths(1);
        } else if (StringUtils.equals("6m", searchDateType)) {
            dateTime = dateTime.minusMonths(6);
        }

        return QItem.item.regTime.after(dateTime);
    }
    /*searchBy의 값에 따라 상품명에 검색어를 포함하고 있는 상품 또는 상품 생성자의 아이디에
    * 검색어를 포함하고 있는 상품을 조회하도록 저건값을 반환한다.*/
    private BooleanExpression searchByLike(String searchBy, String searchQuery) {

        if (StringUtils.equals("itemNm", searchBy)) {
            return QItem.item.itemNm.like("%" + searchQuery + "%");
        } else if (StringUtils.equals("createdBy", searchBy)) {
            return QItem.item.createdBy.like("%" + searchQuery + "%");
        }

        return null;
    }

    @Override
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        /*queryFactory를 이용해서 쿼리를 생성한다. 쿼리문을 직접 작성할 때의 형태와 문법이 비슷한
        * 것을 볼 수 있다.*/
        QueryResults<Item> results = queryFactory
                .selectFrom(QItem.item) //상품 데이터를 조회하기 위해 Qitem의 item을 지정
                .where(regDtsAfter(itemSearchDto.getSearchDateType()), // BooleanExpression 반환하는 조건문들 넣음
                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                        searchByLike(itemSearchDto.getSearchBy(),
                                itemSearchDto.getSearchQuery()))
                .orderBy(QItem.item.id.desc())
                .offset(pageable.getOffset()) // offset : 데이터를 가지고 올 시작 인덱스를 지정
                .limit(pageable.getPageSize()) // limit : 한 번에 가지고 올 최대 개수를 지정
                .fetchResults(); // fetchResult() : 조회한 리스트 및 전체 개수를 포함하는 QueryResult를 반환한다. 상품 데이터 리스트 조회 및 상품 데이터 전체 개수를 조회하는 2번의 쿠리문이 실행된다.

        List<Item> content = results.getResults();
        long total = results.getTotal();
        /*조회한 데이터를 Page클래스의 구현체인 PageImpl객체로 반환한다.*/
        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression itemNmLike(String searchQuery){
        return StringUtils.isEmpty(searchQuery) ? null : QItem.item.itemNm.like("%" + searchQuery + "%");
    }

    /*검색어가 null이 아니면 상품명에 해당하는 검색어가 포함되는 상품을 조회하는 조건을 반환*/
    @Override
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;

        QueryResults<MainItemDto> results = queryFactory
                .select(
                        new QMainItemDto(
                                item.id,
                                item.itemNm,
                                item.itemDetail,
                                itemImg.imgUrl,
                                item.price)
                )
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repimgYn.eq("Y"))
                .where(itemNmLike(itemSearchDto.getSearchQuery()))
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<MainItemDto> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }

}
