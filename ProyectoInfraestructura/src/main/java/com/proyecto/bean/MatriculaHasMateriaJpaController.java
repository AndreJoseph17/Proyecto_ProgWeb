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
import com.proyecto.modelo.Materia;
import com.proyecto.modelo.Matricula;
import com.proyecto.modelo.MatriculaHasMateria;
import com.proyecto.modelo.MatriculaHasMateriaId;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author andre
 */
public class MatriculaHasMateriaJpaController implements Serializable {

    public MatriculaHasMateriaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(MatriculaHasMateria matriculaHasMateria) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (matriculaHasMateria.getId() == null) {
            matriculaHasMateria.setId(new MatriculaHasMateriaId());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Materia materia = matriculaHasMateria.getMateria();
            if (materia != null) {
                materia = em.getReference(materia.getClass(), materia.getIdMateria());
                matriculaHasMateria.setMateria(materia);
            }
            Matricula matricula = matriculaHasMateria.getMatricula();
            if (matricula != null) {
                matricula = em.getReference(matricula.getClass(), matricula.getIdMatricula());
                matriculaHasMateria.setMatricula(matricula);
            }
            em.persist(matriculaHasMateria);
            if (materia != null) {
                materia.getMatriculaHasMaterias().add(matriculaHasMateria);
                materia = em.merge(materia);
            }
            if (matricula != null) {
                matricula.getMatriculaHasMaterias().add(matriculaHasMateria);
                matricula = em.merge(matricula);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findMatriculaHasMateria(matriculaHasMateria.getId()) != null) {
                throw new PreexistingEntityException("MatriculaHasMateria " + matriculaHasMateria + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(MatriculaHasMateria matriculaHasMateria) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            MatriculaHasMateria persistentMatriculaHasMateria = em.find(MatriculaHasMateria.class, matriculaHasMateria.getId());
            Materia materiaOld = persistentMatriculaHasMateria.getMateria();
            Materia materiaNew = matriculaHasMateria.getMateria();
            Matricula matriculaOld = persistentMatriculaHasMateria.getMatricula();
            Matricula matriculaNew = matriculaHasMateria.getMatricula();
            if (materiaNew != null) {
                materiaNew = em.getReference(materiaNew.getClass(), materiaNew.getIdMateria());
                matriculaHasMateria.setMateria(materiaNew);
            }
            if (matriculaNew != null) {
                matriculaNew = em.getReference(matriculaNew.getClass(), matriculaNew.getIdMatricula());
                matriculaHasMateria.setMatricula(matriculaNew);
            }
            matriculaHasMateria = em.merge(matriculaHasMateria);
            if (materiaOld != null && !materiaOld.equals(materiaNew)) {
                materiaOld.getMatriculaHasMaterias().remove(matriculaHasMateria);
                materiaOld = em.merge(materiaOld);
            }
            if (materiaNew != null && !materiaNew.equals(materiaOld)) {
                materiaNew.getMatriculaHasMaterias().add(matriculaHasMateria);
                materiaNew = em.merge(materiaNew);
            }
            if (matriculaOld != null && !matriculaOld.equals(matriculaNew)) {
                matriculaOld.getMatriculaHasMaterias().remove(matriculaHasMateria);
                matriculaOld = em.merge(matriculaOld);
            }
            if (matriculaNew != null && !matriculaNew.equals(matriculaOld)) {
                matriculaNew.getMatriculaHasMaterias().add(matriculaHasMateria);
                matriculaNew = em.merge(matriculaNew);
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
                MatriculaHasMateriaId id = matriculaHasMateria.getId();
                if (findMatriculaHasMateria(id) == null) {
                    throw new NonexistentEntityException("The matriculaHasMateria with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(MatriculaHasMateriaId id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            MatriculaHasMateria matriculaHasMateria;
            try {
                matriculaHasMateria = em.getReference(MatriculaHasMateria.class, id);
                matriculaHasMateria.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The matriculaHasMateria with id " + id + " no longer exists.", enfe);
            }
            Materia materia = matriculaHasMateria.getMateria();
            if (materia != null) {
                materia.getMatriculaHasMaterias().remove(matriculaHasMateria);
                materia = em.merge(materia);
            }
            Matricula matricula = matriculaHasMateria.getMatricula();
            if (matricula != null) {
                matricula.getMatriculaHasMaterias().remove(matriculaHasMateria);
                matricula = em.merge(matricula);
            }
            em.remove(matriculaHasMateria);
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

    public List<MatriculaHasMateria> findMatriculaHasMateriaEntities() {
        return findMatriculaHasMateriaEntities(true, -1, -1);
    }

    public List<MatriculaHasMateria> findMatriculaHasMateriaEntities(int maxResults, int firstResult) {
        return findMatriculaHasMateriaEntities(false, maxResults, firstResult);
    }

    private List<MatriculaHasMateria> findMatriculaHasMateriaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(MatriculaHasMateria.class));
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

    public MatriculaHasMateria findMatriculaHasMateria(MatriculaHasMateriaId id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(MatriculaHasMateria.class, id);
        } finally {
            em.close();
        }
    }

    public int getMatriculaHasMateriaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<MatriculaHasMateria> rt = cq.from(MatriculaHasMateria.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
