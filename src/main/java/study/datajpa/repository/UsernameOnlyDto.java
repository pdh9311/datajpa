package study.datajpa.repository;

import lombok.Getter;

@Getter
public class UsernameOnlyDto {
    private final String username;

    /**
     * @param username : 이 값을 보고 판단합니다. projections
     */
    public UsernameOnlyDto(String username) {
        this.username = username;
    }

}
