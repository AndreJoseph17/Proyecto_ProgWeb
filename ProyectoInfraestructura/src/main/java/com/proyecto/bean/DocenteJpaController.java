/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proyecto.bean;

import com.proyecto.bean.exceptions.IllegalOrphanException;
import com.proyecto.bean.exceptions.NonexistentEntityException;
import com.proyecto.bean.exceptions.RollbackFailureException;
import com.proyecto.modelo.Docente;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.proyecto.modelo.MateriaHasDocente;
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
public class DocenteJpaController implements Serializable {

    public DocenteJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Docente docente) throws RollbackFailureException, Exception {
        if (docente.getMateriaHasDocentes() == null) {
            docente.setMateriaHasDocentes(new HashSet<MateriaHasDocente>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Set<MateriaHasDocente> attachedMateriaHasDocentes = new HashSet<MateriaHasDocente>();
            for (MateriaHasDocente materiaHasDocentesMateriaHasDocenteToAttach : docente.getMateriaHasDocentes()) {
                materiaHasDocentesMateriaHasDocenteToAttach = em.getReference(materiaHasDocentesMateriaHasDocenteToAttach.getClass(), materiaHasDocentesMateriaHasDocenteToAttach.getId());
                attachedMateriaHasDocentes.add(materiaHasDocentesMateriaHasDocenteToAttach);
            }
            docente.setMateriaHasDocentes(attachedMateriaHasDocentes);
            em.persist(docente);
            for (MateriaHasDocente materiaHasDocentesMateriaHasDocente : docente.getMateriaHasDocentes()) {
                Docente oldDocenteOfMateriaHasDocentesMateriaHasDocente = materiaHasDocentesMateriaHasDocente.getDocente();
                materiaHasDocentesMateriaHasDocente.setDocente(docente);
                materiaHasDocentesMateriaHasDocente = em.merge(materiaHasDocentesMateriaHasDocente);
                if (oldDocenteOfMateriaHasDocentesMateriaHasDocente != null) {
                    oldDocenteOfMateriaHasDocentesMateriaHasDocente.getMateriaHasDocentes().remove(materiaHasDocentesMateriaHasDocente);
                    oldDocenteOfMateriaHasDocentesMateriaHasDocente = em.merge(oldDocenteOfMateriaHasDocentesMateriaHasDocente);
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

    public void edit(Docente docente) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Docente persistentDocente = em.find(Docente.class, docente.getIdDocente());
            Set<MateriaHasDocente> materiaHasDocentesOld = persistentDocente.getMateriaHasDocentes();
            Set<MateriaHasDocente> materiaHasDocentesNew = docente.getMateriaHasDocentes();
            List<String> illegalOrphanMessages = null;
            for (MateriaHasDocente materiaHasDocentesOldMateriaHasDocente : materiaHasDocentesOld) {
                if (!materiaHasDocentesNew.contains(materiaHasDocentesOldMateriaHasDocente)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain MateriaHasDocente " + materiaHasDocentesOldMateriaHasDocente + " since its docente field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Set<MateriaHasDocente> attachedMateriaHasDocentesNew = new HashSet<MateriaHasDocente>();
            for (MateriaHasDocente materiaHasDocentesNewMateriaHasDocenteToAttach : materiaHasDocentesNew) {
                materiaHasDocentesNewMateriaHasDocenteToAttach = em.getReference(materiaHasDocentesNewMateriaHasDocenteToAttach.getClass(), materiaHasDocentesNewMateriaHasDocenteToAttach.getId());
                attachedMateriaHasDocentesNew.add(materiaHasDocentesNewMateriaHasDocenteToAttach);
            }
            materiaHasDocentesNew = attachedMateriaHasDocentesNew;
            docente.setMateriaHasDocentes(materiaHasDocentesNew);
            docente = em.merge(docente);
            for (MateriaHasDocente materiaHasDocentesNewMateriaHasDocente : materiaHasDocentesNew) {
                if (!materiaHasDocentesOld.contains(materiaHasDocentesNewMateriaHasDocente)) {
                    Docente oldDocenteOfMateriaHasDocentesNewMateriaHasDocente = materiaHasDocentesNewMateriaHasDocente.getDocente();
                    materiaHasDocentesNewMateriaHasDocente.setDocente(docente);
                    materiaHasDocentesNewMateriaHasDocente = em.merge(materiaHasDocentesNewMateriaHasDocente);
                    if (oldDocenteOfMateriaHasDocentesNewMateriaHasDocente != null && !oldDocenteOfMateriaHasDocentesNewMateriaHasDocente.equals(docente)) {
                        oldDocenteOfMateriaHasDocentesNewMateriaHasDocente.getMateriaHasDocentes().remove(materiaHasDocentesNewMateriaHasDocente);
                        oldDocenteOfMateriaHasDocentesNewMateriaHasDocente = em.merge(oldDocenteOfMateriaHasDocentesNewMateriaHasDocente);
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
                Integer id = docente.getIdDocente();
                if (findDocente(id) == null) {
                    throw new NonexistentEntityException("The docente with id " + id + " no longer exists.");
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
            Docente docente;
            try {
                docente = em.getReference(Docente.class, id);
                docente.getIdDocente();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The docente with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Set<MateriaHasDocente> materiaHasDocentesOrphanCheck = docente.getMateriaHasDocentes();
            for (MateriaHasDocente materiaHasDocentesOrphanCheckMateriaHasDocente : materiaHasDocentesOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Docente (" + docente + ") cannot be destroyed since the MateriaHasDocente " + materiaHasDocentesOrphanCheckMateriaHasDocente + " in its materiaHasDocentes field has a non-nullable docente field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(docente);
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

    public List<Docente> findDocenteEntities() {
        return findDocenteEntities(true, -1, -1);
    }

    public List<Docente> findDocenteEntities(int maxResults, int firstResult) {
        return findDocenteEntities(false, maxResults, firstResult);
    }

    private List<Docente> findDocenteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Docente.class));
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

    public Docente findDocente(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Docente.class, id);
        } finally {
            em.close();
        }
    }

    public int getDocenteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Docente> rt = cq.from(Docente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
