package domain.visitor.example.shapes

import domain.visitor.example.visitor.Visitor

class CompoundShape(var id: Int) : Shape {
    var children: MutableList<Shape> = ArrayList()
    override fun move(x: Int, y: Int) {
        // move shape
    }

    override fun draw() {
        // draw shape
    }

    override fun accept(visitor: Visitor): String {
        return visitor.visitCompoundGraphic(this)
    }

    fun add(shape: Shape) {
        children.add(shape)
    }
}
