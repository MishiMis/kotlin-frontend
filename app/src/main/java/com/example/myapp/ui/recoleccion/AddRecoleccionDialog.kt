package com.example.myapp.ui.recoleccion

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapp.model.residuo.ItemRecoleccion
import com.example.myapp.model.residuo.RecoleccionRequest
import com.example.myapp.model.residuo.Ubicacion
import com.example.myapp.viewModel.residuo.RecoleccionViewModel

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecoleccionDialog(
    viewModel: RecoleccionViewModel,
    onDismiss: () -> Unit
) {
    var responsable by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    val items = remember { mutableStateListOf<ItemRecoleccion>() }

    // Agregar un ítem vacío inicial
    if (items.isEmpty()) {
        items.add(ItemRecoleccion("", 0.0, "kg"))
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nueva Recolección") },
        text = {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    OutlinedTextField(
                        value = responsable,
                        onValueChange = { responsable = it },
                        label = { Text("Responsable") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = direccion,
                        onValueChange = { direccion = it },
                        label = { Text("Dirección") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                itemsIndexed(items) { index, item ->
                    ItemRecoleccionForm(
                        item = item,
                        onItemChange = { updatedItem ->
                            items[index] = updatedItem
                        },
                        onRemove = {
                            if (items.size > 1) {
                                items.removeAt(index)
                            }
                        }
                    )
                }

                item {
                    Button(
                        onClick = {
                            items.add(ItemRecoleccion("", 0.0, "kg"))
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Agregar ítem")
                        Spacer(Modifier.width(8.dp))
                        Text("Agregar otro ítem")
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val fechaActual = LocalDateTime.now()
                        .format(DateTimeFormatter.ISO_DATE_TIME)

                    val recoleccion = RecoleccionRequest(
                        fecha = fechaActual,
                        ubicacion = Ubicacion(direccion),
                        items = items.toList(),
                        responsable = responsable
                    )

                    viewModel.createRecoleccion(recoleccion, onDismiss)
                }
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun ItemRecoleccionForm(
    item: ItemRecoleccion,
    onItemChange: (ItemRecoleccion) -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Ítem de recolección", style = MaterialTheme.typography.titleSmall)
                IconButton(onClick = onRemove) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                }
            }

            OutlinedTextField(
                value = item.tipoResiduo,
                onValueChange = {
                    onItemChange(item.copy(tipoResiduo = it))
                },
                label = { Text("Tipo de residuo") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = item.cantidad.toString(),
                    onValueChange = {
                        val cantidad = it.toDoubleOrNull() ?: 0.0
                        onItemChange(item.copy(cantidad = cantidad))
                    },
                    label = { Text("Cantidad") },
                    modifier = Modifier.weight(1f)
                )

                OutlinedTextField(
                    value = item.unidad,
                    onValueChange = {
                        onItemChange(item.copy(unidad = it))
                    },
                    label = { Text("Unidad") },
                    modifier = Modifier.weight(1f)
                )
            }

            OutlinedTextField(
                value = item.observaciones ?: "",
                onValueChange = {
                    onItemChange(item.copy(observaciones = it.ifEmpty { null }))
                },
                label = { Text("Observaciones (opcional)") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}