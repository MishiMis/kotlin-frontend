package com.example.myapp.ui.software

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapp.viewModel.evaluacion.EvaluationViewModel
import com.example.myapp.viewModel.software.SoftwareViewModel
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Scaffold
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SoftwareScreen() {
    val evaluationViewModel: EvaluationViewModel = viewModel()
    val viewModel: SoftwareViewModel = viewModel()
    val softwareList by viewModel.softwareList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()


    // Estados para los diálogos
    var mostrarFormulario by remember { mutableStateOf(false) }
    var mostrarEvaluacionDialog by remember { mutableStateOf(false) }
    var selectedSoftwareId by remember { mutableStateOf("") }

    // Snackbar
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Manejar estados de evaluación
    val evaluationState by evaluationViewModel.isSuccess.collectAsState()
    val evaluationError by evaluationViewModel.errorMessage.collectAsState()

    LaunchedEffect(evaluationState) {
        if (evaluationState) {
            mostrarEvaluacionDialog = false
            scope.launch {
                snackbarHostState.showSnackbar("Evaluación enviada con éxito")
            }
        }
    }

    LaunchedEffect(evaluationError) {
        evaluationError?.let { error ->
            snackbarHostState.showSnackbar(error)
            delay(3000)
            evaluationViewModel.clearErrorMessage()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { mostrarFormulario = true },
                modifier = Modifier.size(56.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Software")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.TopStart
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Software",
                    fontSize = 24.sp,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                when {
                    isLoading && softwareList.isEmpty() -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                    errorMessage != null -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(
                                text = errorMessage ?: "Error desconocido",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                    softwareList.isEmpty() -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(
                                text = "No hay software registrado",
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    }
                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(softwareList) { software ->
                                SoftwareItem(
                                    software = software,
                                    onEvaluateClick = { softwareId ->
                                        selectedSoftwareId = softwareId
                                        mostrarEvaluacionDialog = true
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (mostrarEvaluacionDialog) {
        SoftwareEvaluationDialog(
            softwareId = selectedSoftwareId,
            onDismiss = { mostrarEvaluacionDialog = false },
            onSubmit = { evaluationData ->
                evaluationViewModel.submitEvaluation(evaluationData)
            }
        )
    }
}