package edu.evgen.repository;

import edu.evgen.entity.CustomerSeller;
import edu.evgen.service.HibernateService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CustomerSellerRepository implements CrudRepository<CustomerSeller> {


    private final HibernateService hibernateService;

    @Override
    public List<CustomerSeller> findAll() {
        return hibernateService.executeTransactionWithResult(session ->
                session.createQuery("from CustomerSeller", CustomerSeller.class).list()
        );

    }

    @Override
    public CustomerSeller findById(Long id) {
        return hibernateService.executeTransactionWithResult(session ->
                session.get(CustomerSeller.class, id)
        );

    }

    @Override
    public void saveOrUpdate(CustomerSeller customerSeller) {
        hibernateService.executeTransaction(session -> {
            session.merge(customerSeller);
        });
    }


    @Override
    public void deleteById(Long id) {
        hibernateService.executeTransaction(session -> {
            CustomerSeller customerSeller = session.get(CustomerSeller.class, id);
            if (customerSeller != null) {
                session.remove(customerSeller);
            }

        });
    }
}
