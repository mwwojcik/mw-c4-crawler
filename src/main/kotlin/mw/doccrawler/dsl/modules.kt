generate{
    to{"doc"}
    withFormat{"png","puml"}
    withContent {
        select {
            module {
                name = "Shapes"
                withRootsWhen {
                    nameEquals("")
                    nameContains("")
                }
            }
            module {
                name = "Circles"
                withRootsWhen {
                    nameEquals("")
                    nameContains("")
                }
            }
            module {
                name = "Rectangles"
                withRootsWhen {
                    nameEquals("")
                    nameContains("")
                }
            }
        }
        withComponents{
            endpoint{
                nameContains("")
            }
            service{
                nameContains("")
            }
            repository{
                nameContains("")
            }
            channel{
                nameContains("")
            }
        }
        fromSources { "src/main/kotlin/domain" }
        withExclusionsWhen {
            nemeEquals("")
            nameContains("")
        }
    }
}


