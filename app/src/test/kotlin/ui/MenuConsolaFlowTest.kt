package ui

import fisicas.gravedad.Jupiter
import fisicas.gravedad.Luna
import fisicas.gravedad.Mercurio
import fisicas.gravedad.Tierra
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/**
 * Pruebas unitarias para el flujo de interacción y selección de opciones en [MenuConsola].
 */
class MenuConsolaFlowTest {

    private fun crearMenuConEntradas(vararg entradas: String): MenuConsola {
        val cola = entradas.toMutableList()
        return MenuConsola(
            lector = { if (cola.isNotEmpty()) cola.removeAt(0) else null },
            escritor = {},
            escritorLinea = {}
        )
    }

    @Test
    fun testSeleccionarPlanetaTierra() {
        val menu = crearMenuConEntradas("1")
        val planeta = menu.solicitarPlaneta()
        assertIs<Tierra>(planeta)
    }

    @Test
    fun testSeleccionarPlanetaLuna() {
        val menu = crearMenuConEntradas("2")
        val planeta = menu.solicitarPlaneta()
        assertIs<Luna>(planeta)
    }

    @Test
    fun testSeleccionarPlanetaMercurio() {
        val menu = crearMenuConEntradas("3")
        val planeta = menu.solicitarPlaneta()
        assertIs<Mercurio>(planeta)
    }

    @Test
    fun testSeleccionarPlanetaJupiter() {
        val menu = crearMenuConEntradas("4")
        val planeta = menu.solicitarPlaneta()
        assertIs<Jupiter>(planeta)
    }

    @Test
    fun testSolicitarConfiguracionCompleta() {
        val menu = crearMenuConEntradas("2", "20.0", "30.0")
        val config = menu.solicitarConfiguracion()

        assertIs<Luna>(config.fuenteGravedad)
        assertEquals(20.0, config.velocidadInicial)
        assertEquals(30.0, config.angulo)
    }
}
