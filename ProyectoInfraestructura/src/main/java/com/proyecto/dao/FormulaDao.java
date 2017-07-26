/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proyecto.dao;

import com.proyecto.modelo.Formula;
import java.util.List;

/**
 *
 * @author andre
 */
public interface FormulaDao {
    
    public List<Formula> listarFormulas();
    public void agregarFormulas(Formula formula);
    public void eliminarFormula(Formula formula);
    public void actualizarFormula(Formula formula);
}
