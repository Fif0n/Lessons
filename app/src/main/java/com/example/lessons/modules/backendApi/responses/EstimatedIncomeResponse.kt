package com.example.lessons.modules.backendApi.responses

data class EstimatedIncomeResponse(
    val status: String,
    val data: EstimatedIncomeResponseResult
)

data class EstimatedIncomeResponseResult(
    val results: EstimatedIncomeResponseResultContent
)

data class EstimatedIncomeResponseResultContent(
    val currentIncome: Int,
    val futureIncome: Int,
    val estimatedIncome: Int,
)
