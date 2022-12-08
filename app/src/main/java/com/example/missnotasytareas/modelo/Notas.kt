package com.example.missnotasytareas.modelo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Notas (
    @PrimaryKey(autoGenerate = true)
    var idNota: Long,
    var titulo: String,
    var descripcion : String
)