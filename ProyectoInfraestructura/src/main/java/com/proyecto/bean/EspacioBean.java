package com.proyecto.bean;

import com.proyecto.dao.*;
import com.proyecto.imp.*;
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
import org.hibernate.Hibernate;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

@ManagedBean
@ViewScoped
public class EspacioBean implements Serializable {

    private int ID_MATERIA_SELECCIONADA=-1;
    
    
    public void guardarIdSelect(){
        ID_MATERIA_SELECCIONADA= espacioSeleccionado.getIdEspacio();
    }

    public int getID_MATERIA_SELECCIONADA() {
        return ID_MATERIA_SELECCIONADA;
    }
    
    
    
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

    private List<Materia> materiasDisponibles;

    public Map<String, String> getMateriasDisponibles() {
        return extraerMaterias();
    }

    public void setMateriasDisponibles(List<Materia> materiasDisponibles) {
        this.materiasDisponibles = materiasDisponibles;
    }

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
        EquipoInformaticoDao dao = new EquipoInformaticoDaoImp();
        List<EquipoInformatico> lista =dao.listarEquiposInformaticos();
        
        List<EquipoInformatico> aux = new ArrayList<>();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getEspacio().getIdEspacio() == ID_MATERIA_SELECCIONADA) {

                
                aux.add(lista.get(i));
            }
        }

        return aux;
    }

    public void setEquipos(List<EquipoInformatico> equipos) {
        this.equipos = equipos;
    }

    public List<EspacioHasMateria> getMaterias() {
        EspacioDao dao = new EspacioDaoImp();

        List<EspacioHasMateria> lista = dao.buscarEspacioHasMateria();
        List<EspacioHasMateria> aux = new ArrayList<>();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getEspacio().getIdEspacio() == espacioSeleccionado.getIdEspacio()) {

                System.out.println("entro");
                aux.add(lista.get(i));
            }
        }
        return aux;
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
        EspacioDao dao = new EspacioDaoImp();
        TipoDao daoTipo = new TipoDaoImp();

        Tipo tipo = daoTipo.buscarTipo(Integer.parseInt(tipoEspacio));
        tipo.setNombre(nombre);

        Espacio m = new Espacio();
        m.setIluminacion(iluminacion);
        m.setInternet(internet);
        m.setTipo(tipo);
        m.setNombreEspacio(nombre);

        if (tipoEspacio.equals("1") || tipoEspacio.equals("3")) {
            m.setCapacidad(Integer.parseInt(capacidad));
        } else if (tipoEspacio.equals("2") || tipoEspacio.equals("4")) {
            m.setNrescritorios(Integer.parseInt(numEscritorios));
            tipo.setNombre(otroEspacio);
        }
        dao.agregarEspacio(m);

    }

    private List<Espacio> listarEspacios() {
        List<Espacio> listaEspacios = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction t = session.beginTransaction();
        String hql = "From Espacio";
        try {
            listaEspacios = session.createQuery(hql).list();
            for (Espacio mhh : listaEspacios) {
                Hibernate.initialize(mhh.getTipo());
                Hibernate.initialize(mhh.getIdEspacio());
                Hibernate.initialize(mhh.getNombreEspacio());
            }
            t.commit();
            session.close();
        } catch (Exception e) {
            t.rollback();
        }
        return listaEspacios;
    }

    public void registrarMateria(ActionEvent event) {
        MateriaDao daoMateria = new MateriaDaoImp();
        EspacioDao daoEspacio = new EspacioDaoImp();
        int id = Integer.parseInt(idMateriaAgregar);

        Materia materia = daoMateria.buscarMateria(id);

        //materiaSeleccionada = mDao.buscarMateria(1);
        EspacioHasMateriaId espacioHasMateriaId = new EspacioHasMateriaId(espacioSeleccionado.getIdEspacio(), materia.getIdMateria());

        EspacioHasMateria m = new EspacioHasMateria(espacioHasMateriaId, espacioSeleccionado, materia);

        daoEspacio.agregarEspacioHasMateria(m);

    }

    public void registrarEquipo(ActionEvent event) {

        EquipoInformaticoDao dao = new EquipoInformaticoDaoImp();

        EquipoInformatico equipo = new EquipoInformatico(espacioSeleccionado, nombreEquipo, descripcionEquipo);

        dao.agregarEquipo(equipo);
    }

    private Map<String, String> extraerMaterias() {
        Map<String, String> map = new HashMap<>();

        List<Materia> listaMaterias = null;

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction t = session.beginTransaction();
        String hql = "FROM Materia";
        try {
            listaMaterias = session.createQuery(hql).list();
            t.commit();
            session.close();
        } catch (Exception e) {
            t.rollback();
        }
        for (int i = 0; i < listaMaterias.size(); i++) {

            map.put(listaMaterias.get(i).getNombre(), listaMaterias.get(i).getIdMateria().toString());
        }
        return map;
    }
    
    public void eliminarEspacio(){
        EspacioDao mDao = new EspacioDaoImp();
        Espacio e = espacioSeleccionado;
        mDao.eliminarEspacio(e);
        
    }

}
