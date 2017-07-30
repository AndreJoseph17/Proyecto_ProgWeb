/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proyecto.dao;

import com.proyecto.modelo.Tipo;
import java.util.List;

/**
 *
 * @author LuisEduardo
 */
public interface TipoDao {
    
    public List<Tipo> listarTipo();      
    public void newTipo(Tipo tipo);        
    public void updateTipo(Tipo tipo);
    public void deleteTipo(Tipo tipo);
    public Tipo buscarTipo(int codigo);
    
}
