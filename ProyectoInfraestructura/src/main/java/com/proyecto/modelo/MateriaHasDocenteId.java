package com.proyecto.modelo;
// Generated 27/07/2017 15:17:03 by Hibernate Tools 4.3.1


import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * MateriaHasDocenteId generated by hbm2java
 */
@Embeddable
public class MateriaHasDocenteId  implements java.io.Serializable {


     private int materiaIdMateria;
     private int docenteIdDocente;

    public MateriaHasDocenteId() {
    }

    public MateriaHasDocenteId(int materiaIdMateria, int docenteIdDocente) {
       this.materiaIdMateria = materiaIdMateria;
       this.docenteIdDocente = docenteIdDocente;
    }
   


    @Column(name="materia_idMateria", nullable=false)
    public int getMateriaIdMateria() {
        return this.materiaIdMateria;
    }
    
    public void setMateriaIdMateria(int materiaIdMateria) {
        this.materiaIdMateria = materiaIdMateria;
    }


    @Column(name="docente_idDocente", nullable=false)
    public int getDocenteIdDocente() {
        return this.docenteIdDocente;
    }
    
    public void setDocenteIdDocente(int docenteIdDocente) {
        this.docenteIdDocente = docenteIdDocente;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof MateriaHasDocenteId) ) return false;
		 MateriaHasDocenteId castOther = ( MateriaHasDocenteId ) other; 
         
		 return (this.getMateriaIdMateria()==castOther.getMateriaIdMateria())
 && (this.getDocenteIdDocente()==castOther.getDocenteIdDocente());
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + this.getMateriaIdMateria();
         result = 37 * result + this.getDocenteIdDocente();
         return result;
   }   


}


