package edu.evgen.repository;

import edu.evgen.entity.Transfer;
import edu.evgen.service.HibernateService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor

public class TransferRepository implements CrudRepository<Transfer> {

    private final HibernateService hibernateService;

    @Override
    public List<Transfer> findAll() {
        return hibernateService.executeTransactionWithResult(session ->
                session.createQuery("from Transfer", Transfer.class).list()
        );
    }

    @Override
    public Transfer findById(Long id) {
        return hibernateService.executeTransactionWithResult(session ->
                session.get(Transfer.class, id)
        );
    }

    @Override
    public void saveOrUpdate(Transfer transfer) {
        hibernateService.executeTransaction(session -> {
            session.merge(transfer);
        });
    }

    @Override
    public void deleteById(Long id) {
        hibernateService.executeTransaction(session -> {
            Transfer transfer = session.get(Transfer.class, id);
            if (transfer != null) {
                session.remove(transfer);
            }
        });
    }
}
