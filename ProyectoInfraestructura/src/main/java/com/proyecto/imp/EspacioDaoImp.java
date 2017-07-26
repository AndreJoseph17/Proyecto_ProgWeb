/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proyecto.imp;

import com.proyecto.dao.EspacioDao;
import com.proyecto.modelo.Espacio;
import com.proyecto.util.HibernateUtil;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author andre
 */
public class EspacioDaoImp implements EspacioDao{

    @Override
    public List<Espacio> listarEspacios() {
        List<Espacio> lista = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction t = session.beginTransaction();
        String hql = "FROM Espacio";
        try {
            lista = session.createQuery(hql).list();
            t.commit();
            session.close();
        } catch (Exception e) {
            t.rollback();
        }
        return lista;
        
    }

    @Override
    public void agregarEspacio(Espacio espacio) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.save(espacio);
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
    public void eliminarEspacio(Espacio espacio) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.delete(espacio);
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
    public void actualizarEspacio(Espacio espacio) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.update(espacio);
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
    
}
