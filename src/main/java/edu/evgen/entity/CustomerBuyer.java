package edu.evgen.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "customer_buyer")
public class CustomerBuyer {
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
    private Date saleDate; // Дата продажи

    @NotNull
    private String invoiceNumber; // Номер счета

    @NotNull
    private String paymentType; // Вид оплаты
}
