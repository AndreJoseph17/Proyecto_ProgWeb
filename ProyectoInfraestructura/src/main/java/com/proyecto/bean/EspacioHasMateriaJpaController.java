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
import com.proyecto.modelo.Espacio;
import com.proyecto.modelo.EspacioHasMateria;
import com.proyecto.modelo.EspacioHasMateriaId;
import com.proyecto.modelo.Materia;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author andre
 */
public class EspacioHasMateriaJpaController implements Serializable {

    public EspacioHasMateriaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(EspacioHasMateria espacioHasMateria) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (espacioHasMateria.getId() == null) {
            espacioHasMateria.setId(new EspacioHasMateriaId());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Espacio espacio = espacioHasMateria.getEspacio();
            if (espacio != null) {
                espacio = em.getReference(espacio.getClass(), espacio.getIdEspacio());
                espacioHasMateria.setEspacio(espacio);
            }
            Materia materia = espacioHasMateria.getMateria();
            if (materia != null) {
                materia = em.getReference(materia.getClass(), materia.getIdMateria());
                espacioHasMateria.setMateria(materia);
            }
            em.persist(espacioHasMateria);
            if (espacio != null) {
                espacio.getEspacioHasMaterias().add(espacioHasMateria);
                espacio = em.merge(espacio);
            }
            if (materia != null) {
                materia.getEspacioHasMaterias().add(espacioHasMateria);
                materia = em.merge(materia);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findEspacioHasMateria(espacioHasMateria.getId()) != null) {
                throw new PreexistingEntityException("EspacioHasMateria " + espacioHasMateria + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(EspacioHasMateria espacioHasMateria) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            EspacioHasMateria persistentEspacioHasMateria = em.find(EspacioHasMateria.class, espacioHasMateria.getId());
            Espacio espacioOld = persistentEspacioHasMateria.getEspacio();
            Espacio espacioNew = espacioHasMateria.getEspacio();
            Materia materiaOld = persistentEspacioHasMateria.getMateria();
            Materia materiaNew = espacioHasMateria.getMateria();
            if (espacioNew != null) {
                espacioNew = em.getReference(espacioNew.getClass(), espacioNew.getIdEspacio());
                espacioHasMateria.setEspacio(espacioNew);
            }
            if (materiaNew != null) {
                materiaNew = em.getReference(materiaNew.getClass(), materiaNew.getIdMateria());
                espacioHasMateria.setMateria(materiaNew);
            }
            espacioHasMateria = em.merge(espacioHasMateria);
            if (espacioOld != null && !espacioOld.equals(espacioNew)) {
                espacioOld.getEspacioHasMaterias().remove(espacioHasMateria);
                espacioOld = em.merge(espacioOld);
            }
            if (espacioNew != null && !espacioNew.equals(espacioOld)) {
                espacioNew.getEspacioHasMaterias().add(espacioHasMateria);
                espacioNew = em.merge(espacioNew);
            }
            if (materiaOld != null && !materiaOld.equals(materiaNew)) {
                materiaOld.getEspacioHasMaterias().remove(espacioHasMateria);
                materiaOld = em.merge(materiaOld);
            }
            if (materiaNew != null && !materiaNew.equals(materiaOld)) {
                materiaNew.getEspacioHasMaterias().add(espacioHasMateria);
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
                EspacioHasMateriaId id = espacioHasMateria.getId();
                if (findEspacioHasMateria(id) == null) {
                    throw new NonexistentEntityException("The espacioHasMateria with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(EspacioHasMateriaId id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            EspacioHasMateria espacioHasMateria;
            try {
                espacioHasMateria = em.getReference(EspacioHasMateria.class, id);
                espacioHasMateria.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The espacioHasMateria with id " + id + " no longer exists.", enfe);
            }
            Espacio espacio = espacioHasMateria.getEspacio();
            if (espacio != null) {
                espacio.getEspacioHasMaterias().remove(espacioHasMateria);
                espacio = em.merge(espacio);
            }
            Materia materia = espacioHasMateria.getMateria();
            if (materia != null) {
                materia.getEspacioHasMaterias().remove(espacioHasMateria);
                materia = em.merge(materia);
            }
            em.remove(espacioHasMateria);
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

    public List<EspacioHasMateria> findEspacioHasMateriaEntities() {
        return findEspacioHasMateriaEntities(true, -1, -1);
    }

    public List<EspacioHasMateria> findEspacioHasMateriaEntities(int maxResults, int firstResult) {
        return findEspacioHasMateriaEntities(false, maxResults, firstResult);
    }

    private List<EspacioHasMateria> findEspacioHasMateriaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(EspacioHasMateria.class));
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

    public EspacioHasMateria findEspacioHasMateria(EspacioHasMateriaId id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(EspacioHasMateria.class, id);
        } finally {
            em.close();
        }
    }

    public int getEspacioHasMateriaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<EspacioHasMateria> rt = cq.from(EspacioHasMateria.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
