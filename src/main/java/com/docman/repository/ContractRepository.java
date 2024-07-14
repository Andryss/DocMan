package com.docman.repository;

import com.docman.model.ContractEntity;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class ContractRepository {
    public static ContractRepository INSTANCE = new ContractRepository();
    private ContractRepository() { }

    private final SessionFactory sessionFactory = SessionFactoryHolder.get();

    public List<ContractEntity> findAll() {
        return executeInSession(session ->
                session.createQuery("select c from ContractEntity c", ContractEntity.class).list());
    }

    public void save(ContractEntity contract) {
        executeInTransaction(session -> session.persist(contract));
    }

    public void update(ContractEntity contract) {
        executeInTransaction(session -> session.update(contract));
    }

    private void executeInTransaction(Consumer<Session> action) {
        executeInSession(session -> {
            Transaction tx = session.beginTransaction();
            action.accept(session);
            tx.commit();
            return null;
        });
    }

    private <T> T executeInSession(Function<Session, T> action) {
        Session session = sessionFactory.openSession();
        T result = action.apply(session);
        session.close();
        return result;
    }
}
