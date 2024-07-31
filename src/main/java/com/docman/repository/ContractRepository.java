package com.docman.repository;

import com.docman.model.ContractEntity;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Репозиторий для работы с договорами
 */
@Repository
public class ContractRepository extends AbstractRepository {

    public ContractRepository(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

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

    public void updateByIdAddRemainingValue(long id, long add) {
        executeInTransaction(session -> {
            //noinspection rawtypes
            Query query = session.createQuery(
                    "update from ContractEntity c set c.remainingValue = c.remainingValue + :add where c.id = :id"
            );
            query.setParameter("id", id);
            query.setParameter("add", add);
            query.executeUpdate();
        });
    }
}
