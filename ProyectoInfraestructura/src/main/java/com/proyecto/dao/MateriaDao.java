/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proyecto.dao;

import com.proyecto.modelo.Materia;
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
}
