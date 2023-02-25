package mw.doccrawler

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import mw.doccrawler.fixture.KtClassFixture
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.psiUtil.getChildrenOfType
import org.springframework.util.ResourceUtils
import java.nio.file.Files

class TraverseContextTest : FunSpec({
    test("my first test") {
     val workspace=KtClassFixture().createWorkspaceWithKtClasses()
        println(workspace)
    }

})
