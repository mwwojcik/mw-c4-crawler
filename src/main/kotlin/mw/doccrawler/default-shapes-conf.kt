package mw.doccrawler.shapes

import mw.doccrawler.dsl.generate

val defaultShapesConfiguration = generate {
    to { "doc" }
    withFormats { "png,puml" }
    withContent {
        select {
            module {
                name { "Shapes" }
                withRoots {
                    whenNameEquals { "eq" }
                    whenNameContains { "const" }
                }
            }
            module {
                name { "Circles" }
                withRoots {
                    whenNameEquals { "eq" }
                    whenNameContains { "const" }
                }
            }
            module {
                name { "Rectangles" }
                withRoots {
                    whenNameEquals { "eq" }
                    whenNameContains { "const" }
                }
            }
        }
        fromSources { "src/main/kotlin/domain" }
        withExclusions {
            whenNameEquals { "eq exclusion" }
            whenNameContains { "const exclusion" }
        }
        withComponentsMapping {
            endpoint {
                whenNameContains {"Endpoint"}
            }
            service {
                whenNameContains{"Service"}
            }
            repository {
                whenNameContains{"repository"}
            }
            channel {
                whenNameContains{"channel"}
            }
        }

    }
}

fun main() {
    println(defaultShapesConfiguration)
}
