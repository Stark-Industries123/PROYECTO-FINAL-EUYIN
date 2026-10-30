package condiciones

import condiciones.io.EscritorConsola
import condiciones.io.LectorConsola
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Pruebas de integración para la aplicación [ConfiguradorCondicionesApp].
 */
class ConfiguradorCondicionesAppTest {

    private class LectorMock(entradas: List<String>) : LectorConsola {
        private val cola = entradas.toMutableList()
        override fun leerLinea(): String? = if (cola.isNotEmpty()) cola.removeAt(0) else null
    }

    private class EscritorMock : EscritorConsola {
        val lineas = mutableListOf<String>()
        override fun escribir(texto: String) { lineas.add(texto) }
        override fun escribirLinea(texto: String) { lineas.add(texto + "\n") }
    }

    @Test
    fun testInstanciacionPorDefecto() {
        val app = ConfiguradorCondicionesApp()
        assertNotNull(app)
    }

    @Test
    fun testFlujoCompletoEjecucion() {
        // Entradas: dirección = 135.0 (SE), velocidad = 25.0 km/h, peso = 5.0 kg
        val lector = LectorMock(listOf("135.0", "25.0", "5.0"))
        val escritor = EscritorMock()
        val app = ConfiguradorCondicionesApp(lector = lector, escritor = escritor)

        val resultado = app.ejecutar()

        // 1. Verificación de los modelos configurados
        assertEquals(5.0, resultado.proyectil.pesoKg)
        assertEquals(135.0, resultado.viento.direccionGrados)
        assertEquals(25.0, resultado.viento.velocidad)
        assertEquals("SE", resultado.viento.rumbo.codigo)
        assertEquals("Sureste", resultado.viento.rumbo.nombreCompleto)
        assertEquals(135.0, resultado.viento.rumbo.anguloNominal)

        // 2. Verificación de la salida en consola
        val salidaTotal = escritor.lineas.joinToString("")

        // Verificación de los 4 cuadrantes descriptivos
        assertTrue(salidaTotal.contains("Norte a Este (0°-90°: N, NpE, NNE, NEpN, NE, NEpE, ENE, EpN, E)"))
        assertTrue(salidaTotal.contains("Este a Sur (90°-180°: EpS, ESE, SEpE, SE, SEpS, SSE, SpE, S)"))
        assertTrue(salidaTotal.contains("Sur a Oeste (180°-270°: SpO, SSO, SOpS, SO, SOpO, OSO, OpS, O)"))
        assertTrue(salidaTotal.contains("Oeste a Norte (270°-360°: OpN, ONO, NOpO, NO, NOpN, NNO, NpO, N)"))

        // Verificación del reporte final
        assertTrue(salidaTotal.contains("REPORTE FINAL"))
        assertTrue(salidaTotal.contains("5.00 kg"))
        assertTrue(salidaTotal.contains("SE (Sureste)"))
        assertTrue(salidaTotal.contains("25.00 km/h"))
        assertTrue(salidaTotal.contains("Sopla DESDE el rumbo SE (Sureste)"))
    }
}
