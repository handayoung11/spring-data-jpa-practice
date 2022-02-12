package study.datajpapractice.dto;

import lombok.Getter;

@Getter
public class MemberInfoDTO {
    String username;
    int age;

    public MemberInfoDTO(String username, int age) {
        this.username = username;
        this.age = age;
    }
}
