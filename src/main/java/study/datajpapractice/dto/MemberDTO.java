package study.datajpapractice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberDTO {

    private Long id;
    private String username;
    private String teamName;
}
