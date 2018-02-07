package br.com.montecardo.simplistic.item

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.montecardo.simplistic.R
import kotlinx.android.synthetic.main.item_view.view.*

class ItemAdapter(val presenter: ItemContract.ListPresenter) :
    RecyclerView.Adapter<ItemAdapter.ItemHolder>() {

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        presenter.bind(holder, position)
    }

    override fun getItemCount() = presenter.getRowCount()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val rowView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_view, parent, false)

        return ItemHolder(rowView)
    }

    class ItemHolder(view: View) : RecyclerView.ViewHolder(view), ItemContract.ItemView {
        override fun setDescription(description: String) {
            itemView.item_description.text = description
        }

        override fun setOnClickListener(listener: (View) -> Unit) {
            itemView.setOnClickListener(listener)
        }
    }
}