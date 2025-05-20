import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapp.api.RetrofitInstance
import com.example.myapp.model.residuo.Residuo
import com.example.myapp.model.residuo.ResiduoRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ResiduosViewModel : ViewModel() {
    private val _residuos = MutableStateFlow<List<Residuo>>(emptyList())
    val residuos: StateFlow<List<Residuo>> = _residuos

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        loadResiduos()
    }

    // ResiduosViewModel.kt
    fun loadResiduos() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitInstance.apiService.getResiduos()
                if (response.isSuccessful) {
                    _residuos.value = response.body() ?: emptyList()
                } else {
                    _errorMessage.value = "Error ${response.code()}: ${response.message()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error de conexión: ${e.localizedMessage}"
                e.printStackTrace() // Para ver detalles en el log
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createResiduo(residuo: ResiduoRequest, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitInstance.apiService.createResiduo(residuo)
                if (response.isSuccessful) {
                    loadResiduos() // Recargar la lista
                    onSuccess()
                } else {
                    _errorMessage.value = "Error al crear: ${response.code()} - ${response.message()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error de red: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}