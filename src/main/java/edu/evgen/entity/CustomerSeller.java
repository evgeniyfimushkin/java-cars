package edu.evgen.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customer_seller")
public class CustomerSeller {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String passportData; // Паспортные данные

    @OneToOne
    @NotNull
    @JoinColumn(name = "car_id")
    private Car car; // Автомобиль

    @NotNull
    @Temporal(TemporalType.DATE)
    private Date purchaseDate; // Дата покупки

    @NotNull
    private String documentName; // Документ, удостоверяющий право собственности

    @NotNull
    private String documentNumber; // Номер документа

    @NotNull
    @Temporal(TemporalType.DATE)
    private Date documentIssueDate; // Дата выдачи документа

    @NotNull
    private String issuedBy; // Кем выдан документ
    @Override
    public String toString() {
        return id.toString();
    }
}
