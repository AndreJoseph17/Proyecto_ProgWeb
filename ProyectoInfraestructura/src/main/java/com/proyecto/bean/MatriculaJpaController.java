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
import com.proyecto.modelo.Estudiante;
import com.proyecto.modelo.Matricula;
import com.proyecto.modelo.MatriculaHasMateria;
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
public class MatriculaJpaController implements Serializable {

    public MatriculaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Matricula matricula) throws RollbackFailureException, Exception {
        if (matricula.getMatriculaHasMaterias() == null) {
            matricula.setMatriculaHasMaterias(new HashSet<MatriculaHasMateria>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Estudiante estudiante = matricula.getEstudiante();
            if (estudiante != null) {
                estudiante = em.getReference(estudiante.getClass(), estudiante.getIdEstudiante());
                matricula.setEstudiante(estudiante);
            }
            Set<MatriculaHasMateria> attachedMatriculaHasMaterias = new HashSet<MatriculaHasMateria>();
            for (MatriculaHasMateria matriculaHasMateriasMatriculaHasMateriaToAttach : matricula.getMatriculaHasMaterias()) {
                matriculaHasMateriasMatriculaHasMateriaToAttach = em.getReference(matriculaHasMateriasMatriculaHasMateriaToAttach.getClass(), matriculaHasMateriasMatriculaHasMateriaToAttach.getId());
                attachedMatriculaHasMaterias.add(matriculaHasMateriasMatriculaHasMateriaToAttach);
            }
            matricula.setMatriculaHasMaterias(attachedMatriculaHasMaterias);
            em.persist(matricula);
            if (estudiante != null) {
                estudiante.getMatriculas().add(matricula);
                estudiante = em.merge(estudiante);
            }
            for (MatriculaHasMateria matriculaHasMateriasMatriculaHasMateria : matricula.getMatriculaHasMaterias()) {
                Matricula oldMatriculaOfMatriculaHasMateriasMatriculaHasMateria = matriculaHasMateriasMatriculaHasMateria.getMatricula();
                matriculaHasMateriasMatriculaHasMateria.setMatricula(matricula);
                matriculaHasMateriasMatriculaHasMateria = em.merge(matriculaHasMateriasMatriculaHasMateria);
                if (oldMatriculaOfMatriculaHasMateriasMatriculaHasMateria != null) {
                    oldMatriculaOfMatriculaHasMateriasMatriculaHasMateria.getMatriculaHasMaterias().remove(matriculaHasMateriasMatriculaHasMateria);
                    oldMatriculaOfMatriculaHasMateriasMatriculaHasMateria = em.merge(oldMatriculaOfMatriculaHasMateriasMatriculaHasMateria);
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

    public void edit(Matricula matricula) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Matricula persistentMatricula = em.find(Matricula.class, matricula.getIdMatricula());
            Estudiante estudianteOld = persistentMatricula.getEstudiante();
            Estudiante estudianteNew = matricula.getEstudiante();
            Set<MatriculaHasMateria> matriculaHasMateriasOld = persistentMatricula.getMatriculaHasMaterias();
            Set<MatriculaHasMateria> matriculaHasMateriasNew = matricula.getMatriculaHasMaterias();
            List<String> illegalOrphanMessages = null;
            for (MatriculaHasMateria matriculaHasMateriasOldMatriculaHasMateria : matriculaHasMateriasOld) {
                if (!matriculaHasMateriasNew.contains(matriculaHasMateriasOldMatriculaHasMateria)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain MatriculaHasMateria " + matriculaHasMateriasOldMatriculaHasMateria + " since its matricula field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (estudianteNew != null) {
                estudianteNew = em.getReference(estudianteNew.getClass(), estudianteNew.getIdEstudiante());
                matricula.setEstudiante(estudianteNew);
            }
            Set<MatriculaHasMateria> attachedMatriculaHasMateriasNew = new HashSet<MatriculaHasMateria>();
            for (MatriculaHasMateria matriculaHasMateriasNewMatriculaHasMateriaToAttach : matriculaHasMateriasNew) {
                matriculaHasMateriasNewMatriculaHasMateriaToAttach = em.getReference(matriculaHasMateriasNewMatriculaHasMateriaToAttach.getClass(), matriculaHasMateriasNewMatriculaHasMateriaToAttach.getId());
                attachedMatriculaHasMateriasNew.add(matriculaHasMateriasNewMatriculaHasMateriaToAttach);
            }
            matriculaHasMateriasNew = attachedMatriculaHasMateriasNew;
            matricula.setMatriculaHasMaterias(matriculaHasMateriasNew);
            matricula = em.merge(matricula);
            if (estudianteOld != null && !estudianteOld.equals(estudianteNew)) {
                estudianteOld.getMatriculas().remove(matricula);
                estudianteOld = em.merge(estudianteOld);
            }
            if (estudianteNew != null && !estudianteNew.equals(estudianteOld)) {
                estudianteNew.getMatriculas().add(matricula);
                estudianteNew = em.merge(estudianteNew);
            }
            for (MatriculaHasMateria matriculaHasMateriasNewMatriculaHasMateria : matriculaHasMateriasNew) {
                if (!matriculaHasMateriasOld.contains(matriculaHasMateriasNewMatriculaHasMateria)) {
                    Matricula oldMatriculaOfMatriculaHasMateriasNewMatriculaHasMateria = matriculaHasMateriasNewMatriculaHasMateria.getMatricula();
                    matriculaHasMateriasNewMatriculaHasMateria.setMatricula(matricula);
                    matriculaHasMateriasNewMatriculaHasMateria = em.merge(matriculaHasMateriasNewMatriculaHasMateria);
                    if (oldMatriculaOfMatriculaHasMateriasNewMatriculaHasMateria != null && !oldMatriculaOfMatriculaHasMateriasNewMatriculaHasMateria.equals(matricula)) {
                        oldMatriculaOfMatriculaHasMateriasNewMatriculaHasMateria.getMatriculaHasMaterias().remove(matriculaHasMateriasNewMatriculaHasMateria);
                        oldMatriculaOfMatriculaHasMateriasNewMatriculaHasMateria = em.merge(oldMatriculaOfMatriculaHasMateriasNewMatriculaHasMateria);
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
                Integer id = matricula.getIdMatricula();
                if (findMatricula(id) == null) {
                    throw new NonexistentEntityException("The matricula with id " + id + " no longer exists.");
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
            Matricula matricula;
            try {
                matricula = em.getReference(Matricula.class, id);
                matricula.getIdMatricula();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The matricula with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Set<MatriculaHasMateria> matriculaHasMateriasOrphanCheck = matricula.getMatriculaHasMaterias();
            for (MatriculaHasMateria matriculaHasMateriasOrphanCheckMatriculaHasMateria : matriculaHasMateriasOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Matricula (" + matricula + ") cannot be destroyed since the MatriculaHasMateria " + matriculaHasMateriasOrphanCheckMatriculaHasMateria + " in its matriculaHasMaterias field has a non-nullable matricula field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Estudiante estudiante = matricula.getEstudiante();
            if (estudiante != null) {
                estudiante.getMatriculas().remove(matricula);
                estudiante = em.merge(estudiante);
            }
            em.remove(matricula);
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

    public List<Matricula> findMatriculaEntities() {
        return findMatriculaEntities(true, -1, -1);
    }

    public List<Matricula> findMatriculaEntities(int maxResults, int firstResult) {
        return findMatriculaEntities(false, maxResults, firstResult);
    }

    private List<Matricula> findMatriculaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Matricula.class));
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

    public Matricula findMatricula(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Matricula.class, id);
        } finally {
            em.close();
        }
    }

    public int getMatriculaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Matricula> rt = cq.from(Matricula.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
