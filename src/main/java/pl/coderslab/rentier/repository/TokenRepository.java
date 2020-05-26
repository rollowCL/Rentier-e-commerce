package pl.coderslab.rentier.repository;

import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import pl.coderslab.rentier.entity.Brand;
import pl.coderslab.rentier.entity.Token;

import java.time.LocalDateTime;
import java.util.List;

public interface TokenRepository extends JpaRepository<Token, Long> {

    boolean existsTokenByTokenValueEqualsAndValidTrueAndExpiryDateAfterAndTokenType(String token, LocalDateTime now, int tokenType);
    Token findTokenByTokenValue(String tokenValue);

    @Modifying
    @Transactional
    @Query(value = "UPDATE tokens t SET t.valid = false WHERE t.token_value =:tokenValue", nativeQuery = true)
    void invalidateToken(@Param("tokenValue") String tokenValue);

    @Modifying
    @Transactional
    @Query(value = "UPDATE tokens t SET t.valid = false WHERE t.user_id =:userId AND t.token_type =:tokenType", nativeQuery = true)
    void invalidateUserTokens(@Param("userId") Long userId, @Param("tokenType") int tokenType);
}
