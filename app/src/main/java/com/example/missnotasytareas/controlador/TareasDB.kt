package com.example.missnotasytareas.controlador

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.missnotasytareas.dao.NotasDao
import com.example.missnotasytareas.dao.TareasDao
import com.example.missnotasytareas.modelo.Notas
import com.example.missnotasytareas.modelo.Tareas

@Database(
    entities = [Tareas::class],
    version = 1
)
abstract class TareasDB: RoomDatabase() {
    abstract fun tareasDao(): TareasDao
}
