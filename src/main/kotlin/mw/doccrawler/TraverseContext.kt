package mw.doccrawler

import mw.doccrawler.print.C4atC3LevelPrinterStrategyImpl
import mw.doccrawler.print.PrintStrategy
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtParameter
import org.jetbrains.kotlin.psi.KtUserType
import org.jetbrains.kotlin.psi.psiUtil.getSuperNames


class TraverseContext(
    ktElements: List<KtClass> = emptyList(),
    val printStrategy: PrintStrategy = C4atC3LevelPrinterStrategyImpl()
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

    fun traverse():List<ModuleSet> =roots.map {
            visit(it, builder = ModuleBuilder(), visited = mutableSetOf()).withPrintStrategy(printStrategy).build()
        }.toList()

    private fun visit(
        node: KtClassItem,
        visited: MutableSet<String> = mutableSetOf(),
        root: KtClassItem? = null,
        builder: ModuleBuilder
    ) :ModuleBuilder {
        if (visited.contains(node.shortName))
            return builder
        root?.let { builder.withComponent(root) } ?: run { builder.withComponent(node) }
        visited.add(node.shortName)

        node.primaryConstructorDependencies()?.forEach {
            if (isReferenceType(it)) {
                extractReferenceName(it)?.let { typeShortName ->
                    resolveTypeByShortName(typeShortName)?.let { classItem ->
                        if (root == null) {
                            builder.withRelationBetween(node, classItem)
                        } else {
                            builder.withRelationBetween(root, classItem)
                        }
                        if (classItem.isInterface())
                            classItem.knownImplementations.forEach { item ->
                                visit(node = item, visited = visited, root = classItem, builder = builder)
                            }
                        else
                            visit(node = classItem, visited, builder = builder, root = null)
                    }
                }
            }
        }
        return builder
    }

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

    fun primaryConstructorDependencies() = ktClass.primaryConstructor?.valueParameterList?.parameters
}


class ModuleBuilder {
    private lateinit var printStrategy: PrintStrategy
    private val components: MutableSet<KtClassItem> = mutableSetOf()
    private val relations: MutableSet<Relation> = mutableSetOf()

    fun withComponent(item: KtClassItem) {
        components.add(item)
    }

    fun withRelationBetween(from: KtClassItem, to: KtClassItem) {
        relations.add(Relation(from, to))
    }

    fun withPrintStrategy(printStrategy: PrintStrategy): ModuleBuilder {
        this.printStrategy = printStrategy
        return this
    }

    fun build() = ModuleSet(components, relations, printStrategy)
}

data class Relation(val from: KtClassItem, val to: KtClassItem) {
    override fun toString(): String {
        return "Rel: ${from.shortName}==>${to.shortName}"
    }
}

data class ModuleSet(
    val components: MutableSet<KtClassItem> = mutableSetOf(),
    val relations: MutableSet<Relation> = mutableSetOf(),
    val printStrategy: PrintStrategy
) {
    fun prettyPrint() = printStrategy.print(this)
}

