package SpringDataJpa.prac.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter @Getter
@ToString(of = {"id","name"})
@NamedQuery(
        name = "Member.findByName",
        query = "select m from Member  m where m.name = :name"
)
@NamedEntityGraph(name = "Member.all", attributeNodes = @NamedAttributeNode("team"))
public class Member {
    @Id @GeneratedValue
    private Long id;
    private String name;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    public Member(String name) {
        this.name = name;
    }

    public Member(String name, int age) {
        this.name = name;
        this.age= age;
    }

    public Member(String member1, int i, Team teamA) {
        this.name = member1;
        this.age = i;
        this.team = teamA;
    }

    public void changeTeam(Team team){
        this.team = team;
        team.getMemberList().add(this);
    }
}
