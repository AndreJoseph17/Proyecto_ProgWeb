package com.proyecto.modelo;
// Generated 27/07/2017 15:17:03 by Hibernate Tools 4.3.1


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * EquipoInformatico generated by hbm2java
 */
@Entity
@Table(name="equipo_informatico"
    ,catalog="proyecto"
)
public class EquipoInformatico  implements java.io.Serializable {


     private Integer idEquipoInformatico;
     private Espacio espacio;
     private String nombre;
     private String descripcion;

    public EquipoInformatico() {
    }

    public EquipoInformatico(Espacio espacio, String nombre, String descripcion) {
       this.espacio = espacio;
       this.nombre = nombre;
       this.descripcion = descripcion;
    }
   
     @Id @GeneratedValue(strategy=IDENTITY)

    
    @Column(name="idEquipo_Informatico", unique=true, nullable=false)
    public Integer getIdEquipoInformatico() {
        return this.idEquipoInformatico;
    }
    
    public void setIdEquipoInformatico(Integer idEquipoInformatico) {
        this.idEquipoInformatico = idEquipoInformatico;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="Espacio_idEspacio")
    public Espacio getEspacio() {
        return this.espacio;
    }
    
    public void setEspacio(Espacio espacio) {
        this.espacio = espacio;
    }

    
    @Column(name="nombre", length=45)
    public String getNombre() {
        return this.nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    
    @Column(name="descripcion", length=45)
    public String getDescripcion() {
        return this.descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }




}


