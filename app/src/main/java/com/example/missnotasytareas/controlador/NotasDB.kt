package com.example.missnotasytareas.controlador

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.missnotasytareas.dao.NotasDao
import com.example.missnotasytareas.modelo.Notas

@Database(
    entities = [Notas::class],
    version = 1
)
abstract class NotasDB: RoomDatabase() {
    abstract fun notasDao(): NotasDao
}
