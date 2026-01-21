package com.kt.springproject.domain.item;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype") // 뭐여
@Getter  @Setter
public abstract class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;
    // 상속 관계 매핑 공부
    // 상속 관계 전략을 지정해야 되는데 이 지정을 부모클래스에 잡아야 함 왜 싱글테이블 전략을 쓰니까

    private String name;
    private int price;
    private int stockQuantity;
}