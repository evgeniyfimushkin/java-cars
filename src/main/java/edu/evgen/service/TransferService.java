package edu.evgen.service;

import edu.evgen.entity.Transfer;
import edu.evgen.repository.EmployeeRepository;
import edu.evgen.repository.TransferRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class TransferService implements CrudService<Transfer> {

    private final TransferRepository transferRepository;
    private final EmployeeRepository employeeRepository;

    public List<Transfer> findAll() {
        return transferRepository.findAll();
    }

    public Transfer findById(Long id) {
        return transferRepository.findById(id);
    }

    public void saveOrUpdate(Transfer transfer) {
        transferRepository.saveOrUpdate(transfer);
        employeeRepository.saveOrUpdate(transfer.getEmployee());
    }
    public void deleteById(Long id) {
        transferRepository.deleteById(id);
    }
}
