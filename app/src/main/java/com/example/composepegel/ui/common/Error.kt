package com.example.composepegel.ui.common

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun DefaultError(error: String) {
    Text(text = error, modifier = Modifier.fillMaxSize(), textAlign = TextAlign.Center)
}

@Preview
@Composable
fun PreviewDefaultError() {
    DefaultError(error = "This is a test error")
}