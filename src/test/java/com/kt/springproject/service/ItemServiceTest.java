package com.kt.springproject.service;

import com.kt.springproject.domain.item.Book;
import com.kt.springproject.domain.item.Item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class ItemServiceTest {

    @Autowired ItemService itemService;

    @Test
    void 상품_저장() {
        //given
        Book book = new Book();
        book.setName("Book");
        book.setPrice(10000);
        book.setStockQuantity(5);

        //when
        itemService.saveItem(book);

        //then
        assertTrue(book.getId() != null);
    }

    @Test
    void 상품_단건_조회() {
        //given
        Book book = new Book();
        book.setName("Book");
        book.setPrice(10000);
        book.setStockQuantity(5);
        itemService.saveItem(book);

        //when
        Item found = itemService.findOne(book.getId());

        //then
        assertEquals(book, found);
    }

    @Test
    void 상품_목록_조회() {
        //given
        Book book1 = new Book();
        book1.setName("Book1");
        book1.setPrice(12000);
        book1.setStockQuantity(3);

        Book book2 = new Book();
        book2.setName("Book2");
        book2.setPrice(15000);
        book2.setStockQuantity(7);

        //when
        itemService.saveItem(book1);
        itemService.saveItem(book2);

        //then
        List<Item> items = itemService.findItems();
        assertEquals(2, items.size());
        assertTrue(items.contains(book1));
        assertTrue(items.contains(book2));
    }
}
