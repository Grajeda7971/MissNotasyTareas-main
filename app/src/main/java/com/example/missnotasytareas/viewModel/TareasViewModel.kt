package com.example.missnotasytareas.viewModel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.missnotasytareas.controlador.NotasApp.Companion.dbT
import com.example.missnotasytareas.controlador.constantes
import com.example.missnotasytareas.modelo.Tareas
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TareasViewModel:ViewModel() {
    var id = MutableLiveData<Long>()
    var titulo = MutableLiveData<String>()
    var descripcion = MutableLiveData<String>()
    var fecha = MutableLiveData<String>()
    var hora = MutableLiveData<String>()
    var fotouri = MutableLiveData<String>()
    var videouri = MutableLiveData<String>()
    var operacion = constantes.OPERACION_INSERTAR
    var operacionExitosa = MutableLiveData<Boolean>()


    fun guardarTarea(){
        if (validarInformacion()){
            var tTarea = Tareas(0, titulo.value!!,descripcion.value!!,fecha.value!!,hora.value!!,videouri.value!!,fotouri.value!!)
            when(operacion){
                constantes.OPERACION_INSERTAR->{
                    viewModelScope.launch {
                        val result = withContext(Dispatchers.IO){
                            dbT.tareasDao().insert(
                                arrayListOf<Tareas>(
                                    tTarea
                                )
                            )
                        }
                        operacionExitosa.value = result.isNotEmpty()
                    }
                }
                constantes.OPERACION_EDITAR->{
                    tTarea.idTarea= id.value!!
                    viewModelScope.launch {
                        val result= withContext(Dispatchers.IO){
                            dbT.tareasDao().update(tTarea)
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
            var tarea = withContext(Dispatchers.IO){
                dbT.tareasDao().getById(id.value!!)
            }
            titulo.value = tarea.tituloTarea
            descripcion.value = tarea.descripcionTarea
            fecha.value = tarea.fechaTarea
            hora.value = tarea.horaTarea
            fotouri.value = tarea.uriFotoTarea
            videouri.value = tarea.uriVideoTarea
        }
    }

    private fun validarInformacion():Boolean{
        return !(titulo.value.isNullOrEmpty()||
                descripcion.value.isNullOrEmpty()||
                fecha.value.isNullOrEmpty()||
                hora.value.isNullOrEmpty()||
                fotouri.value.isNullOrEmpty()||
                videouri.value.isNullOrEmpty())
    }

    fun eliminarTarea() {
        var tTareas = Tareas(id.value!!, "","","","","", "")
        viewModelScope.launch {
            var result = withContext(Dispatchers.IO){
                dbT.tareasDao().delete(tTareas)
            }
            operacionExitosa.value = (result>0)
        }
    }

}