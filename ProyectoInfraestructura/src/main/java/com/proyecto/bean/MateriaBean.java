package com.proyecto.bean;

import com.proyecto.dao.MateriaDao;
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
public class MateriaBean  implements Serializable{
    
    private int ID_MATERIA_SELECCIONADA=-1;
    public void guardarIdSelect(){
        ID_MATERIA_SELECCIONADA = materiaSeleccionada.getIdMateria();
    }
    private String nombre;
    private String descripcion;
    private int idMateriaSeleccionadada;
    
    private String docente;
    private String idDia;
    private String idHorario;
    private String idDocente;
    
    private Map<String,String> horariosDisponibles;
    private Map<String,String> diasDisponibles;
    private Map<String,String> docentesDisponibles;
    
    private List<Materia> materias;
    private List<MateriaHasHorario> horarios= new ArrayList<>();
    
    private List<MateriaHasDocente> docentes;
    
    private Materia materiaSeleccionada = new Materia();
    private MateriaHasHorario horarioSeleccionado;

    public Map<String, String> getDocentesDisponibles() {
         return extraerDocentes();
    }

    public void setDocentesDisponibles(Map<String, String> docentesDisponibles) {
        this.docentesDisponibles = docentesDisponibles;
    }

    
    public Map<String, String> getDiasDisponibles() {
        return extraerDias();
    }

    public Map<String, String> getHorariosDisponibles() {
        return extraerHorarios();
    }

    public void setDiasDisponibles(Map<String, String> diasDisponibles) {
        this.diasDisponibles = diasDisponibles;
    }

    public void setHorariosDisponibles(Map<String, String> horariosDisponibles) {
        this.horariosDisponibles = horariosDisponibles;
    }

    public String getIdDocente() {
        return idDocente;
    }

    public void setIdDocente(String idDocente) {
        this.idDocente = idDocente;
    }
    
    public String getDocente() {
        return docente;
    }

    public void setDocente(String docente) {
        this.docente = docente;
    }

    public int getIdMateriaSeleccionadada() {
        return idMateriaSeleccionadada;
    }

    public void setIdMateriaSeleccionadada(int idMateriaSeleccionadada) {
        this.idMateriaSeleccionadada = idMateriaSeleccionadada;
    }
    
    
    public String getIdDia() {
        return idDia;
    }

    public String getIdHorario() {
        return idHorario;
    }

    public void setIdDia(String idDia) {
        this.idDia = idDia;
    }

    public void setIdHorario(String idHorario) {
        this.idHorario = idHorario;
    }

    public List<MateriaHasDocente> getDocentes() {
        MateriaDao dao= new MateriaDaoImp();
        
        List<MateriaHasDocente> lista = dao.buscarDocenteHasMateria();
        List<MateriaHasDocente> aux = new ArrayList<>();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getMateria().getIdMateria()== ID_MATERIA_SELECCIONADA) {
                aux.add(lista.get(i));
            }
        }
       return aux;
       
        
    }
    public List<MateriaHasHorario> getHorarios() {
        MateriaDao dao= new MateriaDaoImp();
        
        List<MateriaHasHorario> lista = dao.buscarHorarioHasMateria();
        List<MateriaHasHorario> aux = new ArrayList<>();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getMateria().getIdMateria() == ID_MATERIA_SELECCIONADA) {
                aux.add(lista.get(i));
            }
        }
       return aux;

    
    }


    public void setDocentes(List<MateriaHasDocente> docentes) {
        this.docentes = docentes;
    }
    
    
    


    public String getDescripcion() {
        return descripcion;
    }

   

    public List<Materia> getMaterias() {
        return listarMaterias();
    }

    
    public void setHorarios(List<MateriaHasHorario> horarios) {
        this.horarios = horarios;
    }
    
    

    public String getNombre() {
        return nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

   
    public void setMaterias(List<Materia> materias) {
        this.materias = materias;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    

    public Materia getMateriaSeleccionada() {
        
        return materiaSeleccionada;
    }

    public void setMateriaSeleccionada(Materia materiaSeleccionada) {
        this.materiaSeleccionada = materiaSeleccionada;
    }

    public MateriaHasHorario getHorarioSeleccionado() {
        return horarioSeleccionado;
    }

    public void setHorarioSeleccionado(MateriaHasHorario horarioSeleccionado) {
        this.horarioSeleccionado = horarioSeleccionado;
    }
  
    
    
    
    
    //////////////////////////////////
    ///////////////////////////////
    //      METODOS             //
    ////////////////////////////
    
   

    public void registrarMateria(ActionEvent event) {  
       
        if(nombre != null && descripcion != null) {
            Materia m = new Materia();
            m.setNombre(nombre);
            m.setDescripcion(descripcion);
            
            MateriaDao mDao = new MateriaDaoImp();
            mDao.agregarMaterias(m);
        }
        else{
           
        }
      
    }   
   
    public void registrarHorario(ActionEvent event) {
       
        MateriaDao mDao = new MateriaDaoImp();
        int idD = Integer.parseInt(idDia);
        int idH = Integer.parseInt(idHorario);

       
        Horario horario = mDao.buscarHorario(idH);
        Dia dia = mDao.buscarDia(idD);
        
        //materiaSeleccionada = mDao.buscarMateria(1);
        MateriaHasHorarioId materiaHasHorarioId = new MateriaHasHorarioId(materiaSeleccionada.getIdMateria(),horario.getIdHorario(),dia.getIdDias());

        MateriaHasHorario m = new MateriaHasHorario(materiaHasHorarioId, dia, horario, materiaSeleccionada);

        mDao.agregarMateriaHasHorario(m);
               // materiaController.create(m);

    }
    public void registrarDocente(ActionEvent event){
        MateriaDao mDao = new MateriaDaoImp();
        int idD = Integer.parseInt(idDocente);
       

       
        Docente docente = mDao.buscarDocente(idD);
       
        //materiaSeleccionada = mDao.buscarMateria(1);
        MateriaHasDocenteId materiaHasDocenteId = new MateriaHasDocenteId(materiaSeleccionada.getIdMateria(),docente.getIdDocente());

        MateriaHasDocente m = new MateriaHasDocente(materiaHasDocenteId, docente, materiaSeleccionada);

        mDao.agregarMateriaHasDocente(m);
    }
    
  
    
   public List<Materia> listarMaterias(){
        List<Materia> listaMaterias=null;
        Session session=HibernateUtil.getSessionFactory().openSession();
        Transaction t=session.beginTransaction();
        String hql="FROM Materia";
        try{
            listaMaterias=session.createQuery(hql).list();
            t.commit();
            session.close();
        }
        catch(Exception e){
        t.rollback();
        }
        return listaMaterias;
    }
   public  List<MateriaHasHorario> process(){
       List<MateriaHasHorario> listaMaterias=null;
        Session session=HibernateUtil.getSessionFactory().openSession();
        Transaction t=session.beginTransaction();
        String hql="FROM MateriaHasHorario";
        try{
            listaMaterias=session.createQuery(hql).list();
            t.commit();
            session.close();
        }
        catch(Exception e){
        t.rollback();
        }
        return listaMaterias;
   }

    

    private Map<String, String> extraerDias() {
        Map<String,String> map = new HashMap<>();
        
        List<Dia> listaDias=null;
        
        Session session=HibernateUtil.getSessionFactory().openSession();
        Transaction t=session.beginTransaction();
        String hql="FROM Dia";
        try{
            listaDias=session.createQuery(hql).list();
            t.commit();
            session.close();
        }
        catch(Exception e){
        t.rollback();
        }
        List<Dia> l = null;
        
        for (int i = 0 ; i<listaDias.size();i++){
           
            map.put(listaDias.get(i).getDiaSemana(), listaDias.get(i).getIdDias().toString());
        }
        return map;
    }

    private Map<String, String> extraerHorarios() {
        Map<String,String> map = new HashMap<>();
        
        List<Horario> listaHorarios=null;
        
        Session session=HibernateUtil.getSessionFactory().openSession();
        Transaction t=session.beginTransaction();
        String hql="FROM Horario";
        try{
            listaHorarios=session.createQuery(hql).list();
            t.commit();
            session.close();
        }
        catch(Exception e){
        t.rollback();
        }
        
        for (int i = 0 ; i<listaHorarios.size();i++){
            map.put(listaHorarios.get(i).getHoraInicio()+" a "+listaHorarios.get(i).getHoraFinal()
                    , listaHorarios.get(i).getIdHorario().toString());
        }
        return map;
    }

    private List<MateriaHasDocente> listarDocentes() {
        List<MateriaHasDocente> listaHorarios=null;
        Session session=HibernateUtil.getSessionFactory().openSession();
        Transaction t=session.beginTransaction();
        String hql="FROM MateriasHasDocente";
        try{
            listaHorarios=session.createQuery(hql).list();
            t.commit();
            session.close();
        }
        catch(Exception e){
            t.rollback();
        }
        return listaHorarios;
    }

    private Map<String, String> extraerDocentes() {
        Map<String,String> map = new HashMap<>();
        
        List<Docente> listaDocentes=null;
        
        Session session=HibernateUtil.getSessionFactory().openSession();
        Transaction t=session.beginTransaction();
        String hql="FROM Docente";
        try{
            listaDocentes=session.createQuery(hql).list();
            t.commit();
            session.close();
        }
        catch(Exception e){
        t.rollback();
        }
        List<Dia> l = null;
        
        for (int i = 0 ; i<listaDocentes.size();i++){
           
            map.put(listaDocentes.get(i).getNombre()+" "+listaDocentes.get(i).getApellido(), listaDocentes.get(i).getIdDocente().toString());
        }
        return map;
    }
}