package domain.visitor.example.visitor

import domain.visitor.example.shapes.Circle
import domain.visitor.example.shapes.CompoundShape
import domain.visitor.example.shapes.Dot
import domain.visitor.example.shapes.Rectangle

interface Visitor {
    fun visitDot(dot: Dot): String
    fun visitCircle(circle: Circle): String
    fun visitRectangle(rectangle: Rectangle): String
    fun visitCompoundGraphic(cg: CompoundShape): String
}
