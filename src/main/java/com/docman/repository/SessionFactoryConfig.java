package com.docman.repository;

import com.docman.model.ContractEntity;
import com.docman.model.NotificationEntity;
import com.docman.model.PaymentEntity;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Класс для получения фабрики для создания соединений к БД
 */
@Configuration
public class SessionFactoryConfig implements DisposableBean {

    @Bean
    public SessionFactory sessionFactory() {
        org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration().configure();
        SessionFactory sessionFactory = configuration
                .addAnnotatedClass(ContractEntity.class)
                .addAnnotatedClass(PaymentEntity.class)
                .addAnnotatedClass(NotificationEntity.class)
                .buildSessionFactory(
                        new StandardServiceRegistryBuilder()
                                .applySettings(configuration.getProperties())
                                .build()
                );
        Runtime.getRuntime().addShutdownHook(new Thread(sessionFactory::close));
        return sessionFactory;
    }

    @Override
    public void destroy() {
        sessionFactory().close();
    }
}
