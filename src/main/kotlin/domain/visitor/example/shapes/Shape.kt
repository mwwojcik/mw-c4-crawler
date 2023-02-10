package domain.visitor.example.shapes

import domain.visitor.example.visitor.Visitor


interface Shape {
    fun move(x: Int, y: Int)
    fun draw()
    fun accept(visitor: Visitor): String
}
