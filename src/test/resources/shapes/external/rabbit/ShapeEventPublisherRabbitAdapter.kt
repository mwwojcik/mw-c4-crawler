package shapes.external.rabbit

import domain.visitor.example.shapes.Shape
import domain.visitor.example.shapes.ShapeEventPublisher

class ShapeEventPublisherRabbitAdapter(val client: RabbitClient):ShapeEventPublisher {
    override fun publish(shape: Shape) {
        TODO("Not yet implemented")
    }
}
