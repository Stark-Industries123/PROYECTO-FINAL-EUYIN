package ui

import fisicas.gravedad.Jupiter
import fisicas.gravedad.Luna
import fisicas.gravedad.Mercurio
import fisicas.gravedad.Tierra
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class MenuConsolaTest {

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
    fun testSeleccionarPlanetaConEntradasInvalidas() {
        // Entradas: no numérico ("abc"), fuera de rango ("0"), fuera de rango ("5"), válido ("1")
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
        // No numérico ("invalido"), negativo ("-10"), cero ("0"), positivo ("15.5")
        val menu = crearMenuConEntradas("invalido", "-10", "0", "15.5")
        val velocidad = menu.solicitarVelocidadInicial()
        assertEquals(15.5, velocidad)
    }

    @Test
    fun testSolicitarAnguloLimites() {
        // Ángulo 0° es válido
        val menuMin = crearMenuConEntradas("0")
        assertEquals(0.0, menuMin.solicitarAngulo())

        // Ángulo 90° es válido
        val menuMax = crearMenuConEntradas("90")
        assertEquals(90.0, menuMax.solicitarAngulo())
    }

    @Test
    fun testSolicitarAnguloReintentos() {
        // No numérico ("hola"), negativo ("-0.1"), mayor a 90 ("90.1"), válido ("45")
        val menu = crearMenuConEntradas("hola", "-0.1", "90.1", "45")
        val angulo = menu.solicitarAngulo()
        assertEquals(45.0, angulo)
    }

    @Test
    fun testSolicitarConfiguracionCompleta() {
        // Planeta: 2 (Luna), Velocidad: 20.0, Ángulo: 30.0
        val menu = crearMenuConEntradas("2", "20.0", "30.0")
        val config = menu.solicitarConfiguracion()

        assertIs<Luna>(config.fuenteGravedad)
        assertEquals(20.0, config.velocidadInicial)
        assertEquals(30.0, config.angulo)
    }
}
