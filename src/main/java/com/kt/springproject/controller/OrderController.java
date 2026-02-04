package com.kt.springproject.controller;

import com.kt.springproject.domain.Member;
import com.kt.springproject.domain.item.Item;
import com.kt.springproject.service.ItemService;
import com.kt.springproject.service.MemberService;
import com.kt.springproject.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemService itemService;

    @GetMapping("/order")
    public String createForm(Model model) {
        List<Member> members = memberService.findMembers();
        List<Item> items = itemService.findItems();

        model.addAttribute("members", members);
        model.addAttribute("items", items);

        return "order/orderForm";
    }

    //todo: 하나의 상품이 아니라 여러개의 상품을 주문할 수 있도록
    @PostMapping("/order")
    public String order(@RequestParam("memberId")Long memberId,
                        @RequestParam("itemId")Long itemId,
                        @RequestParam("count")int count) {
        orderService.order(memberId, itemId, count);
        return "redirect:/orders";  // 주문이 끝나면 상품 주문 내역이 있는 /orders URL로 리다이랙트
    }

}
