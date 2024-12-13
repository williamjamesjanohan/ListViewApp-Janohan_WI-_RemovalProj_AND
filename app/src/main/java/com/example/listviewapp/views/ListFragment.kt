package com.example.listviewapp.views

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.listviewapp.R
import com.example.listviewapp.adapters.ListAdapter
import com.example.listviewapp.model.Animal
import com.example.listviewapp.viewmodels.ListViewModel

class ListFragment : Fragment() {

    private lateinit var listView: ListView
    private lateinit var viewModel: ListViewModel
    private lateinit var listAdapter: ListAdapter




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_list, container, false)

        viewModel = ViewModelProvider(requireActivity()).get(ListViewModel::class.java)
        listView = rootView.findViewById(R.id.listView)

        // Initialize the adapter with an empty list
        listAdapter = ListAdapter(
            requireContext(),
            onDeleteClicked = { animal -> viewModel.deleteAnimal(animal.id) },
            onEditClicked = { animal -> showEditDialog(animal) }
        )
        listView.adapter = listAdapter

        // Observe the animal list LiveData
        viewModel.animalList.observe(viewLifecycleOwner) { animals ->
            if (animals != null) {
                // Update adapter with the new data
                listAdapter.updateData(animals)
                listAdapter.notifyDataSetChanged() // Refresh the ListView
            }
        }

        viewModel.fetchAnimals()

        return rootView
    }



    private fun showEditDialog(animal: Animal) {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_update_animal, null)
        builder.setView(dialogView)

        val editText = dialogView.findViewById<EditText>(R.id.editAnimalName)
        val saveButton = dialogView.findViewById<Button>(R.id.saveButton)

        editText.setText(animal.name)

        val dialog = builder.create()

        saveButton.setOnClickListener {
            val updatedName = editText.text.toString().trim()
            if (updatedName.isNotEmpty()) {
                viewModel.updateAnimal(animal.id, updatedName)
                dialog.dismiss()
            } else {
                Toast.makeText(requireContext(), "Name cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }
}

