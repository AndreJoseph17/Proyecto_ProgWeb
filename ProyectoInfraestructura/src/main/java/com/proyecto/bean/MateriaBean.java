package com.proyecto.bean;

import com.proyecto.dao.MateriaDao;
import com.proyecto.imp.MateriaDaoImp;
import com.proyecto.modelo.*;
import com.proyecto.util.HibernateUtil;
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
public class MateriaBean  {
     EntityManagerFactory entityMf ;
        EntityManager entityM;
        MateriaHasHorarioJpaController materiaController;
        
        private Materia auxMat;

    public void setAuxMat(Materia auxMat) {
        this.auxMat = auxMat;
    }

    public Materia getAuxMat() {
        return auxMat;
    }
        
        
    private String nombre;
    private String descripcion;
    private int idMateriaSeleccionadada;
    
    private String docente;
    private String idDia;
    private String idHorario;
    
    private Map<String,String> horariosDisponibles;
    private Map<String,String> diasDisponibles;
    
    private List<Materia> materias;
    private List<MateriaHasHorario> horarios;
    
    private List<MateriaHasDocente> docentes;
    
    private Materia materiaSeleccionada = new Materia();
    private MateriaHasHorario horarioSeleccionado;

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
        return listarDocentes();
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

    public List<MateriaHasHorario> getHorarios() {
        return listarHorarios();
     //return null;
    
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
    
    public void onRowSelect(SelectEvent event) {
        FacesMessage msg = new FacesMessage("Materia Seleccionada", ((Materia) event.getObject()).getNombre());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
 
    public void onRowUnselect(UnselectEvent event) {
        FacesMessage msg = new FacesMessage("Materia no seleccionada", ((Materia) event.getObject()).getNombre());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    
    
    
    //////////////////////////////////
    ///////////////////////////////
    //      METODOS             //
    ////////////////////////////
    
    public void crearInstancia(){
        entityMf = Persistence.createEntityManagerFactory("persistencia_infraestructura");
        materiaController = new MateriaHasHorarioJpaController(entityMf);
        entityM = materiaController.getEntityManager();
        
    }

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
           // message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Loggin Error", "Invalid credentials");
        
           
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
    
    public void otroMetodo(ActionEvent event){
        try {
            System.out.println("alskfjalskdfjadslfkjdsalf");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    
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
   public void process(ActionEvent event){
       RequestContext.getCurrentInstance().execute("alert('jfjfjfjf')");
       
   }

    private List<MateriaHasHorario> listarHorarios() {
        MateriaDao dao= new MateriaDaoImp();
        Materia m= dao.buscarMateria(materiaSeleccionada.getIdMateria());
        // = dao.buscarMateria(materiaSeleccionada.getIdMateria());
        /*
        List<MateriaHasHorario> listaHorarios=null;
        Session session=HibernateUtil.getSessionFactory().openSession();
        Transaction t=session.beginTransaction();
        String hql="FROM MateriaHasHorario";
        try{
            listaHorarios=session.createQuery(hql).list();
            t.commit();
            session.close();
        }
        catch(Exception e){
            t.rollback();
        }
        
        List<MateriaHasHorario> lista=null;
        for (int i = 0 ; i<listaHorarios.size();i++){
           if(listaHorarios.get(i).getMateria().getIdMateria().equals(materiaSeleccionada.getIdMateria()))
                lista.add(listaHorarios.get(i));
        }
        */
        
        List<MateriaHasHorario> listaHorarios= new ArrayList(m.getMateriaHasHorarios());
        
        /* MateriaDao mDao = new MateriaDaoImp();
        Horario horario = mDao.buscarHorario(1);
        Dia dia = mDao.buscarDia(1);
         Materia m = mDao.buscarMateria(1);
        
         MateriaHasHorarioId materiaHasHorarioId = new MateriaHasHorarioId(m.getIdMateria(),horario.getIdHorario(),dia.getIdDias());

                MateriaHasHorario mm = new MateriaHasHorario(materiaHasHorarioId, dia, horario, materiaSeleccionada);
        
        
        listaHorarios.add(mm);*/
        // List l = new ArrayList<MateriaHasHorario>(m.getMateriaHasHorarios());
        return listaHorarios;
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
}