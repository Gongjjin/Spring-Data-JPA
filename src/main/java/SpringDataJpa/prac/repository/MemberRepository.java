package SpringDataJpa.prac.repository;

import SpringDataJpa.prac.dto.MemberDto;
import SpringDataJpa.prac.entity.Member;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long>, CustomMemberRepository {
    /**
     * 아래와 같이 특정 엔티티에 조건을 넣어 검색하는 것을 쿼리 메소드라고한다.
     * @param name
     * @return
     */
    List<Member> findByNameAndAgeGreaterThan(String name, int age);
    List<Member> findByNameIn(List<String> names);
    List<Member> countMemberByAgeIsLessThan(int age);


    /**
     * 아래와 같이 @Query를 사용할 땐 메소드 이름을 맞출 필요가 없다.
     * @Query 결과를 반환한다.
     * @param name
     * @return
     */
    @Query(name = "Member.findByName") //네임드 쿼리
    List<Member> findByName(@Param("name")String name);

    @Query("select m from Member m where m.age =:age and m.name = :name")
    List<Member> findUser(@Param("age")int age, @Param("name")String name);

    @Query("select m.name from Member m")
    List<String> findUsernameList();

    @Query("select new SpringDataJpa.prac.dto.MemberDto(m.id, m.name, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();
    @Query("select m from Member  m where m.name in :names")
    List<Member> findByNames(@Param("names") List<String> names);

//    Page<Member> findByAge(int age, Pageable pageable);

    /**
     * 아래 totalcount가 필요한 경우, left join을 하나 안하나 어차피 수는 같다
     * 근데 join까지 해서 totalcount할 때 데이터가 너무 많으면 성능이 안나온다.
     * 그럴 때 count 쿼리를 분류하면 된다
     * @param age
     * @param pageable
     * @return
     */
    @Query(value = "select m from Member m left join m.team t",
    countQuery = "select count(m) from Member  m")
    Slice<Member> findByAge(int age, Pageable pageable);

    // mybatis, jdbc를 쓸 때 바로 DB를 업데이트 쳐버리면 영속성 컨텍스트에 반영되지 않는다. 따라서 clear를 반드시 해줄상황이 발생한다.
    @Modifying // modifying을 통해 영속성 컨텍스트 초기화. 벌크 연산 후 이거 안 쓰면 영속성 컨텍스트의 값을 업데이트할 수 없음
    @Query("update Member  m set m.age =  m.age +1 where m.age >= :age")
    int bulkAgePlus(@Param("age")int age);

    @Query("select m from Member  m left join fetch m.team")
    List<Member> findMemberFetchJoin();

    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberByEntityGraph();

    /**
     * @Param을 쓰는 경우 JPQL에 들어가는 파라미터의 이름과 메소드의 파라미터 이름이 다를 때
     * + entitygraph의 경우 패치 조인이 left join
     * 복잡할 것 같으면 그냥 패치 조인 써라
     * @param username
     * @return
     */
//    @EntityGraph(attributePaths = {"team"})
    @EntityGraph("Member.all")
    @Query("select m from Member m where m.name = :name")
    List<Member> findEntityGraphBy(@Param("name")String username);

    @QueryHints(value = @QueryHint( name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByName(String name);

    /**
     *     JPA에서 지원하는 비관적 락
     *     select for update
      */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByName(String name);

}
