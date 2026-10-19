package ui

import fisicas.gravedad.Tierra
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/**
 * Pruebas unitarias para validar las solicitudes con reintentos y control de errores en [MenuConsola].
 */
class MenuConsolaValidationTest {

    private fun crearMenuConEntradas(vararg entradas: String): MenuConsola {
        val cola = entradas.toMutableList()
        return MenuConsola(
            lector = { if (cola.isNotEmpty()) cola.removeAt(0) else null },
            escritor = {},
            escritorLinea = {}
        )
    }

    @Test
    fun testSeleccionarPlanetaConEntradasInvalidas() {
        val menu = crearMenuConEntradas("abc", "0", "5", "1")
        val planeta = menu.solicitarPlaneta()
        assertIs<Tierra>(planeta)
    }

    @Test
    fun testSolicitarVelocidadInicialValida() {
        val menu = crearMenuConEntradas("50.0")
        val velocidad = menu.solicitarVelocidadInicial()
        assertEquals(50.0, velocidad)
    }

    @Test
    fun testSolicitarVelocidadInicialReintentos() {
        val menu = crearMenuConEntradas("invalido", "-10", "0", "15.5")
        val velocidad = menu.solicitarVelocidadInicial()
        assertEquals(15.5, velocidad)
    }

    @Test
    fun testSolicitarAnguloLimites() {
        val menuMin = crearMenuConEntradas("0")
        assertEquals(0.0, menuMin.solicitarAngulo())

        val menuMax = crearMenuConEntradas("90")
        assertEquals(90.0, menuMax.solicitarAngulo())
    }

    @Test
    fun testSolicitarAnguloReintentos() {
        val menu = crearMenuConEntradas("hola", "-0.1", "90.1", "45")
        val angulo = menu.solicitarAngulo()
        assertEquals(45.0, angulo)
    }

    @Test
    fun testSolicitarMasaReintentos() {
        val menu = crearMenuConEntradas("invalido", "-2.0", "0", "3.5")
        val masa = menu.solicitarMasa()
        assertEquals(3.5, masa)
    }

    @Test
    fun testSolicitarVientoYDireccion() {
        val menuViento = crearMenuConEntradas("invalido", "-5.0", "25.0")
        assertEquals(25.0, menuViento.solicitarViento())

        val menuDireccion = crearMenuConEntradas("invalido", "-1.0", "361.0", "180.0")
        assertEquals(180.0, menuDireccion.solicitarDireccion())
    }
}
