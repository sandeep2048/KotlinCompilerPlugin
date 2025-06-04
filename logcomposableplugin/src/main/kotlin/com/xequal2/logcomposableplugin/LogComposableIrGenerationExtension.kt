package com.xequal2.logcomposableplugin

import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.common.ir.createIrBuilder
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.builders.irCall
import org.jetbrains.kotlin.ir.builders.irString
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.expressions.IrBlockBody
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrConstKind
import org.jetbrains.kotlin.ir.expressions.IrConst
import org.jetbrains.kotlin.ir.util.fqNameWhenAvailable
import org.jetbrains.kotlin.ir.visitors.IrElementTransformerVoid
import org.jetbrains.kotlin.ir.visitors.transformChildrenVoid
import org.jetbrains.kotlin.name.FqName

class LogComposableIrGenerationExtension : IrGenerationExtension {
    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        val logSymbol = pluginContext.referenceFunctions(FqName("android.util.Log.d")).singleOrNull()
        if (logSymbol == null) return
        moduleFragment.transformChildrenVoid(object : IrElementTransformerVoid() {
            override fun visitFunctionNew(declaration: IrFunction): IrStatement {
                declaration.transformChildrenVoid(this)
                if (declaration.annotations.any { it.symbol.owner.fqNameWhenAvailable?.asString() == "androidx.compose.runtime.Composable" }) {
                    val body = declaration.body as? IrBlockBody ?: return declaration
                    val first = body.statements.firstOrNull()
                    if (!first.isLogStatement(logSymbol)) {
                        val builder = pluginContext.createIrBuilder(declaration.symbol)
                        val logCall = builder.irCall(logSymbol).apply {
                            putValueArgument(0, builder.irString("TAG"))
                            putValueArgument(1, builder.irString("Hello"))
                        }
                        body.statements.add(0, logCall)
                    }
                }
                return declaration
            }

            private fun IrStatement?.isLogStatement(symbol: org.jetbrains.kotlin.ir.symbols.IrSymbol): Boolean {
                val call = this as? IrCall ?: return false
                if (call.symbol != symbol) return false
                val arg0 = call.getValueArgument(0) as? IrConst<*>
                val arg1 = call.getValueArgument(1) as? IrConst<*>
                return arg0?.kind == IrConstKind.String && arg0.value == "TAG" &&
                        arg1?.kind == IrConstKind.String && arg1.value == "Hello"
            }
        })
    }
}
