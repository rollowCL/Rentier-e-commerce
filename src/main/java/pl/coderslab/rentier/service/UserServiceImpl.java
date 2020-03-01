package pl.coderslab.rentier.service;

import org.springframework.stereotype.Service;
import pl.coderslab.rentier.entity.Address;
import pl.coderslab.rentier.entity.User;
import pl.coderslab.rentier.repository.AddressRepository;
import pl.coderslab.rentier.repository.UserRepository;
import pl.coderslab.rentier.utils.BCrypt;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    public UserServiceImpl(UserRepository userRepository, AddressRepository addressRepository) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
    }

    @Override
    public void updateUserBillAddress(User user, Address sourceBillAddress) {
        Address newUserBillAddress = new Address();

        if (user.getShipAddress() != null) {
            newUserBillAddress = user.getBillAddress();
        }

        newUserBillAddress.setZipCode(sourceBillAddress.getZipCode());
        newUserBillAddress.setCity(sourceBillAddress.getCity());
        newUserBillAddress.setStreet(sourceBillAddress.getStreet());
        newUserBillAddress.setStreetNumber(sourceBillAddress.getStreetNumber());

        user.setBillAddress(newUserBillAddress);
        userRepository.save(user);
        addressRepository.save(newUserBillAddress);

    }

    @Override
    public void updateUserShipAddress(User user, Address sourceShipAddress) {

        Address newUserShipAddress = new Address();

        if (user.getShipAddress() != null) {
            newUserShipAddress = user.getShipAddress();
        }

        newUserShipAddress.setZipCode(sourceShipAddress.getZipCode());
        newUserShipAddress.setCity(sourceShipAddress.getCity());
        newUserShipAddress.setStreet(sourceShipAddress.getStreet());
        newUserShipAddress.setStreetNumber(sourceShipAddress.getStreetNumber());

        user.setShipAddress(newUserShipAddress);
        userRepository.save(user);
        addressRepository.save(newUserShipAddress);

    }

    @Override
    public void updateUserPassword(User user) {
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        userRepository.updateUserPassword(user.getId(), user.getPassword());
    }
}
