package study.datajpa.dto;

import lombok.Data;
import study.datajpa.entity.Member;

@Data
public class MemberDto {

    private Long memberId;
    private String username;
    private String teamName;


    public MemberDto(Member member) {
        this.memberId = member.getId();
        this.username = member.getUsername();
        this.teamName = null;
    }

    public MemberDto(Long memberId, String username, String teamName) {
        this.memberId = memberId;
        this.username = username;
        this.teamName = teamName;
    }
}
