package com.proyecto.modelo;
// Generated 26/07/2017 14:26:42 by Hibernate Tools 4.3.1



/**
 * MateriaHasDocenteId generated by hbm2java
 */
public class MateriaHasDocenteId  implements java.io.Serializable {


     private int materiaIdMateria;
     private int docenteIdDocente;

    public MateriaHasDocenteId() {
    }

    public MateriaHasDocenteId(int materiaIdMateria, int docenteIdDocente) {
       this.materiaIdMateria = materiaIdMateria;
       this.docenteIdDocente = docenteIdDocente;
    }
   
    public int getMateriaIdMateria() {
        return this.materiaIdMateria;
    }
    
    public void setMateriaIdMateria(int materiaIdMateria) {
        this.materiaIdMateria = materiaIdMateria;
    }
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

