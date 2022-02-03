package study.datajpapractice.entity;

import lombok.*;

import javax.persistence.*;

@Data
@Entity
@Setter(AccessLevel.NONE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@NamedQuery(name = "Member.findByUsername"
        , query = "select m from Member m where m.username = :username")
@NamedEntityGraph(name = "Members.all"
        , attributeNodes = {@NamedAttributeNode("team")})
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String username;
    private int age;

    @ToString.Exclude
    @JoinColumn(name = "team_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Team team;

    public Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;
        if(team != null) changeTeam(team);
    }

    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }
}
