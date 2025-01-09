package SpringDataJpa.prac.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data @AllArgsConstructor @NoArgsConstructor
public class MemberDto {
    private Long id;
    private String username;
    private String teamname;
}
