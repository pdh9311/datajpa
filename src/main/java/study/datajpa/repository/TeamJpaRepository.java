package study.datajpa.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import study.datajpa.entity.Team;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TeamJpaRepository {

    private final EntityManager em;

    // Create
    public Team save(Team team) {
        em.persist(team);
        return team;
    }

    // Read
    public Team find(Long id) {
        return em.find(Team.class, id);
    }

    // Read
    public List<Team> findAll() {
        return em.createQuery("select m from Team m", Team.class)
                .getResultList();
    }

    // Read
    public Optional<Team> findById(Long id) {
        Team team = em.find(Team.class, id);
        return Optional.of(team);
    }

    // Update
    public void update(Team team) {
        em.merge(team);
    }

    // Delete
    public void delete(Team team) {
        em.remove(team);
    }

    // count
    public long count() {
         return em.createQuery("select count(m) from Team m", Long.class)
                 .getSingleResult();
    }

}
