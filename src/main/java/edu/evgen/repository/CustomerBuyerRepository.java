package edu.evgen.repository;

import edu.evgen.entity.CustomerBuyer;
import edu.evgen.service.HibernateService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CustomerBuyerRepository implements CrudRepository<CustomerBuyer> {

    private final HibernateService hibernateService;

    @Override
    public List<CustomerBuyer> findAll() {
        return hibernateService.executeTransactionWithResult(session ->
                session.createQuery("from CustomerBuyer", CustomerBuyer.class).list()
        );
    }

    @Override
    public CustomerBuyer findById(Long id) {
        return hibernateService.executeTransactionWithResult(session ->
                session.get(CustomerBuyer.class, id)
        );
    }

    @Override
    public void saveOrUpdate(CustomerBuyer customerBuyer) {
        hibernateService.executeTransaction(session -> {
            session.merge(customerBuyer);
        });
    }

    @Override
    public void deleteById(Long id) {
        hibernateService.executeTransaction(session -> {
            CustomerBuyer customerBuyer = session.get(CustomerBuyer.class, id);
            if (customerBuyer != null) {
                session.remove(customerBuyer);
            }

        });
    }
}
