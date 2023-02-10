package domain.visitor.example.shapes

interface Shape {
    fun move(x: Int, y: Int)
    fun draw()
}

class Rectangle:Shape{
    override fun move(x: Int, y: Int) {
        TODO("Not yet implemented")
    }

    override fun draw() {
        TODO("Not yet implemented")
    }

}

class Circle:Shape{
    override fun move(x: Int, y: Int) {
        TODO("Not yet implemented")
    }

    override fun draw() {
        TODO("Not yet implemented")
    }

}
