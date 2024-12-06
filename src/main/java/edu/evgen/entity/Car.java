package edu.evgen.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "car")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String brand; // Марка
    @NotNull
    private String model; // Модель
    @NotNull
    private String color; // Цвет
    private String engineNumber; // Номер двигателя
    private String registrationNumber; // Номер гос. регистрации
    private String bodyNumber; // Номер кузова
    private String chassisNumber; // Номер шасси

    @Temporal(TemporalType.DATE)
    private Date manufactureDate; // Дата выпуска

    private Double mileage; // Пробег
    private Double releasePrice; // Цена на момент выпуска
    private Double salePrice; // Цена продажи

    private Double purchasePrice; // Цена покупки (для подержанных)

    private String techConditionCertificateNumber; // Номер справки о техническом состоянии
    private String techConditionExpert; // Эксперт, выдавший справку

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<SparePart> spareParts;

    @Override
    public String toString() {
        return id.toString();
    }
}
