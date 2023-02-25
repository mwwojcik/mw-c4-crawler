package shapes

import org.springframework.stereotype.Service

@Service
class ShapeServiceImpl(shapeRepository: ShapeRepository,shapeStorage:ShapeStorageProvider,shapeEventPublisher: ShapeEventPublisher):ShapeService {
    override fun draw(shape: String) {
        TODO("Not yet implemented")
    }
}
