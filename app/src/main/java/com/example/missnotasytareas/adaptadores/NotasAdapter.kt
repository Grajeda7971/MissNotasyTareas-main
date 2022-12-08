package com.example.missnotasytareas.adaptadores

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.missnotasytareas.R
import com.example.missnotasytareas.controlador.constantes
import com.example.missnotasytareas.databinding.ItemListBinding
import com.example.missnotasytareas.modelo.Notas
import com.example.missnotasytareas.ui.NotasActivity


class NotasAdapter(private val dataSet: List<Notas>?) :
    RecyclerView.Adapter<NotasAdapter.ViewHolder>() {


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_list, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        val item = dataSet?.get(position)
        viewHolder.enlazarItem(item!!)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet!!.size

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding = ItemListBinding.bind(view)
        var contexto = view.context
        fun enlazarItem(n: Notas) {
            binding.txtTituloNota.text = "${n.titulo}"
            binding.txtDescripcionNota.text = "${n.descripcion}"
            binding.root.setOnClickListener{
                val intent = Intent(contexto, NotasActivity::class.java)
                intent.putExtra(constantes.OPERACION_KEY, constantes.OPERACION_EDITAR)
                intent.putExtra(constantes.ID_NOTAS_KEY,n.idNota)
                contexto.startActivity(intent)
            }
        }
    }
}