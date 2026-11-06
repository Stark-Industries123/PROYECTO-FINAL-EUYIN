package condiciones.dominio

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Pruebas unitarias para el modelo de dominio [Viento].
 */
class VientoTest {

    private fun puntoNorte() = PuntoRosaVientos(0, "N", "Norte", 0.0, 354.375, 5.625, CuadranteRosa.NORTE_A_ESTE)

    private fun puntoEste() = PuntoRosaVientos(8, "E", "Este", 90.0, 84.375, 95.625, CuadranteRosa.NORTE_A_ESTE)

    @Test
    fun testCreacionValidaViento() {
        val este = puntoEste()
        val viento = Viento(direccionGrados = 90.0, velocidad = 36.0, rumbo = este)
        assertEquals(90.0, viento.direccionGrados)
        assertEquals(36.0, viento.velocidad)
        assertEquals(este, viento.rumbo)
        assertEquals(10.0, viento.velocidadEnMS(), 0.001)
        assertFalse(viento.esCalma())
    }

    @Test
    fun testVientoEnCalma() {
        val viento = Viento(direccionGrados = 0.0, velocidad = 0.0, rumbo = puntoNorte())
        assertTrue(viento.esCalma())
        assertEquals(0.0, viento.velocidadEnMS(), 0.001)
        assertTrue(viento.descripcionFormateada().contains("calma"))
    }

    @Test
    fun testValidacionesRangoViento() {
        val norte = puntoNorte()
        assertFailsWith<IllegalArgumentException> { Viento(-1.0, 10.0, norte) }
        assertFailsWith<IllegalArgumentException> { Viento(360.1, 10.0, norte) }
        assertFailsWith<IllegalArgumentException> { Viento(180.0, -5.0, norte) }
    }

    @Test
    fun testComponentesVectoriales() {
        val viento = Viento(direccionGrados = 90.0, velocidad = 50.0, rumbo = puntoEste())
        val (vx, vy) = viento.componentesVectoriales()
        assertEquals(50.0, vx, 0.001)
        assertEquals(0.0, vy, 0.001)
    }
}
