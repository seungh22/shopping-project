package com.kt.springproject.domain;

import com.kt.springproject.domain.item.Item;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "order_item_Id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToMany
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;
    private int count;
}
