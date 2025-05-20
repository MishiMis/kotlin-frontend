package com.example.myapp.ui.residuos

import ResiduosViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapp.ui.residuos.AddResiduoDialog
import com.example.myapp.ui.residuos.FullScreenError
import com.example.myapp.ui.residuos.ResiduoItem


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResiduosScreen(viewModel: ResiduosViewModel = viewModel()) {
    val residuos by viewModel.residuos.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            if (errorMessage == null) { // Solo mostrar FAB si no hay error
                FloatingActionButton(onClick = { showDialog = true }) {
                    Icon(Icons.Default.Add, contentDescription = "Agregar residuo")
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            when {
                errorMessage != null -> {
                    FullScreenError(
                        message = errorMessage ?: "Error desconocido",
                        onRetry = { viewModel.loadResiduos() },
                        modifier = Modifier.fillMaxSize()
                    )
                }

                isLoading && residuos.isEmpty() -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(residuos) { residuo ->
                            ResiduoItem(residuo = residuo)
                        }
                    }
                }
            }

            if (showDialog) {
                AddResiduoDialog(
                    viewModel = viewModel,
                    onDismiss = { showDialog = false }
                )
            }
        }
    }
}