package mw.doccrawler.dsl

import java.nio.file.Path
import java.nio.file.Paths

@DslMarker
annotation class DocCrawlerDsl

fun generate(lambda: GeneratorContextBuilder.() -> Unit): GeneratorContext {
    return GeneratorContextBuilder().apply(lambda).build()
}

@DocCrawlerDsl
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

@DocCrawlerDsl
class ContentContextBuilder {
    val modules: MutableList<ModuleContext> = mutableListOf()
    val componentMapper: MutableMap<String, SingleComponentMapperContext> = mutableMapOf()

    fun module(lambda: ModuleContextBuilder.() -> Unit) {
        modules.add(ModuleContextBuilder().apply(lambda).build())
    }

    fun withComponentsMapping(lambda: ComponentsMappingContextBuilder.() -> Unit) {
        componentMapper.putAll(ComponentsMappingContextBuilder().apply(lambda).build())
    }

    fun build(): ContentContext = ContentContext(modules.toList(), componentMapper)
}

@DocCrawlerDsl
class ModuleContextBuilder {
    private var name: String = ""
    private lateinit var rootContext: RootContext
    private lateinit var componentsContext: ComponentsContext

    fun name(lambda: ModuleContextBuilder.() -> String) {
        name = lambda()
    }

    fun withRoots(lambda: RootContextBuilder.() -> Unit) {
        rootContext = RootContextBuilder().apply(lambda).build()
    }

    fun withComponents(lambda: ComponentsContextBuilder.() -> Unit) {
        componentsContext = ComponentsContextBuilder().apply(lambda).build()
    }

    fun build() = ModuleContext(name, rootContext = rootContext, componentsContext = componentsContext)

}

@DocCrawlerDsl
class RootContextBuilder {
    private val excl: MutableList<Condition> = mutableListOf()
    private val incl: MutableList<Condition> = mutableListOf()

    fun include(lambda: RootContextBuilder.() -> String) {
        incl.add(Condition(lambda()))
    }

    fun exclude(lambda: RootContextBuilder.() -> String) {
        excl.add(Condition(lambda()))
    }

    fun build() = RootContext(inclusions = incl.toList(), exclusions = excl.toList())
}

@DocCrawlerDsl
class ComponentsContextBuilder {
    private val excl: MutableList<Condition> = mutableListOf()
    private val incl: MutableList<Condition> = mutableListOf()

    fun include(lambda: ComponentsContextBuilder.() -> String) {
        incl.add(Condition(lambda()))
    }

    fun exclude(lambda: ComponentsContextBuilder.() -> String) {
        excl.add(Condition(lambda()))
    }

    fun build() = ComponentsContext(inclusions = incl.toList(), exclusions = excl.toList())
}


class ComponentsMappingContextBuilder {
    val componentMapper: MutableMap<String, SingleComponentMapperContext> = mutableMapOf()

    fun endpoint(lambda: SingleComponentMapperContextBuilder.() -> Unit) {
        componentMapper.put("endpoint", SingleComponentMapperContextBuilder("endpoint").apply(lambda).build())
    }

    fun service(lambda: SingleComponentMapperContextBuilder.() -> Unit) {
        componentMapper.put("service", SingleComponentMapperContextBuilder("service").apply(lambda).build())
    }

    fun repository(lambda: SingleComponentMapperContextBuilder.() -> Unit) {
        componentMapper.put("repository", SingleComponentMapperContextBuilder("repository").apply(lambda).build())
    }

    fun channel(lambda: SingleComponentMapperContextBuilder.() -> Unit) {
        componentMapper.put("channel", SingleComponentMapperContextBuilder("channel").apply(lambda).build())
    }

    fun build() = componentMapper.toMap()
}

class SingleComponentMapperContextBuilder(val name: String) {
    private val exclude: MutableList<Condition> = mutableListOf()
    private val include: MutableList<Condition> = mutableListOf()

    fun include(lambda: SingleComponentMapperContextBuilder.() -> String) {
        include.add(Condition(lambda()))
    }

    fun exclude(lambda: SingleComponentMapperContextBuilder.() -> String) {
        exclude.add(Condition(lambda()))
    }

    fun build() =
        SingleComponentMapperContext(name = name, inclusions = include.toList(), exclusions = exclude.toList())
}

data class GeneratorContext(
    val from: Path,
    val to: Path,
    val formats: Set<String> = emptySet(),
    val contentContext: ContentContext
)

data class ContentContext(
    val modules: List<ModuleContext> = emptyList(),
    val componentMapper: Map<String, SingleComponentMapperContext>
)

data class ModuleContext(
    val name: String = "",
    val rootContext: RootContext? = null,
    val componentsContext: ComponentsContext? = null
)

data class Condition(
    val value: String
)

data class RootContext(val inclusions: List<Condition>, val exclusions: List<Condition>)

data class ComponentsContext(val inclusions: List<Condition>, val exclusions: List<Condition>)

data class SingleComponentMapperContext(
    val name: String,
    val inclusions: List<Condition>,
    val exclusions: List<Condition>
)
