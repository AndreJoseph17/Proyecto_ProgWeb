/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proyecto.dao;

import com.proyecto.modelo.Normativa;
import java.util.List;

/**
 *
 * @author andre
 */
public interface NormativaDao {
    
    public List<Normativa> listarNormativas();
    public void agregarNormartiva(Normativa normativa);
    public void eliminarNormativa(Normativa normativa);
    public void actualizarNormativa(Normativa normativa);
}
