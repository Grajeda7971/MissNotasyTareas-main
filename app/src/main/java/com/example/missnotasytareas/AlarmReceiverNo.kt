package com.example.missnotasytareas

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.databinding.DataBindingUtil
import com.example.missnotasytareas.databinding.FragmentListTareasBinding
import com.example.missnotasytareas.ui.TareasActivity

class AlarmReceiverNo: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val palmain = Intent(context, MainActivity::class.java)
        val binding: FragmentListTareasBinding
        intent!!.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context,0,palmain,0)

        val builder = NotificationCompat.Builder(context!!,"jano")
            .setSmallIcon(R.drawable.iconchido)
            .setContentTitle("Recordatorio de tareas")
            .setContentText("Tienes una tarea pendiente")
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(1,builder.build())
    }
}