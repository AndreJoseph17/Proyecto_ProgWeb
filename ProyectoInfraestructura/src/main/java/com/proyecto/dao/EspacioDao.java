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
public interface EspacioDao {
    
    public List<Espacio> listarEspacios();
    public void agregarEspacio(Espacio espacio);
    public void eliminarEspacio(Espacio espacio);
    public void actualizarEspacio(Espacio espacio);
    public Espacio buscarEspacio(int codigo);
    public void agregarEspacioHasMateria(EspacioHasMateria espacioHasMateria);
}
