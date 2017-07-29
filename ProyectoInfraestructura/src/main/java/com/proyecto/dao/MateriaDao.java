/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proyecto.dao;

import com.proyecto.modelo.*;
import java.util.List;

/**
 *
 * @author andre
 */
public interface MateriaDao {
    
    List<Materia> listarMaterias();
    public void agregarMaterias(Materia materia);
    public void eliminarMateria(Materia materia);
    public void actualizarMateria(Materia materia);
    
    public Materia buscarMateria (int codigo);
    public Horario buscarHorario (int codigo);
    public Docente buscarDocente (int codigo);
    
    public Dia buscarDia (int codigo);
    public List<MateriaHasHorario> buscarHorarioHasMateria ();
    
    public void agregarMateriaHasHorario(MateriaHasHorario materiaHasHorario);
    
    
    
}
