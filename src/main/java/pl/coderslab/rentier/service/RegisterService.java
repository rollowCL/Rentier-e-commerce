package pl.coderslab.rentier.service;

import pl.coderslab.rentier.entity.User;

import java.util.Optional;

public interface RegisterService {

    void registerUser(User user);
    void updateUserPassword(User user);
    void sendActivationEmail(User user, String generatedToken);
    void sendPasswordReminderEmail(User user, String generatedToken);
    String generateActivationToken(int len);
    void saveToken(User user, String generatedToken, int tokenType);
    boolean validateToken(String token, int tokenType);
    void invalidateToken(String token);
    void makeUserVerified(String token);
    void resetPasswordProcess(String email);
    User getUserForToken(String token);
}
