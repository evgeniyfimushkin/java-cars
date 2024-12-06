package edu.evgen.cli;

import edu.evgen.entity.*;
import edu.evgen.service.CrudService;
import jakarta.persistence.Id;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Field;
import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@RequiredArgsConstructor
public class CLI {

    private final List<CrudService> services;

    private Scanner scanner = new Scanner(System.in);

    public void start() {


        while (true) {

            clearScreen();
            System.out.println("=== Главное меню ===");
            System.out.println("1. Edit Cars");
            System.out.println("2. Edit Employees");
            System.out.println("3. Edit Spare Parts");
            System.out.println("4. Edit CustomerBuyers");
            System.out.println("5. Edit CustomerSellers");
            System.out.println("6. Edit Transfer");
            System.out.println("0. Выход");
            System.out.print("Выберите действие: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> handle(Car.class);
                case "2" -> handle(Employee.class);
                case "3" -> handle(SparePart.class);
                case "4" -> handle(CustomerBuyer.class);
                case "5" -> handle(CustomerSeller.class);
                case "6" -> handle(Transfer.class);
                case "0" -> {
                    return;
                }
                default -> {
                }
            }
        }
    }


    private void handle(Class _class) {

        clearScreen();
        System.out.println("=== Menu of " + _class.getSimpleName() + " ===");
        System.out.println("1. List of " + _class.getSimpleName() + "'s");
        System.out.println("2. Print " + _class.getSimpleName() + "'s details");
        System.out.println("3. Delete " + _class.getSimpleName());
        System.out.println("4. Update " + _class.getSimpleName());
        System.out.println("5. Add " + _class.getSimpleName());


        String choice = scanner.nextLine();

        switch (choice) {
            case "1" -> listOf(_class);
            case "2" -> printById(_class);
            case "3" -> deleteById(_class);
            case "4" -> update(_class);
            case "5" -> save(_class);
            default -> {
            }
        }
    }

    private CrudService getService(Class aClass) {
        return services.stream().filter(service -> service.getClass()
                        .getSimpleName().toLowerCase()
                        .contains(aClass.getSimpleName().toLowerCase()))
                .findFirst().orElseThrow();
    }

    private void listOf(Class aClass) {
        printAsTable(getService(aClass).findAll());
    }

    private void printById(Class aClass) {
        System.out.println("Enter id of " + aClass.getSimpleName());
        Long id = scanner.nextLong();
        Optional.ofNullable(getService(aClass).findById(id))
                .ifPresentOrElse(
                        System.out::println,
                        () -> System.out.println("not found")
                );
    }

    private void deleteById(Class aClass) {
        System.out.println("Enter id of " + aClass.getSimpleName());
        Long id = scanner.nextLong();
        getService(aClass).deleteById(id);
    }

    private void update(Class aClass) {

    }

    private void save(Class aClass) {
        try{
            Object object = aClass.getDeclaredConstructor().newInstance();
            for (Field field : aClass.getDeclaredFields()) {
                if (field.getAnnotation(Id.class) != null) {
                    continue;
                }
                field.setAccessible(true);
                System.out.print("Enter value for " + field.getName() + ": ");
                if (field.getType().equals(String.class)) {
                    String value = scanner.nextLine();
                    field.set(object, value);
                } else if (field.getType().equals(Long.class)) {
                    Long value = scanner.nextLong();
                    field.set(object, value);
                } else if (field.getType().equals(Double.class)) {
                    Double value = scanner.nextDouble();
                    field.set(object, value);
                } else if (field.getType().equals(Date.class)) {
                    System.out.println("Enter date (yyyy-MM-dd): ");
                    String dateStr = scanner.nextLine();
                    Date value = Date.valueOf(dateStr); // Преобразуем строку в дату
                    field.set(object, value);
                }else if (field.getType().equals(List.class)){

                }
                else{
                    handleRelatedObject(field, object);
                }
//                }else if (field.getType().equals(Employee.class)) {
//                    System.out.println("Enter employee id: ");
//                    Long id = scanner.nextLong();
//                    Optional.ofNullable ((Employee) getService(Employee.class).findById(id))
//                            .ifPresentOrElse(employee -> {
//                                try {
//                                    field.set(object,employee);
//                                } catch (IllegalAccessException e) {
//                                    throw new RuntimeException(e);
//                                }
//                            },() -> {
//                                save(Employee.class);
//                                try {
//                                    field.set(object,(Employee) getService(Employee.class).findById(id));
//                                } catch (IllegalAccessException e) {
//                                    throw new RuntimeException(e);
//                                }
//                            });
//                }
            }
            getService(aClass).saveOrUpdate(object);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleRelatedObject(Field field, Object parentObject) throws IllegalAccessException {
        System.out.println(field.getType()
        );
        printAsTable(getService(field.getType()).findAll());
        System.out.println("Enter ID for " + field.getType().getSimpleName() + ": ");
        Long id = Long.parseLong(scanner.nextLine().trim());
        clearScreen();
        // Попытка загрузить объект по ID
        Object relatedObject = getService(field.getType()).findById(id);
        if (relatedObject == null) {
            System.out.println(field.getType().getSimpleName() + " not found. Creating a new one...");
            save(field.getType());
            printAsTable(getService(field.getType()).findAll());
            System.out.println("Enter ID for " + field.getType().getSimpleName() + ": ");
            id = scanner.nextLong();
            relatedObject = getService(field.getType()).findById(id);
        }

        if (relatedObject != null) {
            field.set(parentObject, relatedObject);
        } else {
            System.out.println("Failed to set " + field.getName() + " field. Related object not found or created.");
        }
    }

    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    public <T> void printAsTable(List<T> objects) {
        var COLUMN_WIDTH = 8;

        if (objects == null || objects.isEmpty()) {
            System.out.println("No data available");
            return;
        }

        // Получаем класс объекта и его поля
        Class<?> clazz = objects.get(0).getClass();
        Field[] fields = clazz.getDeclaredFields();

        // Подготовка заголовков
        StringBuilder headerBuilder = new StringBuilder();
        StringBuilder separatorBuilder = new StringBuilder();

        for (Field field : fields) {
            field.setAccessible(true);
            String name = truncate(field.getName(), COLUMN_WIDTH);
            headerBuilder.append(String.format("| %-8s ", name));
            separatorBuilder.append("+----------");
        }

        // Закрытие строки
        headerBuilder.append("|");
        separatorBuilder.append("+");

        // Печать заголовков
        System.out.println(separatorBuilder);
        System.out.println(headerBuilder);
        System.out.println(separatorBuilder);

        // Печать строк данных
        for (T object : objects) {
            StringBuilder rowBuilder = new StringBuilder();
            for (Field field : fields) {
                try {
                    Object value = field.get(object);
                    String stringValue = value != null ? value.toString() : "NULL";
                    rowBuilder.append(String.format("| %-8s ", truncate(stringValue, COLUMN_WIDTH)));
                } catch (IllegalAccessException e) {
                    rowBuilder.append("| ERROR    ");
                }
            }
            rowBuilder.append("|");
            System.out.println(rowBuilder);
        }

        // Закрытие таблицы
        System.out.println(separatorBuilder);
    }
    private String truncate(String text, int maxLength) {
        if (text.length() > maxLength) {
            return text.substring(0, maxLength - 3) + "...";
        }
        return text;
    }
}