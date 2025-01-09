package SpringDataJpa.prac.repository;

import SpringDataJpa.prac.entity.Team;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class TeamRepository {
    @PersistenceContext
    EntityManager em;

    public Team save(Team team){
        em.persist(team);
        return team;
    }
}