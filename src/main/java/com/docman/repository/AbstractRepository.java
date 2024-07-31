package com.docman.repository;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.function.Consumer;
import java.util.function.Function;

@RequiredArgsConstructor
public abstract class AbstractRepository {
    private final SessionFactory sessionFactory;

    /**
     * Выполнение операции внутри транзакции
     * @param action действие для выполнения внутри транзакции
     */
    protected void executeInTransaction(Consumer<Session> action) {
        executeInSession(session -> {
            Transaction tx = session.beginTransaction();
            action.accept(session);
            tx.commit();
            return null;
        });
    }

    /**
     * Выполнение операции без транзакции (read-only)
     * @param action действие операции
     * @return возвращаемое операцией значение
     */
    protected  <T> T executeInSession(Function<Session, T> action) {
        Session session = sessionFactory.openSession();
        T result = action.apply(session);
        session.close();
        return result;
    }
}
