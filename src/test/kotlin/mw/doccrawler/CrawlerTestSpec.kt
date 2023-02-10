package mw.doccrawler

import io.kotest.core.spec.style.FunSpec
import org.jetbrains.kotlin.cli.jvm.compiler.EnvironmentConfigFiles
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.jetbrains.kotlin.com.intellij.openapi.util.Disposer
import org.jetbrains.kotlin.com.intellij.psi.PsiElementFactory
import org.jetbrains.kotlin.com.intellij.psi.PsiManager
import org.jetbrains.kotlin.com.intellij.testFramework.LightVirtualFile
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.idea.KotlinFileType
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.psiUtil.getChildrenOfType
import org.springframework.util.ResourceUtils
import java.nio.file.Files

class CrawlerTestSpec : FunSpec({
    test("my first test") {
        val item0=object{}.javaClass.getResourceAsStream("Circle.kt")?.bufferedReader()?.readLines()
        val item=KtClassFixture().createKtClass("class Circle{}")
        val circle= ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "Circle.kt")
        val circlekt=KtClassFixture().createKtFile(Files.readString(circle.toPath()), circle.name)
        val context=TraverseContext(circlekt.getChildrenOfType<KtClass>().asList())
        val elements: MutableList<String> = mutableListOf()

        elements.add("hello")
        println()
    }
})

class KtClassFixture {
    private val project =
        KotlinCoreEnvironment.createForProduction(
            Disposer.newDisposable(),
            CompilerConfiguration(),
            EnvironmentConfigFiles.JVM_CONFIG_FILES //Can be JS/NATIVE_CONFIG_FILES for non JVM projects
        ).project


    fun createKtFile(codeString: String, fileName: String) =
        PsiManager.getInstance(project)
            .findFile(
                LightVirtualFile(fileName, KotlinFileType.INSTANCE, codeString)
            ) as KtFile

    fun createKtClass(content: String) {
        PsiElementFactory.getInstance(project).createClassFromText(content, null)
    }
}
