package mw.doccrawler.shapes

import mw.doccrawler.dsl.generate

val defaultShapesConfiguration = generate {
    from { "src/main/kotlin/domain" }
    to { "doc" }
    withFormats { "png,puml" }
    withContent {
            module {
                name { "Shapes" }
                withRoots {
                    include{ "in-1" }
                    include{ "in-2" }
                    include{ "in-3" }
                    exclude{ "ex-1" }
                    exclude{ "ex-2" }
                }
                withComponents {
                    include { "in-1" }
                    exclude { "ex-2" }
                }
            }

        module {
            name { "Circles" }
            withRoots {
                include { "in-test-2" }
                exclude { "ex-2" }
            }
            withComponents {
                include { "in-2" }
                exclude { "ex-2" }
            }

        }
        module {
            name { "Rectangles" }
            withRoots {
                include { "in-2" }
                exclude { "ex-2" }
            }
            withComponents {
                include { "in-2" }
                exclude { "ex-2" }
            }

        }
        withComponentsMapping {
            endpoint {
                include { "in-2" }
                exclude { "ex-2" }
            }
            service {
                include { "in-2" }
                exclude { "ex-2" }
            }
            repository {
                include { "in-2" }
                exclude { "ex-2" }
            }
            channel {
                include { "in-2" }
                exclude { "ex-2" }
                exclude { "ex-3" }
            }
        }
    }
}

fun main() {
    println(defaultShapesConfiguration)
}
