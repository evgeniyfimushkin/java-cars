package edu.evgen.service;

import edu.evgen.entity.Employee;
import edu.evgen.repository.EmployeeRepository;
import edu.evgen.repository.TransferRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
public class EmployeeService implements CrudService<Employee> {
    final EmployeeRepository employeeRepository;
    final TransferRepository transferRepository;

    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    public Employee findById(Long id) {
        return employeeRepository.findById(id);
    }

    public void saveOrUpdate(Employee employee) {
        employeeRepository.saveOrUpdate(employee);
        Optional.ofNullable(employee.getTransfers())
                .ifPresent(
                        list -> list.forEach(transferRepository::saveOrUpdate));
    }

    public void deleteById(Long id) {
        employeeRepository.deleteById(id);
    }
}
