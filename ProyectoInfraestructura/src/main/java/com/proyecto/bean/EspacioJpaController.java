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
import com.proyecto.modelo.Normativa;
import com.proyecto.modelo.Tipo;
import com.proyecto.modelo.EquipoInformatico;
import com.proyecto.modelo.Espacio;
import java.util.HashSet;
import java.util.Set;
import com.proyecto.modelo.EspacioHasMateria;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author andre
 */
public class EspacioJpaController implements Serializable {

    public EspacioJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Espacio espacio) throws RollbackFailureException, Exception {
        if (espacio.getEquipoInformaticos() == null) {
            espacio.setEquipoInformaticos(new HashSet<EquipoInformatico>());
        }
        if (espacio.getEspacioHasMaterias() == null) {
            espacio.setEspacioHasMaterias(new HashSet<EspacioHasMateria>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Normativa normativa = espacio.getNormativa();
            if (normativa != null) {
                normativa = em.getReference(normativa.getClass(), normativa.getIdNormativa());
                espacio.setNormativa(normativa);
            }
            Tipo tipo = espacio.getTipo();
            if (tipo != null) {
                tipo = em.getReference(tipo.getClass(), tipo.getIdTipo());
                espacio.setTipo(tipo);
            }
            Set<EquipoInformatico> attachedEquipoInformaticos = new HashSet<EquipoInformatico>();
            for (EquipoInformatico equipoInformaticosEquipoInformaticoToAttach : espacio.getEquipoInformaticos()) {
                equipoInformaticosEquipoInformaticoToAttach = em.getReference(equipoInformaticosEquipoInformaticoToAttach.getClass(), equipoInformaticosEquipoInformaticoToAttach.getIdEquipoInformatico());
                attachedEquipoInformaticos.add(equipoInformaticosEquipoInformaticoToAttach);
            }
            espacio.setEquipoInformaticos(attachedEquipoInformaticos);
            Set<EspacioHasMateria> attachedEspacioHasMaterias = new HashSet<EspacioHasMateria>();
            for (EspacioHasMateria espacioHasMateriasEspacioHasMateriaToAttach : espacio.getEspacioHasMaterias()) {
                espacioHasMateriasEspacioHasMateriaToAttach = em.getReference(espacioHasMateriasEspacioHasMateriaToAttach.getClass(), espacioHasMateriasEspacioHasMateriaToAttach.getId());
                attachedEspacioHasMaterias.add(espacioHasMateriasEspacioHasMateriaToAttach);
            }
            espacio.setEspacioHasMaterias(attachedEspacioHasMaterias);
            em.persist(espacio);
            if (normativa != null) {
                normativa.getEspacios().add(espacio);
                normativa = em.merge(normativa);
            }
            if (tipo != null) {
                tipo.getEspacios().add(espacio);
                tipo = em.merge(tipo);
            }
            for (EquipoInformatico equipoInformaticosEquipoInformatico : espacio.getEquipoInformaticos()) {
                Espacio oldEspacioOfEquipoInformaticosEquipoInformatico = equipoInformaticosEquipoInformatico.getEspacio();
                equipoInformaticosEquipoInformatico.setEspacio(espacio);
                equipoInformaticosEquipoInformatico = em.merge(equipoInformaticosEquipoInformatico);
                if (oldEspacioOfEquipoInformaticosEquipoInformatico != null) {
                    oldEspacioOfEquipoInformaticosEquipoInformatico.getEquipoInformaticos().remove(equipoInformaticosEquipoInformatico);
                    oldEspacioOfEquipoInformaticosEquipoInformatico = em.merge(oldEspacioOfEquipoInformaticosEquipoInformatico);
                }
            }
            for (EspacioHasMateria espacioHasMateriasEspacioHasMateria : espacio.getEspacioHasMaterias()) {
                Espacio oldEspacioOfEspacioHasMateriasEspacioHasMateria = espacioHasMateriasEspacioHasMateria.getEspacio();
                espacioHasMateriasEspacioHasMateria.setEspacio(espacio);
                espacioHasMateriasEspacioHasMateria = em.merge(espacioHasMateriasEspacioHasMateria);
                if (oldEspacioOfEspacioHasMateriasEspacioHasMateria != null) {
                    oldEspacioOfEspacioHasMateriasEspacioHasMateria.getEspacioHasMaterias().remove(espacioHasMateriasEspacioHasMateria);
                    oldEspacioOfEspacioHasMateriasEspacioHasMateria = em.merge(oldEspacioOfEspacioHasMateriasEspacioHasMateria);
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

    public void edit(Espacio espacio) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Espacio persistentEspacio = em.find(Espacio.class, espacio.getIdEspacio());
            Normativa normativaOld = persistentEspacio.getNormativa();
            Normativa normativaNew = espacio.getNormativa();
            Tipo tipoOld = persistentEspacio.getTipo();
            Tipo tipoNew = espacio.getTipo();
            Set<EquipoInformatico> equipoInformaticosOld = persistentEspacio.getEquipoInformaticos();
            Set<EquipoInformatico> equipoInformaticosNew = espacio.getEquipoInformaticos();
            Set<EspacioHasMateria> espacioHasMateriasOld = persistentEspacio.getEspacioHasMaterias();
            Set<EspacioHasMateria> espacioHasMateriasNew = espacio.getEspacioHasMaterias();
            List<String> illegalOrphanMessages = null;
            for (EspacioHasMateria espacioHasMateriasOldEspacioHasMateria : espacioHasMateriasOld) {
                if (!espacioHasMateriasNew.contains(espacioHasMateriasOldEspacioHasMateria)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain EspacioHasMateria " + espacioHasMateriasOldEspacioHasMateria + " since its espacio field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (normativaNew != null) {
                normativaNew = em.getReference(normativaNew.getClass(), normativaNew.getIdNormativa());
                espacio.setNormativa(normativaNew);
            }
            if (tipoNew != null) {
                tipoNew = em.getReference(tipoNew.getClass(), tipoNew.getIdTipo());
                espacio.setTipo(tipoNew);
            }
            Set<EquipoInformatico> attachedEquipoInformaticosNew = new HashSet<EquipoInformatico>();
            for (EquipoInformatico equipoInformaticosNewEquipoInformaticoToAttach : equipoInformaticosNew) {
                equipoInformaticosNewEquipoInformaticoToAttach = em.getReference(equipoInformaticosNewEquipoInformaticoToAttach.getClass(), equipoInformaticosNewEquipoInformaticoToAttach.getIdEquipoInformatico());
                attachedEquipoInformaticosNew.add(equipoInformaticosNewEquipoInformaticoToAttach);
            }
            equipoInformaticosNew = attachedEquipoInformaticosNew;
            espacio.setEquipoInformaticos(equipoInformaticosNew);
            Set<EspacioHasMateria> attachedEspacioHasMateriasNew = new HashSet<EspacioHasMateria>();
            for (EspacioHasMateria espacioHasMateriasNewEspacioHasMateriaToAttach : espacioHasMateriasNew) {
                espacioHasMateriasNewEspacioHasMateriaToAttach = em.getReference(espacioHasMateriasNewEspacioHasMateriaToAttach.getClass(), espacioHasMateriasNewEspacioHasMateriaToAttach.getId());
                attachedEspacioHasMateriasNew.add(espacioHasMateriasNewEspacioHasMateriaToAttach);
            }
            espacioHasMateriasNew = attachedEspacioHasMateriasNew;
            espacio.setEspacioHasMaterias(espacioHasMateriasNew);
            espacio = em.merge(espacio);
            if (normativaOld != null && !normativaOld.equals(normativaNew)) {
                normativaOld.getEspacios().remove(espacio);
                normativaOld = em.merge(normativaOld);
            }
            if (normativaNew != null && !normativaNew.equals(normativaOld)) {
                normativaNew.getEspacios().add(espacio);
                normativaNew = em.merge(normativaNew);
            }
            if (tipoOld != null && !tipoOld.equals(tipoNew)) {
                tipoOld.getEspacios().remove(espacio);
                tipoOld = em.merge(tipoOld);
            }
            if (tipoNew != null && !tipoNew.equals(tipoOld)) {
                tipoNew.getEspacios().add(espacio);
                tipoNew = em.merge(tipoNew);
            }
            for (EquipoInformatico equipoInformaticosOldEquipoInformatico : equipoInformaticosOld) {
                if (!equipoInformaticosNew.contains(equipoInformaticosOldEquipoInformatico)) {
                    equipoInformaticosOldEquipoInformatico.setEspacio(null);
                    equipoInformaticosOldEquipoInformatico = em.merge(equipoInformaticosOldEquipoInformatico);
                }
            }
            for (EquipoInformatico equipoInformaticosNewEquipoInformatico : equipoInformaticosNew) {
                if (!equipoInformaticosOld.contains(equipoInformaticosNewEquipoInformatico)) {
                    Espacio oldEspacioOfEquipoInformaticosNewEquipoInformatico = equipoInformaticosNewEquipoInformatico.getEspacio();
                    equipoInformaticosNewEquipoInformatico.setEspacio(espacio);
                    equipoInformaticosNewEquipoInformatico = em.merge(equipoInformaticosNewEquipoInformatico);
                    if (oldEspacioOfEquipoInformaticosNewEquipoInformatico != null && !oldEspacioOfEquipoInformaticosNewEquipoInformatico.equals(espacio)) {
                        oldEspacioOfEquipoInformaticosNewEquipoInformatico.getEquipoInformaticos().remove(equipoInformaticosNewEquipoInformatico);
                        oldEspacioOfEquipoInformaticosNewEquipoInformatico = em.merge(oldEspacioOfEquipoInformaticosNewEquipoInformatico);
                    }
                }
            }
            for (EspacioHasMateria espacioHasMateriasNewEspacioHasMateria : espacioHasMateriasNew) {
                if (!espacioHasMateriasOld.contains(espacioHasMateriasNewEspacioHasMateria)) {
                    Espacio oldEspacioOfEspacioHasMateriasNewEspacioHasMateria = espacioHasMateriasNewEspacioHasMateria.getEspacio();
                    espacioHasMateriasNewEspacioHasMateria.setEspacio(espacio);
                    espacioHasMateriasNewEspacioHasMateria = em.merge(espacioHasMateriasNewEspacioHasMateria);
                    if (oldEspacioOfEspacioHasMateriasNewEspacioHasMateria != null && !oldEspacioOfEspacioHasMateriasNewEspacioHasMateria.equals(espacio)) {
                        oldEspacioOfEspacioHasMateriasNewEspacioHasMateria.getEspacioHasMaterias().remove(espacioHasMateriasNewEspacioHasMateria);
                        oldEspacioOfEspacioHasMateriasNewEspacioHasMateria = em.merge(oldEspacioOfEspacioHasMateriasNewEspacioHasMateria);
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
                Integer id = espacio.getIdEspacio();
                if (findEspacio(id) == null) {
                    throw new NonexistentEntityException("The espacio with id " + id + " no longer exists.");
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
            Espacio espacio;
            try {
                espacio = em.getReference(Espacio.class, id);
                espacio.getIdEspacio();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The espacio with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Set<EspacioHasMateria> espacioHasMateriasOrphanCheck = espacio.getEspacioHasMaterias();
            for (EspacioHasMateria espacioHasMateriasOrphanCheckEspacioHasMateria : espacioHasMateriasOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Espacio (" + espacio + ") cannot be destroyed since the EspacioHasMateria " + espacioHasMateriasOrphanCheckEspacioHasMateria + " in its espacioHasMaterias field has a non-nullable espacio field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Normativa normativa = espacio.getNormativa();
            if (normativa != null) {
                normativa.getEspacios().remove(espacio);
                normativa = em.merge(normativa);
            }
            Tipo tipo = espacio.getTipo();
            if (tipo != null) {
                tipo.getEspacios().remove(espacio);
                tipo = em.merge(tipo);
            }
            Set<EquipoInformatico> equipoInformaticos = espacio.getEquipoInformaticos();
            for (EquipoInformatico equipoInformaticosEquipoInformatico : equipoInformaticos) {
                equipoInformaticosEquipoInformatico.setEspacio(null);
                equipoInformaticosEquipoInformatico = em.merge(equipoInformaticosEquipoInformatico);
            }
            em.remove(espacio);
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

    public List<Espacio> findEspacioEntities() {
        return findEspacioEntities(true, -1, -1);
    }

    public List<Espacio> findEspacioEntities(int maxResults, int firstResult) {
        return findEspacioEntities(false, maxResults, firstResult);
    }

    private List<Espacio> findEspacioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Espacio.class));
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

    public Espacio findEspacio(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Espacio.class, id);
        } finally {
            em.close();
        }
    }

    public int getEspacioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Espacio> rt = cq.from(Espacio.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
