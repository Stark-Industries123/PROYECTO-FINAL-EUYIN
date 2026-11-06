package ui

import fisicas.gravedad.Jupiter
import fisicas.gravedad.Luna
import fisicas.gravedad.Mercurio
import fisicas.gravedad.Tierra
import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertNull

/**
 * Pruebas unitarias para la selección de planetas mediante [SelectorPlaneta] y [ValidadorEntrada].
 */
class SelectorPlanetaTest {

    private val validador = ValidadorEntrada()

    @Test
    fun testParsearPlanetaValidoEInvalido() {
        assertIs<Tierra>(validador.parsearPlaneta("1"))
        assertIs<Luna>(validador.parsearPlaneta(" 2 "))
        assertIs<Mercurio>(validador.parsearPlaneta("3"))
        assertIs<Jupiter>(validador.parsearPlaneta("4"))
        assertNull(validador.parsearPlaneta("0"))
        assertNull(validador.parsearPlaneta("5"))
        assertNull(validador.parsearPlaneta("abc"))
        assertNull(validador.parsearPlaneta(null))
    }
}
