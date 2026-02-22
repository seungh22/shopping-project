package com.kt.springproject.api;

import com.kt.springproject.domain.Address;
import com.kt.springproject.domain.Order;
import com.kt.springproject.domain.OrderStatus;
import com.kt.springproject.repository.OrderRepository;
import com.kt.springproject.repository.OrderSearch;
import com.kt.springproject.repository.order.simplequery.OrderSimpleQueryDto;
import com.kt.springproject.repository.order.simplequery.OrderSimpleQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * xToOne(ManyToOne, OneToOne)에서의 성능최적화 방법
 * Order
 * Order -> Member
 * Order -> Delivery
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName(); //Lazy 강제 초기화
            order.getMember().getAddress(); //Lazy 강제 초기화
        }
        return all;
    }

    // 엔티티를 DTO로 변환하는 일반적인 방법.
    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2() {
        //ORDER
        //N+1 문제
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());

        //2개
        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());
        return result;
    }
    // 쿼리가 총 1+N+N번 실행된다
    // order 조회 1번 (order 조회 결과 수가 N번 (여기선 2번)
    // order -> member 지연 로딩 조회 N번 (여기선 2번)
    // order -> delivery 지연 로딩 조회 N번 (여기선 2번)
    // 지연 로딩은 영속성 컨텍스트에서(바로 디비 쿼리X) 조회하므로, 이미 조회된 경우 쿼리를 생략한다.

    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithMemberDelivery( );
        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());
        return result;
    }

    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> ordersV4() {
        return orderSimpleQueryRepository.findOrderDtos();
    }
    // 엔티티를 페치 조인을 사용해서 쿼리 1번에 조리
    // 페치 조인으로 order -> member, order -> delivery는 이미 조회 된 상태이므로 지연로딩X
    // 단점 : select에서 다 끌고 옴 -> 엔티티를 찍어서 조회하는 것의 단점

    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate; //주문시간
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
        }
    }
}
