package pl.coderslab.rentier.service;

import pl.coderslab.rentier.entity.User;

public interface TokenService {

    void saveToken(User user, String generatedToken, int tokenType);
    boolean validateToken(String token, int tokenType);
    void invalidateToken(String token);
    User getUserForToken(String token);
    String generateToken(int len);
    void invalidateAllUserResetTokens(User user);
}
