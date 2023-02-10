package domain.visitor.example.shapes

import domain.visitor.example.visitor.Visitor

open class Dot : Shape {
    var id = 0
        private set
    var x = 0
        private set
    var y = 0
        private set

    constructor() {}
    constructor(id: Int, x: Int, y: Int) {
        this.id = id
        this.x = x
        this.y = y
    }

    override fun move(x: Int, y: Int) {
        // move shape
    }

    override fun draw() {
        // draw shape
    }

    override fun accept(visitor: Visitor): String {
        return visitor.visitDot(this)
    }
}
