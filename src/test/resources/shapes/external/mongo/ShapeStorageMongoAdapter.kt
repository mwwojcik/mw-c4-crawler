package shapes.external.mongo

import domain.visitor.example.shapes.Shape
import domain.visitor.example.shapes.ShapeStorageProvider

class ShapeStorageMongoAdapter(val client: MongoClient):ShapeStorageProvider {
    override fun save(shape: Shape) {
        TODO("Not yet implemented")
    }
}
