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
@Table(name = "transfer")
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String position; // Должность на новом месте
    @NotNull
    private String transferReason; // Причина перевода
    @NotNull
    private String orderNumber; // Номер приказа

    @NotNull
    @Temporal(TemporalType.DATE)
    private Date orderDate; // Дата приказа

    @NotNull
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee; // Сотрудник

    @Override
    public String toString() {
        return id.toString();
    }
}
