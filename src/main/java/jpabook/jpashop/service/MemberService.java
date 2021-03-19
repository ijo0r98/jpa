package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) //트랜잭션 안에서 실행되어야함, 기본값 읽기 전용으로 설정 -> 최적화
//@AllArgsConstructor
@RequiredArgsConstructor //final있는 필드만 의존성 주입 *추천
public class MemberService {

    private final MemberRepository memberRepository; //변경할 일이 없기 때문에 final -> 컴파일 시점 확인

//    @Autowired //repository 직접 주입해서 사용 -> 생성자 injection으로 하는 것 추천
//    public void MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    //생성자 하나일 경우 자동으로 injection
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    //회원가입
    @Transactional //읽기전용 아닌 것으로 설정됨
    public Long join(Member member) {
        validateDuplicateMember(member); //중복확인
        memberRepository.save(member);
        return member.getId(); //pk임으로 항상 값이 있음
    }

    private void validateDuplicateMember(Member member) {
        //중복회원 Exception
        List<Member> findMembers = memberRepository.findByName(member.getName()); //name을 unique 제약조건 거는 것을 추천
        if(!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    //회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    //회원 아이디로 조회
    public Member findByOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }
}
