package condiciones.dominio

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Pruebas unitarias para el modelo de dominio [PuntoRosaVientos].
 */
class PuntoRosaVientosTest {

    private fun crearPunto(indice: Int, codigo: String, nombre: String, angulo: Double): PuntoRosaVientos {
        val min = if (angulo == 0.0) 354.375 else angulo - 5.625
        val max = angulo + 5.625
        return PuntoRosaVientos(indice, codigo, nombre, angulo, min, max, CuadranteRosa.NORTE_A_ESTE)
    }

    @Test
    fun testCreacionValidaPuntoRosaVientos() {
        val punto = crearPunto(4, "NE", "Noreste", 45.0)
        assertEquals(4, punto.indice)
        assertEquals("NE", punto.codigo)
        assertEquals("Noreste", punto.nombreCompleto)
        assertEquals(45.0, punto.anguloNominal)
        assertEquals(39.375, punto.rangoMinGrados)
        assertEquals(50.625, punto.rangoMaxGrados)
        assertEquals(CuadranteRosa.NORTE_A_ESTE, punto.cuadrante)
    }

    @Test
    fun testValidacionesInvariantesIndiceYTexto() {
        assertFailsWith<IllegalArgumentException> { crearPunto(-1, "N", "Norte", 0.0) }
        assertFailsWith<IllegalArgumentException> { crearPunto(32, "N", "Norte", 0.0) }
        assertFailsWith<IllegalArgumentException> { crearPunto(0, "", "Norte", 0.0) }
        assertFailsWith<IllegalArgumentException> { crearPunto(0, "N", "", 0.0) }
    }

    @Test
    fun testAbarcaAnguloRumboEstandar() {
        val ne = crearPunto(4, "NE", "Noreste", 45.0)
        assertTrue(ne.abarcaAngulo(45.0))
        assertTrue(ne.abarcaAngulo(39.375))
        assertTrue(ne.abarcaAngulo(50.624))
        assertFalse(ne.abarcaAngulo(50.625))
        assertFalse(ne.abarcaAngulo(39.374))
    }

    @Test
    fun testAbarcaAnguloRumboNorteQueCruzaCero() {
        val norte = crearPunto(0, "N", "Norte", 0.0)
        assertTrue(norte.abarcaAngulo(0.0))
        assertTrue(norte.abarcaAngulo(360.0))
        assertTrue(norte.abarcaAngulo(355.0))
        assertTrue(norte.abarcaAngulo(5.0))
        assertFalse(norte.abarcaAngulo(5.625))
        assertFalse(norte.abarcaAngulo(354.374))
    }

    @Test
    fun testCalcularDesviacion() {
        val este = crearPunto(8, "E", "Este", 90.0)
        assertEquals(0.0, este.calcularDesviacion(90.0), 0.001)
        assertEquals(2.0, este.calcularDesviacion(92.0), 0.001)
        assertEquals(-2.0, este.calcularDesviacion(88.0), 0.001)
    }
}
