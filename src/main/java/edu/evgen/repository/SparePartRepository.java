package edu.evgen.repository;

import edu.evgen.entity.SparePart;
import edu.evgen.service.HibernateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
public class SparePartRepository implements CrudRepository<SparePart>{

    private final HibernateService hibernateService;

    @Override
    public List<SparePart> findAll() {
        return hibernateService.executeTransactionWithResult(session ->
                session.createQuery("from SparePart", SparePart.class).list()
        );
    }

    @Override
    public SparePart findById(Long id) {
        return hibernateService.executeTransactionWithResult(session ->
                session.get(SparePart.class, id)
        );
    }

    @Override
    public void saveOrUpdate(SparePart part) {
        hibernateService.executeTransaction(session -> {
            session.merge(part);
        });
    }
    @Override
    public void deleteById(Long id) {
        hibernateService.executeTransaction(session -> {
            SparePart part = session.get(SparePart.class, id);
            if (part != null) {
                session.remove(part);
            }

        });
    }
}
