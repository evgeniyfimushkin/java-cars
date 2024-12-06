package edu.evgen.service;

import edu.evgen.entity.CustomerSeller;
import edu.evgen.repository.CarRepository;
import edu.evgen.repository.CustomerSellerRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class CustomerSellerService implements CrudService<CustomerSeller> {

    private final CustomerSellerRepository customerSellerRepository;
    private final CarRepository carRepository;

    public List<CustomerSeller> findAll() {
        return customerSellerRepository.findAll();
    }

    public CustomerSeller findById(Long id) {
        return customerSellerRepository.findById(id);
    }

    public void saveOrUpdate(CustomerSeller customerSeller) {
        customerSellerRepository.saveOrUpdate(customerSeller);
        Optional.of(customerSeller.getCar()).ifPresent(carRepository::saveOrUpdate);
    }


    public void deleteById(Long id) {
        customerSellerRepository.deleteById(id);
    }
}
