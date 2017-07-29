/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proyecto.bean;

import com.proyecto.bean.exceptions.NonexistentEntityException;
import com.proyecto.bean.exceptions.RollbackFailureException;
import com.proyecto.modelo.EquipoInformatico;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.proyecto.modelo.Espacio;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author andre
 */
public class EquipoInformaticoJpaController implements Serializable {

    public EquipoInformaticoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(EquipoInformatico equipoInformatico) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Espacio espacio = equipoInformatico.getEspacio();
            if (espacio != null) {
                espacio = em.getReference(espacio.getClass(), espacio.getIdEspacio());
                equipoInformatico.setEspacio(espacio);
            }
            em.persist(equipoInformatico);
            if (espacio != null) {
                espacio.getEquipoInformaticos().add(equipoInformatico);
                espacio = em.merge(espacio);
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

    public void edit(EquipoInformatico equipoInformatico) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            EquipoInformatico persistentEquipoInformatico = em.find(EquipoInformatico.class, equipoInformatico.getIdEquipoInformatico());
            Espacio espacioOld = persistentEquipoInformatico.getEspacio();
            Espacio espacioNew = equipoInformatico.getEspacio();
            if (espacioNew != null) {
                espacioNew = em.getReference(espacioNew.getClass(), espacioNew.getIdEspacio());
                equipoInformatico.setEspacio(espacioNew);
            }
            equipoInformatico = em.merge(equipoInformatico);
            if (espacioOld != null && !espacioOld.equals(espacioNew)) {
                espacioOld.getEquipoInformaticos().remove(equipoInformatico);
                espacioOld = em.merge(espacioOld);
            }
            if (espacioNew != null && !espacioNew.equals(espacioOld)) {
                espacioNew.getEquipoInformaticos().add(equipoInformatico);
                espacioNew = em.merge(espacioNew);
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
                Integer id = equipoInformatico.getIdEquipoInformatico();
                if (findEquipoInformatico(id) == null) {
                    throw new NonexistentEntityException("The equipoInformatico with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            EquipoInformatico equipoInformatico;
            try {
                equipoInformatico = em.getReference(EquipoInformatico.class, id);
                equipoInformatico.getIdEquipoInformatico();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The equipoInformatico with id " + id + " no longer exists.", enfe);
            }
            Espacio espacio = equipoInformatico.getEspacio();
            if (espacio != null) {
                espacio.getEquipoInformaticos().remove(equipoInformatico);
                espacio = em.merge(espacio);
            }
            em.remove(equipoInformatico);
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

    public List<EquipoInformatico> findEquipoInformaticoEntities() {
        return findEquipoInformaticoEntities(true, -1, -1);
    }

    public List<EquipoInformatico> findEquipoInformaticoEntities(int maxResults, int firstResult) {
        return findEquipoInformaticoEntities(false, maxResults, firstResult);
    }

    private List<EquipoInformatico> findEquipoInformaticoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(EquipoInformatico.class));
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

    public EquipoInformatico findEquipoInformatico(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(EquipoInformatico.class, id);
        } finally {
            em.close();
        }
    }

    public int getEquipoInformaticoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<EquipoInformatico> rt = cq.from(EquipoInformatico.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
