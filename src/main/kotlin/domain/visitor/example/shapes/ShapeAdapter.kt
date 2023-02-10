package domain.visitor.example.shapes

import org.springframework.stereotype.Component

@Component
class ShapeAdapter:ShapeRepository {
    override fun draw(shape: String) {
        TODO("Not yet implemented")
    }
}
