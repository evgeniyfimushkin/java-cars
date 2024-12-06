package edu.evgen.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String firstName; // Имя
    @NotNull
    private String lastName;  // Фамилия
    @NotNull
    private String patronymic; // Отчество

    @NotNull
    private String address; // Адрес

    @NotNull
    @Temporal(TemporalType.DATE)
    private Date birthDate; // Дата рождения

    @NotNull
    private String position; // Должность
    @NotNull
    private Double salary; // Оклад

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Transfer> transfers; // Сведения о перемещении (переводы)

    @Override
    public String toString() {
        return id.toString();
    }
}
