package com.example.listviewapp.adapters

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import com.example.listviewapp.R
import com.example.listviewapp.model.Animal

//class ListAdapter(
//    context: Context,
//    private val animals: List<Animal>,
//    private val onDeleteClicked: (Animal) -> Unit,
//    private val onEditClicked: (Animal) -> Unit
//) : ArrayAdapter<Animal>(context, R.layout.item_list, animals) {
//
//    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_list, parent, false)
//
//        val textView = view.findViewById<TextView>(R.id.itemText)
//        val deleteButton = view.findViewById<Button>(R.id.deleteButton)
//        val editButton = view.findViewById<Button>(R.id.editButton)
//
//        val animal = animals[position]
//        textView.text = animal.name
//
//        // Set up the delete button
//        deleteButton.setOnClickListener {
//            onDeleteClicked(animal)
//        }
//
//        // Set up the edit button
//        editButton.setOnClickListener {
//            onEditClicked(animal)
//        }
//
//        return view
//    }
//}


class ListAdapter(
    context: Context,
    private val onDeleteClicked: (Animal) -> Unit,
    private val onEditClicked: (Animal) -> Unit
) : ArrayAdapter<Animal>(context, R.layout.item_list, ArrayList()) {

    private var animals: MutableList<Animal> = ArrayList()

    override fun getCount(): Int = animals.size

    override fun getItem(position: Int): Animal? = animals[position]

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_list, parent, false)

        val textView = view.findViewById<TextView>(R.id.itemText)
        val deleteButton = view.findViewById<Button>(R.id.deleteButton)
        val editButton = view.findViewById<Button>(R.id.editButton)

        val animal = animals[position]
        textView.text = animal.name

        // Set up the delete button
        deleteButton.setOnClickListener {
            onDeleteClicked(animal)
        }

        // Set up the edit button
        editButton.setOnClickListener {
            onEditClicked(animal)
        }

        return view
    }

    // Method to update the adapter's data
    fun updateData(newAnimals: List<Animal>) {
        this.animals.clear()  // Clear existing data
        this.animals.addAll(newAnimals)  // Add new data
    }
}


