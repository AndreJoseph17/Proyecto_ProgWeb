/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proyecto.imp;

import com.proyecto.dao.EquipoInformaticoDao;
import com.proyecto.dao.EspacioDao;
import com.proyecto.modelo.Dia;
import com.proyecto.modelo.EquipoInformatico;
import com.proyecto.modelo.Espacio;
import com.proyecto.modelo.EspacioHasMateria;
import com.proyecto.modelo.MateriaHasDocente;
import com.proyecto.modelo.MateriaHasHorario;
import com.proyecto.modelo.Tipo;
import com.proyecto.util.HibernateUtil;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author andre
 */
public class EquipoInformaticoDaoImp implements EquipoInformaticoDao{

    @Override
    public List<EquipoInformatico> listarEquiposInformaticos() {
        List<EquipoInformatico> listaEquipos = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction t = session.beginTransaction();
        String hql = "FROM EquipoInformatico";
        
        try {
            listaEquipos = session.createQuery(hql).list();
            for (EquipoInformatico mhh : listaEquipos) {
                Hibernate.initialize(mhh.getEspacio());
            }
            
            t.commit();

        } catch (Exception e) {
            t.rollback();
        }

        
        session.close();
        return listaEquipos;
    }
    
    

    @Override
    public void agregarEquipo(EquipoInformatico equipo) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.save(equipo);
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
    public void actualizarEquipo(EquipoInformatico equipo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void eliminarEquipo(EquipoInformatico equipo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

   
    
}
