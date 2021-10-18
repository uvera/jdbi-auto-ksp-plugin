package io.uvera.jdbiauto

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeSpec
import org.jdbi.v3.core.Jdbi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Modifier
import javax.lang.model.element.PackageElement
import javax.lang.model.element.TypeElement

class JavaJdbiAutoProcessor : AbstractProcessor() {
    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf("io.uvera.jdbiauto.JdbiAuto")
    }

    private fun processType(typeElement: TypeElement) {
        val typeSpecBuilder = TypeSpec.classBuilder("${typeElement.simpleName}AutoJdbiConfiguration")

        val constructorSpec = MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PUBLIC)
            .addParameter(Jdbi::class.java, "jdbi")
            .addStatement("this.\$N = \$N", "jdbi", "jdbi")
            .build()

        with(typeSpecBuilder) {
            addAnnotation(Configuration::class.java)
            addModifiers(Modifier.PUBLIC)
            addField(Jdbi::class.java, "jdbi", Modifier.PRIVATE, Modifier.FINAL)
            addMethod(constructorSpec)
            addMethod(
                MethodSpec.methodBuilder("jdbiAutoBean${typeElement.simpleName}")
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Bean::class.java)
                    .returns(ClassName.get(typeElement.asType()))
                    .addStatement("return this.jdbi.onDemand(${typeElement.simpleName}::class)")
                    .build()
            )
        }
        val javaFileBuilder =
            JavaFile.builder((typeElement.enclosingElement as PackageElement).qualifiedName.toString(),
                typeSpecBuilder.build())
        javaFileBuilder.skipJavaLangImports(true).build().writeTo(processingEnv.filer)
    }

    override fun process(annotations: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment?): Boolean {
        val elements = roundEnv!!.getElementsAnnotatedWith(JdbiAuto::class.java)
        elements.filterIsInstance<TypeElement>()
            .map { processType(it) }
        return true
    }

}
