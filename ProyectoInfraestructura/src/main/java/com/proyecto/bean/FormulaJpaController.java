/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proyecto.bean;

import com.proyecto.bean.exceptions.IllegalOrphanException;
import com.proyecto.bean.exceptions.NonexistentEntityException;
import com.proyecto.bean.exceptions.RollbackFailureException;
import com.proyecto.modelo.Formula;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.proyecto.modelo.Normativa;
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
public class FormulaJpaController implements Serializable {

    public FormulaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Formula formula) throws RollbackFailureException, Exception {
        if (formula.getNormativas() == null) {
            formula.setNormativas(new HashSet<Normativa>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Set<Normativa> attachedNormativas = new HashSet<Normativa>();
            for (Normativa normativasNormativaToAttach : formula.getNormativas()) {
                normativasNormativaToAttach = em.getReference(normativasNormativaToAttach.getClass(), normativasNormativaToAttach.getIdNormativa());
                attachedNormativas.add(normativasNormativaToAttach);
            }
            formula.setNormativas(attachedNormativas);
            em.persist(formula);
            for (Normativa normativasNormativa : formula.getNormativas()) {
                Formula oldFormulaOfNormativasNormativa = normativasNormativa.getFormula();
                normativasNormativa.setFormula(formula);
                normativasNormativa = em.merge(normativasNormativa);
                if (oldFormulaOfNormativasNormativa != null) {
                    oldFormulaOfNormativasNormativa.getNormativas().remove(normativasNormativa);
                    oldFormulaOfNormativasNormativa = em.merge(oldFormulaOfNormativasNormativa);
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

    public void edit(Formula formula) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Formula persistentFormula = em.find(Formula.class, formula.getIdFormula());
            Set<Normativa> normativasOld = persistentFormula.getNormativas();
            Set<Normativa> normativasNew = formula.getNormativas();
            List<String> illegalOrphanMessages = null;
            for (Normativa normativasOldNormativa : normativasOld) {
                if (!normativasNew.contains(normativasOldNormativa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Normativa " + normativasOldNormativa + " since its formula field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Set<Normativa> attachedNormativasNew = new HashSet<Normativa>();
            for (Normativa normativasNewNormativaToAttach : normativasNew) {
                normativasNewNormativaToAttach = em.getReference(normativasNewNormativaToAttach.getClass(), normativasNewNormativaToAttach.getIdNormativa());
                attachedNormativasNew.add(normativasNewNormativaToAttach);
            }
            normativasNew = attachedNormativasNew;
            formula.setNormativas(normativasNew);
            formula = em.merge(formula);
            for (Normativa normativasNewNormativa : normativasNew) {
                if (!normativasOld.contains(normativasNewNormativa)) {
                    Formula oldFormulaOfNormativasNewNormativa = normativasNewNormativa.getFormula();
                    normativasNewNormativa.setFormula(formula);
                    normativasNewNormativa = em.merge(normativasNewNormativa);
                    if (oldFormulaOfNormativasNewNormativa != null && !oldFormulaOfNormativasNewNormativa.equals(formula)) {
                        oldFormulaOfNormativasNewNormativa.getNormativas().remove(normativasNewNormativa);
                        oldFormulaOfNormativasNewNormativa = em.merge(oldFormulaOfNormativasNewNormativa);
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
                Integer id = formula.getIdFormula();
                if (findFormula(id) == null) {
                    throw new NonexistentEntityException("The formula with id " + id + " no longer exists.");
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
            Formula formula;
            try {
                formula = em.getReference(Formula.class, id);
                formula.getIdFormula();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The formula with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Set<Normativa> normativasOrphanCheck = formula.getNormativas();
            for (Normativa normativasOrphanCheckNormativa : normativasOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Formula (" + formula + ") cannot be destroyed since the Normativa " + normativasOrphanCheckNormativa + " in its normativas field has a non-nullable formula field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(formula);
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

    public List<Formula> findFormulaEntities() {
        return findFormulaEntities(true, -1, -1);
    }

    public List<Formula> findFormulaEntities(int maxResults, int firstResult) {
        return findFormulaEntities(false, maxResults, firstResult);
    }

    private List<Formula> findFormulaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Formula.class));
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

    public Formula findFormula(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Formula.class, id);
        } finally {
            em.close();
        }
    }

    public int getFormulaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Formula> rt = cq.from(Formula.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
