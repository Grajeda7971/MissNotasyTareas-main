package com.example.missnotasytareas.ui

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.webkit.WebView
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.example.missnotasytareas.Dialog.borrarDialogo
import com.example.missnotasytareas.MainActivity
import com.example.missnotasytareas.controlador.constantes
import com.example.missnotasytareas.databinding.ActivityTareasBinding
import com.example.missnotasytareas.modelo.Tareas
import com.example.missnotasytareas.service.AlarmService
import com.example.missnotasytareas.viewModel.TareasViewModel
import com.google.android.material.timepicker.MaterialTimePicker
import datePickerFragment
import timePickerFragment
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*



class TareasActivity : AppCompatActivity(), borrarDialogo.BorrarListener {
    lateinit var binding: com.example.missnotasytareas.databinding.ActivityTareasBinding
    lateinit var viewModel: TareasViewModel
    lateinit var dialogo: borrarDialogo
    lateinit var context:Context
    lateinit var picker: MaterialTimePicker
    lateinit var alarmManager: AlarmManager
    lateinit var alarmService: AlarmService
    lateinit var pendingIntent: PendingIntent
    private val REQUEST_CAMERA = 100
    lateinit var webView: WebView
    lateinit var videoView: VideoView
    val REQUEST_IMAGE_CAPTURE = 1
    val REQUEST_TAKE_FOTO = 1

    var currentPhotoPath: String? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTareasBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createNotificationChannel()
        alarmService= AlarmService(this)
        dialogo = borrarDialogo(this)
        viewModel = ViewModelProvider(this).get()
        viewModel.operacion = intent.getStringExtra(constantes.OPERACION_KEY)!!
        binding.modeloTareas = viewModel
        binding.lifecycleOwner = this

        binding.txtUriVideoTarea.text =""
        binding.txtUriImagenTarea.text =""

        binding.btnSelecionarAlarma.setOnClickListener{
            setAlarmChingon { timeInMillis -> alarmService.setExactAlarm(timeInMillis) }
        }



        binding.videoView.setOnClickListener{
        }

        binding.btnVerimagen.setOnClickListener{

        }

        binding.AgregarAudio.setOnClickListener{

        }


        viewModel.operacionExitosa.observe(this, Observer {
            if(it){
                mostrarMensaje("Guardado!")
                irAlInicio()
            }else{
                mostrarMensaje("Ocurrio un error")
            }
        })

        if (viewModel.operacion.equals(constantes.OPERACION_EDITAR)){
            viewModel.id.value = intent.getLongExtra(constantes.ID_TAREAS_KEY,0)
            viewModel.cargarDatos()

            binding.btnVervideo.setOnClickListener{
                val intent = Intent(this, verVideo::class.java)
                intent.putExtra("uriVideo",(binding.txtUriImagenTarea.text.toString()) )
                startActivity(intent)
            }

            binding.btnVervideo.visibility = View.VISIBLE
            binding.btnVerimagen.visibility = View.GONE
            binding.linearEditar.visibility= View.VISIBLE
            binding.btnCargardatos.visibility = View.VISIBLE
            binding.btnGuardarTarea.visibility = View.GONE

            //var ur = binding.txtTituloTarea.text
            //Toast.makeText(applicationContext, "esto consegi $ur", Toast.LENGTH_SHORT).show()
            //binding.imageView2.setImageURI(Uri.parse(ur.toString()))
        }else{
            binding.linearEditar.visibility = View.GONE
            binding.btnGuardarTarea.visibility = View.VISIBLE
        }
        binding.btnBorrar.setOnClickListener{
            mostrarDialogo()
        }

        binding.btnFotovideoTareas.setOnClickListener{
            val intent = Intent(applicationContext, camaraActivity::class.java)
            startActivityForResult(intent,1001)
        }

        binding.AgregarFoto.setOnClickListener{
           //dispatchTakePictureIntent()
            val intent = Intent(applicationContext, camaraActivity::class.java)
            startActivityForResult(intent,1001)
        }


        binding.AgregarVideo.setOnClickListener{
            //captureVideo()
            val intent = Intent(applicationContext, VideoActivity::class.java)
            startActivityForResult(intent,100)
        }

        binding.btnCargardatos.setOnClickListener{
            var ur = binding.txtUriImagenTarea.text
              var urV = binding.txtUriVideoTarea.text

            Toast.makeText(applicationContext, "esto consegi imagen ${binding.txtUriImagenTarea.text}", Toast.LENGTH_SHORT).show()
            //Toast.makeText(applicationContext, "esto consegi $urV", Toast.LENGTH_SHORT).show()
            binding.imageView2.setImageURI(Uri.parse(binding.txtUriVideoTarea.text.toString()))
            binding.videoView.setVideoURI(Uri.parse(binding.txtUriImagenTarea.text.toString()))
            //binding.videoView.setVideoURI(Uri.parse(urV.toString()))
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode ==  1001){
            val b = data?.extras
            val y = b?.get("lauri").toString()
            Toast.makeText(this,"aqui va la uri${y}",Toast.LENGTH_SHORT).show()
            binding.imageView2.setImageURI(Uri.parse(y))
            binding.txtUriImagenTarea.setText(y)
        }else{
            val m = data?.extras
            val n = m?.get("lauri2").toString()
            Toast.makeText(this,"aqui va la uri2${n}",Toast.LENGTH_SHORT).show()
            binding.videoView.setVideoURI(Uri.parse(n))
            binding.txtUriVideoTarea.setText(n)
        }
        //val x = data?.getStringExtra("lauri")
    }

    override fun onActivityReenter(resultCode: Int, data: Intent?) {
        super.onActivityReenter(resultCode, data)
    }

    private fun captureImage() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, REQUEST_CAMERA)
    }


    private fun captureVideo() {
        val cameraIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        startActivityForResult(cameraIntent, REQUEST_CAMERA)
    }




    private fun setAlarmChingon(callback: (Long) -> Unit){
        Calendar.getInstance().apply {
            this.set(Calendar.SECOND,0)
            this.set(Calendar.MILLISECOND,0)
            DatePickerDialog(
                this@TareasActivity,
                0,
                { _, year, month, dayOfMonth ->
                    this.set(Calendar.YEAR, year)
                    this.set(Calendar.MONTH,  month)
                    this.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    TimePickerDialog(
                        this@TareasActivity,
                        0,
                        {_,hour,min ->
                            this.set(Calendar.HOUR_OF_DAY, hour)
                            this.set(Calendar.MINUTE, min)
                            callback(this.timeInMillis)
                        },
                        this.get(Calendar.HOUR_OF_DAY),
                        this.get(Calendar.MINUTE),
                        false
                    ).show()
                    binding.txtHoraTarea.setText("${this.get(Calendar.HOUR_OF_DAY)}"+" : " +"${this.get(Calendar.MINUTE)}")
                },
                this.get(Calendar.YEAR),
                this.get(Calendar.MONTH),
                this.get(Calendar.DAY_OF_MONTH)
            ).show()
            binding.txtFechaTarea.setText("${this.get(Calendar.DAY_OF_MONTH)}" +"/"+ "${this.get(Calendar.MONTH)}" +"/"+ "${this.get(Calendar.YEAR)}")
        }
    }




    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "janoReminderChannel"
            val description = "channel for alarm"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("jano", name, importance)
            channel.description = description
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showTimePickerDialog() {
        val timePicker = timePickerFragment{selectorHora(it)}
        timePicker.show(supportFragmentManager,"time")
    }

    fun selectorHora(time:String){
        binding.txtHoraTarea.setText("$time")
    }

    private fun showDatePickerDialog() {
        val datePicker = datePickerFragment{day, month, year -> selectorFecha(day, month, year)}
        datePicker.show(supportFragmentManager, "datePicker")
    }
    fun selectorFecha(day: Int, month: Int, year:Int){
        binding.txtFechaTarea.setText("Dia: $day Mes: $month Año: $year")
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
        viewModel.eliminarTarea()
    }


    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }



}
