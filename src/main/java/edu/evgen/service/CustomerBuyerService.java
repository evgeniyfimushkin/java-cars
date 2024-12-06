package edu.evgen.service;

import edu.evgen.entity.CustomerBuyer;
import edu.evgen.repository.CarRepository;
import edu.evgen.repository.CustomerBuyerRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class CustomerBuyerService implements CrudService<CustomerBuyer> {

    private final CustomerBuyerRepository customerBuyerRepository;
    private final CarRepository carRepository;

    public List<CustomerBuyer> findAll() {
        return customerBuyerRepository.findAll();
    }

    public CustomerBuyer findById(Long id) {
        return customerBuyerRepository.findById(id);
    }

    public void saveOrUpdate(CustomerBuyer customerBuyer) {
        customerBuyerRepository.saveOrUpdate(customerBuyer);
        Optional.of(customerBuyer.getCar()).ifPresent(carRepository::saveOrUpdate);
    }


    public void deleteById(Long id) {
        customerBuyerRepository.deleteById(id);
    }
}
