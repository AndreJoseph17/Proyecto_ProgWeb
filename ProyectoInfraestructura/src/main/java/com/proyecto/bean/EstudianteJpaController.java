/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proyecto.bean;

import com.proyecto.bean.exceptions.IllegalOrphanException;
import com.proyecto.bean.exceptions.NonexistentEntityException;
import com.proyecto.bean.exceptions.RollbackFailureException;
import com.proyecto.modelo.Estudiante;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.proyecto.modelo.Matricula;
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
public class EstudianteJpaController implements Serializable {

    public EstudianteJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Estudiante estudiante) throws RollbackFailureException, Exception {
        if (estudiante.getMatriculas() == null) {
            estudiante.setMatriculas(new HashSet<Matricula>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Set<Matricula> attachedMatriculas = new HashSet<Matricula>();
            for (Matricula matriculasMatriculaToAttach : estudiante.getMatriculas()) {
                matriculasMatriculaToAttach = em.getReference(matriculasMatriculaToAttach.getClass(), matriculasMatriculaToAttach.getIdMatricula());
                attachedMatriculas.add(matriculasMatriculaToAttach);
            }
            estudiante.setMatriculas(attachedMatriculas);
            em.persist(estudiante);
            for (Matricula matriculasMatricula : estudiante.getMatriculas()) {
                Estudiante oldEstudianteOfMatriculasMatricula = matriculasMatricula.getEstudiante();
                matriculasMatricula.setEstudiante(estudiante);
                matriculasMatricula = em.merge(matriculasMatricula);
                if (oldEstudianteOfMatriculasMatricula != null) {
                    oldEstudianteOfMatriculasMatricula.getMatriculas().remove(matriculasMatricula);
                    oldEstudianteOfMatriculasMatricula = em.merge(oldEstudianteOfMatriculasMatricula);
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

    public void edit(Estudiante estudiante) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Estudiante persistentEstudiante = em.find(Estudiante.class, estudiante.getIdEstudiante());
            Set<Matricula> matriculasOld = persistentEstudiante.getMatriculas();
            Set<Matricula> matriculasNew = estudiante.getMatriculas();
            List<String> illegalOrphanMessages = null;
            for (Matricula matriculasOldMatricula : matriculasOld) {
                if (!matriculasNew.contains(matriculasOldMatricula)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Matricula " + matriculasOldMatricula + " since its estudiante field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Set<Matricula> attachedMatriculasNew = new HashSet<Matricula>();
            for (Matricula matriculasNewMatriculaToAttach : matriculasNew) {
                matriculasNewMatriculaToAttach = em.getReference(matriculasNewMatriculaToAttach.getClass(), matriculasNewMatriculaToAttach.getIdMatricula());
                attachedMatriculasNew.add(matriculasNewMatriculaToAttach);
            }
            matriculasNew = attachedMatriculasNew;
            estudiante.setMatriculas(matriculasNew);
            estudiante = em.merge(estudiante);
            for (Matricula matriculasNewMatricula : matriculasNew) {
                if (!matriculasOld.contains(matriculasNewMatricula)) {
                    Estudiante oldEstudianteOfMatriculasNewMatricula = matriculasNewMatricula.getEstudiante();
                    matriculasNewMatricula.setEstudiante(estudiante);
                    matriculasNewMatricula = em.merge(matriculasNewMatricula);
                    if (oldEstudianteOfMatriculasNewMatricula != null && !oldEstudianteOfMatriculasNewMatricula.equals(estudiante)) {
                        oldEstudianteOfMatriculasNewMatricula.getMatriculas().remove(matriculasNewMatricula);
                        oldEstudianteOfMatriculasNewMatricula = em.merge(oldEstudianteOfMatriculasNewMatricula);
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
                Integer id = estudiante.getIdEstudiante();
                if (findEstudiante(id) == null) {
                    throw new NonexistentEntityException("The estudiante with id " + id + " no longer exists.");
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
            Estudiante estudiante;
            try {
                estudiante = em.getReference(Estudiante.class, id);
                estudiante.getIdEstudiante();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The estudiante with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Set<Matricula> matriculasOrphanCheck = estudiante.getMatriculas();
            for (Matricula matriculasOrphanCheckMatricula : matriculasOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Estudiante (" + estudiante + ") cannot be destroyed since the Matricula " + matriculasOrphanCheckMatricula + " in its matriculas field has a non-nullable estudiante field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(estudiante);
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

    public List<Estudiante> findEstudianteEntities() {
        return findEstudianteEntities(true, -1, -1);
    }

    public List<Estudiante> findEstudianteEntities(int maxResults, int firstResult) {
        return findEstudianteEntities(false, maxResults, firstResult);
    }

    private List<Estudiante> findEstudianteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Estudiante.class));
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

    public Estudiante findEstudiante(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Estudiante.class, id);
        } finally {
            em.close();
        }
    }

    public int getEstudianteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Estudiante> rt = cq.from(Estudiante.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
