package com.docman.repository;

import com.docman.model.ContractEntity;
import com.docman.model.NotificationEntity;
import com.docman.model.PaymentEntity;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

/**
 * Класс для получения фабрики для создания соединений к БД
 */
public class SessionFactoryHolder {
    private static SessionFactory sessionFactory;

    public static SessionFactory get() {
        if (sessionFactory == null) {
            Configuration configuration = new Configuration().configure();
            sessionFactory = configuration
                    .addAnnotatedClass(ContractEntity.class)
                    .addAnnotatedClass(PaymentEntity.class)
                    .addAnnotatedClass(NotificationEntity.class)
                    .buildSessionFactory(
                            new StandardServiceRegistryBuilder()
                                    .applySettings(configuration.getProperties())
                                    .build()
                    );
            Runtime.getRuntime().addShutdownHook(new Thread(() -> sessionFactory.close()));
        }
        return sessionFactory;
    }
}
