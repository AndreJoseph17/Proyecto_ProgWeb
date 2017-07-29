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
import com.proyecto.modelo.EspacioHasMateria;
import com.proyecto.modelo.Materia;
import java.util.HashSet;
import java.util.Set;
import com.proyecto.modelo.MateriaHasHorario;
import com.proyecto.modelo.MatriculaHasMateria;
import com.proyecto.modelo.MateriaHasDocente;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author andre
 */
public class MateriaJpaController implements Serializable {

    public MateriaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Materia materia) throws RollbackFailureException, Exception {
        if (materia.getEspacioHasMaterias() == null) {
            materia.setEspacioHasMaterias(new HashSet<EspacioHasMateria>());
        }
        if (materia.getMateriaHasHorarios() == null) {
            materia.setMateriaHasHorarios(new HashSet<MateriaHasHorario>());
        }
        if (materia.getMatriculaHasMaterias() == null) {
            materia.setMatriculaHasMaterias(new HashSet<MatriculaHasMateria>());
        }
        if (materia.getMateriaHasDocentes() == null) {
            materia.setMateriaHasDocentes(new HashSet<MateriaHasDocente>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Set<EspacioHasMateria> attachedEspacioHasMaterias = new HashSet<EspacioHasMateria>();
            for (EspacioHasMateria espacioHasMateriasEspacioHasMateriaToAttach : materia.getEspacioHasMaterias()) {
                espacioHasMateriasEspacioHasMateriaToAttach = em.getReference(espacioHasMateriasEspacioHasMateriaToAttach.getClass(), espacioHasMateriasEspacioHasMateriaToAttach.getId());
                attachedEspacioHasMaterias.add(espacioHasMateriasEspacioHasMateriaToAttach);
            }
            materia.setEspacioHasMaterias(attachedEspacioHasMaterias);
            Set<MateriaHasHorario> attachedMateriaHasHorarios = new HashSet<MateriaHasHorario>();
            for (MateriaHasHorario materiaHasHorariosMateriaHasHorarioToAttach : materia.getMateriaHasHorarios()) {
                materiaHasHorariosMateriaHasHorarioToAttach = em.getReference(materiaHasHorariosMateriaHasHorarioToAttach.getClass(), materiaHasHorariosMateriaHasHorarioToAttach.getId());
                attachedMateriaHasHorarios.add(materiaHasHorariosMateriaHasHorarioToAttach);
            }
            materia.setMateriaHasHorarios(attachedMateriaHasHorarios);
            Set<MatriculaHasMateria> attachedMatriculaHasMaterias = new HashSet<MatriculaHasMateria>();
            for (MatriculaHasMateria matriculaHasMateriasMatriculaHasMateriaToAttach : materia.getMatriculaHasMaterias()) {
                matriculaHasMateriasMatriculaHasMateriaToAttach = em.getReference(matriculaHasMateriasMatriculaHasMateriaToAttach.getClass(), matriculaHasMateriasMatriculaHasMateriaToAttach.getId());
                attachedMatriculaHasMaterias.add(matriculaHasMateriasMatriculaHasMateriaToAttach);
            }
            materia.setMatriculaHasMaterias(attachedMatriculaHasMaterias);
            Set<MateriaHasDocente> attachedMateriaHasDocentes = new HashSet<MateriaHasDocente>();
            for (MateriaHasDocente materiaHasDocentesMateriaHasDocenteToAttach : materia.getMateriaHasDocentes()) {
                materiaHasDocentesMateriaHasDocenteToAttach = em.getReference(materiaHasDocentesMateriaHasDocenteToAttach.getClass(), materiaHasDocentesMateriaHasDocenteToAttach.getId());
                attachedMateriaHasDocentes.add(materiaHasDocentesMateriaHasDocenteToAttach);
            }
            materia.setMateriaHasDocentes(attachedMateriaHasDocentes);
            em.persist(materia);
            for (EspacioHasMateria espacioHasMateriasEspacioHasMateria : materia.getEspacioHasMaterias()) {
                Materia oldMateriaOfEspacioHasMateriasEspacioHasMateria = espacioHasMateriasEspacioHasMateria.getMateria();
                espacioHasMateriasEspacioHasMateria.setMateria(materia);
                espacioHasMateriasEspacioHasMateria = em.merge(espacioHasMateriasEspacioHasMateria);
                if (oldMateriaOfEspacioHasMateriasEspacioHasMateria != null) {
                    oldMateriaOfEspacioHasMateriasEspacioHasMateria.getEspacioHasMaterias().remove(espacioHasMateriasEspacioHasMateria);
                    oldMateriaOfEspacioHasMateriasEspacioHasMateria = em.merge(oldMateriaOfEspacioHasMateriasEspacioHasMateria);
                }
            }
            for (MateriaHasHorario materiaHasHorariosMateriaHasHorario : materia.getMateriaHasHorarios()) {
                Materia oldMateriaOfMateriaHasHorariosMateriaHasHorario = materiaHasHorariosMateriaHasHorario.getMateria();
                materiaHasHorariosMateriaHasHorario.setMateria(materia);
                materiaHasHorariosMateriaHasHorario = em.merge(materiaHasHorariosMateriaHasHorario);
                if (oldMateriaOfMateriaHasHorariosMateriaHasHorario != null) {
                    oldMateriaOfMateriaHasHorariosMateriaHasHorario.getMateriaHasHorarios().remove(materiaHasHorariosMateriaHasHorario);
                    oldMateriaOfMateriaHasHorariosMateriaHasHorario = em.merge(oldMateriaOfMateriaHasHorariosMateriaHasHorario);
                }
            }
            for (MatriculaHasMateria matriculaHasMateriasMatriculaHasMateria : materia.getMatriculaHasMaterias()) {
                Materia oldMateriaOfMatriculaHasMateriasMatriculaHasMateria = matriculaHasMateriasMatriculaHasMateria.getMateria();
                matriculaHasMateriasMatriculaHasMateria.setMateria(materia);
                matriculaHasMateriasMatriculaHasMateria = em.merge(matriculaHasMateriasMatriculaHasMateria);
                if (oldMateriaOfMatriculaHasMateriasMatriculaHasMateria != null) {
                    oldMateriaOfMatriculaHasMateriasMatriculaHasMateria.getMatriculaHasMaterias().remove(matriculaHasMateriasMatriculaHasMateria);
                    oldMateriaOfMatriculaHasMateriasMatriculaHasMateria = em.merge(oldMateriaOfMatriculaHasMateriasMatriculaHasMateria);
                }
            }
            for (MateriaHasDocente materiaHasDocentesMateriaHasDocente : materia.getMateriaHasDocentes()) {
                Materia oldMateriaOfMateriaHasDocentesMateriaHasDocente = materiaHasDocentesMateriaHasDocente.getMateria();
                materiaHasDocentesMateriaHasDocente.setMateria(materia);
                materiaHasDocentesMateriaHasDocente = em.merge(materiaHasDocentesMateriaHasDocente);
                if (oldMateriaOfMateriaHasDocentesMateriaHasDocente != null) {
                    oldMateriaOfMateriaHasDocentesMateriaHasDocente.getMateriaHasDocentes().remove(materiaHasDocentesMateriaHasDocente);
                    oldMateriaOfMateriaHasDocentesMateriaHasDocente = em.merge(oldMateriaOfMateriaHasDocentesMateriaHasDocente);
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

    public void edit(Materia materia) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Materia persistentMateria = em.find(Materia.class, materia.getIdMateria());
            Set<EspacioHasMateria> espacioHasMateriasOld = persistentMateria.getEspacioHasMaterias();
            Set<EspacioHasMateria> espacioHasMateriasNew = materia.getEspacioHasMaterias();
            Set<MateriaHasHorario> materiaHasHorariosOld = persistentMateria.getMateriaHasHorarios();
            Set<MateriaHasHorario> materiaHasHorariosNew = materia.getMateriaHasHorarios();
            Set<MatriculaHasMateria> matriculaHasMateriasOld = persistentMateria.getMatriculaHasMaterias();
            Set<MatriculaHasMateria> matriculaHasMateriasNew = materia.getMatriculaHasMaterias();
            Set<MateriaHasDocente> materiaHasDocentesOld = persistentMateria.getMateriaHasDocentes();
            Set<MateriaHasDocente> materiaHasDocentesNew = materia.getMateriaHasDocentes();
            List<String> illegalOrphanMessages = null;
            for (EspacioHasMateria espacioHasMateriasOldEspacioHasMateria : espacioHasMateriasOld) {
                if (!espacioHasMateriasNew.contains(espacioHasMateriasOldEspacioHasMateria)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain EspacioHasMateria " + espacioHasMateriasOldEspacioHasMateria + " since its materia field is not nullable.");
                }
            }
            for (MateriaHasHorario materiaHasHorariosOldMateriaHasHorario : materiaHasHorariosOld) {
                if (!materiaHasHorariosNew.contains(materiaHasHorariosOldMateriaHasHorario)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain MateriaHasHorario " + materiaHasHorariosOldMateriaHasHorario + " since its materia field is not nullable.");
                }
            }
            for (MatriculaHasMateria matriculaHasMateriasOldMatriculaHasMateria : matriculaHasMateriasOld) {
                if (!matriculaHasMateriasNew.contains(matriculaHasMateriasOldMatriculaHasMateria)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain MatriculaHasMateria " + matriculaHasMateriasOldMatriculaHasMateria + " since its materia field is not nullable.");
                }
            }
            for (MateriaHasDocente materiaHasDocentesOldMateriaHasDocente : materiaHasDocentesOld) {
                if (!materiaHasDocentesNew.contains(materiaHasDocentesOldMateriaHasDocente)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain MateriaHasDocente " + materiaHasDocentesOldMateriaHasDocente + " since its materia field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Set<EspacioHasMateria> attachedEspacioHasMateriasNew = new HashSet<EspacioHasMateria>();
            for (EspacioHasMateria espacioHasMateriasNewEspacioHasMateriaToAttach : espacioHasMateriasNew) {
                espacioHasMateriasNewEspacioHasMateriaToAttach = em.getReference(espacioHasMateriasNewEspacioHasMateriaToAttach.getClass(), espacioHasMateriasNewEspacioHasMateriaToAttach.getId());
                attachedEspacioHasMateriasNew.add(espacioHasMateriasNewEspacioHasMateriaToAttach);
            }
            espacioHasMateriasNew = attachedEspacioHasMateriasNew;
            materia.setEspacioHasMaterias(espacioHasMateriasNew);
            Set<MateriaHasHorario> attachedMateriaHasHorariosNew = new HashSet<MateriaHasHorario>();
            for (MateriaHasHorario materiaHasHorariosNewMateriaHasHorarioToAttach : materiaHasHorariosNew) {
                materiaHasHorariosNewMateriaHasHorarioToAttach = em.getReference(materiaHasHorariosNewMateriaHasHorarioToAttach.getClass(), materiaHasHorariosNewMateriaHasHorarioToAttach.getId());
                attachedMateriaHasHorariosNew.add(materiaHasHorariosNewMateriaHasHorarioToAttach);
            }
            materiaHasHorariosNew = attachedMateriaHasHorariosNew;
            materia.setMateriaHasHorarios(materiaHasHorariosNew);
            Set<MatriculaHasMateria> attachedMatriculaHasMateriasNew = new HashSet<MatriculaHasMateria>();
            for (MatriculaHasMateria matriculaHasMateriasNewMatriculaHasMateriaToAttach : matriculaHasMateriasNew) {
                matriculaHasMateriasNewMatriculaHasMateriaToAttach = em.getReference(matriculaHasMateriasNewMatriculaHasMateriaToAttach.getClass(), matriculaHasMateriasNewMatriculaHasMateriaToAttach.getId());
                attachedMatriculaHasMateriasNew.add(matriculaHasMateriasNewMatriculaHasMateriaToAttach);
            }
            matriculaHasMateriasNew = attachedMatriculaHasMateriasNew;
            materia.setMatriculaHasMaterias(matriculaHasMateriasNew);
            Set<MateriaHasDocente> attachedMateriaHasDocentesNew = new HashSet<MateriaHasDocente>();
            for (MateriaHasDocente materiaHasDocentesNewMateriaHasDocenteToAttach : materiaHasDocentesNew) {
                materiaHasDocentesNewMateriaHasDocenteToAttach = em.getReference(materiaHasDocentesNewMateriaHasDocenteToAttach.getClass(), materiaHasDocentesNewMateriaHasDocenteToAttach.getId());
                attachedMateriaHasDocentesNew.add(materiaHasDocentesNewMateriaHasDocenteToAttach);
            }
            materiaHasDocentesNew = attachedMateriaHasDocentesNew;
            materia.setMateriaHasDocentes(materiaHasDocentesNew);
            materia = em.merge(materia);
            for (EspacioHasMateria espacioHasMateriasNewEspacioHasMateria : espacioHasMateriasNew) {
                if (!espacioHasMateriasOld.contains(espacioHasMateriasNewEspacioHasMateria)) {
                    Materia oldMateriaOfEspacioHasMateriasNewEspacioHasMateria = espacioHasMateriasNewEspacioHasMateria.getMateria();
                    espacioHasMateriasNewEspacioHasMateria.setMateria(materia);
                    espacioHasMateriasNewEspacioHasMateria = em.merge(espacioHasMateriasNewEspacioHasMateria);
                    if (oldMateriaOfEspacioHasMateriasNewEspacioHasMateria != null && !oldMateriaOfEspacioHasMateriasNewEspacioHasMateria.equals(materia)) {
                        oldMateriaOfEspacioHasMateriasNewEspacioHasMateria.getEspacioHasMaterias().remove(espacioHasMateriasNewEspacioHasMateria);
                        oldMateriaOfEspacioHasMateriasNewEspacioHasMateria = em.merge(oldMateriaOfEspacioHasMateriasNewEspacioHasMateria);
                    }
                }
            }
            for (MateriaHasHorario materiaHasHorariosNewMateriaHasHorario : materiaHasHorariosNew) {
                if (!materiaHasHorariosOld.contains(materiaHasHorariosNewMateriaHasHorario)) {
                    Materia oldMateriaOfMateriaHasHorariosNewMateriaHasHorario = materiaHasHorariosNewMateriaHasHorario.getMateria();
                    materiaHasHorariosNewMateriaHasHorario.setMateria(materia);
                    materiaHasHorariosNewMateriaHasHorario = em.merge(materiaHasHorariosNewMateriaHasHorario);
                    if (oldMateriaOfMateriaHasHorariosNewMateriaHasHorario != null && !oldMateriaOfMateriaHasHorariosNewMateriaHasHorario.equals(materia)) {
                        oldMateriaOfMateriaHasHorariosNewMateriaHasHorario.getMateriaHasHorarios().remove(materiaHasHorariosNewMateriaHasHorario);
                        oldMateriaOfMateriaHasHorariosNewMateriaHasHorario = em.merge(oldMateriaOfMateriaHasHorariosNewMateriaHasHorario);
                    }
                }
            }
            for (MatriculaHasMateria matriculaHasMateriasNewMatriculaHasMateria : matriculaHasMateriasNew) {
                if (!matriculaHasMateriasOld.contains(matriculaHasMateriasNewMatriculaHasMateria)) {
                    Materia oldMateriaOfMatriculaHasMateriasNewMatriculaHasMateria = matriculaHasMateriasNewMatriculaHasMateria.getMateria();
                    matriculaHasMateriasNewMatriculaHasMateria.setMateria(materia);
                    matriculaHasMateriasNewMatriculaHasMateria = em.merge(matriculaHasMateriasNewMatriculaHasMateria);
                    if (oldMateriaOfMatriculaHasMateriasNewMatriculaHasMateria != null && !oldMateriaOfMatriculaHasMateriasNewMatriculaHasMateria.equals(materia)) {
                        oldMateriaOfMatriculaHasMateriasNewMatriculaHasMateria.getMatriculaHasMaterias().remove(matriculaHasMateriasNewMatriculaHasMateria);
                        oldMateriaOfMatriculaHasMateriasNewMatriculaHasMateria = em.merge(oldMateriaOfMatriculaHasMateriasNewMatriculaHasMateria);
                    }
                }
            }
            for (MateriaHasDocente materiaHasDocentesNewMateriaHasDocente : materiaHasDocentesNew) {
                if (!materiaHasDocentesOld.contains(materiaHasDocentesNewMateriaHasDocente)) {
                    Materia oldMateriaOfMateriaHasDocentesNewMateriaHasDocente = materiaHasDocentesNewMateriaHasDocente.getMateria();
                    materiaHasDocentesNewMateriaHasDocente.setMateria(materia);
                    materiaHasDocentesNewMateriaHasDocente = em.merge(materiaHasDocentesNewMateriaHasDocente);
                    if (oldMateriaOfMateriaHasDocentesNewMateriaHasDocente != null && !oldMateriaOfMateriaHasDocentesNewMateriaHasDocente.equals(materia)) {
                        oldMateriaOfMateriaHasDocentesNewMateriaHasDocente.getMateriaHasDocentes().remove(materiaHasDocentesNewMateriaHasDocente);
                        oldMateriaOfMateriaHasDocentesNewMateriaHasDocente = em.merge(oldMateriaOfMateriaHasDocentesNewMateriaHasDocente);
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
                Integer id = materia.getIdMateria();
                if (findMateria(id) == null) {
                    throw new NonexistentEntityException("The materia with id " + id + " no longer exists.");
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
            Materia materia;
            try {
                materia = em.getReference(Materia.class, id);
                materia.getIdMateria();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The materia with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Set<EspacioHasMateria> espacioHasMateriasOrphanCheck = materia.getEspacioHasMaterias();
            for (EspacioHasMateria espacioHasMateriasOrphanCheckEspacioHasMateria : espacioHasMateriasOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Materia (" + materia + ") cannot be destroyed since the EspacioHasMateria " + espacioHasMateriasOrphanCheckEspacioHasMateria + " in its espacioHasMaterias field has a non-nullable materia field.");
            }
            Set<MateriaHasHorario> materiaHasHorariosOrphanCheck = materia.getMateriaHasHorarios();
            for (MateriaHasHorario materiaHasHorariosOrphanCheckMateriaHasHorario : materiaHasHorariosOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Materia (" + materia + ") cannot be destroyed since the MateriaHasHorario " + materiaHasHorariosOrphanCheckMateriaHasHorario + " in its materiaHasHorarios field has a non-nullable materia field.");
            }
            Set<MatriculaHasMateria> matriculaHasMateriasOrphanCheck = materia.getMatriculaHasMaterias();
            for (MatriculaHasMateria matriculaHasMateriasOrphanCheckMatriculaHasMateria : matriculaHasMateriasOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Materia (" + materia + ") cannot be destroyed since the MatriculaHasMateria " + matriculaHasMateriasOrphanCheckMatriculaHasMateria + " in its matriculaHasMaterias field has a non-nullable materia field.");
            }
            Set<MateriaHasDocente> materiaHasDocentesOrphanCheck = materia.getMateriaHasDocentes();
            for (MateriaHasDocente materiaHasDocentesOrphanCheckMateriaHasDocente : materiaHasDocentesOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Materia (" + materia + ") cannot be destroyed since the MateriaHasDocente " + materiaHasDocentesOrphanCheckMateriaHasDocente + " in its materiaHasDocentes field has a non-nullable materia field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(materia);
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

    public List<Materia> findMateriaEntities() {
        return findMateriaEntities(true, -1, -1);
    }

    public List<Materia> findMateriaEntities(int maxResults, int firstResult) {
        return findMateriaEntities(false, maxResults, firstResult);
    }

    private List<Materia> findMateriaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Materia.class));
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

    public Materia findMateria(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Materia.class, id);
        } finally {
            em.close();
        }
    }

    public int getMateriaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Materia> rt = cq.from(Materia.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
