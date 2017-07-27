package com.proyecto.bean;


import com.proyecto.modelo.*;
import com.proyecto.util.HibernateUtil;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;


@ManagedBean
public class EspacioBean {
    private int disponibilidad;
    private int funcionalidad;
    private int equipamiento;
    
    private String idFormula;
    private String nombreEquipo;
    private String descripcionEquipo;
    
    private String idMateriaAgregar;
    
    private String otroEspacio;
    private String tipoEspacio;
    private String idEspacio;
    private String nombre;
    private String tipo;
    private String ubicacion;
    private String area;
    private String numEscritorios;
    private String iluminacion;
    private String internet;
    private String capacidad;
    
    private List<Espacio> espacios;
    private Espacio espacioSeleccionado;
    
    private List<EspacioHasMateria> materias;
    private List<EquipoInformatico> equipos;

    public int getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(int disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public int getEquipamiento() {
        return equipamiento;
    }

    public void setEquipamiento(int equipamiento) {
        this.equipamiento = equipamiento;
    }

    public int getFuncionalidad() {
        return funcionalidad;
    }

    public void setFuncionalidad(int funcionalidad) {
        this.funcionalidad = funcionalidad;
    }
    
    

    public String getIdFormula() {
        return idFormula;
    }

    public void setIdFormula(String idFormula) {
        this.idFormula = idFormula;
    }
    
    

    public String getDescripcionEquipo() {
        return descripcionEquipo;
    }

    public void setDescripcionEquipo(String descripcionEquipo) {
        this.descripcionEquipo = descripcionEquipo;
    }

    public String getNombreEquipo() {
        return nombreEquipo;
    }

    public void setNombreEquipo(String nombreEquipo) {
        this.nombreEquipo = nombreEquipo;
    }

    public List<EquipoInformatico> getEquipos() {
        return equipos;
    }

    public void setEquipos(List<EquipoInformatico> equipos) {
        this.equipos = equipos;
    }
    
    
    
    public List<EspacioHasMateria> getMaterias() {
        return materias;
    }

    public void setMaterias(List<EspacioHasMateria> materias) {
        this.materias = materias;
    }
    
    

    public String getIdMateriaAgregar() {
        return idMateriaAgregar;
    }

    public void setIdMateriaAgregar(String idMateriaAgregar) {
        this.idMateriaAgregar = idMateriaAgregar;
    }

    
    public String getOtroEspacio() {
        return otroEspacio;
    }

    public void setOtroEspacio(String otroEspacio) {
        this.otroEspacio = otroEspacio;
    }

    
    public String getInternet() {
        return internet;
    }

    public void setInternet(String internet) {
        this.internet = internet;
    }

    public String getTipoEspacio() {
        return tipoEspacio;
    }

    public void setTipoEspacio(String tipoEspacio) {
        this.tipoEspacio = tipoEspacio;
    }

    

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Espacio getEspacioSeleccionado() {
        return espacioSeleccionado;
    }

    public void setEspacioSeleccionado(Espacio espacioSeleccionado) {
        this.espacioSeleccionado = espacioSeleccionado;
    }

    public List<Espacio> getEspacios() {
        return listarEspacios();
        //return null;
    }

    public void setEspacios(List<Espacio> espacios) {
        this.espacios = espacios;
    }

    public String getIdEspacio() {
        return idEspacio;
    }

    public void setIdEspacio(String idEspacio) {
        this.idEspacio = idEspacio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setIluminacion(String iluminacion) {
        this.iluminacion = iluminacion;
    }
    public String getIluminacion() {
        return iluminacion;
    }
    public String getCapacidad() {
        return capacidad;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCapacidad(String capacidad) {
        this.capacidad = capacidad;
    }

    public String getNumEscritorios() {
        return numEscritorios;
    }

    public void setNumEscritorios(String numEscritorios) {
        this.numEscritorios = numEscritorios;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }
    

 
    public void onRowSelect(SelectEvent event) {
        FacesMessage msg = new FacesMessage("Espacio Seleccionado", ((Espacio) event.getObject()).getNombreEspacio());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
 
    public void onRowUnselect(UnselectEvent event) {
        FacesMessage msg = new FacesMessage("Espacio no seleccionado", ((Espacio) event.getObject()).getNombreEspacio());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    
    public void registrarEspacio(ActionEvent event) {
           
       
        
    }   

    private List<Espacio> listarEspacios() {
        List<Espacio> listaEspacios=null;
        Session session=HibernateUtil.getSessionFactory().openSession();
        Transaction t=session.beginTransaction();
        String hql="from espacio";
        try{
            listaEspacios=session.createQuery(hql).list();
            t.commit();
            session.close();
        }
        catch(Exception e){
            t.rollback();
        }
        return listaEspacios;
    }
    
  
    public void registrarMateria(ActionEvent event){
        
    }
    public void registrarEquipo(ActionEvent event){
        
    }
    
}