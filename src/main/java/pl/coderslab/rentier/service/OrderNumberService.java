package pl.coderslab.rentier.service;

public interface OrderNumberService {
    Integer checkLastOrderNumber(int year);
    void registerOrderNumber(int year, int orderNumber);
}
