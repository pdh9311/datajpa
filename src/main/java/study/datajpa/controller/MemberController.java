package study.datajpa.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/members1/{id}")
    public String findMemberV1(@PathVariable("id") Long id) {
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    @GetMapping("/members2/{id}")
    public String findMemberV2(@PathVariable("id") Member member) {
        return member.getUsername();
    }

    @GetMapping("/members1")
    public Page<Member> listV1(Pageable pageable) {   // default : page=0&size=20
        return memberRepository.findAll(pageable);
    }

    @GetMapping("/members2")
    public Page<Member> listV2(
            @PageableDefault(size=5, sort="username",
                    direction = Sort.Direction.DESC) Pageable pageable) {
        return memberRepository.findAll(pageable);
    }

    @GetMapping("/members3")
    public Page<MemberDto> listV3(
            @PageableDefault(size=5, sort={"id"},
                    direction = Sort.Direction.ASC) Pageable pageable) {
        return memberRepository.findAll(pageable)
                .map(MemberDto::new);
    }

    @GetMapping("/members4")
    public Page<MemberDto> listV4(@PageableDefault(size=5, sort={"id"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return memberRepository.findAll(pageable)
                .map(MemberDto::new);
    }

    @GetMapping("members")
    public Page<MemberDto> list(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.DESC, "id");
        return memberRepository.findAll(pageRequest)
                .map(MemberDto::new);
    }

//    @PostConstruct
//    public void init() {
//        for (int i = 0; i < 100; i++) {
//            memberRepository.save(new Member("user" + i, i));
//        }
//    }

}
