package jpabook.jpashop.api;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController //@Controller + @ResponseBody
@RequiredArgsConstructor
public class MemberApiController {
    //화면 컨트롤러와 api 컨트롤러 패키지를 분리하는 것이 좋음

    private final MemberService memberService;

    //회원가입
    @PostMapping("api/v1/members/join")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {

        Long id = memberService.join(member); //공백도 들어감 -> @NotEmpty
        return new CreateMemberResponse(id);
    }

    //api 스펙을 위한 별도의 DTO 객체 필요함, 엔티티 외부에 노출x

    @PostMapping("api/v2/members/join")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest createMemberRequest) {

        Member member = new Member();
        member.setName(createMemberRequest.getName()); //api 스펙이 별도의 객체로 관리되어 유지보수가 더 쉬움

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }


    //회원정보 수정
    @PutMapping("api/v2/members/update/{id}")
    public UpdateMemberResponse saveMemberV1(@PathVariable(name = "id") Long id,
                                             @RequestBody @Valid UpdateMemberRequest updateMemberRequest) {

        memberService.update(id, updateMemberRequest.getName());
        Member findMember = memberService.findByOne(id); //조회 쿼리와 서비스 로직(변경) 분리
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
    }


    //회원조회
    @GetMapping("api/v1/members/list")
    public List<Member> listMemberV1() {
        //엔티티 직접 반환은 매우 좋지 않음
        return memberService.findMembers();
    }

    @GetMapping("api/v2/members/list")
    public Result listMemberV2() {
        List<Member> findMembers = memberService.findMembers();

        //Member -> MembersDto
        List<MemberDto> memberDtos = findMembers.stream()
                                        .map(m -> new MemberDto(m.getName()))
                                        .collect(Collectors.toList());

        return new Result(memberDtos.size(), memberDtos);
    }


    //등록
    @Data
    static class CreateMemberRequest {
        private String name;
    }

    @Data
    static class CreateMemberResponse {
        private Long id;
        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }

    //수정
    @Data
    static class UpdateMemberRequest {
        private String name;
    }

    @Data
    @AllArgsConstructor //DTO에는 어노테이션 많이 씀
    static class UpdateMemberResponse {
        private Long id;
        private String name;
    }

    //조회
    @Data
    @AllArgsConstructor
    static class Result<T> {
        private int count;
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String name;
    }
}
