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
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Espacio generated by hbm2java
 */
@Entity
@Table(name="espacio"
    ,catalog="proyecto"
)
public class Espacio  implements java.io.Serializable {


     private Integer idEspacio;
     private Normativa normativa;
     private Tipo tipo;
     private String nombreEspacio;
     private Integer capacidad;
     private String iluminacion;
     private String internet;
     private Integer nrescritorios;
     private Set<EquipoInformatico> equipoInformaticos = new HashSet<EquipoInformatico>(0);
     private Set<EspacioHasMateria> espacioHasMaterias = new HashSet<EspacioHasMateria>(0);

    public Espacio() {
    }

	
    public Espacio(Tipo tipo) {
        this.tipo = tipo;
    }
    public Espacio(Normativa normativa, Tipo tipo, String nombreEspacio, Integer capacidad, String iluminacion, String internet, Integer nrescritorios, Set<EquipoInformatico> equipoInformaticos, Set<EspacioHasMateria> espacioHasMaterias) {
       this.normativa = normativa;
       this.tipo = tipo;
       this.nombreEspacio = nombreEspacio;
       this.capacidad = capacidad;
       this.iluminacion = iluminacion;
       this.internet = internet;
       this.nrescritorios = nrescritorios;
       this.equipoInformaticos = equipoInformaticos;
       this.espacioHasMaterias = espacioHasMaterias;
    }
   
     @Id @GeneratedValue(strategy=IDENTITY)

    
    @Column(name="idEspacio", unique=true, nullable=false)
    public Integer getIdEspacio() {
        return this.idEspacio;
    }
    
    public void setIdEspacio(Integer idEspacio) {
        this.idEspacio = idEspacio;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="normativa_idNormativa")
    public Normativa getNormativa() {
        return this.normativa;
    }
    
    public void setNormativa(Normativa normativa) {
        this.normativa = normativa;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="tipo_idTipo", nullable=false)
    public Tipo getTipo() {
        return this.tipo;
    }
    
    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    
    @Column(name="nombre_espacio", length=45)
    public String getNombreEspacio() {
        return this.nombreEspacio;
    }
    
    public void setNombreEspacio(String nombreEspacio) {
        this.nombreEspacio = nombreEspacio;
    }

    
    @Column(name="capacidad")
    public Integer getCapacidad() {
        return this.capacidad;
    }
    
    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }

    
    @Column(name="iluminacion", length=5)
    public String getIluminacion() {
        return this.iluminacion;
    }
    
    public void setIluminacion(String iluminacion) {
        this.iluminacion = iluminacion;
    }

    
    @Column(name="internet", length=45)
    public String getInternet() {
        return this.internet;
    }
    
    public void setInternet(String internet) {
        this.internet = internet;
    }

    
    @Column(name="nrescritorios")
    public Integer getNrescritorios() {
        return this.nrescritorios;
    }
    
    public void setNrescritorios(Integer nrescritorios) {
        this.nrescritorios = nrescritorios;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="espacio")
    public Set<EquipoInformatico> getEquipoInformaticos() {
        return this.equipoInformaticos;
    }
    
    public void setEquipoInformaticos(Set<EquipoInformatico> equipoInformaticos) {
        this.equipoInformaticos = equipoInformaticos;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="espacio")
    public Set<EspacioHasMateria> getEspacioHasMaterias() {
        return this.espacioHasMaterias;
    }
    
    public void setEspacioHasMaterias(Set<EspacioHasMateria> espacioHasMaterias) {
        this.espacioHasMaterias = espacioHasMaterias;
    }




}


