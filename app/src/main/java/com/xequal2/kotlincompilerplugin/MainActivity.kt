package com.xequal2.kotlincompilerplugin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import com.xequal2.kotlincompilerplugin.ui.theme.KotlinCompilerPluginTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KotlinCompilerPluginTheme {
                TodoApp()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TodoPreview() {
    KotlinCompilerPluginTheme {
        TodoApp()
    }
}