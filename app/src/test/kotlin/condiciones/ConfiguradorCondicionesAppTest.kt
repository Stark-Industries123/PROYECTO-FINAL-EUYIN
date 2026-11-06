package condiciones

import condiciones.io.EscritorMemoriaConsola
import condiciones.io.LectorListaConsola
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Pruebas de integración para la aplicación [ConfiguradorCondicionesApp].
 */
class ConfiguradorCondicionesAppTest {

    @Test
    fun testInstanciacionPorDefecto() {
        val app = ConfiguradorCondicionesApp()
        assertNotNull(app)
    }

    @Test
    fun testFlujoCompletoEjecucion() {
        val lector = LectorListaConsola(listOf("135.0", "25.0", "5.0"))
        val escritor = EscritorMemoriaConsola()
        val resultado = ConfiguradorCondicionesApp(lector = lector, escritor = escritor).ejecutar()
        assertEquals(5.0, resultado.proyectil.pesoKg)
        assertEquals(135.0, resultado.viento.direccionGrados)
        assertEquals(25.0, resultado.viento.velocidad)
        assertEquals("SE", resultado.viento.rumbo.codigo)
        assertEquals("Sureste", resultado.viento.rumbo.nombreCompleto)
        assertEquals(135.0, resultado.viento.rumbo.anguloNominal)
        verificarSalidaConsola(escritor.contenidoCompleto())
    }

    private fun verificarSalidaConsola(salidaTotal: String) {
        assertTrue(salidaTotal.contains("Norte a Este (0°-90°: N, NpE, NNE, NEpN, NE, NEpE, ENE, EpN, E)"))
        assertTrue(salidaTotal.contains("Este a Sur (90°-180°: EpS, ESE, SEpE, SE, SEpS, SSE, SpE, S)"))
        assertTrue(salidaTotal.contains("Sur a Oeste (180°-270°: SpO, SSO, SOpS, SO, SOpO, OSO, OpS, O)"))
        assertTrue(salidaTotal.contains("Oeste a Norte (270°-360°: OpN, ONO, NOpO, NO, NOpN, NNO, NpO, N)"))
        assertTrue(salidaTotal.contains("REPORTE FINAL"))
        assertTrue(salidaTotal.contains("5.00 kg") || salidaTotal.contains("5,00 kg"))
        assertTrue(salidaTotal.contains("SE (Sureste)"))
        assertTrue(salidaTotal.contains("25.00 km/h") || salidaTotal.contains("25,00 km/h"))
        assertTrue(salidaTotal.contains("Sopla DESDE el rumbo SE (Sureste)"))
    }
}
