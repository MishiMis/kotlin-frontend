package com.example.myapp.ui.parametro

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
import com.example.myapp.ui.parametro.ParameterFormDialog
import com.example.myapp.ui.parametros.ParameterItem
import com.example.myapp.viewModel.parametro.ParameterViewModel

@Composable
fun ParametrosScreen() {
    val viewModel: ParameterViewModel = viewModel()
    val parameters by viewModel.parameters.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    var showFormDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopStart
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Parámetros",
                    fontSize = 24.sp,
                    style = MaterialTheme.typography.headlineSmall
                )

                FloatingActionButton(
                    onClick = { showFormDialog = true },
                    modifier = Modifier.size(40.dp),
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Agregar Parámetro"
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when {
                isLoading && parameters.isEmpty() -> {
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
                parameters.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "No hay parámetros registrados",
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(parameters) { parameter ->
                            ParameterItem(parameter = parameter)
                        }
                    }
                }
            }
        }
    }

    if (showFormDialog) {
        ParameterFormDialog(
            isLoading = isLoading,
            onDismiss = { showFormDialog = false },
            onSubmit = { name, description, importance ->
                viewModel.createParameter(name, description, importance)
                showFormDialog = false
            }
        )
    }
}