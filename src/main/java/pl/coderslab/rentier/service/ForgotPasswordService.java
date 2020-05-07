package pl.coderslab.rentier.service;

import pl.coderslab.rentier.entity.User;

import javax.servlet.http.HttpServletRequest;


public interface ForgotPasswordService {

    void resetPasswordProcess(String email, HttpServletRequest request);


}
