package com.docman.repository;

import com.docman.model.ContractEntity;

import java.util.List;

public class ContractRepository extends AbstractRepository {
    public static ContractRepository INSTANCE = new ContractRepository();
    private ContractRepository() { }

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
}
