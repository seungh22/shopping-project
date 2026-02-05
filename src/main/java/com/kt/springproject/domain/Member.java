package com.kt.springproject.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @NotEmpty
    private String name;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "member") // 오더 테이블에 있는 멤버 필드에 의해서 매핑 된거 그래서 값을 넣어도 fk값이 변경되지 않음
    private List<Order> orders = new ArrayList<>();
}
