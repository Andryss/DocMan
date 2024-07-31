package com.docman.repository;

import com.docman.model.NotificationEntity;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

/**
 * Репозиторий для работы с уведомлениями
 */
@Repository
public class NotificationRepository extends AbstractRepository {

    public NotificationRepository(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public void saveAll(List<NotificationEntity> notifications) {
        executeInTransaction(session -> notifications.forEach(session::persist));
    }

    public void deleteAllNotShownByContractId(long contractId) {
        executeInTransaction(session -> {
            //noinspection rawtypes
            Query query = session.createQuery(
                    "delete from NotificationEntity n where n.contractId = :contractId and n.isShown is false"
            );
            query.setParameter("contractId", contractId);
            query.executeUpdate();
        });
    }

    public List<NotificationEntity> findAllByContractId(long contractId) {
        return executeInSession(session -> {
            Query<NotificationEntity> query = session.createQuery(
                    "select n from NotificationEntity n where n.contractId = :contractId",
                    NotificationEntity.class
            );
            query.setParameter("contractId", contractId);
            return query.list();
        });
    }

    public List<NotificationEntity> findAllNotShownByTimeoutBefore(Instant timeout) {
        return executeInSession(session -> {
            Query<NotificationEntity> query = session.createQuery(
                    "select n from NotificationEntity n where n.timeout < :timeout and n.isShown is false",
                    NotificationEntity.class
            );
            query.setParameter("timeout", timeout);
            return query.list();
        });
    }

    public void setShownByIds(List<Long> ids) {
        executeInTransaction(session -> {
            //noinspection rawtypes
            Query query = session.createQuery(
                    "update from NotificationEntity n set n.isShown = true where n.id in :ids"
            );
            query.setParameterList("ids", ids);
            query.executeUpdate();
        });
    }
}
