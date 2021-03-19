package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "member") //주인이 아닌 테이블
    private List<Order> orders = new ArrayList<>(); //먼저 초기화 -> 초기화에 대한 고민 안해도 됨
    //컬렉션은 가급적 변경하지 않는 것이 좋음 -하이버네이트 메커니즘 오류

}
