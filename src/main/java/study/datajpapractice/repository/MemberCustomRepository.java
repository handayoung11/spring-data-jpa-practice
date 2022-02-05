package study.datajpapractice.repository;

import study.datajpapractice.entity.Member;

import java.util.List;

public interface MemberCustomRepository {
    List<Member> findMemberCustom();
}
