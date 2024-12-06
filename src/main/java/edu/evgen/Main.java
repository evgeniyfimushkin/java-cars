package edu.evgen;

import edu.evgen.cli.CLI;
import edu.evgen.repository.*;
import edu.evgen.service.*;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        try (HibernateService hibernateService = new HibernateService();) {

            CarRepository carRepository = new CarRepository(hibernateService);
            EmployeeRepository employeeRepository = new EmployeeRepository(hibernateService);
            SparePartRepository sparePartRepository = new SparePartRepository(hibernateService);
            CustomerBuyerRepository customerBuyerRepository = new CustomerBuyerRepository(hibernateService);
            CustomerSellerRepository customerSellerRepository = new CustomerSellerRepository(hibernateService);
            TransferRepository transferRepository = new TransferRepository(hibernateService);

            CLI cli = new CLI(
                    List.of(
                            new CarService(carRepository, sparePartRepository),
                            new EmployeeService(employeeRepository, transferRepository),
                            new SparePartService(sparePartRepository, carRepository),
                            new CustomerBuyerService(customerBuyerRepository, carRepository),
                            new CustomerSellerService(customerSellerRepository, carRepository),
                            new TransferService(transferRepository, employeeRepository))
            );
            cli.start();
        }
    }
}