package com.example.myapp.ui.parametro

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun ParameterFormDialog(
    isLoading: Boolean,
    onDismiss: () -> Unit,
    onSubmit: (String, String, Int) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var importance by remember { mutableStateOf(1) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 8.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Nuevo Parámetro",
                    style = MaterialTheme.typography.headlineSmall
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3,
                    shape = RoundedCornerShape(12.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Importancia:")
                    Icon(Icons.Default.Star, contentDescription = "Importancia")
                    Slider(
                        value = importance.toFloat(),
                        onValueChange = { importance = it.toInt() },
                        valueRange = 1f..3f,
                        steps = 2,
                        modifier = Modifier.weight(1f)
                    )
                    Text(text = importance.toString())
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    } else {
                        Text(
                            text = "Cancelar",
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .clickable { onDismiss() }
                        )

                        Button(
                            onClick = { onSubmit(name, description, importance) },
                            shape = RoundedCornerShape(12.dp),
                            enabled = name.isNotBlank() && description.isNotBlank() && !isLoading
                        ) {
                            Text("Guardar")
                        }
                    }
                }
            }
        }
    }
}