package com.docman.repository;

import com.docman.model.ContractEntity;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class SessionFactoryHolder {
    private static SessionFactory sessionFactory;

    public static SessionFactory get() {
        if (sessionFactory == null) {
            Configuration configuration = new Configuration().configure();
            sessionFactory = configuration
                    .addAnnotatedClass(ContractEntity.class)
                    .buildSessionFactory(
                            new StandardServiceRegistryBuilder()
                                    .applySettings(configuration.getProperties())
                                    .build()
                    );
        }
        return sessionFactory;
    }
}
