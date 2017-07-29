package com.proyecto.bean;

import com.proyecto.dao.MateriaDao;
import com.proyecto.imp.MateriaDaoImp;
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
public class MateriaBean {
    private String nombre;
    private String descripcion;
    
    private String docente;
    private String idDia;
    private String idHorario;
    
    private Map<String,String> horariosDisponibles;
    private Map<String,String> diasDisponibles;
    
    private List<Materia> materias;
    private List<MateriaHasHorario> horarios;
    
    private List<MateriaHasDocente> docentes;
    
    private Materia materiaSeleccionada;
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
    public void login(ActionEvent event) {
        RequestContext context = RequestContext.getCurrentInstance();
        FacesMessage message = null;
        
        Integer idD = Integer.parseInt(idDia);
        Integer idH = Integer.parseInt(idHorario);
       idD=1;
       idH=1;
        boolean loggedIn = false;
        
        if(!FacesContext.getCurrentInstance().isPostback()) {
            RequestContext.getCurrentInstance().execute("alert('This onload script is added from backing bean.')");
        }
         
        if(idD!= 0 && idH != 0 ) {
            loggedIn = true;
            Dia dia = new Dia();
            dia.setIdDias(1);
            dia.setDiaSemana("Lunes");
            Horario horario = new Horario();
            horario.setIdHorario(1);
            horario.setHoraInicio("07:00");
            horario.setHoraFinal("09:11");
            
            
            MateriaHasHorarioId materiaHasHorarioId = new MateriaHasHorarioId(materiaSeleccionada.getIdMateria(),horario.getIdHorario(),dia.getIdDias());
            
            MateriaHasHorario m = new MateriaHasHorario(materiaHasHorarioId, dia, horario, materiaSeleccionada);
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();

            session.save(m);
            session.getTransaction().commit();
            session.close();
            
            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Welcome", idDia+" "+idHorario);
        } else {
            loggedIn = false;
            message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Loggin Error", "Invalid credentials");
        }
         
        FacesContext.getCurrentInstance().addMessage(null, message);
        context.addCallbackParam("loggedIn", loggedIn);
    } 
    
    public void registrarHorario(ActionEvent event) {
        
        MateriaDao mDao = new MateriaDaoImp();
        
        
        Integer idD = Integer.parseInt(idDia);
        Integer idH = Integer.parseInt(idHorario);
        
        Horario h = new Horario();
        
        if(idD!= 0 && idH != 0 ) {
            
        }
        
        /*Integer idD = Integer.parseInt(idDia);
        Integer idH = Integer.parseInt(idHorario);
        
        if(idD!= 0 && idH != 0 ) {
            
            
            Dia dia = new Dia();
            dia.setIdDias(idD);
            dia.setDiaSemana("Viernes");
            Horario horario = new Horario();
            horario.setIdHorario(idH);
            horario.setHoraInicio("07:00");
            horario.setHoraFinal("09:11");
            
            
            
            MateriaHasHorarioId materiaHasHorarioId = new MateriaHasHorarioId(materiaSeleccionada.getIdMateria(),horario.getIdHorario(),dia.getIdDias());
            
            MateriaHasHorario m = new MateriaHasHorario(materiaHasHorarioId, dia, horario, materiaSeleccionada);
            
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();

            session.save(m);
            session.getTransaction().commit();
            session.close();
            
          
           // message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Agregado", nombre);
        } else {
            
            
           // message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Loggin Error", "Invalid credentials");
        }
         
        //FacesContext.getCurrentInstance().addMessage(null, message);
       // context.addCallbackParam("loggedIn", loggedIn);
       */
        
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
        List<MateriaHasHorario> listaHorarios=null;
        Session session=HibernateUtil.getSessionFactory().openSession();
        Transaction t=session.beginTransaction();
        String hql="FROM materia_has_horario";
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