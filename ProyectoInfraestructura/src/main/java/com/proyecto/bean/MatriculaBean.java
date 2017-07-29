package com.proyecto.bean;

import com.proyecto.modelo.*;
import com.proyecto.util.HibernateUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;


@ManagedBean
public class MatriculaBean {
    private String idEstudiante;
    
    private Integer idMatricula;
    private Estudiante estudiante;
    private String periodo;
    private String observacion;

    public String getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(String idEstudiante) {
        this.idEstudiante = idEstudiante;
    }
    
    public void matricularEstrudiante(){
        
    }
}