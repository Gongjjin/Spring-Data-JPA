package SpringDataJpa.prac.repository;

import SpringDataJpa.prac.entity.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MemberJpaRepository {
    @PersistenceContext
    private EntityManager em;

    public Member save(Member member){
        em.persist(member);
        return member;
    }

    public Member find(Long id){
        return em.find(Member.class, id);
    }

    public void delete(Member member){
        em.remove(member);
    }

    public Optional<Member> findById(Long id){
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    public long count(){
        return em.createQuery("select count(m) from Member  m", Long.class).getSingleResult();
    }

    public List<Member> findAll(){
        return em.createQuery("select m from Member  m", Member.class).getResultList();
    }

    public List<Member> findByUsernameAndAgeGreaterthan(String name, int age){
        return em.createQuery("select m from Member m where m.name = :name and m.age > :age", Member.class)
                .setParameter("name", name)
                .setParameter("age", age).getResultList();
    }

    public List<Member> findByUsername(String username){
        return em.createNamedQuery("Member.findByName", Member.class)
                .setParameter("name", "회원1").getResultList();
    }

    /**
     * 페이징 쿼리
     */
    public List<Member> findByPage(int age, int offset, int limit){
        return em.createQuery("select m from Member m where m.age =:age order by m.name desc ", Member.class)
                .setParameter("age", age)
                .setFirstResult(offset) //어디서 부터 가져올것인가?
                .setMaxResults(limit) //어디까지 가져올 것인가?
                .getResultList();
    }

    public long totalCount(int age){
        return em.createQuery("select count(m) from Member  m where m.age = :age", Long.class)
                .setParameter("age", age)
                .getSingleResult();
    }

    /**
     * 벌크성 수정 쿼리
     * ex) 모든 직원의 연봉을 10% 인상하라
     */
    public int bulkPlus(int age){
        return em.createQuery("update Member m set m.age = m.age + 1" + " where m.age >= :age")
                .setParameter("age", age)
                .executeUpdate(); // 응답 값의 개수를 리턴하는 메소드
    }
}
