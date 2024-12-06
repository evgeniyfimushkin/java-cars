package edu.evgen.service;

import jakarta.persistence.Entity;
import lombok.Data;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.reflections.Reflections;

import java.util.function.Consumer;
import java.util.function.Function;

@Data
public class HibernateService implements AutoCloseable {
    private SessionFactory sessionFactory;

    public HibernateService() {
        final StandardServiceRegistry registry =
                new StandardServiceRegistryBuilder()
                        .build();
        try {
            MetadataSources metadataSources = new MetadataSources(registry);

            // Сканируем пакет на наличие классов с аннотацией @Entity
            Reflections reflections = new Reflections("edu.evgen.entity");
            reflections.getTypesAnnotatedWith(Entity.class)
                    .stream()
                    .forEach(metadataSources::addAnnotatedClass);
            sessionFactory = metadataSources.buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    public void executeTransaction(Consumer<Session> action) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                action.accept(session);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                throw e;
            }
        }
    }

    public <T> T executeTransactionWithResult(Function<Session, T> action) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                T result = action.apply(session);
                transaction.commit();
                return result;
            } catch (Exception e) {
                transaction.rollback();
                throw e;
            }
        }
    }

    @Override
    public void close() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}
