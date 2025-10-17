package com.example.lessons.ui.student.uiComponents

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun InputField(value: String, error: String?, onValueChange: (field: String, value: String) -> Unit, title: String, icon: ImageVector, fieldName: String) {
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(fieldName, it) },
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
    )
    if (error != null) {
        Text(
            text = error,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall
        )
    }
}