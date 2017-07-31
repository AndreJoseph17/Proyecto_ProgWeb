package com.proyecto.bean;

import com.proyecto.dao.EspacioDao;
import com.proyecto.dao.MateriaDao;
import com.proyecto.imp.EspacioDaoImp;
import com.proyecto.imp.MateriaDaoImp;
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

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.primefaces.component.api.UIData;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;


@ManagedBean
@ViewScoped
public class reportesBean  implements Serializable{
    private int ID_MATERIA_SELECCIONADA=-1;
    private List<Materia> materias;
    private Materia materiaSeleccionada;

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
        
        return listarMaterias();
    }

    public void setMaterias(List<Materia> materias) {
        this.materias = materias;
    }

    private List<Materia> listarMaterias() {
        
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
    
    
    
    public void onSelect(SelectEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Item Selected", event.getObject().toString()));
    }
    
    
}