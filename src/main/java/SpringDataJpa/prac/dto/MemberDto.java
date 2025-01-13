package SpringDataJpa.prac.dto;

import SpringDataJpa.prac.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data @AllArgsConstructor @NoArgsConstructor
public class MemberDto {
    private Long id;
    private String username;
    private String teamname;

    public MemberDto(Member member){
        this.id = member.getId();
        this.username = member.getName();
        this.teamname = member.getTeam().getName();
    }
}
