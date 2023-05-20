package study.datajpa.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import study.datajpa.entity.Member;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberJpaRepository {

    private final EntityManager em;

    // Create
    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    // Read
    public Member find(Long id) {
        return em.find(Member.class, id);
    }

    // Read
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    // Read
    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id);
        return Optional.of(member);
    }

    // Update
    public void update(Member member) {
        em.merge(member);
    }

    // Delete
    public void delete(Member member) {
        em.remove(member);
    }

    // count
    public long count() {
         return em.createQuery("select count(m) from Member m", Long.class)
                 .getSingleResult();
    }

    public List<Member> findByUsernameAndAgeGreaterThan(String username, int age) {
        return em.createQuery("select m from Member m" +
                        " where m.username = :username" +
                        " and m.age > :age", Member.class)
                .setParameter("username", username)
                .setParameter("age", age)
                .getResultList();
    }

    public List<Member> findByUsername(String username) {
        return em.createNamedQuery("Member.findByUsername", Member.class)
                .setParameter("username", username)
                .getResultList();
    }

    public List<Member> findByPage(int age, int offset, int limit) {
        return em.createQuery("select m from Member m where m.age = :age order by m.username desc", Member.class)
                .setParameter("age", age)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    public long totalCount(int age) {
        return em.createQuery("select count(m) from Member m where m.age = :age", Long.class)
                .setParameter("age", age)
                .getSingleResult();
    }

    public int bulkAgePlus(int age) {
        return em.createQuery("update Member m set m.age = m.age + 1" +
                        " where m.age >= :age")
                .setParameter("age", age)
                .executeUpdate();
    }

}
