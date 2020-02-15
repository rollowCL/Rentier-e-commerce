package pl.coderslab.rentier.service;

import pl.coderslab.rentier.entity.User;

public interface RegisterService {

    void registerUser(User user);
    void sendActivationEmail(User user, String generatedToken);
    String generateActivationToken(int len);
    void saveToken(User user, String generatedToken);
    boolean validateToken(String token);
    void invalidateToken(String token);
    void makeUserVerified(String token);

}
