package com.example.missnotasytareas.receiver

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.text.format.DateFormat
import com.example.missnotasytareas.MainActivity
import com.example.missnotasytareas.controlador.constantes
import com.example.missnotasytareas.service.AlarmService
import io.karn.notify.Notify
import java.util.Calendar
import java.util.concurrent.TimeUnit

class AlarmReceiver : BroadcastReceiver(){
    override fun onReceive(context: Context, intent: Intent) {
        val palmain = Intent(context, MainActivity::class.java)
        intent!!.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context,0,palmain,0)
        val timeInMillis = intent.getLongExtra(constantes.EXTRA_EXACT_ALARM_TIME,0L)
        when(intent.action){
            constantes.ACTION_SET_EXACT_ALARM ->{
                buildNotification(context,"Recordatorio de Tarea", convertDate(timeInMillis))
            }
             constantes.ACTION_SET_REPETITIVE_ALARM ->{
                 val cal = Calendar.getInstance().apply {
                     this.timeInMillis = timeInMillis + TimeUnit.DAYS.toMillis(7)
                 }
                 AlarmService(context).setRepetitiveAlarm(cal.timeInMillis)
                 buildNotification(context, "Recordatorio repetitivo", convertDate(cal.timeInMillis))
             }
        }
    }

    private fun buildNotification(context: Context, title: String, message:String){
        Notify
            .with(context)
            .content {
                this.title = title
                this.text = "Tienes una tarea pendiente - $message"
            }
            .show()
    }

    private fun convertDate(timeInMillis: Long): String =
        DateFormat.format("dd/MM/yyyy hh:mm:ss", timeInMillis).toString()

}