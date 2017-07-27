/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proyecto.bean;

import com.proyecto.bean.exceptions.NonexistentEntityException;
import com.proyecto.bean.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.proyecto.modelo.Formula;
import com.proyecto.modelo.Espacio;
import com.proyecto.modelo.Normativa;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author andre
 */
public class NormativaJpaController implements Serializable {

    public NormativaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Normativa normativa) throws RollbackFailureException, Exception {
        if (normativa.getEspacios() == null) {
            normativa.setEspacios(new HashSet<Espacio>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Formula formula = normativa.getFormula();
            if (formula != null) {
                formula = em.getReference(formula.getClass(), formula.getIdFormula());
                normativa.setFormula(formula);
            }
            Set<Espacio> attachedEspacios = new HashSet<Espacio>();
            for (Espacio espaciosEspacioToAttach : normativa.getEspacios()) {
                espaciosEspacioToAttach = em.getReference(espaciosEspacioToAttach.getClass(), espaciosEspacioToAttach.getIdEspacio());
                attachedEspacios.add(espaciosEspacioToAttach);
            }
            normativa.setEspacios(attachedEspacios);
            em.persist(normativa);
            if (formula != null) {
                formula.getNormativas().add(normativa);
                formula = em.merge(formula);
            }
            for (Espacio espaciosEspacio : normativa.getEspacios()) {
                Normativa oldNormativaOfEspaciosEspacio = espaciosEspacio.getNormativa();
                espaciosEspacio.setNormativa(normativa);
                espaciosEspacio = em.merge(espaciosEspacio);
                if (oldNormativaOfEspaciosEspacio != null) {
                    oldNormativaOfEspaciosEspacio.getEspacios().remove(espaciosEspacio);
                    oldNormativaOfEspaciosEspacio = em.merge(oldNormativaOfEspaciosEspacio);
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

    public void edit(Normativa normativa) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Normativa persistentNormativa = em.find(Normativa.class, normativa.getIdNormativa());
            Formula formulaOld = persistentNormativa.getFormula();
            Formula formulaNew = normativa.getFormula();
            Set<Espacio> espaciosOld = persistentNormativa.getEspacios();
            Set<Espacio> espaciosNew = normativa.getEspacios();
            if (formulaNew != null) {
                formulaNew = em.getReference(formulaNew.getClass(), formulaNew.getIdFormula());
                normativa.setFormula(formulaNew);
            }
            Set<Espacio> attachedEspaciosNew = new HashSet<Espacio>();
            for (Espacio espaciosNewEspacioToAttach : espaciosNew) {
                espaciosNewEspacioToAttach = em.getReference(espaciosNewEspacioToAttach.getClass(), espaciosNewEspacioToAttach.getIdEspacio());
                attachedEspaciosNew.add(espaciosNewEspacioToAttach);
            }
            espaciosNew = attachedEspaciosNew;
            normativa.setEspacios(espaciosNew);
            normativa = em.merge(normativa);
            if (formulaOld != null && !formulaOld.equals(formulaNew)) {
                formulaOld.getNormativas().remove(normativa);
                formulaOld = em.merge(formulaOld);
            }
            if (formulaNew != null && !formulaNew.equals(formulaOld)) {
                formulaNew.getNormativas().add(normativa);
                formulaNew = em.merge(formulaNew);
            }
            for (Espacio espaciosOldEspacio : espaciosOld) {
                if (!espaciosNew.contains(espaciosOldEspacio)) {
                    espaciosOldEspacio.setNormativa(null);
                    espaciosOldEspacio = em.merge(espaciosOldEspacio);
                }
            }
            for (Espacio espaciosNewEspacio : espaciosNew) {
                if (!espaciosOld.contains(espaciosNewEspacio)) {
                    Normativa oldNormativaOfEspaciosNewEspacio = espaciosNewEspacio.getNormativa();
                    espaciosNewEspacio.setNormativa(normativa);
                    espaciosNewEspacio = em.merge(espaciosNewEspacio);
                    if (oldNormativaOfEspaciosNewEspacio != null && !oldNormativaOfEspaciosNewEspacio.equals(normativa)) {
                        oldNormativaOfEspaciosNewEspacio.getEspacios().remove(espaciosNewEspacio);
                        oldNormativaOfEspaciosNewEspacio = em.merge(oldNormativaOfEspaciosNewEspacio);
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
                Integer id = normativa.getIdNormativa();
                if (findNormativa(id) == null) {
                    throw new NonexistentEntityException("The normativa with id " + id + " no longer exists.");
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
            Normativa normativa;
            try {
                normativa = em.getReference(Normativa.class, id);
                normativa.getIdNormativa();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The normativa with id " + id + " no longer exists.", enfe);
            }
            Formula formula = normativa.getFormula();
            if (formula != null) {
                formula.getNormativas().remove(normativa);
                formula = em.merge(formula);
            }
            Set<Espacio> espacios = normativa.getEspacios();
            for (Espacio espaciosEspacio : espacios) {
                espaciosEspacio.setNormativa(null);
                espaciosEspacio = em.merge(espaciosEspacio);
            }
            em.remove(normativa);
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

    public List<Normativa> findNormativaEntities() {
        return findNormativaEntities(true, -1, -1);
    }

    public List<Normativa> findNormativaEntities(int maxResults, int firstResult) {
        return findNormativaEntities(false, maxResults, firstResult);
    }

    private List<Normativa> findNormativaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Normativa.class));
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

    public Normativa findNormativa(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Normativa.class, id);
        } finally {
            em.close();
        }
    }

    public int getNormativaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Normativa> rt = cq.from(Normativa.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
