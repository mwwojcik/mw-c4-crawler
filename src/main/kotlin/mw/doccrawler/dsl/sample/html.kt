package mw.doccrawler.dsl.sample

val html = html {
    head {
        title = "My website"
        script(body = "Some script body")
        script(url = "Some script url")
    }
    body {
        h1("Title")
        +"Text 1"
        h3("Subtitle")
        +"Text 2"
    }
}

fun main() {
    println(html)
}

@DslMarker
annotation class HtmlDsl

@HtmlDsl
fun html(init: HtmlBuilder.() -> Unit): HtmlBuilder {
    val builder = HtmlBuilder()
    builder.init()
    return builder
}

@HtmlDsl
data class HtmlBuilder(
    var head: HeadBuilder? = null,
    var body: BodyBuilder? = null,
) {
    @HtmlDsl
    fun head(init: HeadBuilder.() -> Unit) {
        val builder = HeadBuilder()
        init(builder)
        this.head = builder
    }
    @HtmlDsl
    fun body(init: BodyBuilder.() -> Unit) {
        val builder = BodyBuilder()
        init.invoke(builder)
        this.body = builder
    }
}

@HtmlDsl
data class HeadBuilder(
    var title: String = "",
    var scripts: List<Script> = emptyList()
) {
    @HtmlDsl
    fun script(body: String? = null, url: String? = null) {
        scripts += Script(body, url)
    }

    data class Script(val body: String?, val url: String?)
}

@HtmlDsl
data class BodyBuilder(
    var elements: List<BodyElement> = emptyList()
) {
    @HtmlDsl
    fun h1(text: String) {
        elements += H1(text)
    }

    @HtmlDsl
    fun h3(text: String) {
        elements += H3(text)
    }

    operator fun String.unaryPlus() {
        elements += Text(this)
    }

    sealed interface BodyElement
    data class H1(val text: String): BodyElement
    data class H3(val text: String): BodyElement
    data class Text(val text: String): BodyElement
}
