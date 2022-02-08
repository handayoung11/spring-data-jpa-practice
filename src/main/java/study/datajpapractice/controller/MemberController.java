package study.datajpapractice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpapractice.dto.MemberDTO;
import study.datajpapractice.entity.Member;
import study.datajpapractice.repository.MemberJpaRepository;
import study.datajpapractice.repository.MemberRepository;

import javax.annotation.PostConstruct;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;
    private final MemberJpaRepository memberJpaRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new NullPointerException("존재하지 않는 회원입니다."));
        return member.getUsername();
    }

    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable("id") Member member) {
        return member.getUsername();
    }

    @GetMapping("/members")
    public Page<MemberDTO> pagingMember(
            @PageableDefault(size = 5)
            @SortDefault.SortDefaults({
                    @SortDefault(value = "username", direction = Sort.Direction.ASC),
                    @SortDefault(value = "id", direction = Sort.Direction.DESC)})
                    Pageable pageable) {
        Page<Member> members = memberRepository.findAll(pageable);
        return members.map(MemberDTO::new);
    }

    @PostConstruct
    public void init() {
        for (int i = 0; i < 100; i++) {
            memberRepository.save(new Member("memberA", 10, null));
        }
    }
}
