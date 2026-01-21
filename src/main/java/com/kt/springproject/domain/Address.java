package com.kt.springproject.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable // 임베디드(내장타입)이기 때문에
@Getter  // 얘는 왜 setter 안씀?
public class Address {

    private String city;
    private String street;
    private String zipcode;
}
