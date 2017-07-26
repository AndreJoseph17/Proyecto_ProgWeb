/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proyecto.dao;

import com.proyecto.modelo.Estudiante;
import java.util.List;

/**
 *
 * @author andre
 */
public interface EstudianteDao {
 
    public List<Estudiante> listarEstudiante();
    public void agregarUsuario(Estudiante estudiante);
    public void actualizarUsuario(Estudiante estudiante);
    public void eliminarEstudiante(Estudiante estudiante);
}
