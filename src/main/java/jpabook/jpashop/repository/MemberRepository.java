package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    //<Type, Id>

    //공통적인 기능 이미 구현

    List<Member> findByName(String name); //자동으로 이름으로 검색 기능 만들어줌
    //select m from member where m.name = :name
}
