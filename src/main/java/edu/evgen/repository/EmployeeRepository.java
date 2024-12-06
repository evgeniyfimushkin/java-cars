package edu.evgen.repository;

import edu.evgen.entity.Employee;
import edu.evgen.service.HibernateService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class EmployeeRepository implements CrudRepository<Employee> {

    private final HibernateService hibernateService;

    @Override
    public List<Employee> findAll() {
        return hibernateService.executeTransactionWithResult(session ->
                session.createQuery("from Employee", Employee.class).list()
        );
    }

    @Override
    public Employee findById(Long id) {
        return hibernateService.executeTransactionWithResult(session ->
                session.get(Employee.class, id)
        );
    }

    @Override
    public void saveOrUpdate(Employee employee) {
        hibernateService.executeTransaction(session -> {
            session.merge(employee);
        });
    }

    @Override
    public void deleteById(Long id) {
        hibernateService.executeTransaction(session -> {
            Employee employee = session.get(Employee.class, id);
            if (employee != null) {
                session.remove(employee);
            }
        });
    }
}
