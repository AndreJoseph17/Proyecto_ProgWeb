package com.proyecto.modelo;
// Generated 27/07/2017 15:17:03 by Hibernate Tools 4.3.1


import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * MatriculaHasMateria generated by hbm2java
 */
@Entity
@Table(name="matricula_has_materia"
    ,catalog="proyecto"
)
public class MatriculaHasMateria  implements java.io.Serializable {


     private MatriculaHasMateriaId id;
     private Materia materia;
     private Matricula matricula;
     private String periodo;

    public MatriculaHasMateria() {
    }

	
    public MatriculaHasMateria(MatriculaHasMateriaId id, Materia materia, Matricula matricula) {
        this.id = id;
        this.materia = materia;
        this.matricula = matricula;
    }
    public MatriculaHasMateria(MatriculaHasMateriaId id, Materia materia, Matricula matricula, String periodo) {
       this.id = id;
       this.materia = materia;
       this.matricula = matricula;
       this.periodo = periodo;
    }
   
     @EmbeddedId

    
    @AttributeOverrides( {
        @AttributeOverride(name="matriculaIdMatricula", column=@Column(name="matricula_idMatricula", nullable=false) ), 
        @AttributeOverride(name="materiaIdMateria", column=@Column(name="materia_idMateria", nullable=false) ) } )
    public MatriculaHasMateriaId getId() {
        return this.id;
    }
    
    public void setId(MatriculaHasMateriaId id) {
        this.id = id;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="materia_idMateria", nullable=false, insertable=false, updatable=false)
    public Materia getMateria() {
        return this.materia;
    }
    
    public void setMateria(Materia materia) {
        this.materia = materia;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="matricula_idMatricula", nullable=false, insertable=false, updatable=false)
    public Matricula getMatricula() {
        return this.matricula;
    }
    
    public void setMatricula(Matricula matricula) {
        this.matricula = matricula;
    }

    
    @Column(name="periodo", length=45)
    public String getPeriodo() {
        return this.periodo;
    }
    
    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }




}


