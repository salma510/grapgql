package ma.ensa.TP_graph.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ma.ensa.TP_graph.GetAllComptesQuery
import ma.ensa.TP_graph.R

class ComptesAdapter : RecyclerView.Adapter<ComptesAdapter.CompteViewHolder>() {
    private var comptes: List<GetAllComptesQuery.AllCompte> = listOf()

    fun updateList(newList: List<GetAllComptesQuery.AllCompte>) {
        comptes = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_compte, parent, false)
        return CompteViewHolder(view)
    }

    override fun onBindViewHolder(holder: CompteViewHolder, position: Int) {
        holder.bind(comptes[position])
    }

    override fun getItemCount() = comptes.size

    class CompteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val accountIdText: TextView = view.findViewById(R.id.accountIdText)
        private val soldeText: TextView = view.findViewById(R.id.soldeText)
        private val typeText: TextView = view.findViewById(R.id.typeText)
        private val dateText: TextView = view.findViewById(R.id.dateText)

        fun bind(compte: GetAllComptesQuery.AllCompte) {
            accountIdText.text = "Id du compte: ${compte.id}"
            soldeText.text = "solde: ${compte.solde} Dh"
            typeText.text = "Type: ${compte.type}"

        }
    }
}