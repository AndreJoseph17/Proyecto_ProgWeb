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
 * EspacioHasMateria generated by hbm2java
 */
@Entity
@Table(name="espacio_has_materia"
    ,catalog="proyecto"
)
public class EspacioHasMateria  implements java.io.Serializable {


     private EspacioHasMateriaId id;
     private Espacio espacio;
     private Materia materia;
     private String periodo;

    public EspacioHasMateria() {
    }

	
    public EspacioHasMateria(EspacioHasMateriaId id, Espacio espacio, Materia materia) {
        this.id = id;
        this.espacio = espacio;
        this.materia = materia;
    }
    public EspacioHasMateria(EspacioHasMateriaId id, Espacio espacio, Materia materia, String periodo) {
       this.id = id;
       this.espacio = espacio;
       this.materia = materia;
       this.periodo = periodo;
    }
   
     @EmbeddedId

    
    @AttributeOverrides( {
        @AttributeOverride(name="espacioIdEspacio", column=@Column(name="Espacio_idEspacio", nullable=false) ), 
        @AttributeOverride(name="materiaIdMateria", column=@Column(name="materia_idMateria", nullable=false) ) } )
    public EspacioHasMateriaId getId() {
        return this.id;
    }
    
    public void setId(EspacioHasMateriaId id) {
        this.id = id;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="Espacio_idEspacio", nullable=false, insertable=false, updatable=false)
    public Espacio getEspacio() {
        return this.espacio;
    }
    
    public void setEspacio(Espacio espacio) {
        this.espacio = espacio;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="materia_idMateria", nullable=false, insertable=false, updatable=false)
    public Materia getMateria() {
        return this.materia;
    }
    
    public void setMateria(Materia materia) {
        this.materia = materia;
    }

    
    @Column(name="periodo", length=45)
    public String getPeriodo() {
        return this.periodo;
    }
    
    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }




}


