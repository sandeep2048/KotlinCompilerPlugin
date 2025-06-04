package com.xequal2.logcomposableplugin

import org.jetbrains.kotlin.compiler.plugin.CompilerPluginRegistrar
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import com.xequal2.logcomposableplugin.LogComposableIrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension

@OptIn(ExperimentalCompilerApi::class)
class LogComposablePluginRegistrar : CompilerPluginRegistrar() {
    override val supportsK2: Boolean = true

    override fun ExtensionStorage.registerExtensions(configuration: org.jetbrains.kotlin.config.CompilerConfiguration) {
        IrGenerationExtension.registerExtension(LogComposableIrGenerationExtension())
    }
}
