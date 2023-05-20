package study.datajpa.repository;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.thymeleaf.util.StringUtils;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

public class MemberSpec {
    public static Specification<Member> teamName(final String teamName) {
        return (root, query, cb) -> {
            if (StringUtils.isEmpty(teamName)) {
                return null;
            }
            Join<Member, Team> t = root.join("team", JoinType.INNER);// 회원과 조인
            return cb.equal(t.get("name"), teamName);   // where
        };
    }

    public static Specification<Member> username(final String username) {
        return (root, query, cb) -> {
            if (StringUtils.isEmpty(username)) {
                return null;
            }
            return cb.equal(root.get("username"), username);
        };
    }

}
