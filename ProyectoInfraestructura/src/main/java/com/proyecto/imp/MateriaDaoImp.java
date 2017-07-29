/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proyecto.imp;

import com.proyecto.dao.MateriaDao;
import com.proyecto.modelo.*;
import com.proyecto.util.HibernateUtil;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author andre
 */
public class MateriaDaoImp implements MateriaDao{

    @Override
    public List<Materia> listarMaterias() {
        
        List<Materia> lista = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction t = session.beginTransaction();
        String hql = "FROM Materia";
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
    public void agregarMaterias(Materia materia) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.save(materia);
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
    public void eliminarMateria(Materia materia) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.delete(materia);
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
    public void actualizarMateria(Materia materia) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.update(materia);
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
    public Materia buscarMateria(int codigo) {
        Materia materia=new Materia();
        List <Materia> listaDocentes=new ArrayList<Materia>();
        Session session=HibernateUtil.getSessionFactory().openSession();
        Transaction t=session.beginTransaction();
        String hql="FROM Materia where idMateria="+codigo;
       // Query query=session.createQuery(hql);
        //query.setInteger("codigo",codigo);
        try{
        listaDocentes=session.createQuery(hql).list();
        materia=listaDocentes.get(0);
        t.commit();
        session.close();
        }
        catch(Exception e){
        t.rollback();
        }
        return materia;    
    }
    
     @Override
    public Horario buscarHorario(int codigo) {
        Horario horario=new Horario();
        List <Horario> listaDocentes=new ArrayList<Horario>();
        Session session=HibernateUtil.getSessionFactory().openSession();
        Transaction t=session.beginTransaction();
        String hql="FROM Horario where idHorario="+codigo;
       // Query query=session.createQuery(hql);
        //query.setInteger("codigo",codigo);
        try{
        listaDocentes=session.createQuery(hql).list();
        horario=listaDocentes.get(0);
        t.commit();
        session.close();
        }
        catch(Exception e){
        t.rollback();
        }
        return horario;    
    }
    @Override
    public Docente buscarDocente(int codigo) {
        Docente docente=new Docente();
        List <Docente> listaDocentes=new ArrayList<Docente>();
        Session session=HibernateUtil.getSessionFactory().openSession();
        Transaction t=session.beginTransaction();
        String hql="FROM Docente where idDocente="+codigo;
       // Query query=session.createQuery(hql);
        //query.setInteger("codigo",codigo);
        try{
        listaDocentes=session.createQuery(hql).list();
        docente=listaDocentes.get(0);
        t.commit();
        session.close();
        }
        catch(Exception e){
        t.rollback();
        }
        return docente;    
    }
    
    @Override
    public Dia buscarDia(int codigo) {
        Dia docente=new Dia();
        List <Dia> listaDocentes=new ArrayList<Dia>();
        Session session=HibernateUtil.getSessionFactory().openSession();
        Transaction t=session.beginTransaction();
        String hql="FROM Dia where idDias="+codigo;
       // Query query=session.createQuery(hql);
        //query.setInteger("codigo",codigo);
        try{
        listaDocentes=session.createQuery(hql).list();
        docente=listaDocentes.get(0);
        t.commit();
        session.close();
        }
        catch(Exception e){
        t.rollback();
        }
        return docente;    
    }

    @Override
    public void agregarMateriaHasHorario(MateriaHasHorario materiaHasHorario) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.save(materiaHasHorario);
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
    public List<MateriaHasHorario> buscarHorarioHasMateria() {
        MateriaHasHorario materiahashorario=new MateriaHasHorario();
        List <MateriaHasHorario> listaDocentes=new ArrayList<MateriaHasHorario>();
        Session session=HibernateUtil.getSessionFactory().openSession();
        Transaction t=session.beginTransaction();
        String hql="FROM MateriaHasHorario";
       // Query query=session.createQuery(hql);
        //query.setInteger("codigo",codigo);
        try{
        listaDocentes=session.createQuery(hql).list();        
        t.commit();
        session.close();
        }
        catch(Exception e){
        t.rollback();
        }
        return listaDocentes;    
    }
    
    
}
