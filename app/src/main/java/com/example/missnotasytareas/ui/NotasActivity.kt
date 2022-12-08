package com.example.missnotasytareas.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.example.missnotasytareas.Dialog.borrarDialogo
import com.example.missnotasytareas.MainActivity
import com.example.missnotasytareas.controlador.constantes
import com.example.missnotasytareas.databinding.ActivityNotasBinding
import com.example.missnotasytareas.viewModel.NotasViewModel

class NotasActivity : AppCompatActivity(),borrarDialogo.BorrarListener{
    lateinit var binding: ActivityNotasBinding
    lateinit var viewModel: NotasViewModel
    lateinit var dialogo: borrarDialogo
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotasBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dialogo = borrarDialogo(this, )
        viewModel = ViewModelProvider(this).get()
        viewModel.operacion = intent.getStringExtra(constantes.OPERACION_KEY)!!
        binding.modelo = viewModel
        binding.lifecycleOwner = this

        viewModel.operacionExitosa.observe(this, Observer {
            if(it){
                mostrarMensaje("Guardado!")
                irAlInicio()
            }else{
                mostrarMensaje("Ocurrio un error")
            }
        })

        if (viewModel.operacion.equals(constantes.OPERACION_EDITAR)){
            viewModel.id.value = intent.getLongExtra(constantes.ID_NOTAS_KEY,0)
            viewModel.cargarDatos()
            binding.linearEditar.visibility= View.VISIBLE
            binding.btnGuardar.visibility = View.GONE
        }else{
            binding.linearEditar.visibility = View.GONE
            binding.btnGuardar.visibility = View.VISIBLE
        }
        binding.btnBorrar.setOnClickListener{
            mostrarDialogo()
        }

        binding.btnFotovideo.setOnClickListener{
            val intent = Intent(applicationContext, camaraActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }

    private fun mostrarDialogo() {
        dialogo.show(supportFragmentManager,"Dialogo Borrar")
    }


    private fun mostrarMensaje(s: String){
        Toast.makeText(applicationContext, s, Toast.LENGTH_SHORT).show()
    }

    private fun irAlInicio(){
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    override fun borrarNota() {
        viewModel.eliminarNota()
    }
}