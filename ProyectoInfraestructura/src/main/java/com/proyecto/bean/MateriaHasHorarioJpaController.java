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
import com.proyecto.modelo.Dia;
import com.proyecto.modelo.Horario;
import com.proyecto.modelo.Materia;
import com.proyecto.modelo.MateriaHasHorario;
import com.proyecto.modelo.MateriaHasHorarioId;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author andre
 */
public class MateriaHasHorarioJpaController implements Serializable {

    public MateriaHasHorarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    
    
    public MateriaHasHorarioJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(MateriaHasHorario materiaHasHorario) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (materiaHasHorario.getId() == null) {
            materiaHasHorario.setId(new MateriaHasHorarioId());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Dia dia = materiaHasHorario.getDia();
            if (dia != null) {
                dia = em.getReference(dia.getClass(), dia.getIdDias());
                materiaHasHorario.setDia(dia);
            }
            Horario horario = materiaHasHorario.getHorario();
            if (horario != null) {
                horario = em.getReference(horario.getClass(), horario.getIdHorario());
                materiaHasHorario.setHorario(horario);
            }
            Materia materia = materiaHasHorario.getMateria();
            if (materia != null) {
                materia = em.getReference(materia.getClass(), materia.getIdMateria());
                materiaHasHorario.setMateria(materia);
            }
            em.persist(materiaHasHorario);
            if (dia != null) {
                dia.getMateriaHasHorarios().add(materiaHasHorario);
                dia = em.merge(dia);
            }
            if (horario != null) {
                horario.getMateriaHasHorarios().add(materiaHasHorario);
                horario = em.merge(horario);
            }
            if (materia != null) {
                materia.getMateriaHasHorarios().add(materiaHasHorario);
                materia = em.merge(materia);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findMateriaHasHorario(materiaHasHorario.getId()) != null) {
                throw new PreexistingEntityException("MateriaHasHorario " + materiaHasHorario + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(MateriaHasHorario materiaHasHorario) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            MateriaHasHorario persistentMateriaHasHorario = em.find(MateriaHasHorario.class, materiaHasHorario.getId());
            Dia diaOld = persistentMateriaHasHorario.getDia();
            Dia diaNew = materiaHasHorario.getDia();
            Horario horarioOld = persistentMateriaHasHorario.getHorario();
            Horario horarioNew = materiaHasHorario.getHorario();
            Materia materiaOld = persistentMateriaHasHorario.getMateria();
            Materia materiaNew = materiaHasHorario.getMateria();
            if (diaNew != null) {
                diaNew = em.getReference(diaNew.getClass(), diaNew.getIdDias());
                materiaHasHorario.setDia(diaNew);
            }
            if (horarioNew != null) {
                horarioNew = em.getReference(horarioNew.getClass(), horarioNew.getIdHorario());
                materiaHasHorario.setHorario(horarioNew);
            }
            if (materiaNew != null) {
                materiaNew = em.getReference(materiaNew.getClass(), materiaNew.getIdMateria());
                materiaHasHorario.setMateria(materiaNew);
            }
            materiaHasHorario = em.merge(materiaHasHorario);
            if (diaOld != null && !diaOld.equals(diaNew)) {
                diaOld.getMateriaHasHorarios().remove(materiaHasHorario);
                diaOld = em.merge(diaOld);
            }
            if (diaNew != null && !diaNew.equals(diaOld)) {
                diaNew.getMateriaHasHorarios().add(materiaHasHorario);
                diaNew = em.merge(diaNew);
            }
            if (horarioOld != null && !horarioOld.equals(horarioNew)) {
                horarioOld.getMateriaHasHorarios().remove(materiaHasHorario);
                horarioOld = em.merge(horarioOld);
            }
            if (horarioNew != null && !horarioNew.equals(horarioOld)) {
                horarioNew.getMateriaHasHorarios().add(materiaHasHorario);
                horarioNew = em.merge(horarioNew);
            }
            if (materiaOld != null && !materiaOld.equals(materiaNew)) {
                materiaOld.getMateriaHasHorarios().remove(materiaHasHorario);
                materiaOld = em.merge(materiaOld);
            }
            if (materiaNew != null && !materiaNew.equals(materiaOld)) {
                materiaNew.getMateriaHasHorarios().add(materiaHasHorario);
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
                MateriaHasHorarioId id = materiaHasHorario.getId();
                if (findMateriaHasHorario(id) == null) {
                    throw new NonexistentEntityException("The materiaHasHorario with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(MateriaHasHorarioId id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            MateriaHasHorario materiaHasHorario;
            try {
                materiaHasHorario = em.getReference(MateriaHasHorario.class, id);
                materiaHasHorario.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The materiaHasHorario with id " + id + " no longer exists.", enfe);
            }
            Dia dia = materiaHasHorario.getDia();
            if (dia != null) {
                dia.getMateriaHasHorarios().remove(materiaHasHorario);
                dia = em.merge(dia);
            }
            Horario horario = materiaHasHorario.getHorario();
            if (horario != null) {
                horario.getMateriaHasHorarios().remove(materiaHasHorario);
                horario = em.merge(horario);
            }
            Materia materia = materiaHasHorario.getMateria();
            if (materia != null) {
                materia.getMateriaHasHorarios().remove(materiaHasHorario);
                materia = em.merge(materia);
            }
            em.remove(materiaHasHorario);
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

    public List<MateriaHasHorario> findMateriaHasHorarioEntities() {
        return findMateriaHasHorarioEntities(true, -1, -1);
    }

    public List<MateriaHasHorario> findMateriaHasHorarioEntities(int maxResults, int firstResult) {
        return findMateriaHasHorarioEntities(false, maxResults, firstResult);
    }

    private List<MateriaHasHorario> findMateriaHasHorarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(MateriaHasHorario.class));
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

    public MateriaHasHorario findMateriaHasHorario(MateriaHasHorarioId id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(MateriaHasHorario.class, id);
        } finally {
            em.close();
        }
    }

    public int getMateriaHasHorarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<MateriaHasHorario> rt = cq.from(MateriaHasHorario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
