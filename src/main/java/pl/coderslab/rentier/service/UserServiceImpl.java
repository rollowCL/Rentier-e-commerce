package pl.coderslab.rentier.service;

import org.springframework.stereotype.Service;
import pl.coderslab.rentier.entity.Address;
import pl.coderslab.rentier.entity.User;
import pl.coderslab.rentier.repository.AddressRepository;
import pl.coderslab.rentier.repository.UserRepository;

import javax.persistence.SequenceGenerators;

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

        Address userBillAddress = new Address();
        userBillAddress.setZipCode(sourceBillAddress.getZipCode());
        userBillAddress.setCity(sourceBillAddress.getCity());
        userBillAddress.setStreet(sourceBillAddress.getStreet());
        userBillAddress.setStreetNumber(sourceBillAddress.getStreetNumber());
        user.setBillAddress(userBillAddress);
        userRepository.save(user);
        addressRepository.save(userBillAddress);


    }

    @Override
    public void updateUserShipAddress(User user, Address sourceShipAddress) {

        Address userShipAddress = new Address();
        userShipAddress.setZipCode(sourceShipAddress.getZipCode());
        userShipAddress.setCity(sourceShipAddress.getCity());
        userShipAddress.setStreet(sourceShipAddress.getStreet());
        userShipAddress.setStreetNumber(sourceShipAddress.getStreetNumber());
        user.setShipAddress(userShipAddress);
        userRepository.save(user);
        addressRepository.save(userShipAddress);

    }
}
