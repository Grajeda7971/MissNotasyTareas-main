package com.example.missnotasytareas.viewModel

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Data
import androidx.work.WorkManager
import com.example.missnotasytareas.controlador.NotasApp.Companion.dbN
import com.example.missnotasytareas.controlador.constantes
import com.example.missnotasytareas.modelo.Notas
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class NotasViewModel:ViewModel() {
    var id = MutableLiveData<Long>()
    var titulo = MutableLiveData<String>()
    var descripcion = MutableLiveData<String>()
    var operacion = constantes.OPERACION_INSERTAR
    var operacionExitosa = MutableLiveData<Boolean>()


    fun guardarNota(){
        if (validarInformacion()){
            var mNota = Notas(0, titulo.value!!,descripcion.value!!)
            when(operacion){
                constantes.OPERACION_INSERTAR->{
                    viewModelScope.launch {
                        val result = withContext(Dispatchers.IO){
                            dbN.notasDao().insert(
                                arrayListOf<Notas>(
                                    mNota

                                )
                            )
                        }
                        operacionExitosa.value = result.isNotEmpty()
                    }
                }
                constantes.OPERACION_EDITAR->{
                    mNota.idNota= id.value!!
                    viewModelScope.launch {
                        val result= withContext(Dispatchers.IO){
                            dbN.notasDao().update(mNota)
                        }
                        operacionExitosa.value = (result>0)
                    }
                }
            }
        }else{
            operacionExitosa.value = false
        }
    }

    fun cargarDatos() {
        viewModelScope.launch {
            var nota = withContext(Dispatchers.IO){
                dbN.notasDao().getById(id.value!!)
            }
            titulo.value = nota.titulo
            descripcion.value = nota.descripcion
        }
    }

    private fun validarInformacion():Boolean{
        return !(titulo.value.isNullOrEmpty()||
                descripcion.value.isNullOrEmpty())
    }

    fun eliminarNota() {
        var mNota = Notas(id.value!!, "","")
        viewModelScope.launch {
            var result = withContext(Dispatchers.IO){
                dbN.notasDao().delete(mNota)
            }
            operacionExitosa.value = (result>0)
        }
    }



}