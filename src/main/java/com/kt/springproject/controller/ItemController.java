package com.kt.springproject.controller;

import com.kt.springproject.domain.item.Book;
import com.kt.springproject.domain.item.Item;
import com.kt.springproject.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/items/new")
    public String createForm(Model model) {
        model.addAttribute("form", new BookForm());
        return "items/createItemForm";
    }

    //todo: setter 제거
    @PostMapping("/items/new")
    public String create (BookForm form) {
        Book book = new Book();
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());

        itemService.saveItem(book);
        return "redirect:/";
    }

    @GetMapping("/items")
    public String list(Model model) {
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);
        return "items/itemList";
    }

    @GetMapping("items/{itemId}/edit")
    public String updateItemForm(@PathVariable("itemId") Long itemsId, Model model) {
        Book item = (Book) itemService.findOne(itemsId);

        BookForm form = new BookForm();
        form.setId(item.getId());
        form.setName(item.getName());
        form.setPrice(item.getPrice());
        form.setStockQuantity(item.getStockQuantity());
        form.setAuthor(item.getAuthor());
        form.setIsbn(item.getIsbn());

        model.addAttribute("form", form);
        return "items/updateItemForm";
    }

    //todo: form에서 넘어올때 아이디를 조작할 수 있기 때문에 서비스 계층에서 이 유저가 아이템에 대해서 수정할 권한이 있는지 체크해주는 로직이 필요
    @PostMapping("items/{itemId}/edit")
    public String updateItem(@PathVariable Long itemId, @ModelAttribute("form") BookForm form) {
        // 어설프게 컨트롤러에서 엔티티를 파라미터로 쓰지 말자
        // 업데이트할게 많다 싶으면 dto만들면 됨
//        Book book = new Book();
//
//        book.setId(form.getId());
//        book.setName(form.getName());
//        book.setPrice(form.getPrice());
//        book.setStockQuantity(form.getStockQuantity());
//        book.setAuthor(form.getAuthor());
//        book.setIsbn(form.getIsbn());
//
//        itemService.saveItem(book);
        itemService.updateItem(itemId,form.getName(),form.getPrice(),form.getStockQuantity());
        return "redirect:/items";
    }
    // itemService.saveItem(book)에서 book 엔티티가 넘어가고 saveItem을 호출하면 트랜잭션이 걸린 상태로 ItemRepository의 save를 호출
    // itemRepository의 save를 보면 item의 id는 null이 아님 (db에서 수정할 목적으로 불렀으니까)
    // -> 만약 null이면 persist(그냥 자바 객체니까 영속성 컨택스트에 넣음), 아니면 id가 있기때문에 else.merge가 호출됨
    // 준영속성 엔티티는 다시 영속성 컨택스트에 넣지 않으면 디비 업데이트를 못 한다 => 준영속성 엔티티는 어떻게 디비를 변경하나?
    // 변경 감지와 병합(merge)의 차이 중요
    // 방법 2가지 => 1. 변경 감지 기능(dirty checking) 사용, 2. 병합(merge) 사용


}
