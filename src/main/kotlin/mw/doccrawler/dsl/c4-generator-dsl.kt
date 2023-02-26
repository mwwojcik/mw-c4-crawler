package mw.doccrawler.dsl

import java.nio.file.Path
import java.nio.file.Paths


fun generate(lambda: GeneratorContextBuilder.() -> Unit): GeneratorContext {
    return GeneratorContextBuilder().apply(lambda).build()
}


class GeneratorContextBuilder {
    val formats: MutableSet<String> = mutableSetOf()
    lateinit var to: Path
    lateinit var from: Path
    lateinit var contentContext: ContentContext

    fun to(lambda: GeneratorContextBuilder.() -> String) {
        to = Paths.get(lambda())
    }

    fun from(lambda: GeneratorContextBuilder.() -> String) {
        from = Paths.get(lambda())
    }

    fun withFormats(lambda: GeneratorContextBuilder.() -> String) {
        formats.addAll(lambda().split(","))
    }

    fun withContent(lambda: ContentContextBuilder.() -> Unit) {
        contentContext = ContentContextBuilder().apply(lambda).build()
    }

    fun build() = GeneratorContext(from, to, formats.toSet(), contentContext)
}

class ContentContextBuilder {
    val modules: MutableList<ModuleContext> = mutableListOf()
    val exclusions: MutableList<Condition> = mutableListOf()
    val componentMapper: MutableMap<String, Condition> = mutableMapOf()

    fun select(lambda: ContentContextBuilder.() -> Unit) {
        this.apply(lambda)
    }

    fun module(lambda: ModuleContextBuilder.() -> Unit) {
        modules.add(ModuleContextBuilder().apply(lambda).build())
    }

    fun withExclusions(lambda: ExclusionsContextBuilder.() -> Unit) {
        exclusions.addAll(ExclusionsContextBuilder().apply(lambda).build())
    }

    fun withComponentsMapping(lambda: ComponentsMappingContextBuilder.() -> Unit) {
        componentMapper.putAll(ComponentsMappingContextBuilder().apply(lambda).build())
    }

    fun build(): ContentContext = ContentContext(modules.toList(), exclusions, componentMapper)
}

class ModuleContextBuilder {
    var name: String = ""
    val conditions: MutableList<Condition> = mutableListOf()

    fun name(lambda: ModuleContextBuilder.() -> String) {
        name = lambda()
    }

    fun withRoots(lambda: ModuleContextBuilder.() -> Unit) {
        this.apply(lambda)
    }

    fun whenNameEquals(lambda: ModuleContextBuilder.() -> String) {
        conditions.add(ConditionEq(value = lambda()))
    }

    fun whenNameContains(lambda: ModuleContextBuilder.() -> String) {
        conditions.add(ConditionContains(value = lambda()))
    }

    fun build() = ModuleContext(name, conditions)

}


class ExclusionsContextBuilder {
    val conditions: MutableList<Condition> = mutableListOf()

    fun whenNameEquals(lambda: ExclusionsContextBuilder.() -> String) {
        conditions.add(ConditionEq(value = lambda()))
    }

    fun whenNameContains(lambda: ExclusionsContextBuilder.() -> String) {
        conditions.add(ConditionContains(value = lambda()))
    }

    fun build() = conditions.toList()
}

class ComponentsMappingContextBuilder {
    val componentMapper: MutableMap<String, Condition> = mutableMapOf()

    fun endpoint(lambda: ConditionContextBuilder.() -> Unit) {
        componentMapper.put("endpoint",ConditionContextBuilder().apply(lambda).build())
    }

    fun service(lambda: ConditionContextBuilder.() -> Unit) {
        componentMapper.put("service",ConditionContextBuilder().apply(lambda).build())
    }

    fun repository(lambda: ConditionContextBuilder.() -> Unit) {
        componentMapper.put("repository",ConditionContextBuilder().apply(lambda).build())
    }

    fun channel(lambda: ConditionContextBuilder.() -> Unit) {
        componentMapper.put("channel",ConditionContextBuilder().apply(lambda).build())
    }

    fun build() = componentMapper.toMap()
}

class ConditionContextBuilder {
    lateinit var condition: Condition

    fun whenNameEquals(lambda: ConditionContextBuilder.() -> String) {
        condition = ConditionEq(value = lambda())
    }

    fun whenNameContains(lambda: ConditionContextBuilder.() -> String) {
        condition = ConditionContains(value = lambda())
    }

    fun build() = condition
}

data class GeneratorContext(
    val from: Path,
    val to: Path,
    val formats: Set<String> = emptySet(),
    val contentContext: ContentContext
)

data class ContentContext(
    val modules: List<ModuleContext> = emptyList(),
    val exclusions: List<Condition>,
    val componentMapper: Map<String, Condition>
)

data class ModuleContext(
    val name: String = "",
    val roots: List<Condition> = emptyList()
)

interface Condition {
    val operator: String
    val value: String
}

data class ConditionEq(override val operator: String = "EQ", override val value: String = "") : Condition
data class ConditionContains(override val operator: String = "Contains", override val value: String = "") : Condition
