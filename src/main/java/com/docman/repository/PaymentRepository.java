package com.docman.repository;

import com.docman.model.PaymentEntity;
import org.hibernate.query.Query;

import java.util.List;

/**
 * Репозиторий для работы с платежами
 */
public class PaymentRepository extends AbstractRepository {
    public static PaymentRepository INSTANCE = new PaymentRepository();
    private PaymentRepository() { }

    public List<PaymentEntity> findAllByContractId(long contractId) {
        return executeInSession(session -> {
            Query<PaymentEntity> query = session.createQuery(
                    "select p from PaymentEntity p where p.contractId = :contractId order by p.date desc",
                    PaymentEntity.class
            );
            query.setParameter("contractId", contractId);
            return query.list();
        });
    }

    public void save(PaymentEntity payment) {
        executeInTransaction(session -> session.persist(payment));
    }

    public void update(PaymentEntity payment) {
        executeInTransaction(session -> session.update(payment));
    }

    public void setPaid(long id, boolean paid) {
        executeInTransaction(session -> {
            //noinspection rawtypes
            Query query = session.createQuery(
                    "update from PaymentEntity p set p.paid = :paid where p.id = :id"
            );
            query.setParameter("id", id);
            query.setParameter("paid", paid);
            query.executeUpdate();
        });
    }
}
