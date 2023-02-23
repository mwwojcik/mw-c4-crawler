package mw.doccrawler.dsl.sample

val bus=business {
    name { "Intelegume" }
    address {
        name { "Mr Peanut" }
        street { "Shell St" }
        city { "Nutelland" }
    }
}

//https://proandroiddev.com/writing-kotlin-dsls-with-nested-builder-pattern-66452476d5ef
class BusinessBuilder {
    private var name = ""
    private var address = Address("", "")
    private val employees = mutableListOf<Employee>()

    fun name(lambda: () -> String) { name = lambda() }

    fun address(lambda: AddressBuilder.() -> Unit) {
        address = AddressBuilder().apply(lambda).build()
    }

    fun employees(lambda: EmployeeListBuilder.() -> Unit) {
        employees.addAll(EmployeeListBuilder().apply(lambda).build())
    }

    fun build() = Business(name, address, employees)
}

class EmployeeListBuilder {
    private val employeeList = mutableListOf<Employee>()

    fun employee(lambda: EmployeeBuilder.() -> Unit) {
        employeeList.add(EmployeeBuilder().apply(lambda).build())
    }

    fun build() = employeeList
}

class EmployeeBuilder {
    private var name: String = ""
    private var id: String = ""
    private var title: String = ""
    private var salary: Int = 0

    fun name(lambda: () -> String) { this.name = lambda() }
    fun id(lambda: () -> String) { this.id = lambda() }
    fun title(lambda: () -> String) { this.title = lambda() }
    fun salary(lambda: () -> Int) { this.salary = lambda() }
    fun build() = Employee(name, id, title, salary)
}



data class Business(
    val name: String,
    val address: Address,
    val employees: List<Employee>
)

data class Address(
    val street: String,
    val city: String
)

data class Employee(
    val name: String,
    val id: String,
    val title: String,
    val salary: Int
)

class AddressBuilder {
    private var street = ""
    private var city = ""

    fun street(lambda: () -> String) { this.street = lambda() }
    fun city(lambda: () -> String) { this.city = lambda() }
    fun build() = Address(street, city)
}


fun business(lambda: BusinessBuilder.() -> Unit) = BusinessBuilder().apply(lambda).build()

fun main() {
    println(bus)
}
