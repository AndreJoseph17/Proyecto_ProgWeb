package com.proyecto.bean;

import com.proyecto.modelo.*;
import com.proyecto.util.HibernateUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.hibernate.Hibernate;

import org.hibernate.Session;
import org.hibernate.Transaction;


@ManagedBean
@ViewScoped
public class reportesBean  implements Serializable{
    private int ID_MATERIA_SELECCIONADA=-1;
    private List<Materia> materias;
    private Materia materiaSeleccionada;
    private List<Matricula> estudiantes;

    public List<Matricula> getEstudiantes() {
        List<MatriculaHasMateria> lista =listarMatriculasHasMateria();
        List<Matricula> listaMatriculas = new ArrayList<>();
        for (MatriculaHasMateria m : lista) {
            if(m.getMateria().getIdMateria()==ID_MATERIA_SELECCIONADA){
                listaMatriculas.add(m.getMatricula());
            }
        }
        return listaMatriculas;
    }

    public void setEstudiantes(List<Matricula> estudiantes) {
        this.estudiantes = estudiantes;
    }
    
    

    public Materia getMateriaSeleccionada() {
        return materiaSeleccionada;
    }

    public void setMateriaSeleccionada(Materia materiaSeleccionada) {
        this.materiaSeleccionada = materiaSeleccionada;
    }
    
    

    public int getID_MATERIA_SELECCIONADA() {
        
        return ID_MATERIA_SELECCIONADA;
    }

    public void setID_MATERIA_SELECCIONADA(int ID_MATERIA_SELECCIONADA) {
        this.ID_MATERIA_SELECCIONADA = ID_MATERIA_SELECCIONADA;
    }

    public List<Materia> getMaterias() {
        List<MatriculaHasMateria> lista =listarMatriculasHasMateria();
        List<Materia> listaMaterias = new ArrayList<>();
        for (MatriculaHasMateria m : lista) {
            if(!listaMaterias.contains(m.getMateria()))
                listaMaterias.add(m.getMateria());
        }
        return listaMaterias;
    }

    public void setMaterias(List<Materia> materias) {
        this.materias = materias;
    }

    private List<MatriculaHasMateria> listarMatriculasHasMateria() {
        List<MatriculaHasMateria> lista = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction t = session.beginTransaction();
        String hql = "FROM MatriculaHasMateria";
        try {
            lista = session.createQuery(hql).list();
            for (MatriculaHasMateria mhh : lista) {
                Hibernate.initialize(mhh.getMateria());
                Hibernate.initialize(mhh.getMatricula());
                Hibernate.initialize(mhh.getMatricula().getEstudiante());
            }
            t.commit();
            
        } catch (Exception e) {
            t.rollback();
        }
        session.close();
        return lista;
    }
    
    
    
    public void guardarIdMateriaSeleccionada() {
        ID_MATERIA_SELECCIONADA = materiaSeleccionada.getIdMateria();
    }
    
    
}