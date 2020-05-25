package pl.coderslab.rentier.service;

import pl.coderslab.rentier.entity.User;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;


public interface RegisterService {

    String registerUser(User user);
    void makeUserVerified(String token);


}
