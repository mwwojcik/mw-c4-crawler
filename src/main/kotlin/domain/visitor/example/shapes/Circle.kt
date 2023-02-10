package domain.visitor.example.shapes

import domain.visitor.example.visitor.Visitor


class Circle(id: Int, x: Int, y: Int, val radius: Int) : Dot(id, x, y) {

    override fun accept(visitor: Visitor): String {
        return visitor.visitCircle(this)
    }
}
