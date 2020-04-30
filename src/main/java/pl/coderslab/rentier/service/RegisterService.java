package pl.coderslab.rentier.service;

import pl.coderslab.rentier.entity.User;

import java.io.UnsupportedEncodingException;


public interface RegisterService {

    void registerUser(User user) throws UnsupportedEncodingException;
    void makeUserVerified(String token);


}
