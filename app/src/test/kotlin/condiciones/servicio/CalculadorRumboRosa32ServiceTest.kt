package condiciones.servicio

import condiciones.dominio.CuadranteRosa
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/**
 * Pruebas unitarias para el servicio [CalculadorRumboRosa32Service].
 */
class CalculadorRumboRosa32ServiceTest {

    private val servicio: CalculadorRumboService = CalculadorRumboRosa32Service()

    @Test
    fun testTotalPuntosGenerados() {
        val puntos = servicio.obtenerPuntos()
        assertEquals(32, puntos.size)
    }

    @Test
    fun testCalculoMatematicoRumbosCardinalesYPrincipales() {
        assertEquals("N", servicio.calcularRumbo(0.0).codigo)
        assertEquals("NpE", servicio.calcularRumbo(11.25).codigo)
        assertEquals("NNE", servicio.calcularRumbo(22.50).codigo)
        assertEquals("NEpN", servicio.calcularRumbo(33.75).codigo)
        assertEquals("NE", servicio.calcularRumbo(45.00).codigo)
        assertEquals("NEpE", servicio.calcularRumbo(56.25).codigo)
        assertEquals("ENE", servicio.calcularRumbo(67.50).codigo)
        assertEquals("EpN", servicio.calcularRumbo(78.75).codigo)
        assertEquals("E", servicio.calcularRumbo(90.00).codigo)
        assertEquals("EpS", servicio.calcularRumbo(101.25).codigo)
        assertEquals("ESE", servicio.calcularRumbo(112.50).codigo)
        assertEquals("SEpE", servicio.calcularRumbo(123.75).codigo)
        assertEquals("SE", servicio.calcularRumbo(135.00).codigo)
        assertEquals("SEpS", servicio.calcularRumbo(146.25).codigo)
        assertEquals("SSE", servicio.calcularRumbo(157.50).codigo)
        assertEquals("SpE", servicio.calcularRumbo(168.75).codigo)
        assertEquals("S", servicio.calcularRumbo(180.00).codigo)
        assertEquals("SpO", servicio.calcularRumbo(191.25).codigo)
        assertEquals("SSO", servicio.calcularRumbo(202.50).codigo)
        assertEquals("SOpS", servicio.calcularRumbo(213.75).codigo)
        assertEquals("SO", servicio.calcularRumbo(225.00).codigo)
        assertEquals("SOpO", servicio.calcularRumbo(236.25).codigo)
        assertEquals("OSO", servicio.calcularRumbo(247.50).codigo)
        assertEquals("OpS", servicio.calcularRumbo(258.75).codigo)
        assertEquals("O", servicio.calcularRumbo(270.00).codigo)
        assertEquals("OpN", servicio.calcularRumbo(281.25).codigo)
        assertEquals("ONO", servicio.calcularRumbo(292.50).codigo)
        assertEquals("NOpO", servicio.calcularRumbo(303.75).codigo)
        assertEquals("NO", servicio.calcularRumbo(315.00).codigo)
        assertEquals("NOpN", servicio.calcularRumbo(326.25).codigo)
        assertEquals("NNO", servicio.calcularRumbo(337.50).codigo)
        assertEquals("NpO", servicio.calcularRumbo(348.75).codigo)
        assertEquals("N", servicio.calcularRumbo(360.00).codigo)
    }

    @Test
    fun testCalculoMatematicoEnLimitesDeSectores() {
        // Límite entre N y NpE (5.625°)
        assertEquals("N", servicio.calcularRumbo(5.624).codigo)
        assertEquals("NpE", servicio.calcularRumbo(5.625).codigo)

        // Límite entre NpE y NNE (16.875°)
        assertEquals("NpE", servicio.calcularRumbo(16.874).codigo)
        assertEquals("NNE", servicio.calcularRumbo(16.875).codigo)

        // Límite entre NpO y N (354.375°)
        assertEquals("NpO", servicio.calcularRumbo(354.374).codigo)
        assertEquals("N", servicio.calcularRumbo(354.375).codigo)
    }

    @Test
    fun testValidacionAnguloFueraDeRango() {
        assertFailsWith<IllegalArgumentException> {
            servicio.calcularRumbo(-0.1)
        }
        assertFailsWith<IllegalArgumentException> {
            servicio.calcularRumbo(360.1)
        }
    }

    @Test
    fun testPuntosPorCuadrante() {
        val mapa = servicio.obtenerPuntosPorCuadrante()
        assertEquals(4, mapa.size)
        assertEquals(9, mapa[CuadranteRosa.NORTE_A_ESTE]?.size) // N hasta E inclusive
        assertEquals(8, mapa[CuadranteRosa.ESTE_A_SUR]?.size)   // EpS hasta S
        assertEquals(8, mapa[CuadranteRosa.SUR_A_OESTE]?.size)  // SpO hasta O
        assertEquals(7, mapa[CuadranteRosa.OESTE_A_NORTE]?.size) // OpN hasta NpO (N pertenece al cuadrante 1)
    }
}
