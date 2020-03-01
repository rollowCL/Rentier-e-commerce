package pl.coderslab.rentier.service;

import pl.coderslab.rentier.entity.User;


public interface ForgotPasswordService {

    void resetPasswordProcess(String email);


}
