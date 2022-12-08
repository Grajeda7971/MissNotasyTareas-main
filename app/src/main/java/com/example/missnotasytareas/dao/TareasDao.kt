package com.example.missnotasytareas.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.missnotasytareas.modelo.Notas
import com.example.missnotasytareas.modelo.Tareas

@androidx.room.Dao
interface TareasDao {
    @Query("SELECT * FROM Tareas")
    suspend fun getAll():List<Tareas>

    @Query("SELECT * FROM Tareas WHERE idTarea = :idTarea")
    suspend fun getById(idTarea:Long):Tareas

    @Query("SELECT * FROM Tareas WHERE tituloTarea LIKE '%'||:name|| '%' OR  descripcionTarea LIKE '%' ||:name")
    suspend fun getByName(name:String):List<Tareas>

    @Insert
    suspend fun insert(tareas: List<Tareas>):List<Long>

    @Update
    suspend fun update(tareas: Tareas):Int

    @Delete
    suspend fun delete(tareas: Tareas):Int

}
