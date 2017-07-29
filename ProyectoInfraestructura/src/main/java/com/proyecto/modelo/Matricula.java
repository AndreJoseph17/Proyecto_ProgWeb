package com.proyecto.modelo;
// Generated 27/07/2017 15:17:03 by Hibernate Tools 4.3.1


import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedStoredProcedureQueries;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.OneToMany;
import javax.persistence.Parameter;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;

/**
 * Matricula generated by hbm2java
 */
@Entity
@Table(name="matricula"
    ,catalog="proyecto"
)

@NamedStoredProcedureQuery(
        name = "matricular",
        procedureName = "matricula",
        parameters = {   @StoredProcedureParameter(mode = ParameterMode.IN, type = String.class, name = "codigo_matricula"),
                            @StoredProcedureParameter(mode = ParameterMode.IN, type = String.class, name = "codigo_materia"),
                            @StoredProcedureParameter(mode = ParameterMode.OUT, type = Integer.class, name = "valor_retorno")         
        })

public class Matricula  implements java.io.Serializable {


     private Integer idMatricula;
     private Estudiante estudiante;
     private String periodo;
     private String observacion;
     private Set<MatriculaHasMateria> matriculaHasMaterias = new HashSet<MatriculaHasMateria>(0);

    public Matricula() {
    }

	
    public Matricula(Estudiante estudiante) {
        this.estudiante = estudiante;
    }
    public Matricula(Estudiante estudiante, String periodo, String observacion, Set<MatriculaHasMateria> matriculaHasMaterias) {
       this.estudiante = estudiante;
       this.periodo = periodo;
       this.observacion = observacion;
       this.matriculaHasMaterias = matriculaHasMaterias;
    }
   
     @Id @GeneratedValue(strategy=IDENTITY)

    
    @Column(name="idMatricula", unique=true, nullable=false)
    public Integer getIdMatricula() {
        return this.idMatricula;
    }
    
    public void setIdMatricula(Integer idMatricula) {
        this.idMatricula = idMatricula;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="estudiante_idEstudiante", nullable=false)
    public Estudiante getEstudiante() {
        return this.estudiante;
    }
    
    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    
    @Column(name="periodo", length=45)
    public String getPeriodo() {
        return this.periodo;
    }
    
    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    
    @Column(name="observacion", length=45)
    public String getObservacion() {
        return this.observacion;
    }
    
    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="matricula")
    public Set<MatriculaHasMateria> getMatriculaHasMaterias() {
        return this.matriculaHasMaterias;
    }
    
    public void setMatriculaHasMaterias(Set<MatriculaHasMateria> matriculaHasMaterias) {
        this.matriculaHasMaterias = matriculaHasMaterias;
    }




}


