package io.uvera.jdbiauto

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.google.devtools.ksp.validate
import org.jdbi.v3.core.Jdbi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.OutputStream

fun OutputStream.appendText(str: String) {
    this.write(str.toByteArray())
}

class JdbiAutoProcessor(
    val codeGenerator: CodeGenerator,
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation("io.uvera.jdbiauto.JdbiAuto")
        val ret = symbols.filter { !it.validate() }.toList()
        symbols.filter {
            it is KSClassDeclaration && it.classKind == ClassKind.INTERFACE && it.validate()
        }.forEach {
            it.accept(JdbiAutoVisitor(), Unit)
        }
        return ret
    }

    inner class JdbiAutoVisitor : KSVisitorVoid() {
        override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
            val packageName = classDeclaration.containingFile!!.packageName.asString()
            val classDeclarationClassName = classDeclaration.simpleName.asString()
            val className = "${classDeclaration.simpleName.asString()}AutoJdbiConfiguration"
            val file = codeGenerator.createNewFile(
                Dependencies(true, classDeclaration.containingFile!!), packageName, className)
            file.appendText("package $packageName\n\n")
            file.appendText("import ${Jdbi::class.qualifiedName}\n")
            file.appendText("import ${Configuration::class.qualifiedName}\n")
            file.appendText("import ${Bean::class.qualifiedName}\n")
            file.appendText("import org.jdbi.v3.sqlobject.kotlin.onDemand\n\n")
            file.appendText("@Configuration\n")
            file.appendText("class $className(private val jdbi: Jdbi) {\n\n")
            file.appendText("    @Bean\n")
            file.appendText("    fun jdbiAutoBean${classDeclarationClassName}(): $classDeclarationClassName = jdbi.onDemand()\n")
            file.appendText("}\n")
            file.close()
        }
    }
}

class JdbiAutoProcessorProvider : SymbolProcessorProvider {
    override fun create(
        environment: SymbolProcessorEnvironment,
    ): SymbolProcessor {
        return JdbiAutoProcessor(environment.codeGenerator)
    }
}
