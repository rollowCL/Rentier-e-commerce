package pl.coderslab.rentier.service;

import pl.coderslab.rentier.entity.User;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;


public interface RegisterService {

    void registerUser(User user, HttpServletRequest request) throws UnsupportedEncodingException;
    void makeUserVerified(String token);


}
