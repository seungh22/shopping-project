package com.kt.springproject.service;

import com.kt.springproject.domain.item.Book;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ItemUpdateTest {

    @Autowired
    EntityManager em;

    @Test
    public void update() {
        Book book = em.find(Book.class, 1L);

        //트랜잭션 안에서는 그냥 book.setName해가지고 이름을 그냥 바꿔치기 한 다음에
        book.setName("book1");

        //변경감지 => dirty checking
        //트랜잭션이 커밋 되어 버리면 JPA가 변경분에 대해서 JPA가 찾아가지고 업데이트 쿼리를 자동으로 생성해서 디비에 반영함 => 더티 체킹
        //=> 이러한 매커니즘으로 기본적으로 JPA의 엔티티를 바꿀 수 있다 -> 내가 원하는 걸로 데이터를 업데이트할 수 있다

    }
}
