package study.datajpapractice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import study.datajpapractice.entity.Member;

@Data
@AllArgsConstructor
public class MemberDTO {

    private Long id;
    private String username;
    private String teamName;

    public MemberDTO(Member member) {
        id = member.getId();
        username = member.getUsername();
    }
}
