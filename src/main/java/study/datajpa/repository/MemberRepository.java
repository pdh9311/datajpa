package study.datajpa.repository;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom, JpaSpecificationExecutor<Member> {

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    @Query(name = "Member.findByUsername")
        // @Query 어노테이션 부분을 생략하면 "Member.메소드명"으로 NamedQuery를 찾기 때문에 생략할 수도 있다.
    List<Member> findByUsername(@Param("username") String username);

    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsername();

    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);

    List<Member> findListByUsername(String username);

    Member findMemberByUsername(String username);

    Optional<Member> findOptionalByUsername(String username);

    Page<Member> findPageByAge(int age, Pageable pageable);

    Slice<Member> findSliceByAge(int age, Pageable pageable);

    List<Member> findListByAge(int age, Pageable pageable);

    @Query(value = "select m from Member m left join Team t on m.team.id = t.id where m.age = :age",
            countQuery = "select count(m.username) from Member m where m.age = :age")
    Page<Member> findLeftJoinByAge(@Param("age") int age, Pageable pageable);

    @Query(value = "select m from Member m join Team t on m.team.id = t.id where m.age = :age")
    Page<Member> findJoinByAge(@Param("age") int age, Pageable pageable);

    @Modifying(clearAutomatically = true) // executeUpdate(); + em.flush(); em.clear();
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    @Query("select m from Member m join fetch m.team")
    List<Member> findMemberFetchJoin();

    @Query("select m from Member m join m.team")
    List<Member> findMemberJoin();


    @Override
    @EntityGraph(attributePaths = {"team"})
        // fetch join
    List<Member> findAll();

    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    @EntityGraph(attributePaths = {"team"})
    List<Member> findEntityGraphByUsername(String username);

    @EntityGraph(value = "Member.all")
    List<Member> findNamedEntityGraphByUsername(String username);

    /**
     * @QueryHints는 변경감지를 위한 스냅샷을 만들지 않습니다.
     * 엔티티를 변경하지 않을 때 hibernate가 최적화하기 위해 읽기전용 최적화 기능을 제공합니다.
     * <p>
     * 실제 업무에서 시간이 많이 걸리거나 조회성능이 안나오는것은 이런게 아니라
     * 복잡한 쿼리같은것 때문에 발생합니다.
     * 따라서 모든 읽기전용 쿼리에 일부러 @QueryHints을 넣어가면서 최적화한다는 건 비효율적이고,
     * 진짜 실시간 트래픽이 많아서 최적화하 필요하다거나 사용자가 많이 조회하는 쿼리에 사용하면 좋습니다.
     * 실무에서 이런 경우는 실제로 Redis같은 것을 사용해서 캐싱한다거나 하는 방법으로 해결해야합니다.
     */
    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String username);

    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"),
            forCounting = true)
    Page<Member> findReadOnlyByUsername(String username, Pageable pageable);

    /**
     * JPA가 제공하는 lock을 어노테이션으로 사용할 수 있다.(내용이 깊고 많아서 JPA책을 참고하세요)
     * select ... for update
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String username);

    ////////////////////////////////////////////////////////////////////////////////////////

    List<UsernameOnly> findProjectionsByUsername(@Param("username") String username);

    List<UsernameOnlyDto> findProjectionsDtoByUsername(@Param("username") String username);
    <T> List<T> findProjectionsDtoV2ByUsername(@Param("username") String username, Class<T> type);

    ////////////////////////////////////////////////////////////////////////////////////////

    /**
     * JPA를 사용하는 입장에서
     * nativeQuery를 사용했다는 것은 대게 통계성 쿼리같은 복잡한 쿼리를 사용하여 데이터를 추려낼때 사용하는 경우가 대부분입니다.
     * 하지만 반환 타입이 몇개 밖에 지원되지 않아요.
     */
    @Query(value = "select * from member where username = :username", nativeQuery = true)
    Member findByNativeQuery(@Param("username") String username);

    @Query(value = "select m.member_id as id, m.username as username, t.name as teamName" +
            " from member m left join team t",
            countQuery = "select count(*) from member",
            nativeQuery = true)
    Page<MemberProjection> findByNativeProjection(Pageable pageable);

}
