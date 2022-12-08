package com.example.missnotasytareas.controlador

import android.app.Application
import androidx.room.Room

class NotasApp :Application() {
    companion object{
        lateinit var dbN: NotasDB
        lateinit var dbT: TareasDB
    }

    override fun onCreate() {
        super.onCreate()
        dbN = Room.databaseBuilder(
            this,
            NotasDB::class.java,
            "Notas"
        ).build()

        dbT = Room.databaseBuilder(
            this,
            TareasDB::class.java,
            "Tareas"
        ).build()



    }
}