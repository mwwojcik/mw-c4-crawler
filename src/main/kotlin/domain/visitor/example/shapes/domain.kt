package domain.visitor.example.shapes

interface ShapeStorageProvider{
    fun save(shape: Shape)
}

interface ShapeEventPublisher{
    fun publish(shape: Shape)
}


