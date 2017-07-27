/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proyecto.bean;

import com.proyecto.bean.exceptions.NonexistentEntityException;
import com.proyecto.bean.exceptions.PreexistingEntityException;
import com.proyecto.bean.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.proyecto.modelo.Docente;
import com.proyecto.modelo.Materia;
import com.proyecto.modelo.MateriaHasDocente;
import com.proyecto.modelo.MateriaHasDocenteId;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author andre
 */
public class MateriaHasDocenteJpaController implements Serializable {

    public MateriaHasDocenteJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(MateriaHasDocente materiaHasDocente) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (materiaHasDocente.getId() == null) {
            materiaHasDocente.setId(new MateriaHasDocenteId());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Docente docente = materiaHasDocente.getDocente();
            if (docente != null) {
                docente = em.getReference(docente.getClass(), docente.getIdDocente());
                materiaHasDocente.setDocente(docente);
            }
            Materia materia = materiaHasDocente.getMateria();
            if (materia != null) {
                materia = em.getReference(materia.getClass(), materia.getIdMateria());
                materiaHasDocente.setMateria(materia);
            }
            em.persist(materiaHasDocente);
            if (docente != null) {
                docente.getMateriaHasDocentes().add(materiaHasDocente);
                docente = em.merge(docente);
            }
            if (materia != null) {
                materia.getMateriaHasDocentes().add(materiaHasDocente);
                materia = em.merge(materia);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findMateriaHasDocente(materiaHasDocente.getId()) != null) {
                throw new PreexistingEntityException("MateriaHasDocente " + materiaHasDocente + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(MateriaHasDocente materiaHasDocente) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            MateriaHasDocente persistentMateriaHasDocente = em.find(MateriaHasDocente.class, materiaHasDocente.getId());
            Docente docenteOld = persistentMateriaHasDocente.getDocente();
            Docente docenteNew = materiaHasDocente.getDocente();
            Materia materiaOld = persistentMateriaHasDocente.getMateria();
            Materia materiaNew = materiaHasDocente.getMateria();
            if (docenteNew != null) {
                docenteNew = em.getReference(docenteNew.getClass(), docenteNew.getIdDocente());
                materiaHasDocente.setDocente(docenteNew);
            }
            if (materiaNew != null) {
                materiaNew = em.getReference(materiaNew.getClass(), materiaNew.getIdMateria());
                materiaHasDocente.setMateria(materiaNew);
            }
            materiaHasDocente = em.merge(materiaHasDocente);
            if (docenteOld != null && !docenteOld.equals(docenteNew)) {
                docenteOld.getMateriaHasDocentes().remove(materiaHasDocente);
                docenteOld = em.merge(docenteOld);
            }
            if (docenteNew != null && !docenteNew.equals(docenteOld)) {
                docenteNew.getMateriaHasDocentes().add(materiaHasDocente);
                docenteNew = em.merge(docenteNew);
            }
            if (materiaOld != null && !materiaOld.equals(materiaNew)) {
                materiaOld.getMateriaHasDocentes().remove(materiaHasDocente);
                materiaOld = em.merge(materiaOld);
            }
            if (materiaNew != null && !materiaNew.equals(materiaOld)) {
                materiaNew.getMateriaHasDocentes().add(materiaHasDocente);
                materiaNew = em.merge(materiaNew);
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
                MateriaHasDocenteId id = materiaHasDocente.getId();
                if (findMateriaHasDocente(id) == null) {
                    throw new NonexistentEntityException("The materiaHasDocente with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(MateriaHasDocenteId id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            MateriaHasDocente materiaHasDocente;
            try {
                materiaHasDocente = em.getReference(MateriaHasDocente.class, id);
                materiaHasDocente.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The materiaHasDocente with id " + id + " no longer exists.", enfe);
            }
            Docente docente = materiaHasDocente.getDocente();
            if (docente != null) {
                docente.getMateriaHasDocentes().remove(materiaHasDocente);
                docente = em.merge(docente);
            }
            Materia materia = materiaHasDocente.getMateria();
            if (materia != null) {
                materia.getMateriaHasDocentes().remove(materiaHasDocente);
                materia = em.merge(materia);
            }
            em.remove(materiaHasDocente);
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

    public List<MateriaHasDocente> findMateriaHasDocenteEntities() {
        return findMateriaHasDocenteEntities(true, -1, -1);
    }

    public List<MateriaHasDocente> findMateriaHasDocenteEntities(int maxResults, int firstResult) {
        return findMateriaHasDocenteEntities(false, maxResults, firstResult);
    }

    private List<MateriaHasDocente> findMateriaHasDocenteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(MateriaHasDocente.class));
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

    public MateriaHasDocente findMateriaHasDocente(MateriaHasDocenteId id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(MateriaHasDocente.class, id);
        } finally {
            em.close();
        }
    }

    public int getMateriaHasDocenteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<MateriaHasDocente> rt = cq.from(MateriaHasDocente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
