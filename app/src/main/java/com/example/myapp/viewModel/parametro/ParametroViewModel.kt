package com.example.myapp.viewModel.parametro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapp.api.RetrofitInstance
import com.example.myapp.model.parametro.Parametro
import com.example.myapp.model.parametro.ParametroRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ParameterViewModel : ViewModel() {
    private val _parameters = MutableStateFlow<List<Parametro>>(emptyList())
    val parameters: StateFlow<List<Parametro>> = _parameters.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        loadParameters()
    }

    fun loadParameters() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitInstance.apiService.getParametros()
                if (response.statusCode == 200) {
                    _parameters.value = response.data
                } else {
                    _errorMessage.value = response.message
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error al cargar los parámetros: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createParameter(name: String, description: String, importance: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                RetrofitInstance.apiService.createParametros(
                    ParametroRequest(name, description, importance)
                )
                loadParameters()
            } catch (e: Exception) {
                _errorMessage.value = "Error al crear el parámetro: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}