// screens/RecoleccionScreen.kt
package com.example.myapp.ui.recoleccion

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapp.ui.recoleccion.AddRecoleccionDialog
import com.example.myapp.ui.residuos.FullScreenError
import com.example.myapp.ui.recoleccion.RecoleccionItem
import com.example.myapp.viewModel.residuo.RecoleccionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecoleccionScreen(viewModel: RecoleccionViewModel = viewModel()) {
    val recolecciones by viewModel.recolecciones.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar recolección")
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            when {
                errorMessage != null -> {
                    FullScreenError(
                        message = errorMessage ?: "Error desconocido",
                        onRetry = { viewModel.loadRecolecciones() }
                    )
                }

                isLoading && recolecciones.isEmpty() -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(recolecciones) { recoleccion ->
                            RecoleccionItem(recoleccion = recoleccion)
                        }
                    }
                }
            }

            if (showDialog) {
                AddRecoleccionDialog(
                    viewModel = viewModel,
                    onDismiss = { showDialog = false }
                )
            }
        }
    }
}