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

    private val puntoNorte = PuntoRosaVientos(
        indice = 0,
        codigo = "N",
        nombreCompleto = "Norte",
        anguloNominal = 0.0,
        rangoMinGrados = 354.375,
        rangoMaxGrados = 5.625,
        cuadrante = CuadranteRosa.NORTE_A_ESTE
    )

    private val puntoEste = PuntoRosaVientos(
        indice = 8,
        codigo = "E",
        nombreCompleto = "Este",
        anguloNominal = 90.0,
        rangoMinGrados = 84.375,
        rangoMaxGrados = 95.625,
        cuadrante = CuadranteRosa.NORTE_A_ESTE
    )

    @Test
    fun testCreacionValidaViento() {
        val viento = Viento(direccionGrados = 90.0, velocidad = 36.0, rumbo = puntoEste)
        assertEquals(90.0, viento.direccionGrados)
        assertEquals(36.0, viento.velocidad)
        assertEquals(puntoEste, viento.rumbo)
        assertEquals(10.0, viento.velocidadEnMS(), 0.001) // 36 km/h = 10 m/s
        assertFalse(viento.esCalma())
    }

    @Test
    fun testVientoEnCalma() {
        val viento = Viento(direccionGrados = 0.0, velocidad = 0.0, rumbo = puntoNorte)
        assertTrue(viento.esCalma())
        assertEquals(0.0, viento.velocidadEnMS(), 0.001)
        assertTrue(viento.descripcionFormateada().contains("calma"))
    }

    @Test
    fun testValidacionesRangoViento() {
        assertFailsWith<IllegalArgumentException> {
            Viento(direccionGrados = -1.0, velocidad = 10.0, rumbo = puntoNorte)
        }
        assertFailsWith<IllegalArgumentException> {
            Viento(direccionGrados = 360.1, velocidad = 10.0, rumbo = puntoNorte)
        }
        assertFailsWith<IllegalArgumentException> {
            Viento(direccionGrados = 180.0, velocidad = -5.0, rumbo = puntoNorte)
        }
    }

    @Test
    fun testComponentesVectoriales() {
        // Viento desde el Este (90°): sin(90°) = 1, cos(90°) = 0
        val viento = Viento(direccionGrados = 90.0, velocidad = 50.0, rumbo = puntoEste)
        val (vx, vy) = viento.componentesVectoriales()
        assertEquals(50.0, vx, 0.001)
        assertEquals(0.0, vy, 0.001)
    }
}
