    package com.example.listviewapp.viewmodels

    import android.util.Log
    import androidx.lifecycle.LiveData
    import androidx.lifecycle.MutableLiveData
    import androidx.lifecycle.ViewModel
    import com.example.listviewapp.model.Animal
    import com.example.listviewapp.network.ApiService

    class ListViewModel : ViewModel() {

        // LiveData to hold the list of animals
        private val _animalList = MutableLiveData<List<Animal>?>()
        val animalList: LiveData<List<Animal>?> get() = _animalList

        // LiveData to indicate if there was an error while fetching data
        private val _error = MutableLiveData<String>()
        val error: LiveData<String> get() = _error

        fun fetchAnimals() {
            ApiService.getAnimals { animals ->
                if (!animals.isNullOrEmpty()) {
                    _animalList.postValue(animals)  // Update LiveData with fetched list only if it's not empty
                    Log.d("ListViewModel", "Fetched animals: ${animals.size}")
                } else {
                    Log.w("ListViewModel", "Fetched animals is empty or null, skipping update.")
                }
            }
        }



        // Function to delete an animal from the list
        fun deleteAnimal(animalId: Int) {
            ApiService.deleteAnimal(animalId) { isSuccess ->
                if (isSuccess) {
                    // After successful deletion, remove the animal from the list manually
                    _animalList.value?.let { currentList ->
                        // Filter out the deleted animal by ID
                        val updatedList = currentList.filter { it.id != animalId }
                        // Post the updated list back to LiveData
                        _animalList.postValue(updatedList)

                        // Optionally, you can check if the list is empty and show a message
                        if (updatedList.isEmpty()) {
                            _error.postValue("No animals left")
                        }
                    }
                } else {
                    _error.postValue("Failed to delete animal.")
                }
            }
        }

        fun updateAnimal(id: Int, newName: String) {
            ApiService.updateAnimal(id, newName) { isSuccess ->
                if (isSuccess) {
                    fetchAnimals() // Reload the list to reflect the updated name
                } else {
                    _error.postValue("Failed to update animal.")
                }
            }
        }




        fun addAnimal(animal: Animal) {
            // Call the ApiService to add the animal
            ApiService.addAnimal(animal) { isSuccess ->
                if (isSuccess) {
                    // Update the LiveData only after a successful response
                    val currentList = _animalList.value ?: mutableListOf()
                    val updatedList = currentList.toMutableList().apply { add(animal) }

                    _animalList.postValue(updatedList) // Update LiveData on the main thread
                    Log.d("ViewModel", "Animal added successfully. Updated list: ${updatedList.joinToString { it.name }}")
                } else {
                    Log.e("ViewModel", "Failed to add animal.")
                }
            }
        }


    }

