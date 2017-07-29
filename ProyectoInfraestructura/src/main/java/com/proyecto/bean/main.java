/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proyecto.bean;

import com.proyecto.dao.MateriaDao;
import com.proyecto.imp.MateriaDaoImp;
import com.proyecto.modelo.*;

/**
 *
 * @author iJonna
 */
public class main {
    public static void main(String[] args) {
        MateriaDao mDao = new MateriaDaoImp();
        Horario horario = mDao.buscarHorario(1);
        Dia dia = mDao.buscarDia(1);
        
        Materia materiaSeleccionada = mDao.buscarMateria(1);
        
         MateriaHasHorarioId materiaHasHorarioId = new MateriaHasHorarioId(materiaSeleccionada.getIdMateria(),horario.getIdHorario(),dia.getIdDias());

                MateriaHasHorario m = new MateriaHasHorario(materiaHasHorarioId, dia, horario, materiaSeleccionada);
                
                mDao.agregarMateriaHasHorario(m);
                
                
    }
}
