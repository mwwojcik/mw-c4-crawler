package mw.doccrawler

import io.kotest.core.spec.style.ShouldSpec
import mw.doccrawler.fixture.KtClassFixture

class TraverseContextTest : ShouldSpec({

    val workspace=KtClassFixture().createWorkspaceWithKtClasses()

    beforeTest{
        println("runningTest =>$it")
    }

    should("my first test") {
        println(workspace)
    }

    should("my second test"){

    }

})
