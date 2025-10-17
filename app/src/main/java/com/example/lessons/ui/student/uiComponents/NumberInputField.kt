package com.example.lessons.ui.student.uiComponents

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun NumberInputField(
    value: String,
    error: String?,
    onValueChange: (field: String, value: String) -> Unit,
    title: String,
    icon: ImageVector,
    fieldName: String
) {
    OutlinedTextField(
        value = value,
        onValueChange = { newText ->
            if (newText.all { it.isDigit() }) {
                onValueChange(fieldName, newText)
            }
        },
        label = { Text(title) },
        isError = error != null,
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = title
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
    if (error != null) {
        Text(
            text = error,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall
        )
    }
}