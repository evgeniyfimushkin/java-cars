package edu.evgen.service;

import edu.evgen.entity.Car;
import edu.evgen.entity.SparePart;
import edu.evgen.repository.CarRepository;
import edu.evgen.repository.SparePartRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class SparePartService implements CrudService<SparePart> {


    private final SparePartRepository sparePartRepository;
    private final CarRepository carRepository;

    public List<SparePart> findAll() {
        return sparePartRepository.findAll();
    }

    public SparePart findById(Long id) {
        return sparePartRepository.findById(id);
    }

    public void saveOrUpdate(SparePart sparePart) {
        sparePartRepository.saveOrUpdate(sparePart);
        carRepository.saveOrUpdate(sparePart.getCar());
    }

    public void deleteById(Long id) {
        sparePartRepository.deleteById(id);
    }
}
