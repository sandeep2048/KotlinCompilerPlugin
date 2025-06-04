package com.xequal2.logcomposableplugin

import org.jetbrains.kotlin.backend.common.extensions.FirIncompatiblePluginAPI
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.builders.IrBlockBodyBuilder
import org.jetbrains.kotlin.ir.builders.Scope
import org.jetbrains.kotlin.ir.builders.irCall
import org.jetbrains.kotlin.ir.builders.irString
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.expressions.IrBlockBody
import org.jetbrains.kotlin.ir.visitors.IrElementTransformerVoid
import org.jetbrains.kotlin.ir.visitors.transformChildrenVoid
import org.jetbrains.kotlin.name.FqName

class LogComposableIrGenerationExtension : IrGenerationExtension {
    @OptIn(FirIncompatiblePluginAPI::class)
    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        // Print at build time to confirm plugin is being loaded
        println("PLUGIN: generate called")

        val printlnSymbol = pluginContext.referenceFunctions(FqName("kotlin.io.println")).singleOrNull()
            ?: run {
                println("PLUGIN ERROR: println not found")
                return
            }

        moduleFragment.transformChildrenVoid(object : IrElementTransformerVoid() {
            override fun visitFunction(declaration: IrFunction): IrFunction {
                declaration.transformChildrenVoid(this)
                val body = declaration.body as? IrBlockBody ?: return declaration

                val scope = Scope(declaration.symbol)
                val logCall = IrBlockBodyBuilder(
                    pluginContext,
                    scope,
                    declaration.startOffset,
                    declaration.endOffset
                ).run {
                    irCall(printlnSymbol).apply {
                        putValueArgument(0, irString("INJECTED BY PLUGIN"))
                    }
                }
                body.statements.add(0, logCall)
                return declaration
            }
        })
    }
}
