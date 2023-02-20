package mw.doccrawler.print

import mw.doccrawler.ModuleSet

interface PrintStrategy {
    fun print(module: ModuleSet): String
}
