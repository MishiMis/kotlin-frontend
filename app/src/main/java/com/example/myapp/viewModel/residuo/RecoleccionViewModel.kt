
package com.example.myapp.viewModel.residuo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapp.api.RetrofitInstance
import com.example.myapp.model.residuo.Recoleccion
import com.example.myapp.model.residuo.RecoleccionRequest

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RecoleccionViewModel : ViewModel() {
    private val _recolecciones = MutableStateFlow<List<Recoleccion>>(emptyList())
    val recolecciones: StateFlow<List<Recoleccion>> = _recolecciones

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        loadRecolecciones()
    }

    fun loadRecolecciones() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitInstance.apiService.getRecolecciones()
                if (response.isSuccessful) {
                    _recolecciones.value = response.body() ?: emptyList()
                } else {
                    _errorMessage.value = "Error ${response.code()}: ${response.message()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error de conexión: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createRecoleccion(recoleccion: RecoleccionRequest, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitInstance.apiService.createRecoleccion(recoleccion)
                if (response.isSuccessful) {
                    loadRecolecciones()
                    onSuccess()
                } else {
                    _errorMessage.value = "Error al crear: ${response.message()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}