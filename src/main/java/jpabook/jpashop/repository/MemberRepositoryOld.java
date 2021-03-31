package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryOld {

//    @PersistenceContext 스프링에서 entity manager 주입 (기본)
//    @Autowired springboot 라이브러리가가 지원
    private final EntityManager em;

//    @PersistenceUnit -entity manager factory 주입


    public void save(Member member) {
        em.persist(member);
        //persist 한다고 바로 쿼리가 실행되는 것은 아님, commit 후 실행
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    //jpql 엔티티 대상으로 쿼리
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }

}
