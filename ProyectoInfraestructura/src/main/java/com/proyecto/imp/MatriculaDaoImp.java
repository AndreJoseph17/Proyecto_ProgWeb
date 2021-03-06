/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proyecto.imp;

import com.proyecto.dao.MatriculaDao;
import com.proyecto.modelo.Matricula;
import com.proyecto.modelo.MatriculaHasMateria;
import com.proyecto.util.HibernateUtil;
import java.util.List;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author andre
 */
public class MatriculaDaoImp implements MatriculaDao{

    @Override
    public List<Matricula> listarMatriculas() {
        
        List<Matricula> lista = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction t = session.beginTransaction();
        String hql = "FROM Matricula";
        try {
            lista = session.createQuery(hql).list();
            for (Matricula mhh : lista) {
                Hibernate.initialize(mhh.getEstudiante());
            }
            t.commit();
            session.close();
        } catch (Exception e) {
            t.rollback();
        }
        return lista;
    }

    @Override
    public void agregarMatricula(Matricula matricula) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.save(matricula);
            session.getTransaction().commit();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            session.getTransaction().rollback();
        } finally {
            if (session != null) {

                session.close();
            }
        }
    }

    @Override
    public void actualizarMatricula(Matricula matricula) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.update(matricula);
            session.getTransaction().commit();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            session.getTransaction().rollback();
        } finally {
            if (session != null) {

                session.close();
            }
        }
    }

    @Override
    public void eliminarMatricula(Matricula matricula) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.delete(matricula);
            session.getTransaction().commit();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            session.getTransaction().rollback();
        } finally {
            if (session != null) {

                session.close();
            }
        }
        
    }

    @Override
    public void agregarMatriculaHasMateria(MatriculaHasMateria matriculaHasMateria) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.save(matriculaHasMateria);
            session.getTransaction().commit();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            session.getTransaction().rollback();
        } finally {
            if (session != null) {

                session.close();
            }
        }
    }

    @Override
    public List<MatriculaHasMateria> listarMateriasMatriculadas() {
        List<MatriculaHasMateria> lista = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction t = session.beginTransaction();
        String hql = "FROM MatriculaHasMateria";
        try {
            lista = session.createQuery(hql).list();
            for (MatriculaHasMateria mhh : lista) {
                Hibernate.initialize(mhh.getMateria());
                Hibernate.initialize(mhh.getMatricula());
            }
            t.commit();
            session.close();
        } catch (Exception e) {
            t.rollback();
        }
        return lista;
    }
    
}
