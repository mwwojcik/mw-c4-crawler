package domain.visitor.example.shapes


//@RestController
class ShapeEndpoint(val service: ShapeService) {
    // @GetMapping("/shapes")
    fun getShape(): String {
        return "Shape"
    }
}
