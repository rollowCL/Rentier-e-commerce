package pl.coderslab.rentier.service;

import pl.coderslab.rentier.entity.Address;
import pl.coderslab.rentier.entity.User;

public interface UserService {

    void updateUserBillAddress(User user, Address sourceBillAddress);
    void updateUserShipAddress(User user, Address sourceShipAddress);

}
