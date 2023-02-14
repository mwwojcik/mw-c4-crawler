package mw.doccrawler

import org.jetbrains.kotlin.asJava.toLightElements
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtParameter
import org.jetbrains.kotlin.psi.KtParameterList
import org.jetbrains.kotlin.psi.KtPrimaryConstructor
import org.jetbrains.kotlin.psi.KtProperty
import org.jetbrains.kotlin.psi.KtTreeVisitorVoid
import org.jetbrains.kotlin.psi.KtTypeArgumentList
import org.jetbrains.kotlin.psi.KtTypeProjection
import org.jetbrains.kotlin.psi.KtTypeReference
import org.jetbrains.kotlin.psi.KtUserType
import org.jetbrains.kotlin.psi.KtValueArgumentList
import org.jetbrains.kotlin.resolve.BindingContextUtils
import org.jetbrains.kotlin.resolve.resolveQualifierAsReceiverInExpression


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


       roots.forEach { visit(it) }
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

        constructor.valueParameterList?.parameters?.forEach {
            if (it.typeReference?.typeElement is KtUserType) {
                val myRef=it.typeReference

                val typeShortName = (it.typeReference?.typeElement as KtUserType).referencedName
                println(typeShortName)
            }
        }
        super.visitPrimaryConstructor(constructor)
    }

    override fun visitParameterList(list: KtParameterList) {
        super.visitParameterList(list)
    }

    override fun visitTypeProjection(typeProjection: KtTypeProjection) {
        super.visitTypeProjection(typeProjection)
    }

    override fun visitValueArgumentList(list: KtValueArgumentList) {
        super.visitValueArgumentList(list)
    }

    override fun visitTypeArgumentList(typeArgumentList: KtTypeArgumentList) {
        super.visitTypeArgumentList(typeArgumentList)
    }

    override fun visitParameter(parameter: KtParameter) {
        super.visitParameter(parameter)
    }

    override fun visitUserType(type: KtUserType) {
        super.visitUserType(type)
    }

    override fun visitProperty(property: KtProperty) {
        super.visitProperty(property)
    }

    override fun visitTypeReference(typeReference: KtTypeReference) {
        super.visitTypeReference(typeReference)
        typeReference.toLightElements()
    }
}
