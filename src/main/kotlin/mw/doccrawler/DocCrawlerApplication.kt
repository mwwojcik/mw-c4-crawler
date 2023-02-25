package mw.doccrawler

import mw.doccrawler.shapes.defaultShapesConfiguration
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class C4DocCrawlerApplication

fun main(args: Array<String>) {
	SpringApplication.run(C4DocCrawlerApplication::class.java, *args)
}
