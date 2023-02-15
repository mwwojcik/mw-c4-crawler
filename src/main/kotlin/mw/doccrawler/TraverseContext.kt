package mw.doccrawler

import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtParameter
import org.jetbrains.kotlin.psi.KtUserType
import org.jetbrains.kotlin.psi.psiUtil.getSuperNames


class TraverseContext(
    ktElements: List<KtClass> = emptyList(),
    exclusions: List<String> = emptyList()
) {
    var elements: List<KtClassItem> = emptyList()
    var roots: List<KtClassItem> = emptyList()

    init {
        val implementations: MutableMap<String, MutableSet<KtClassItem>> = mutableMapOf()
        elements = ktElements.map { KtClassItem(it.name!!, it.fqName!!.asString(), it) }.toList()
        elements.forEach { collectSupertypeRelations(it, implementations) }
        elements = elements.map {
            if (it.isInterface()) {
                implementations[it.shortName]?.let { it1 -> it.copy(knownImplementations = it1) } ?: it
            } else {
                it
            }
        }.toList()
        roots = elements.filter { it.shortName.contains("Endpoint") }.toList()
    }

    fun traverse() {
        roots.forEach { visit(it) }
    }

    private fun visit(it: KtClassItem) {
        println("visited=>${it.ktClass.name}")
        it.ktClass.primaryConstructor?.valueParameterList?.parameters?.forEach {
            if (isReferenceType(it)) {
                extractReferenceName(it)?.let { typeShortName ->
                    resolveTypeByShortName(typeShortName)?.let { classItem ->
                        extractItemsToVisit(classItem).forEach { item -> visit(item) }
                    }
                }
            }
        }
    }

    private fun extractItemsToVisit(classItem: KtClassItem) =
        if (classItem.isInterface())
            classItem.knownImplementations
        else
            setOf(classItem)


    private fun isReferenceType(it: KtParameter) = it.typeReference?.typeElement is KtUserType

    private fun extractReferenceName(it: KtParameter) = (it.typeReference?.typeElement as KtUserType).referencedName

    private fun collectSupertypeRelations(
        ktItem: KtClassItem,
        implementations: MutableMap<String, MutableSet<KtClassItem>>
    ) {
        if (ktItem.ktClass.getSuperNames().isNullOrEmpty())
            return
        ktItem.ktClass.getSuperNames().forEach {
            if (resolveTypeByShortName(it)?.ktClass?.isInterface() == true)
                implementations.computeIfAbsent(it) { mutableSetOf() }.add(ktItem)
        }
    }

    fun resolveTypeByShortName(shortName: String): KtClassItem? {
        return elements.firstOrNull { it.shortName == shortName }
    }
}


data class KtClassItem(
    val shortName: String,
    val fullName: String,
    val ktClass: KtClass,
    var knownImplementations: Set<KtClassItem> = emptySet()
) {
    fun isInterface() = ktClass.isInterface()
}
