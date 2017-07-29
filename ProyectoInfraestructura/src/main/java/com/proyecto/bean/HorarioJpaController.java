/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proyecto.bean;

import com.proyecto.bean.exceptions.IllegalOrphanException;
import com.proyecto.bean.exceptions.NonexistentEntityException;
import com.proyecto.bean.exceptions.RollbackFailureException;
import com.proyecto.modelo.Horario;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.proyecto.modelo.MateriaHasHorario;
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
public class HorarioJpaController implements Serializable {

    public HorarioJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Horario horario) throws RollbackFailureException, Exception {
        if (horario.getMateriaHasHorarios() == null) {
            horario.setMateriaHasHorarios(new HashSet<MateriaHasHorario>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Set<MateriaHasHorario> attachedMateriaHasHorarios = new HashSet<MateriaHasHorario>();
            for (MateriaHasHorario materiaHasHorariosMateriaHasHorarioToAttach : horario.getMateriaHasHorarios()) {
                materiaHasHorariosMateriaHasHorarioToAttach = em.getReference(materiaHasHorariosMateriaHasHorarioToAttach.getClass(), materiaHasHorariosMateriaHasHorarioToAttach.getId());
                attachedMateriaHasHorarios.add(materiaHasHorariosMateriaHasHorarioToAttach);
            }
            horario.setMateriaHasHorarios(attachedMateriaHasHorarios);
            em.persist(horario);
            for (MateriaHasHorario materiaHasHorariosMateriaHasHorario : horario.getMateriaHasHorarios()) {
                Horario oldHorarioOfMateriaHasHorariosMateriaHasHorario = materiaHasHorariosMateriaHasHorario.getHorario();
                materiaHasHorariosMateriaHasHorario.setHorario(horario);
                materiaHasHorariosMateriaHasHorario = em.merge(materiaHasHorariosMateriaHasHorario);
                if (oldHorarioOfMateriaHasHorariosMateriaHasHorario != null) {
                    oldHorarioOfMateriaHasHorariosMateriaHasHorario.getMateriaHasHorarios().remove(materiaHasHorariosMateriaHasHorario);
                    oldHorarioOfMateriaHasHorariosMateriaHasHorario = em.merge(oldHorarioOfMateriaHasHorariosMateriaHasHorario);
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

    public void edit(Horario horario) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Horario persistentHorario = em.find(Horario.class, horario.getIdHorario());
            Set<MateriaHasHorario> materiaHasHorariosOld = persistentHorario.getMateriaHasHorarios();
            Set<MateriaHasHorario> materiaHasHorariosNew = horario.getMateriaHasHorarios();
            List<String> illegalOrphanMessages = null;
            for (MateriaHasHorario materiaHasHorariosOldMateriaHasHorario : materiaHasHorariosOld) {
                if (!materiaHasHorariosNew.contains(materiaHasHorariosOldMateriaHasHorario)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain MateriaHasHorario " + materiaHasHorariosOldMateriaHasHorario + " since its horario field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Set<MateriaHasHorario> attachedMateriaHasHorariosNew = new HashSet<MateriaHasHorario>();
            for (MateriaHasHorario materiaHasHorariosNewMateriaHasHorarioToAttach : materiaHasHorariosNew) {
                materiaHasHorariosNewMateriaHasHorarioToAttach = em.getReference(materiaHasHorariosNewMateriaHasHorarioToAttach.getClass(), materiaHasHorariosNewMateriaHasHorarioToAttach.getId());
                attachedMateriaHasHorariosNew.add(materiaHasHorariosNewMateriaHasHorarioToAttach);
            }
            materiaHasHorariosNew = attachedMateriaHasHorariosNew;
            horario.setMateriaHasHorarios(materiaHasHorariosNew);
            horario = em.merge(horario);
            for (MateriaHasHorario materiaHasHorariosNewMateriaHasHorario : materiaHasHorariosNew) {
                if (!materiaHasHorariosOld.contains(materiaHasHorariosNewMateriaHasHorario)) {
                    Horario oldHorarioOfMateriaHasHorariosNewMateriaHasHorario = materiaHasHorariosNewMateriaHasHorario.getHorario();
                    materiaHasHorariosNewMateriaHasHorario.setHorario(horario);
                    materiaHasHorariosNewMateriaHasHorario = em.merge(materiaHasHorariosNewMateriaHasHorario);
                    if (oldHorarioOfMateriaHasHorariosNewMateriaHasHorario != null && !oldHorarioOfMateriaHasHorariosNewMateriaHasHorario.equals(horario)) {
                        oldHorarioOfMateriaHasHorariosNewMateriaHasHorario.getMateriaHasHorarios().remove(materiaHasHorariosNewMateriaHasHorario);
                        oldHorarioOfMateriaHasHorariosNewMateriaHasHorario = em.merge(oldHorarioOfMateriaHasHorariosNewMateriaHasHorario);
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
                Integer id = horario.getIdHorario();
                if (findHorario(id) == null) {
                    throw new NonexistentEntityException("The horario with id " + id + " no longer exists.");
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
            Horario horario;
            try {
                horario = em.getReference(Horario.class, id);
                horario.getIdHorario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The horario with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Set<MateriaHasHorario> materiaHasHorariosOrphanCheck = horario.getMateriaHasHorarios();
            for (MateriaHasHorario materiaHasHorariosOrphanCheckMateriaHasHorario : materiaHasHorariosOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Horario (" + horario + ") cannot be destroyed since the MateriaHasHorario " + materiaHasHorariosOrphanCheckMateriaHasHorario + " in its materiaHasHorarios field has a non-nullable horario field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(horario);
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

    public List<Horario> findHorarioEntities() {
        return findHorarioEntities(true, -1, -1);
    }

    public List<Horario> findHorarioEntities(int maxResults, int firstResult) {
        return findHorarioEntities(false, maxResults, firstResult);
    }

    private List<Horario> findHorarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Horario.class));
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

    public Horario findHorario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Horario.class, id);
        } finally {
            em.close();
        }
    }

    public int getHorarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Horario> rt = cq.from(Horario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
