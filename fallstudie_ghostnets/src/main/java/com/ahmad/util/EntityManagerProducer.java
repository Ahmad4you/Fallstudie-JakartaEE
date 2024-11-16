package com.ahmad.util;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceContext;

/**
 * 
 * @author Ahmad Alrefai
 */
@ApplicationScoped
public class EntityManagerProducer {

    @PersistenceContext(unitName = "AhmadPU")
    private EntityManager em;

    @Produces
    public EntityManager createEntityManager() {
        if (em == null) {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("AhmadPU");
            em = emf.createEntityManager();
        }
        return em;
    }
}