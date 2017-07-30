/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proyecto.imp;

import com.proyecto.dao.EspacioDao;
import com.proyecto.modelo.Dia;
import com.proyecto.modelo.Espacio;
import com.proyecto.modelo.EspacioHasMateria;
import com.proyecto.modelo.MateriaHasDocente;
import com.proyecto.modelo.MateriaHasHorario;
import com.proyecto.modelo.Tipo;
import com.proyecto.util.HibernateUtil;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Hibernate;
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
    @Override
    public Espacio buscarEspacio(int codigo) {
        
        List<Espacio> listaEspacios = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction t = session.beginTransaction();
        String hql = "FROM Espacio";
        //String hql="FROM MateriaHasHorario where materia_idMateria="+codigo;

        //query.setInteger("codigo",codigo);
        try {
            listaEspacios = session.createQuery(hql).list();
            for (Espacio mhh : listaEspacios) {
                Hibernate.initialize(mhh.getTipo());
                Hibernate.initialize(mhh.getNormativa());
                Hibernate.initialize(mhh.getNombreEspacio());
                Hibernate.initialize(mhh.getIluminacion());
                Hibernate.initialize(mhh.getInternet());
                Hibernate.initialize(mhh.getTipo().getNombre());
            }
            //listaDocentes = session.createCriteria(MateriaHasHorario.class).list();
            t.commit();

        } catch (Exception e) {
            t.rollback();
        }

        
        session.close();
        int i = 0;
        Espacio espacio = null;
        while (i<listaEspacios.size()) {
            if (listaEspacios.get(i).getIdEspacio()== codigo) {
                espacio=listaEspacios.get(i);
                break;
            }
            i++;
        }
        return espacio;
        
      
    }

    @Override
    public void agregarEspacioHasMateria(EspacioHasMateria espacioHasMateria) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.save(espacioHasMateria);
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
    public List<EspacioHasMateria> buscarEspacioHasMateria() {
        List<EspacioHasMateria> listaMaterias = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction t = session.beginTransaction();
        String hql = "FROM EspacioHasMateria";
        
        try {
            listaMaterias = session.createQuery(hql).list();
            for (EspacioHasMateria mhh : listaMaterias) {
                Hibernate.initialize(mhh.getMateria());
                Hibernate.initialize(mhh.getEspacio());
            }
            
            t.commit();

        } catch (Exception e) {
            t.rollback();
        }

        
        session.close();
        return listaMaterias;
    }

    
    
}
