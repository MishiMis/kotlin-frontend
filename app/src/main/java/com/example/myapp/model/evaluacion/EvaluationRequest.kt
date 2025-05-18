package com.example.myapp.model.evaluacion

import com.google.gson.annotations.SerializedName

data class EvaluationRequest(
    @SerializedName("softwareId") val softwareId: String,
    @SerializedName("evaluator") val evaluator: String,
    @SerializedName("parameters") val parameters: List<ParameterEvaluation>,
    @SerializedName("overallScore") val overallScore: Double
) {
    data class ParameterEvaluation(
        @SerializedName("parameterId") val parameterId: String,
        @SerializedName("cumple") val cumple: Boolean,
        @SerializedName("score") val score: Int?,
        @SerializedName("notes") val notes: String
    )
}