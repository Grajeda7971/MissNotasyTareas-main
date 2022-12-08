package com.example.missnotasytareas.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.missnotasytareas.modelo.Notas

@androidx.room.Dao
interface NotasDao {
    @Query("SELECT * FROM Notas")
    suspend fun getAll():List<Notas>

    @Query("SELECT * FROM Notas WHERE idNota = :id")
    suspend fun getById(id:Long):Notas

    @Query("SELECT * FROM Notas WHERE titulo LIKE '%'||:name|| '%' OR  descripcion LIKE '%' ||:name")
    suspend fun getByName(name:String):List<Notas>

    @Insert
    suspend fun insert(notas: List<Notas>):List<Long>

    @Update
    suspend fun update(notas: Notas):Int

    @Delete
    suspend fun delete(notas: Notas):Int
}
