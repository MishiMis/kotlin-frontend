package com.example.myapp.viewModel.software

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapp.api.RetrofitInstance
import com.example.myapp.model.sofware.Software
import com.example.myapp.model.sofware.SoftwareRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SoftwareViewModel : ViewModel() {
    private val _softwareList = MutableStateFlow<List<Software>>(emptyList())
    val softwareList: StateFlow<List<Software>> = _softwareList.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        loadSoftwareList()
    }

    fun loadSoftwareList() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitInstance.apiService.getSoftwareList()
                if (response.statusCode == 200) {
                    _softwareList.value = response.data
                } else {
                    _errorMessage.value = response.message
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error al cargar la lista: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createSoftware(name: String, version: String, developer: String, description: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                RetrofitInstance.apiService.createSoftware(
                    SoftwareRequest(name, version, developer, description)
                )
                loadSoftwareList() // Recargar la lista después de crear
            } catch (e: Exception) {
                _errorMessage.value = "Error al crear el software: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

}