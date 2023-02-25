package mw.doccrawler

import jakarta.annotation.PostConstruct
import mw.doccrawler.shapes.defaultShapesConfiguration
import org.jetbrains.kotlin.cli.jvm.compiler.EnvironmentConfigFiles
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.jetbrains.kotlin.com.intellij.openapi.util.Disposer
import org.jetbrains.kotlin.com.intellij.psi.PsiManager
import org.jetbrains.kotlin.com.intellij.testFramework.LightVirtualFile
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.idea.KotlinFileType
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.psiUtil.getChildrenOfType
import org.springframework.stereotype.Component
import java.nio.file.Files
import java.nio.file.Paths

@Component
class Crawler {
    private val project by lazy {
        KotlinCoreEnvironment.createForProduction(
            Disposer.newDisposable(),
            CompilerConfiguration(),
            EnvironmentConfigFiles.JVM_CONFIG_FILES //Can be JS/NATIVE_CONFIG_FILES for non JVM projects
        ).project
    }

    fun createKtFile(codeString: String, fileName: String) =
        PsiManager.getInstance(project)
            .findFile(
                LightVirtualFile(fileName, KotlinFileType.INSTANCE, codeString)
            ) as KtFile

    @PostConstruct
    fun process() {
        val config = defaultShapesConfiguration

        val psi = Files.walk(config.contentContext.sources)
            .filter { Files.isRegularFile(it) }
            .map {
                createKtFile(Files.readString(it), it.toString()).getChildrenOfType<KtClass>()
            }
            .toList()
        val content:StringBuilder = java.lang.StringBuilder("")
        TraverseContext(psi.flatMap { it.asList() }).traverse().forEach {
            content.append(it.prettyPrint())
        }
        Files.writeString(config.to.resolve("content.puml"), content.toString())
    }
}
