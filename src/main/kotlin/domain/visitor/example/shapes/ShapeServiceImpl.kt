package domain.visitor.example.shapes

import org.springframework.stereotype.Service

@Service
class ShapeServiceImpl(shapeRepository: ShapeRepository):ShapeService {
    override fun draw(shape: String) {
        TODO("Not yet implemented")
    }
}
