package com.example.missnotasytareas.modelo

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Tareas (
    @PrimaryKey(autoGenerate = true)
    var idTarea: Long,
    var tituloTarea: String,
    var descripcionTarea : String,
    var fechaTarea : String,
    var horaTarea : String,
    //var direccion_imagen : String,
    var uriFotoTarea : String,
    var uriVideoTarea : String
    //var direccion_audio : String
)