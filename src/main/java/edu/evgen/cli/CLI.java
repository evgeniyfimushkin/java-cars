package edu.evgen.cli;

import edu.evgen.entity.*;
import edu.evgen.service.CrudService;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Field;
import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.IntStream;

import static edu.evgen.cli.ColorPrinter.*;

@RequiredArgsConstructor
public class CLI {

    private final List<CrudService> services;

    private Scanner scanner = new Scanner(System.in);

    private Integer COLUMN_WIDTH = 8;

    public void start() {


        while (true) {
            System.out.print("Enter to continue...");
            scanner.nextLine();
            clearScreen();
            System.out.println("=== Главное меню ===");
            System.out.println("1. Edit Cars");
            System.out.println("2. Edit Employees");
            System.out.println("3. Edit Spare Parts");
            System.out.println("4. Edit CustomerBuyers");
            System.out.println("5. Edit CustomerSellers");
            System.out.println("6. Edit Transfer");
            System.out.println("99. Edit Column size");
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
                case "99" -> {
                    System.out.println("Enter column size: ");
                    COLUMN_WIDTH = Integer.parseInt(scanner.nextLine());
                }
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
        try {
            printAsTable(getService(aClass).findAll());
            System.out.println("Enter id of " + aClass.getSimpleName());
            Long id = scanner.nextLong();
            Object object;
            if ((object = getService(aClass).findById(id)) == null) {
                System.out.println("not found");
                id = scanner.nextLong();
            }


            for (Field field : aClass.getDeclaredFields()) {
                if (field.getAnnotation(Id.class) != null) {
                    continue;
                }
                field.setAccessible(true);
                while (true) {
                    System.out.print("Enter value for " + field.getName() + ": ");
                    String input = scanner.nextLine().trim();

                    if (input.isEmpty()) {
                        System.out.println("Value will not change");
                        break;
                    }
                    try {
                        setFieldValue(field, object, input);
                        break;
                    } catch (Exception e) {
                        System.out.println("Bad value: " + e.getMessage() + ". Try again");
                    }

                }
            }
            getService(aClass).saveOrUpdate(object);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private void save(Class aClass) {
        try {
            Object object = aClass.getDeclaredConstructor().newInstance();
            for (Field field : aClass.getDeclaredFields()) {
                if (field.getAnnotation(Id.class) != null) {
                    continue;
                }
                field.setAccessible(true);
                boolean isNotNull = field.isAnnotationPresent(NotNull.class);
                while (true) {
                    System.out.print("Enter value for " + field.getName() + ": ");
                    String input = scanner.nextLine().trim();

                    if (input.isEmpty() && !isNotNull) {
                        printBlue("Field will be empty");
                        break;
                    } else if (input.isEmpty()) {
                        printRed("Field can't be empty");
                        continue;
                    }


                    try {
                        setFieldValue(field, object, input);
                        break;
                    } catch (Exception e) {
                        System.out.println("Bad value: " + e.getMessage() + ". Try again");
                    }

                }
            }
            getService(aClass).saveOrUpdate(object);
            printBlue("Table note Added");
            printAsTable(List.of(object));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setFieldValue(Field field, Object object, String input) throws IllegalAccessException {
        if (field.getType().equals(String.class)) {
            field.set(object, input);
        } else if (field.getType().equals(Long.class)) {
            field.set(object, Long.parseLong(input));
        } else if (field.getType().equals(Integer.class)) {
            field.set(object, Integer.parseInt(input));
        } else if (field.getType().equals(Double.class)) {
            field.set(object, Double.parseDouble(input));
        } else if (field.getType().equals(Date.class)) {
            field.set(object, Date.valueOf(input));
        } else if (field.getType().equals(List.class)) {
        } else {
            handleRelatedObject(field, object, input);
        }
    }

    private void handleRelatedObject(Field field, Object parentObject, String input) throws IllegalAccessException {
        printBlue(field.getType().getSimpleName() + " full table:");
        printAsTable(getService(field.getType()).findAll());
//        System.out.println("Enter ID for " + field.getType().getSimpleName() + ": ");
        Long id = null;
        while (id == null) {

            if (!input.isEmpty()) {
                try {
                    id = Long.parseLong(input);
                    break;
                } catch (NumberFormatException e) {
                    printRed("Invalid input. Please enter a valid ID number.");
                }
            } else {
                printRed("Input cannot be empty. Please enter a valid ID number.");
            }
            System.out.println("Enter ID for " + field.getType().getSimpleName() + ": ");
            input = scanner.nextLine().trim();
        }

        Object relatedObject = getService(field.getType()).findById(id);
        if (relatedObject == null) {
            System.out.println(field.getType().getSimpleName() + " not found.");
            printBlue("Creating a new " + field.getType().getSimpleName() + "...");
            save(field.getType());
            printBlue(field.getType().getSimpleName() + " full table:");
            printAsTable(getService(field.getType()).findAll());
            System.out.println("Enter ID for " + field.getType().getSimpleName() + ": ");
            id = scanner.nextLong();
            relatedObject = getService(field.getType()).findById(id);
        }

        if (relatedObject != null) {
            field.set(parentObject, relatedObject);
            System.out.println("RELATED OBJECT + " + relatedObject);
        } else {
            System.out.println("Failed to set " + field.getName() + " field. Related object not found or created.");
        }
    }

    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public <T> void printAsTable(List<T> objects) {

        if (objects == null || objects.isEmpty()) {
            printRed("No data available");
            return;
        }


        Class<?> clazz = objects.get(0).getClass();
        Field[] fields = clazz.getDeclaredFields();


        StringBuilder headerBuilder = new StringBuilder();
        StringBuilder separatorBuilder = new StringBuilder();

        for (Field field : fields) {
            field.setAccessible(true);
            String name = truncate(field.getName(), COLUMN_WIDTH);
            headerBuilder.append(String.format("| %-" + COLUMN_WIDTH + "s ", name));
            separatorBuilder.append("+--");
            IntStream.range(0, COLUMN_WIDTH)
                    .forEach(n -> separatorBuilder.append("-"));
        }


        headerBuilder.append("|");
        separatorBuilder.append("+");


        System.out.println(separatorBuilder);
        System.out.println(headerBuilder);
        System.out.println(separatorBuilder);


        for (T object : objects) {
            StringBuilder rowBuilder = new StringBuilder();
            for (Field field : fields) {
                try {
                    Object value = field.get(object);
                    String stringValue = value != null ? value.toString() : "NULL";
                    rowBuilder.append(String.format("| %-" + COLUMN_WIDTH + "s ", truncate(stringValue, COLUMN_WIDTH)));
                } catch (IllegalAccessException e) {
                    rowBuilder.append("| ERROR    ");
                }
            }
            rowBuilder.append("|");
            System.out.println(rowBuilder);
        }


        System.out.println(separatorBuilder);
    }

    private String truncate(String text, int maxLength) {
        if (text.length() > maxLength) {
            return text.substring(0, maxLength - 3) + "...";
        }
        return text;
    }
}