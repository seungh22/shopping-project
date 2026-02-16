package com.kt.springproject.service;

import com.kt.springproject.domain.Delivery;
import com.kt.springproject.domain.Member;
import com.kt.springproject.domain.Order;
import com.kt.springproject.domain.OrderItem;
import com.kt.springproject.domain.item.Item;
import com.kt.springproject.repository.ItemRepository;
import com.kt.springproject.repository.MemberRepository;
import com.kt.springproject.repository.OrderRepository;
import com.kt.springproject.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문
     */
    @Transactional  // 데이터를 변경하는 것이기 때문에 트랜젝션널 필요
    public Long order(Long memberId, Long itemId, int count) {

        //엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        //배송정보 생성 - 회원의 주소에 있는 값으로 배송한다
        //todo: 배송지 정보
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        //주문상품 생성 -> static 생성 메서드를 통해서 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        //주문 생성 -> static 생성 메서드를 통해서 생성
        //예제를 단순화하려고 한 번에 하나의 상품만 주문할 수 있다.
        //todo: 한 번에 여러 상품을 주문
        Order order = Order.createOrder(member, delivery, orderItem);

        //주문 저장 -> 주문을 저장할 때 cascade옵션이 있기 때문에 orderitem과 delivery는 자동으로 함께 persist되면서 DB테이블에 insert된다
//                    -> 트랜잭션이 커밋되는 시점에 flush가 일어나면서 insert가 DB에 날라감
        orderRepository.save(order);
        return order.getId();
    }
    /**
     * 주문 취소
     */
    @Transactional
    public void cancelOrder(Long orderId) {
        //주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);
        //주문 취소
        order.cancel();
    }
    /**
     * 검색
     */
    // todo: 주문 조회 결과를 대표상품 1개가 아니라 모든 주문상품을 DTO 리스트로 반환하도록 변
    public List<Order> findOrders(OrderSearch orderSearch){
        return orderRepository.findAllByString(orderSearch);
    }
}
