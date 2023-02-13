package mw.doccrawler

import org.jetbrains.kotlin.com.intellij.psi.JavaRecursiveElementVisitor
import org.jetbrains.kotlin.com.intellij.psi.PsiField
import org.jetbrains.kotlin.com.intellij.psi.PsiLocalVariable
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtPrimaryConstructor
import org.jetbrains.kotlin.psi.KtProperty
import org.jetbrains.kotlin.psi.KtTreeVisitor
import org.jetbrains.kotlin.psi.KtTreeVisitorVoid
import org.jetbrains.kotlin.psi.KtUserType


class TraverseContext(
    val ktElements: List<KtClass> = emptyList(),
    val exclusions: List<String> = emptyList()
) {
    var elements: Map<String, KtClass> = emptyMap()
    var roots: List<KtClass> = emptyList()

    init {
        elements = ktElements.filter { psi -> exclusions.none { psi.fqName?.asString()?.contains(it) ?: true } }
            .map { it.fqName!!.asString()!! to it }
            .toMap()

        roots=elements.filterKeys { it.contains("Endpoint") }.values.toList()
    }

    fun traverse(){

        roots.get(0).accept(MyKtVisitor())


      // roots.forEach { visit(it) }
    }

    private fun visit(it: KtClass) {
        println("visited=>${it.name}")
        //it.getChildrenOfType<KtClass>().asList().forEach { visit(it) }
        it.primaryConstructor?.typeParameters?.forEach{
            child->
            println(child)
        }
    }
}

class MyKtVisitor:KtTreeVisitorVoid(){
    override fun visitPrimaryConstructor(constructor: KtPrimaryConstructor) {
        println()
        super.visitPrimaryConstructor(constructor)
    }

    override fun visitUserType(type: KtUserType) {
        super.visitUserType(type)
    }

    override fun visitProperty(property: KtProperty) {
        super.visitProperty(property)
    }
}
