package pl.coderslab.rentier.service;

import pl.coderslab.rentier.entity.User;

import javax.servlet.http.HttpServletRequest;


public interface ForgotPasswordService {

    String resetPasswordProcess(String email);


}
