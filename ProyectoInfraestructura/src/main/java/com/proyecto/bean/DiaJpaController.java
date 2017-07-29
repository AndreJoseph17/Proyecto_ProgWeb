/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proyecto.bean;

import com.proyecto.bean.exceptions.IllegalOrphanException;
import com.proyecto.bean.exceptions.NonexistentEntityException;
import com.proyecto.bean.exceptions.RollbackFailureException;
import com.proyecto.modelo.Dia;
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
public class DiaJpaController implements Serializable {

    public DiaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Dia dia) throws RollbackFailureException, Exception {
        if (dia.getMateriaHasHorarios() == null) {
            dia.setMateriaHasHorarios(new HashSet<MateriaHasHorario>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Set<MateriaHasHorario> attachedMateriaHasHorarios = new HashSet<MateriaHasHorario>();
            for (MateriaHasHorario materiaHasHorariosMateriaHasHorarioToAttach : dia.getMateriaHasHorarios()) {
                materiaHasHorariosMateriaHasHorarioToAttach = em.getReference(materiaHasHorariosMateriaHasHorarioToAttach.getClass(), materiaHasHorariosMateriaHasHorarioToAttach.getId());
                attachedMateriaHasHorarios.add(materiaHasHorariosMateriaHasHorarioToAttach);
            }
            dia.setMateriaHasHorarios(attachedMateriaHasHorarios);
            em.persist(dia);
            for (MateriaHasHorario materiaHasHorariosMateriaHasHorario : dia.getMateriaHasHorarios()) {
                Dia oldDiaOfMateriaHasHorariosMateriaHasHorario = materiaHasHorariosMateriaHasHorario.getDia();
                materiaHasHorariosMateriaHasHorario.setDia(dia);
                materiaHasHorariosMateriaHasHorario = em.merge(materiaHasHorariosMateriaHasHorario);
                if (oldDiaOfMateriaHasHorariosMateriaHasHorario != null) {
                    oldDiaOfMateriaHasHorariosMateriaHasHorario.getMateriaHasHorarios().remove(materiaHasHorariosMateriaHasHorario);
                    oldDiaOfMateriaHasHorariosMateriaHasHorario = em.merge(oldDiaOfMateriaHasHorariosMateriaHasHorario);
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

    public void edit(Dia dia) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Dia persistentDia = em.find(Dia.class, dia.getIdDias());
            Set<MateriaHasHorario> materiaHasHorariosOld = persistentDia.getMateriaHasHorarios();
            Set<MateriaHasHorario> materiaHasHorariosNew = dia.getMateriaHasHorarios();
            List<String> illegalOrphanMessages = null;
            for (MateriaHasHorario materiaHasHorariosOldMateriaHasHorario : materiaHasHorariosOld) {
                if (!materiaHasHorariosNew.contains(materiaHasHorariosOldMateriaHasHorario)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain MateriaHasHorario " + materiaHasHorariosOldMateriaHasHorario + " since its dia field is not nullable.");
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
            dia.setMateriaHasHorarios(materiaHasHorariosNew);
            dia = em.merge(dia);
            for (MateriaHasHorario materiaHasHorariosNewMateriaHasHorario : materiaHasHorariosNew) {
                if (!materiaHasHorariosOld.contains(materiaHasHorariosNewMateriaHasHorario)) {
                    Dia oldDiaOfMateriaHasHorariosNewMateriaHasHorario = materiaHasHorariosNewMateriaHasHorario.getDia();
                    materiaHasHorariosNewMateriaHasHorario.setDia(dia);
                    materiaHasHorariosNewMateriaHasHorario = em.merge(materiaHasHorariosNewMateriaHasHorario);
                    if (oldDiaOfMateriaHasHorariosNewMateriaHasHorario != null && !oldDiaOfMateriaHasHorariosNewMateriaHasHorario.equals(dia)) {
                        oldDiaOfMateriaHasHorariosNewMateriaHasHorario.getMateriaHasHorarios().remove(materiaHasHorariosNewMateriaHasHorario);
                        oldDiaOfMateriaHasHorariosNewMateriaHasHorario = em.merge(oldDiaOfMateriaHasHorariosNewMateriaHasHorario);
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
                Integer id = dia.getIdDias();
                if (findDia(id) == null) {
                    throw new NonexistentEntityException("The dia with id " + id + " no longer exists.");
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
            Dia dia;
            try {
                dia = em.getReference(Dia.class, id);
                dia.getIdDias();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The dia with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Set<MateriaHasHorario> materiaHasHorariosOrphanCheck = dia.getMateriaHasHorarios();
            for (MateriaHasHorario materiaHasHorariosOrphanCheckMateriaHasHorario : materiaHasHorariosOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Dia (" + dia + ") cannot be destroyed since the MateriaHasHorario " + materiaHasHorariosOrphanCheckMateriaHasHorario + " in its materiaHasHorarios field has a non-nullable dia field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(dia);
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

    public List<Dia> findDiaEntities() {
        return findDiaEntities(true, -1, -1);
    }

    public List<Dia> findDiaEntities(int maxResults, int firstResult) {
        return findDiaEntities(false, maxResults, firstResult);
    }

    private List<Dia> findDiaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Dia.class));
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

    public Dia findDia(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Dia.class, id);
        } finally {
            em.close();
        }
    }

    public int getDiaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Dia> rt = cq.from(Dia.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
