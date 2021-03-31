package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(@Valid MemberForm memberForm, BindingResult result) {

        //member 객체를 바로 사용하지 않고 form 객체 따로 만들어 사용할 것 권장

        //binding result -error가 담겨서 실행됨-> 화면에 보여짐, form 데이터는 유지
        if (result.hasErrors()) {
            //error가 있음을 인지하고 해당 화면으로 알아서 이동
            return "members/createMemberForm";
        }

        Address address = new Address(memberForm.getCity(), memberForm.getStreet(), memberForm.getZipcode());
        Member member = new Member();
        member.setAddress(address);

        memberService.join(member);

        return "redirect:/"; //첫번째 페이지로 넘어감
    }

    @GetMapping("/members")
    public String list(Model model) {
        //api 만들때는 절대! 객체 자체를 넘기지 말것ㅠㅠ
        //api 스펙 문제와 정보 외부 누출
        //템플릿 엔진의 경우 선택해서 보여주기 때문에 상관x

        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";
    }
}
