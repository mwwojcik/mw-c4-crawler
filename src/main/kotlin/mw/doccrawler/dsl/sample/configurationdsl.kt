//https://www.baeldung.com/kotlin/dsl
import java.nio.file.Path
import java.nio.file.Paths


val config = specification {
    sources = path("src/main/kotlin/domain")
    module {
        name = "Shapes"
        roots { listOf("Shape", "Endpoint") }
    }
    exclusions { listOf("util", "dispatcher","prop") }
}

fun main() {
    println(config)
}

@DslMarker
annotation class ConfigDSLMarker

//should return ProjectSpecification
@ConfigDSLMarker
fun specification(init: ProjectSpecification.() -> Unit): ProjectSpecification {
    val spec = ProjectSpecification()
    init(spec)
    return spec
}


@ConfigDSLMarker
data class ProjectSpecification(
    var sources: Path = Path.of("."),
    val modules: MutableSet<ModuleSpecification> = mutableSetOf(),
    val exclusions: MutableSet<String> = mutableSetOf()
) {
    @ConfigDSLMarker
    fun path(pathStr: String) = Paths.get(pathStr)

    //converts lambda to object
    @ConfigDSLMarker
    fun module(lambda: ModuleSpecification.() -> Unit) {
        val moduleSpec = ModuleSpecification()
        //function with receiver is compiled to regular function
        // with receiver as the first argument
        lambda(moduleSpec)
        modules.add(moduleSpec)
    }

    fun exclusions(lambda: () -> List<String>) {
        exclusions.addAll(lambda())
    }
}

@ConfigDSLMarker
data class ModuleSpecification(var name: String = "", var roots: MutableSet<String> = mutableSetOf()) {

    @ConfigDSLMarker
    fun roots(lambda: () -> List<String>) {
        roots.addAll(lambda())
    }
}
