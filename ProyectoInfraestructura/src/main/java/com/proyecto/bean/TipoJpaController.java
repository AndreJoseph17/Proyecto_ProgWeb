/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proyecto.bean;

import com.proyecto.bean.exceptions.IllegalOrphanException;
import com.proyecto.bean.exceptions.NonexistentEntityException;
import com.proyecto.bean.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.proyecto.modelo.Espacio;
import com.proyecto.modelo.Tipo;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author andre
 */
public class TipoJpaController implements Serializable {

    public TipoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tipo tipo) throws RollbackFailureException, Exception {
        if (tipo.getEspacios() == null) {
            tipo.setEspacios(new HashSet<Espacio>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Set<Espacio> attachedEspacios = new HashSet<Espacio>();
            for (Espacio espaciosEspacioToAttach : tipo.getEspacios()) {
                espaciosEspacioToAttach = em.getReference(espaciosEspacioToAttach.getClass(), espaciosEspacioToAttach.getIdEspacio());
                attachedEspacios.add(espaciosEspacioToAttach);
            }
            tipo.setEspacios(attachedEspacios);
            em.persist(tipo);
            for (Espacio espaciosEspacio : tipo.getEspacios()) {
                Tipo oldTipoOfEspaciosEspacio = espaciosEspacio.getTipo();
                espaciosEspacio.setTipo(tipo);
                espaciosEspacio = em.merge(espaciosEspacio);
                if (oldTipoOfEspaciosEspacio != null) {
                    oldTipoOfEspaciosEspacio.getEspacios().remove(espaciosEspacio);
                    oldTipoOfEspaciosEspacio = em.merge(oldTipoOfEspaciosEspacio);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tipo tipo) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Tipo persistentTipo = em.find(Tipo.class, tipo.getIdTipo());
            Set<Espacio> espaciosOld = persistentTipo.getEspacios();
            Set<Espacio> espaciosNew = tipo.getEspacios();
            List<String> illegalOrphanMessages = null;
            for (Espacio espaciosOldEspacio : espaciosOld) {
                if (!espaciosNew.contains(espaciosOldEspacio)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Espacio " + espaciosOldEspacio + " since its tipo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Set<Espacio> attachedEspaciosNew = new HashSet<Espacio>();
            for (Espacio espaciosNewEspacioToAttach : espaciosNew) {
                espaciosNewEspacioToAttach = em.getReference(espaciosNewEspacioToAttach.getClass(), espaciosNewEspacioToAttach.getIdEspacio());
                attachedEspaciosNew.add(espaciosNewEspacioToAttach);
            }
            espaciosNew = attachedEspaciosNew;
            tipo.setEspacios(espaciosNew);
            tipo = em.merge(tipo);
            for (Espacio espaciosNewEspacio : espaciosNew) {
                if (!espaciosOld.contains(espaciosNewEspacio)) {
                    Tipo oldTipoOfEspaciosNewEspacio = espaciosNewEspacio.getTipo();
                    espaciosNewEspacio.setTipo(tipo);
                    espaciosNewEspacio = em.merge(espaciosNewEspacio);
                    if (oldTipoOfEspaciosNewEspacio != null && !oldTipoOfEspaciosNewEspacio.equals(tipo)) {
                        oldTipoOfEspaciosNewEspacio.getEspacios().remove(espaciosNewEspacio);
                        oldTipoOfEspaciosNewEspacio = em.merge(oldTipoOfEspaciosNewEspacio);
                    }
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tipo.getIdTipo();
                if (findTipo(id) == null) {
                    throw new NonexistentEntityException("The tipo with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Tipo tipo;
            try {
                tipo = em.getReference(Tipo.class, id);
                tipo.getIdTipo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipo with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Set<Espacio> espaciosOrphanCheck = tipo.getEspacios();
            for (Espacio espaciosOrphanCheckEspacio : espaciosOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tipo (" + tipo + ") cannot be destroyed since the Espacio " + espaciosOrphanCheckEspacio + " in its espacios field has a non-nullable tipo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tipo);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tipo> findTipoEntities() {
        return findTipoEntities(true, -1, -1);
    }

    public List<Tipo> findTipoEntities(int maxResults, int firstResult) {
        return findTipoEntities(false, maxResults, firstResult);
    }

    private List<Tipo> findTipoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tipo.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Tipo findTipo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tipo.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tipo> rt = cq.from(Tipo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
