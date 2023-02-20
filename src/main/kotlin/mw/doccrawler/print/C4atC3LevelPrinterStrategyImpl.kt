package mw.doccrawler.print

import mw.doccrawler.KtClassItem
import mw.doccrawler.ModuleSet
import mw.doccrawler.Relation

class C4atC3LevelPrinterStrategyImpl : PrintStrategy {
    override fun print(module: ModuleSet): String {
        val str: StringBuilder = StringBuilder("Container_Boundary(c2,\"\"){\n")
        module.components.forEach { print(it,str) }
        str.append("}\n").toString()
        module.relations.forEach { print(it,str) }
        return str.toString()
    }

    fun print(component: KtClassItem, str: StringBuilder) {
        str.append("Component(${component.shortName}, \"${component.shortName}\", \"Kotlin\", \"\",\$sprite=\"GPU," + "scale=0.3\")\n")
    }

    fun print(relation: Relation, str: StringBuilder) {
        str.append("Rel(${relation.from.shortName},${relation.to.shortName},\"\")\n")
    }
}
