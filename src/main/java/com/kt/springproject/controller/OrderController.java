package com.kt.springproject.controller;

import com.kt.springproject.domain.Member;
import com.kt.springproject.domain.Order;
import com.kt.springproject.domain.item.Item;
import com.kt.springproject.repository.OrderSearch;
import com.kt.springproject.service.ItemService;
import com.kt.springproject.service.MemberService;
import com.kt.springproject.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    // 단순 조회 같은 경우에는 그냥 컨트롤러에서 리포지토리 바로 불러도 괜찮다
    @GetMapping("/orders")
    public String orderList(@ModelAttribute("orderSearch") OrderSearch orderSearch, Model model) {
        List<Order> orders = orderService.findOrders(orderSearch);
        model.addAttribute("orders", orders);

        return "order/orderList";
    }

    @PostMapping("orders/{orderId}/cancel")
    public String cancelOrder(@PathVariable("orderId") Long orderId) {
        orderService.cancelOrder(orderId);
        return "redirect:/orders";
    }

}
