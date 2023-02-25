package mw.doccrawler.fixture

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

class KtClassFixture {

    private val filepaths = listOf(
        "shapes/ShapeRepository.kt",
        "shapes/Shape.kt",
        "shapes/ShapeEndpoint.kt",
        "shapes/ShapeServiceImpl.kt",
        "shapes/ShapeService.kt",
        "shapes/domain.kt",
        "shapes/ShapeAdapter.kt",
        "shapes/external/rabbit/ShapeEventPublisherRabbitAdapter.kt",
        "shapes/external/rabbit/RabbitClient.kt",
        "shapes/external/mongo/MongoClient.kt",
        "shapes/external/mongo/ShapeStorageMongoAdapter.kt"
    )

    private val project =
        KotlinCoreEnvironment.createForProduction(
            Disposer.newDisposable(),
            CompilerConfiguration(),
            EnvironmentConfigFiles.JVM_CONFIG_FILES //Can be JS/NATIVE_CONFIG_FILES for non JVM projects
        ).project


    private fun createKtFile(codeString: String, fileName: String) =
        PsiManager.getInstance(project)
            .findFile(
                LightVirtualFile(fileName, KotlinFileType.INSTANCE, codeString)
            ) as KtFile

    private fun createKtClass(content: String) {
        PsiElementFactory.getInstance(project).createClassFromText(content, null)
    }

    fun createWorkspaceWithKtClasses(): List<KtClass> {
        val workspace = filepaths
            .map {
                val fileResource = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + it)
                val ktFile = KtClassFixture().createKtFile(Files.readString(fileResource.toPath()), fileResource.name)
                ktFile.getChildrenOfType<KtClass>().asList()
            }
            .flatMap { it }
            .toList()
        return workspace
    }
}

