package pl.coderslab.rentier.service;

import pl.coderslab.rentier.entity.User;

public interface LoginRegisterService {

    void sendActivationEmail(User user);

}
