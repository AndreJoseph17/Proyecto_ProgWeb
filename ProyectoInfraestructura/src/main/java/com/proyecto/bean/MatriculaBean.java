package com.proyecto.bean;

import com.proyecto.dao.EstudianteDao;
import com.proyecto.dao.MateriaDao;
import com.proyecto.dao.MatriculaDao;
import com.proyecto.imp.EstudianteDaoImp;
import com.proyecto.imp.MateriaDaoImp;
import com.proyecto.imp.MatriculaDaoImp;
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
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

@ManagedBean
@ViewScoped
public class MatriculaBean implements Serializable{
    private List<EspacioHasMateria> matHM;

    private int ID_ESTUDIANTE_SELECCIONADO=-1;

    public int getID_ESTUDIANTE_SELECCIONADO() {
        return ID_ESTUDIANTE_SELECCIONADO;
    }
    
    
    public void guardarIdSelectMatricula(){
        ID_ESTUDIANTE_SELECCIONADO = estudianteSeleccionado.getIdMatricula();
    }
    private String idEstudiante;
    
    private String periodo;
    private String observacion;
    
    private String idMateriaAgregar;
    
    private List<Matricula> matriculas;
    
    private List<Materia> materias;
    private Matricula estudianteSeleccionado;
    private MatriculaHasMateria materiaSeleccionada;
    
    private Map<String,String> estudiantesDisponibles;
    
    private Map<String,String> materiasDisponibles;

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }
    
    
    
    
    
    public void setMatHM(List<EspacioHasMateria> matHM) {
        this.matHM = matHM;
    }

    public List<EspacioHasMateria> getMatHM() {
        return listarMMM();
    }
    
    
    
    
    
    

    public Map<String, String> getMateriasDisponibles() {
        return extraerMaterias();
        //return null;
    }

    public void setMateriasDisponibles(Map<String, String> materiasDisponibles) {
        this.materiasDisponibles = materiasDisponibles;
    }
    

    public Map<String, String> getEstudiantesDisponibles() {
        return extraerEstudiantes();
    }

    public void setEstudiantesDisponibles(Map<String, String> estudiantesDisponibles) {
        this.estudiantesDisponibles = estudiantesDisponibles;
    }

    
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

    
    public List<Matricula> getMatriculas() {
        return listarMatriculas();
    }

    public List<Materia> getMaterias() {
        return listarMaterias();
    }

    public void setMaterias(List<Materia> materias) {
        this.materias = materias;
    }

    public MatriculaHasMateria getMateriaSeleccionada() {
        return materiaSeleccionada;
    }

    public void setMateriaSeleccionada(MatriculaHasMateria materiaSeleccionada) {
        this.materiaSeleccionada = materiaSeleccionada;
    }
    
    

    public Matricula getEstudianteSeleccionado() {
        return estudianteSeleccionado;
    }

    public void setEstudianteSeleccionado(Matricula estudianteSeleccionado) {
        this.estudianteSeleccionado = estudianteSeleccionado;
    }
    

    
    
    
    public void matricularEstrudiante(){
        MatriculaDao daoM = new MatriculaDaoImp();
        EstudianteDao daoE = new EstudianteDaoImp();
        
        Estudiante e = daoE.buscarEstudiante(Integer.parseInt(idEstudiante));
        Matricula m = new Matricula(e);
        m.setPeriodo(periodo);
        
        daoM.agregarMatricula(m);
    }
    public void agregarMateria(){
        MatriculaDao mDao = new MatriculaDaoImp();
        MateriaDao daoM = new MateriaDaoImp();
        int idMateria = Integer.parseInt(idMateriaAgregar);

    
        Materia materia = daoM.buscarMateria(idMateria);
        //materiaSeleccionada = mDao.buscarMateria(1);
        MatriculaHasMateriaId matriculaHasHorarioId = new MatriculaHasMateriaId(estudianteSeleccionado.getIdMatricula(), idMateria);

        MatriculaHasMateria m = new MatriculaHasMateria(matriculaHasHorarioId, materia, estudianteSeleccionado);
        m.setPeriodo(estudianteSeleccionado.getPeriodo());

        mDao.agregarMatriculaHasMateria(m);
    }

    private Map<String, String> extraerEstudiantes() {
        Map<String,String> map = new HashMap<>();
        
        List<Estudiante> listaEstudiantes=null;
        
        Session session=HibernateUtil.getSessionFactory().openSession();
        Transaction t=session.beginTransaction();
        String hql="FROM Estudiante";
        try{
            listaEstudiantes=session.createQuery(hql).list();
            t.commit();
            session.close();
        }
        catch(Exception e){
        t.rollback();
        }
        for (int i = 0 ; i<listaEstudiantes.size();i++){
           
            map.put(listaEstudiantes.get(i).getNombre()+" "+listaEstudiantes.get(i).getApellido(), listaEstudiantes.get(i).getIdEstudiante().toString());
        }
        return map;
    }

    private Map<String, String> extraerMaterias() {
        List<EspacioHasMateria> listaEhM = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction t = session.beginTransaction();
        String hql = "FROM EspacioHasMateria";
        
        try {
            listaEhM = session.createQuery(hql).list();
            for (EspacioHasMateria mhh : listaEhM) {
                Hibernate.initialize(mhh.getMateria());
            }
            //listaDocentes = session.createCriteria(MateriaHasHorario.class).list();
            t.commit();

        } catch (Exception e) {
            t.rollback();
        }
        session.close();
        
        
        
        
        Map<String,String> map = new HashMap<>();
        
        List<Materia> listaMaterias=null;
       
        for (int i = 0 ; i<listaEhM.size();i++){
            if (!map.containsKey(listaEhM.get(i).getMateria().getIdMateria())) {
                map.put(listaEhM.get(i).getMateria().getNombre() , listaEhM.get(i).getMateria().getIdMateria().toString());
            }
            
        }
        return map;
    }

    private List<EspacioHasMateria> listarMMM() {
        List<EspacioHasMateria> listaEhM = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction t = session.beginTransaction();
        String hql = "FROM EspacioHasMateria";
        
        try {
            listaEhM = session.createQuery(hql).list();
            for (EspacioHasMateria mhh : listaEhM) {
                Hibernate.initialize(mhh.getMateria());
            }
            //listaDocentes = session.createCriteria(MateriaHasHorario.class).list();
            t.commit();

        } catch (Exception e) {
            t.rollback();
        }
        session.close();
        return listaEhM;
    }

    private List<Matricula> listarMatriculas() {
        MatriculaDao mDao = new MatriculaDaoImp();
        return mDao.listarMatriculas();
    }

    private List<Materia> listarMaterias() {
        MatriculaDao mDao = new MatriculaDaoImp();
        
        List<MatriculaHasMateria> lista = mDao.listarMateriasMatriculadas();
        List<Materia> aux =new ArrayList<>();
        for (MatriculaHasMateria m : lista) {

               if (ID_ESTUDIANTE_SELECCIONADO == m.getMatricula().getIdMatricula()) {
                   aux.add(m.getMateria());
               }
         }
        return aux;
        
    }
    public void eliminarMatricula(){
        MatriculaDao mDao = new MatriculaDaoImp();
        mDao.eliminarMatricula(estudianteSeleccionado);
    }
}