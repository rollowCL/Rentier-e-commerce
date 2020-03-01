package pl.coderslab.rentier.service;

import pl.coderslab.rentier.entity.User;


public interface RegisterService {

    void registerUser(User user);
    void makeUserVerified(String token);


}
