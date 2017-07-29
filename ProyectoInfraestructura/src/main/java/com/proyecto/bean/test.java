/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proyecto.services;

import com.proyecto.bean.MatriculaJpaController;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.StoredProcedureQuery;

/**
 *
 * @author andre
 */
public class test {
    
    EntityManagerFactory emf;
    EntityManager em;
    MatriculaJpaController mjc;
    
    public void iniciar(){
        emf = Persistence.createEntityManagerFactory("persistencia_infraestructura");
        mjc = new MatriculaJpaController(emf);
        em = mjc.getEntityManager();
    }
      
    public Integer matricularEstudiantes(
                                    String codigo_matricula, 
                                    String codigo_materia){
        iniciar();
        em.getTransaction().begin();
        Integer valor_retorno = null;
        try{
            StoredProcedureQuery query = em.createStoredProcedureQuery("matricular");
            query.setParameter("codigo_matricula", codigo_matricula);
            query.setParameter("codigo_materia", codigo_materia);
            query.execute();
            
            valor_retorno = (Integer) query.getOutputParameterValue("valor_objeto");
            em.getTransaction().commit();
        }catch(Exception e){
            e.printStackTrace();;
            em.getTransaction().rollback();
            em.close();
        }
        return valor_retorno;
        
    }
    
}
