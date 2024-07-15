package com.docman.repository;

import com.docman.model.PaymentEntity;
import org.hibernate.query.Query;

import java.util.List;

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
}
