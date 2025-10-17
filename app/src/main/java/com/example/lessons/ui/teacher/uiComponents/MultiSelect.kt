package com.example.lessons.ui.teacher.uiComponents

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MultiSelect(
    enum: Map<String, String>,
    title: String,
    error: String?,
    onClickUpdate: (fieldName: String, key: Map<String, String>) -> Unit,
    fieldName: String,
    currentValues: Map<String, String>
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedValues by remember { mutableStateOf(currentValues) }

    LaunchedEffect(currentValues) {
        selectedValues = currentValues
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
    ) {
        OutlinedTextField(
            value = selectedValues.values.joinToString(", "),
            onValueChange = {},
            label = { Text(title) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
                .padding(vertical = 10.dp),
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            enum.forEach { (key, value) ->
                AnimatedContent(
                    targetState = selectedValues.contains(key),
                    label = "Subjects"
                ) { isSelected ->
                    DropdownMenuItem(
                        text = { Text(value) },
                        onClick = {
                            selectedValues = if (isSelected) {
                                selectedValues - key // Remove selection
                            } else {
                                selectedValues + (key to value) // Add selection
                            }
                            onClickUpdate(fieldName, selectedValues)
                        },
                        leadingIcon = {
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Rounded.Check,
                                    contentDescription = null,
                                    tint = Color.Green
                                )
                            }
                        }
                    )
                }
            }
        }
    }
    if (error != null) {
        Text(
            text = error,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall
        )
    }
}