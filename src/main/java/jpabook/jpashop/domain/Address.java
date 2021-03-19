package jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Getter //값타입은 getter만 제공
@Embeddable
public class Address {

    private String city;
    private String street;
    private String zipcode;

    protected Address() {
        //생성자 제공을 위해 기본 생성자가 있어야함
        //public이나 protected 으로 구현
    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
