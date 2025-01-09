package SpringDataJpa.prac.repository;

import SpringDataJpa.prac.dto.MemberDto;
import SpringDataJpa.prac.entity.Member;
import SpringDataJpa.prac.entity.Team;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.sound.midi.MetaMessage;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@Transactional
@SpringBootTest
@Rollback(value = false)
public class MemberJpaRepositoryTest {
    @Autowired
    MemberJpaRepository memberJpaRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TeamRepository teamRepository;

    @Test
    public void testMember(){
        Member member = new Member("홍공진");
        Member savemember = memberJpaRepository.save(member);
        Member findmember = memberJpaRepository.find(savemember.getId());

        assertThat(findmember.getId()).isEqualTo(savemember.getId());
    }

    @Test
    public void 유저찾기() {
        Member m1 = new Member("ㅂㅂㅂ", 15); // age를 20보다 크게 설정
        Member m2 = new Member("ㅂㅂ", 10);
        memberJpaRepository.save(m1);
        memberJpaRepository.save(m2);

        List<Member> result = memberJpaRepository.findByUsernameAndAgeGreaterthan("ㅂㅂㅂ", 3);
        assertThat(result.size()).isEqualTo(1); // 조건을 만족하는 객체가 1개
        assertThat(result.get(0).getName()).isEqualTo("ㅂㅂㅂ");
    }

    @Test
    public void username(){
        Member m1 = new Member("ㅂㅂㅂ", 15); // age를 20보다 크게 설정
        Member m2 = new Member("ㅂㅂ", 10);
        memberJpaRepository.save(m1);
        memberJpaRepository.save(m2);
        List<Member> result = memberJpaRepository.findByUsername("ㅂㅂㅂ");
        Member findmember = result.get(0);
    }

    @Test
    public void testQuery(){
        Member m1 = new Member("ㅂㅂㅂ", 15); // age를 20보다 크게 설정
        Member m2 = new Member("ㅂㅂ", 10);
        memberJpaRepository.save(m1);
        memberJpaRepository.save(m2);
        List<Member> result = memberRepository.findUser(10, "ㅂㅂㅂ");
    }

    @Test
    public void findName(){
        Member m1 = new Member("ㅂㅂㅂ", 15); // age를 20보다 크게 설정
        Member m2 = new Member("ㅂㅂ", 10);
        memberJpaRepository.save(m1);
        memberJpaRepository.save(m2);
        List<String> result = memberRepository.findUsernameList();
    }

    @Test
    public void dtos(){
        Team team = new Team("TEAM A");
        teamRepository.save(team);

        Member m2 = new Member("ㅂㅂ", 10);
        m2.changeTeam(team);
        memberRepository.save(m2);

        List<MemberDto> memberDtos = memberRepository.findMemberDto();
        for (MemberDto memberDto : memberDtos) {
            System.out.println("dto"+memberDto);
        }

    }

    @Test
    public void findCollection(){
        Member m1 = new Member("ㅂㅂㅂ", 15); // age를 20보다 크게 설정
        Member m2 = new Member("ㅂㅂ", 10);
        memberJpaRepository.save(m1);
        memberJpaRepository.save(m2);
        List<Member> result = memberRepository.findByNames(Arrays.asList("AAA","BBB"));
        for (Member member : result) {
            System.out.println(member);
        }
    }

    @Test
    public void paging(){
        //given
        memberJpaRepository.save(new Member("member1",10));
        memberJpaRepository.save(new Member("member2",10));
        memberJpaRepository.save(new Member("member3",10));
        memberJpaRepository.save(new Member("member4",10));
        memberJpaRepository.save(new Member("member5",10));
        memberJpaRepository.save(new Member("member6",10));

        // 서비스딴에서 처리하면 좋을 것 같음
        PageRequest pageRequest = PageRequest.of(0,3, Sort.by(Sort.Direction.DESC,"name"));

        int age = 10;
        int offset = 0;
        int limit = 3;

        //when
//        Page<Member> page = memberRepository.findByAge(age, pageRequest);
        /**
         * Page를 쓰면 토탈카운트로 알아서 만들어준다.
         */
//       List<Member> members = memberJpaRepository.findByPage(age,offset,limit);
//       long totalCount = memberJpaRepository.totalCount(age);


       //then
//        List<Member> content = page.getContent();
//        long totalElements = page.getTotalElements();
//
//        assertThat(content.size()).isEqualTo(3); // 페이징 조건에 따른 요소의 수
//        assertThat(page.getTotalElements()).isEqualTo(6); //전체 요소 수
//        assertThat(page.getNumber()).isEqualTo(0); // 현재 요청한 페이지 번호
//        assertThat(page.getTotalPages()).isEqualTo(2);// 전체 페이지 수
//        assertThat(page.isFirst()).isTrue();
//        assertThat(page.hasNext()).isTrue();
//        assertThat(members.size()).isEqualTo(3);
//        assertThat(totalCount).isEqualTo(6);
    }

    @Test
    public void slice(){
        //given
        memberJpaRepository.save(new Member("member1",10));
        memberJpaRepository.save(new Member("member2",10));
        memberJpaRepository.save(new Member("member3",10));
        memberJpaRepository.save(new Member("member4",10));
        memberJpaRepository.save(new Member("member5",10));
        memberJpaRepository.save(new Member("member6",10));

        // 서비스딴에서 처리하면 좋을 것 같음, 만약 정렬의 조건이 까다로워진다면 그럴 땐 그냥 JQPL로 직접 쿼리를 짜는게 편하다.
        PageRequest pageRequest = PageRequest.of(0,3, Sort.by(Sort.Direction.DESC,"name"));

        int age = 10;
        //when
        Slice<Member> page = memberRepository.findByAge(age, pageRequest);
        // 바로 DTO로 보내버리기
        Slice <MemberDto> memberDto = page.map(member -> new MemberDto(member.getId(),member.getName(), member.getTeam().getName()));
        //then
        List<Member> content = page.getContent();
//        long totalElements = page.getTotalElements();

        assertThat(content.size()).isEqualTo(3); // 페이징 조건에 따른 요소의 수
//        assertThat(page.getTotalElements()).isEqualTo(6); //전체 요소 수
        assertThat(page.getNumber()).isEqualTo(0); // 현재 요청한 페이지 번호
//        assertThat(page.getTotalPages()).isEqualTo(2);// 전체 페이지 수
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();
    }
}