package com.example.missnotasytareas

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.missnotasytareas.adaptadores.NotasAdapter
import com.example.missnotasytareas.adaptadores.TareasAdapter
import com.example.missnotasytareas.controlador.constantes
import com.example.missnotasytareas.databinding.ActivityMainBinding
import com.example.missnotasytareas.ui.NotasActivity
import com.example.missnotasytareas.ui.TareasActivity
import com.example.missnotasytareas.viewModel.MainViewModel


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var viewModel: MainViewModel
    lateinit var context: Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get()
        binding.lifecycleOwner= this
        binding.modelo= viewModel
        viewModel.iniciarT()
        viewModel.iniciarN()

        binding.reciclador.apply {
            layoutManager = LinearLayoutManager(applicationContext)
        }
        viewModel.notasList.observe(this, Observer {
            binding.reciclador.adapter = NotasAdapter(it)
        })

       /*viewModel.tareasList.observe(this, Observer {
            binding.reciclador.adapter = TareasAdapter(it)
        })*/


        //llamada al boton para cambiar los fragments
        binding.btnVerNotas.setOnClickListener(){
            viewModel.notasList.observe(this, Observer {
                binding.reciclador.adapter = NotasAdapter(it)
            })
            binding.btnVerTareas.visibility = View.VISIBLE
            binding.btnVerNotas.visibility = View.GONE
        }

        binding.btnVerTareas.setOnClickListener(){
            viewModel.tareasList.observe(this, Observer {
                binding.reciclador.adapter = TareasAdapter(it)
            })

            binding.btnVerTareas.visibility = View.GONE
            binding.btnVerNotas.visibility = View.VISIBLE
        }


//////////////////////////////////////////////////////////////////////////////////////////////////
        //metodo para las alarmas


//////////////////////////////////////////////////////////////////////////////////////////////////




        //aqui se agrega una nueva nota
        binding.btnNuevaNota.setOnClickListener(){
            val intent = Intent(this, NotasActivity::class.java )
            intent.putExtra(constantes.OPERACION_KEY, constantes.OPERACION_INSERTAR)
            startActivity(intent)
        }
        //aqui se agrega una nueva tarea
        binding.btnNuevaTarea.setOnClickListener(){
            val intent = Intent(this, TareasActivity::class.java )
            intent.putExtra(constantes.OPERACION_KEY, constantes.OPERACION_INSERTAR)
            startActivity(intent)
        }

        //puede buscar entre notas y tareas xd
        binding.txtBuscar.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                if(s.toString().isNotEmpty()){
                    viewModel.buscarNota()
                    viewModel.buscarTarea()
                }
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isEmpty()){
                    viewModel.iniciarT()
                    viewModel.iniciarN()
                }
            }


        })
    }
}
