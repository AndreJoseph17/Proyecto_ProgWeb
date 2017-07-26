/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proyecto.dao;

import com.proyecto.modelo.EquipoInformatico;
import java.util.List;

/**
 *
 * @author andre
 */
public interface EquipoInformaticoDao {
    
    public List<EquipoInformatico> listarEquiposInformaticos();
    public void agregarEquipo(EquipoInformatico equipo);
    public void actualizarEquipo(EquipoInformatico equipo);
    public void eliminarEquipo(EquipoInformatico equipo);
}
