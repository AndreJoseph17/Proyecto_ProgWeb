package com.proyecto.modelo;
// Generated 26/07/2017 14:26:42 by Hibernate Tools 4.3.1


import java.util.HashSet;
import java.util.Set;

/**
 * Formula generated by hbm2java
 */
public class Formula  implements java.io.Serializable {


     private Integer idFormula;
     private String nombre;
     private String formula;
     private String descripcion;
     private Set<Normativa> normativas = new HashSet<Normativa>(0);

    public Formula() {
    }

    public Formula(String nombre, String formula, String descripcion, Set<Normativa> normativas) {
       this.nombre = nombre;
       this.formula = formula;
       this.descripcion = descripcion;
       this.normativas = normativas;
    }
   
    public Integer getIdFormula() {
        return this.idFormula;
    }
    
    public void setIdFormula(Integer idFormula) {
        this.idFormula = idFormula;
    }
    public String getNombre() {
        return this.nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getFormula() {
        return this.formula;
    }
    
    public void setFormula(String formula) {
        this.formula = formula;
    }
    public String getDescripcion() {
        return this.descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public Set<Normativa> getNormativas() {
        return this.normativas;
    }
    
    public void setNormativas(Set<Normativa> normativas) {
        this.normativas = normativas;
    }




}

