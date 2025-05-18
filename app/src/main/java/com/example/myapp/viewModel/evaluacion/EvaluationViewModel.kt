package com.example.myapp.viewModel.evaluacion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapp.api.RetrofitInstance
import com.example.myapp.model.evaluacion.EvaluationRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EvaluationViewModel : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess

    fun submitEvaluation(evaluationData: Map<String, Any>) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            _isSuccess.value = false

            try {
                // Convertir el Map a EvaluationRequest
                val request = EvaluationRequest(
                    softwareId = evaluationData["softwareId"] as String,
                    evaluator = evaluationData["evaluator"] as String,
                    parameters = (evaluationData["parameters"] as List<Map<String, Any>>).map { param ->
                        EvaluationRequest.ParameterEvaluation(
                            parameterId = param["parameterId"] as String,
                            cumple = param["cumple"] as Boolean,
                            score = param["score"] as? Int,
                            notes = param["notes"] as String
                        )
                    },
                    overallScore = evaluationData["overallScore"] as Double
                )

                val response = RetrofitInstance.apiService.createEvaluation(request)
                if (response.isSuccessful) {
                    _isSuccess.value = true
                } else {
                    _errorMessage.value = response.message() ?: "Error desconocido"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error de conexión: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearErrorMessage() {
        TODO("Not yet implemented")
    }
}