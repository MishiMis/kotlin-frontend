package com.example.myapp.ui.software

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapp.model.parametro.Parametro
import com.example.myapp.viewModel.parametro.ParameterViewModel

data class ParameterEvaluation(
    val parameterId: String,
    val cumple: Boolean,
    val score: Int?,
    val notes: String
)

@Composable
fun SoftwareEvaluationDialog(
    softwareId: String,
    onDismiss: () -> Unit,
    onSubmit: (Map<String, Any>) -> Unit
) {
    val parameterViewModel: ParameterViewModel = viewModel()

    val parameters by parameterViewModel.parameters.collectAsState()
    val isLoading by parameterViewModel.isLoading.collectAsState()

    var evaluator by remember { mutableStateOf("") }

    val evaluations = remember { mutableStateMapOf<String, ParameterEvaluation>() }

    val initialized = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        parameterViewModel.loadParameters()
    }

    LaunchedEffect(parameters) {
        if (!initialized.value && parameters.isNotEmpty()) {
            parameters.forEach { param ->
                evaluations.getOrPut(param._id) {
                    ParameterEvaluation(
                        parameterId = param._id,
                        cumple = false,
                        score = null,
                        notes = ""
                    )
                }
            }
            initialized.value = true
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 8.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Evaluacion de Software",
                    style = MaterialTheme.typography.headlineSmall
                )

                OutlinedTextField(
                    value = evaluator,
                    onValueChange = { evaluator = it },
                    label = { Text("Nombre del Evaluador") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Text(
                    text = "ID Proyecto: $softwareId",
                    style = MaterialTheme.typography.bodyMedium
                )

                Divider()

                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                } else {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        parameters.forEach { parameter ->
                            ParameterEvaluationItem(
                                parameter = parameter,
                                evaluation = evaluations[parameter._id] ?: ParameterEvaluation(
                                    parameterId = parameter._id,
                                    cumple = false,
                                    score = null,
                                    notes = ""
                                ),
                                onEvaluationChange = { updated ->
                                    evaluations[parameter._id] = updated
                                }
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancelar")
                    }
                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        onClick = {
                            val validEvaluations = evaluations.values.toList()

                            val averageScore = validEvaluations
                                .filter { it.score != null }
                                .map { it.score!! }
                                .average()
                                .takeIf { !it.isNaN() } ?: 0.0

                            onSubmit(
                                mapOf(
                                    "softwareId" to softwareId,
                                    "evaluator" to evaluator,
                                    "parameters" to validEvaluations.map { eval ->
                                        mapOf(
                                            "parameterId" to eval.parameterId,
                                            "cumple" to eval.cumple,
                                            "score" to eval.score,
                                            "notes" to eval.notes
                                        )
                                    },
                                    "overallScore" to averageScore
                                )
                            )
                        },
                        enabled = evaluator.isNotBlank() &&
                                evaluations.values.any { it.score != null }
                    ) {
                        Text("Enviar Evaluacion")
                    }
                }
            }
        }
    }
}

@Composable
private fun ParameterEvaluationItem(
    parameter: Parametro,
    evaluation: ParameterEvaluation,
    onEvaluationChange: (ParameterEvaluation) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = parameter.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Importancia: ${parameter.importance}",
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Text(
                text = parameter.description,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp)
            )

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Checkbox(
                    checked = evaluation.cumple,
                    onCheckedChange = {
                        onEvaluationChange(evaluation.copy(cumple = it))
                    },
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text("¿Cumple el parametro?")
            }

            OutlinedTextField(
                value = evaluation.score?.toString() ?: "",
                onValueChange = {
                    val score = it.toIntOrNull()?.coerceIn(0, 10)
                    onEvaluationChange(evaluation.copy(score = score))
                },
                label = { Text("Puntaje (0-10)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = evaluation.notes,
                onValueChange = {
                    onEvaluationChange(evaluation.copy(notes = it))
                },
                label = { Text("Comentario") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                maxLines = 2
            )
        }
    }
}