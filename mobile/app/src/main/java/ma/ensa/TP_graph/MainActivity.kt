package ma.ensa.TP_graph

import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import ma.ensa.TP_graph.adapter.ComptesAdapter
import ma.ensa.TP_graph.data.MainViewModel
import ma.ensa.TP_graph.type.TypeCompte
import ma.ensa.graphqltp.R

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var addButton: Button
    private lateinit var totalCountText: TextView
    private lateinit var totalSumText: TextView
    private lateinit var averageText: TextView
    private lateinit var statsCard: View

    private val viewModel: MainViewModel by viewModels()
    private val comptesAdapter = ComptesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeViews()
        setupRecyclerView()
        setupAddButton()
        observeViewModel()
    }

    private fun initializeViews() {
        recyclerView = findViewById(R.id.comptesRecyclerView)
        addButton = findViewById(R.id.addCompteButton)
        totalCountText = findViewById(R.id.totalCountText)
        totalSumText = findViewById(R.id.totalSumText)
        averageText = findViewById(R.id.averageText)
        statsCard = findViewById(R.id.statsCard)

        // Masquer les éléments au début
        totalCountText.visibility = View.GONE
        totalSumText.visibility = View.GONE
        averageText.visibility = View.GONE
        statsCard.visibility = View.GONE
    }

    private fun setupRecyclerView() {
        recyclerView.apply {
            adapter = comptesAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    private fun setupAddButton() {
        addButton.setOnClickListener {
            showAddCompteDialog()
        }
    }

    private fun observeViewModel() {
        // Observer la liste des comptes
        viewModel.comptesState.observe(this) { state ->
            when (state) {
                is MainViewModel.UIState.Loading -> {
                    // Afficher un état de chargement (optionnel)
                }
                is MainViewModel.UIState.Success -> {
                    comptesAdapter.updateList(state.data)
                }
                is MainViewModel.UIState.Error -> {
                    Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
                }
            }
        }

        // Observer les statistiques
        viewModel.totalSoldeState.observe(this) { state ->
            when (state) {
                is MainViewModel.UIState.Loading -> {
                    statsCard.visibility = View.INVISIBLE
                }
                is MainViewModel.UIState.Success -> {
                    // Mettre à jour les statistiques et afficher les éléments
                    val stats = state.data
                    totalCountText.text = "Total des comptes: ${stats?.count}"
                    totalSumText.text = "Total des soldes: ${stats?.sum} Dh"
                    averageText.text = "Moyenne: ${stats?.average} Dh"

                    // Afficher les vues
                    showStats()
                }
                is MainViewModel.UIState.Error -> {
                    statsCard.visibility = View.INVISIBLE
                    Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun showStats() {
        totalCountText.visibility = View.VISIBLE
        totalSumText.visibility = View.VISIBLE
        averageText.visibility = View.VISIBLE
        statsCard.visibility = View.VISIBLE
    }

    private fun showAddCompteDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_compte, null)
        val soldeInput = dialogView.findViewById<TextInputEditText>(R.id.soldeInput)
        val typeGroup = dialogView.findViewById<RadioGroup>(R.id.typeGroup)

        MaterialAlertDialogBuilder(this)
            .setTitle("Ajoutez un compte")
            .setView(dialogView)
            .setPositiveButton("Ajouter") { _, _ ->
                val solde = soldeInput.text.toString().toDoubleOrNull()
                val selectedId = typeGroup.checkedRadioButtonId
                val typeRadioButton = dialogView.findViewById<RadioButton>(selectedId)
                val type = when (typeRadioButton.text.toString().uppercase()) {
                    "COURANT" -> TypeCompte.COURANT
                    "EPARGNE" -> TypeCompte.EPARGNE
                    else -> TypeCompte.COURANT
                }

                if (solde != null) {
                    viewModel.saveCompte(solde, type)
                } else {
                    Toast.makeText(this, "Veuillez entrer un montant valide", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Annuler", null)
            .show()
    }
}
