package edu.evgen.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "spare_part")
public class SparePart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name; // Наименование


    private String car_model;

    @NotNull
    private Double price; // Цена

    @NotNull
    private Long quantityInStock; // Количество на складе


    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn (name = "car_id")
    private Car car; // Марка и модель автомобиля, к которому запчасть

    @Override
    public String toString() {
        return id.toString();
    }
}

