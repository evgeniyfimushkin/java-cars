package edu.evgen.service;

import edu.evgen.entity.Car;
import edu.evgen.repository.CarRepository;
import edu.evgen.repository.SparePartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class CarService implements CrudService<Car>{

    private final CarRepository carRepository;
    private final SparePartRepository sparePartRepository;

    public List<Car> findAll() {
        return carRepository.findAll();
    }

    public Car findById(Long id) {
        return carRepository.findById(id);
    }

    public void saveOrUpdate(Car car) {
        carRepository.saveOrUpdate(car);
        Optional.ofNullable(car.getSpareParts())
                .ifPresent(spareParts -> spareParts.forEach(sparePartRepository::saveOrUpdate));
    }

    public void deleteById(Long id) {
        carRepository.deleteById(id);
    }
}
