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
public interface MatriculaDao {
    
    public List<Matricula> listarMatriculas();
    public List<MatriculaHasMateria> listarMateriasMatriculadas();
    public void agregarMatricula(Matricula matricula);
    public void actualizarMatricula(Matricula matricula);
    public void eliminarMatricula(Matricula matricula);
    
    public void agregarMatriculaHasMateria(MatriculaHasMateria matriculaHasMateria);
}
