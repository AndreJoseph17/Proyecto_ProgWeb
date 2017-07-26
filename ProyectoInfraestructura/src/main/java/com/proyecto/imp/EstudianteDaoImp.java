/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proyecto.imp;

import com.proyecto.dao.EstudianteDao;
import com.proyecto.modelo.Estudiante;
import com.proyecto.util.HibernateUtil;
import java.util.List;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author andre
 */
public class EstudianteDaoImp implements EstudianteDao{

    @Override
    public List<Estudiante> listarEstudiante() {
        List<Estudiante> lista = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction t = session.beginTransaction();
        String hql = "From Estudiante";
        try{
            lista = session.createQuery(hql).list();
            t.commit();
            session.close();
        } catch(Exception e){
            t.rollback();
        }
        return lista;
    }

    @Override
    public void agregarUsuario(Estudiante estudiante) {
        Session session = null;
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.save(estudiante);
            session.getTransaction().commit();
        }catch(Exception e){
            System.out.println(e.getMessage());
            session.getTransaction().rollback();
        }
        session.close();
    }

    @Override
    public void actualizarUsuario(Estudiante estudiante) {
        Session session = null;
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.update(estudiante);
            session.getTransaction().commit();
            
        }catch(Exception e){
            System.out.println(e.getMessage());
            session.getTransaction().rollback();
        }
        session.close();
    }

    @Override
    public void eliminarEstudiante(Estudiante estudiante) {
        Session session = null;
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.delete(estudiante);
            session.getTransaction().commit();
            
        }catch(Exception e){
            System.out.println(e.getMessage());
            session.getTransaction().rollback();
    
        }
        session.close();
        
    }
    
    
}
