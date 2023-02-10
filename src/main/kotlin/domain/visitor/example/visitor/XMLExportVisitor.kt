package domain.visitor.example.visitor

import domain.visitor.example.shapes.Circle
import domain.visitor.example.shapes.CompoundShape
import domain.visitor.example.shapes.Dot
import domain.visitor.example.shapes.Rectangle
import domain.visitor.example.shapes.Shape

class XMLExportVisitor : Visitor {
    fun export(vararg args: Shape?): String {
        val sb = StringBuilder()
        for (shape in args) {
            sb.append(
                """<?xml version="1.0" encoding="utf-8"?>
"""
            )
            sb.append(shape?.accept(this)).append("\n")
            println(sb.toString())
            sb.setLength(0)
        }
        return sb.toString()
    }

    override fun visitDot(d: Dot): String {
        return """<dot>
    <id>${d.id}</id>
    <x>${d.x}</x>
    <y>${d.y}</y>
</dot>"""
    }

    override fun visitCircle(c: Circle): String {
        return """<circle>
    <id>${c.id}</id>
    <x>${c.x}</x>
    <y>${c.y}</y>
    <radius>${c.radius}</radius>
</circle>"""
    }

    override fun visitRectangle(r: Rectangle): String {
        return """<rectangle>
    <id>${r.id}</id>
    <x>${r.x}</x>
    <y>${r.y}</y>
    <width>${r.width}</width>
    <height>${r.height}</height>
</rectangle>"""
    }

    override fun visitCompoundGraphic(cg: CompoundShape): String {
        return """<compound_graphic>
   <id>${cg.id}</id>
${_visitCompoundGraphic(cg)}</compound_graphic>"""
    }

    private fun _visitCompoundGraphic(cg: CompoundShape): String {
        val sb = StringBuilder()
        for (shape in cg.children) {
            var obj: String = shape.accept(this)
            // Proper indentation for sub-objects.
            obj = """    ${obj.replace("\n", "\n    ")}
"""
            sb.append(obj)
        }
        return sb.toString()
    }
}
