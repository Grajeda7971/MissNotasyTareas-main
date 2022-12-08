package com.example.missnotasytareas.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.missnotasytareas.controlador.NotasApp.Companion.dbN
import com.example.missnotasytareas.controlador.NotasApp.Companion.dbT
import com.example.missnotasytareas.modelo.Notas
import com.example.missnotasytareas.modelo.Tareas
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel: ViewModel() {
    val notasList = MutableLiveData<List<Notas>>()
    val tareasList = MutableLiveData<List<Tareas>>()
    val parametroBusqueda = MutableLiveData<String>()

    fun iniciarN(){
        viewModelScope.launch {
            notasList.value= withContext(Dispatchers.IO){
                dbN.notasDao().getAll()
            }
        }
    }

    fun iniciarT(){
        viewModelScope.launch {
            tareasList.value= withContext(Dispatchers.IO){
                dbT.tareasDao().getAll()
            }
        }
    }


    fun buscarNota() {
        viewModelScope.launch {
            notasList.value= withContext(Dispatchers.IO){
                dbN.notasDao().getByName(parametroBusqueda.value!!)
            }
        }
    }

    fun buscarTarea() {
        viewModelScope.launch {
            tareasList.value= withContext(Dispatchers.IO){
                dbT.tareasDao().getByName(parametroBusqueda.value!!)
            }
        }
    }
}

