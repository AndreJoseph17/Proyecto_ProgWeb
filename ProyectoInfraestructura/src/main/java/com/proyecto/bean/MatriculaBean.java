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
    
    private String periodo;
    private String observacion;
    
    private String idMateriaAgregar;
    
    private List<Matricula> matriculas;
    private List<Materia> materias;
    private Estudiante estudianteSeleccionado;
    private Materia materiaSeleccionada;

    public String getIdMateriaAgregar() {
        return idMateriaAgregar;
    }

    public void setIdMateriaAgregar(String idMateriaAgregar) {
        this.idMateriaAgregar = idMateriaAgregar;
    }
    
    
    
    public String getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(String idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public void setMatriculas(List<Matricula> matriculas) {
        this.matriculas = matriculas;
    }

    public List<Matricula> getMatriculas() {
        return matriculas;
    }

    public List<Materia> getMaterias() {
        return materias;
    }

    public void setMaterias(List<Materia> materias) {
        this.materias = materias;
    }

    public Materia getMateriaSeleccionada() {
        return materiaSeleccionada;
    }

    public void setMateriaSeleccionada(Materia materiaSeleccionada) {
        this.materiaSeleccionada = materiaSeleccionada;
    }
    
    

    public Estudiante getEstudianteSeleccionado() {
        return estudianteSeleccionado;
    }

    public void setEstudianteSeleccionado(Estudiante estudianteSeleccionado) {
        this.estudianteSeleccionado = estudianteSeleccionado;
    }
    

    
    
    
    public void matricularEstrudiante(){
        
    }
    public void agregarMateria(){
        
    }
}