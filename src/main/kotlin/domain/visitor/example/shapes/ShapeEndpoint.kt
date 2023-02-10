package domain.visitor.example.shapes


//@RestController
class ShapeEndpoint(service: ShapeService) {
    // @GetMapping("/shapes")
    fun getShape(): String {
        return "Shape"
    }
}
