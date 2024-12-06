package edu.evgen.repository;

import edu.evgen.entity.Car;
import edu.evgen.service.HibernateService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CarRepository implements CrudRepository<Car> {

    private final HibernateService hibernateService;
    @Override
    public List<Car> findAll() {
        return hibernateService.executeTransactionWithResult(session ->
                session.createQuery("from Car", Car.class).list()
        );
    }
    @Override
    public Car findById(Long id) {
        return hibernateService.executeTransactionWithResult(session ->
                session.get(Car.class, id)
        );
    }
    @Override
    public void saveOrUpdate(Car car) {
        hibernateService.executeTransaction(session -> {
            session.merge(car);
        });
    }
    @Override
    public void deleteById(Long id) {
        hibernateService.executeTransaction(session -> {
            Car car = session.get(Car.class, id);
            if (car != null) {
                session.remove(car);
            }

        });
    }
}
