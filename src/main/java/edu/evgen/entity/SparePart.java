package edu.evgen.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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

    @ManyToOne
    private Car car; // Марка и модель автомобиля, к которому запчасть

    private String car_model;

    @NotNull
    private Double price; // Цена

    @NotNull
    private Integer quantityInStock; // Количество на складе
}

