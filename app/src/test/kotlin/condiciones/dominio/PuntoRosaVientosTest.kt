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

    @Test
    fun testCreacionValidaPuntoRosaVientos() {
        val punto = PuntoRosaVientos(
            indice = 4,
            codigo = "NE",
            nombreCompleto = "Noreste",
            anguloNominal = 45.0,
            rangoMinGrados = 39.375,
            rangoMaxGrados = 50.625,
            cuadrante = CuadranteRosa.NORTE_A_ESTE
        )

        assertEquals(4, punto.indice)
        assertEquals("NE", punto.codigo)
        assertEquals("Noreste", punto.nombreCompleto)
        assertEquals(45.0, punto.anguloNominal)
        assertEquals(39.375, punto.rangoMinGrados)
        assertEquals(50.625, punto.rangoMaxGrados)
        assertEquals(CuadranteRosa.NORTE_A_ESTE, punto.cuadrante)
    }

    @Test
    fun testValidacionesInvariantes() {
        assertFailsWith<IllegalArgumentException> {
            PuntoRosaVientos(
                indice = -1,
                codigo = "N",
                nombreCompleto = "Norte",
                anguloNominal = 0.0,
                rangoMinGrados = 354.375,
                rangoMaxGrados = 5.625,
                cuadrante = CuadranteRosa.NORTE_A_ESTE
            )
        }

        assertFailsWith<IllegalArgumentException> {
            PuntoRosaVientos(
                indice = 32,
                codigo = "N",
                nombreCompleto = "Norte",
                anguloNominal = 0.0,
                rangoMinGrados = 354.375,
                rangoMaxGrados = 5.625,
                cuadrante = CuadranteRosa.NORTE_A_ESTE
            )
        }

        assertFailsWith<IllegalArgumentException> {
            PuntoRosaVientos(
                indice = 0,
                codigo = "",
                nombreCompleto = "Norte",
                anguloNominal = 0.0,
                rangoMinGrados = 354.375,
                rangoMaxGrados = 5.625,
                cuadrante = CuadranteRosa.NORTE_A_ESTE
            )
        }
    }

    @Test
    fun testAbarcaAnguloRumboEstandar() {
        val ne = PuntoRosaVientos(
            indice = 4,
            codigo = "NE",
            nombreCompleto = "Noreste",
            anguloNominal = 45.0,
            rangoMinGrados = 39.375,
            rangoMaxGrados = 50.625,
            cuadrante = CuadranteRosa.NORTE_A_ESTE
        )

        assertTrue(ne.abarcaAngulo(45.0))
        assertTrue(ne.abarcaAngulo(39.375))
        assertTrue(ne.abarcaAngulo(50.624))
        assertFalse(ne.abarcaAngulo(50.625))
        assertFalse(ne.abarcaAngulo(39.374))
    }

    @Test
    fun testAbarcaAnguloRumboNorteQueCruzaCero() {
        val norte = PuntoRosaVientos(
            indice = 0,
            codigo = "N",
            nombreCompleto = "Norte",
            anguloNominal = 0.0,
            rangoMinGrados = 354.375,
            rangoMaxGrados = 5.625,
            cuadrante = CuadranteRosa.NORTE_A_ESTE
        )

        assertTrue(norte.abarcaAngulo(0.0))
        assertTrue(norte.abarcaAngulo(360.0))
        assertTrue(norte.abarcaAngulo(355.0))
        assertTrue(norte.abarcaAngulo(5.0))
        assertFalse(norte.abarcaAngulo(5.625))
        assertFalse(norte.abarcaAngulo(354.374))
    }

    @Test
    fun testCalcularDesviacion() {
        val este = PuntoRosaVientos(
            indice = 8,
            codigo = "E",
            nombreCompleto = "Este",
            anguloNominal = 90.0,
            rangoMinGrados = 84.375,
            rangoMaxGrados = 95.625,
            cuadrante = CuadranteRosa.NORTE_A_ESTE
        )

        assertEquals(0.0, este.calcularDesviacion(90.0), 0.001)
        assertEquals(2.0, este.calcularDesviacion(92.0), 0.001)
        assertEquals(-2.0, este.calcularDesviacion(88.0), 0.001)
    }
}
