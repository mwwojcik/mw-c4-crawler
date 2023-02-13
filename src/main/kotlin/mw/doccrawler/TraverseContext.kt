package mw.doccrawler

import org.jetbrains.kotlin.psi.KtClass

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
        println()
    }
}
