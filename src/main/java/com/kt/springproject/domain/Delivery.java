package com.kt.springproject.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
public class Delivery {

    @Id
    @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @JsonIgnore
    @OneToOne(mappedBy = "delivery", fetch = LAZY)
    private Order order;

    @Embedded
    private Address address;

    // enum을 쓸떄 Enumerated 을 넣어야 되고 이넘 타입을 ordinal,String이 있는데 ordinal은 컬럼이 숫자가 들어감 중간에 다른 상태가 생기면 클남
    // READY, XXX, COMP가 되면 COMP가 3으로 밀림 2를 조회하면 XXX가 나옴
    // 그래서 String을 써야 밀리지 않음
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;
}
