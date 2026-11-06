package org.example

import condiciones.io.EscritorMemoriaConsola
import condiciones.io.LectorListaConsola
import fisicas.gravedad.Luna
import fisicas.gravedad.Mercurio
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Suite de pruebas de integración para la aplicación principal [App] y su flujo unificado.
 */
class AppTest {

    @Test
    fun testAppInstanciacion() {
        val app = App()
        assertNotNull(app)
    }

    @Test
    fun testFlujoCompletoEjecutarConEntradaConsolaUnificada() {
        val lector = LectorListaConsola(listOf("1", "30.0", "45.0", "345.0", "25.0", "4.5"))
        val escritor = EscritorMemoriaConsola()
        val resultado = App(lector = lector, escritor = escritor).ejecutar()
        assertTrue(resultado.tiempoVuelo > 0.0 && resultado.distanciaMaxima > 0.0)
        assertTrue(resultado.alturaMaxima > 0.0 && resultado.desviacionLateralMaxima > 0.0)
        assertTrue(resultado.trayectoria.isNotEmpty())
        verificarSalidaFlujoCompleto(escritor.contenidoCompleto())
    }

    private fun verificarSalidaFlujoCompleto(salida: String) {
        assertTrue(salida.contains("Norte: 315° a 45°") && salida.contains("Este: 45° a 135°"))
        assertTrue(salida.contains("Sur: 135° a 225°") && salida.contains("Oeste: 225° a 315°"))
        assertTrue(!salida.contains("ROSA DE LOS VIENTOS (32 RUMBOS / CUARTAS - 11.25° POR PUNTO)"))
        assertTrue(salida.contains("NpO") && salida.contains("Norte por el Oeste"))
        assertTrue(salida.contains("Sopla DESDE el rumbo NpO (Norte por el Oeste)"))
        assertTrue(salida.contains("Planeta objetivo: Tierra"))
        assertTrue(salida.contains("4.50 kg") || salida.contains("4,50 kg"))
        assertTrue(salida.contains("25.0 km/h") || salida.contains("25,0 km/h"))
        assertTrue(salida.contains("345.0°") || salida.contains("345,0°"))
        assertTrue(salida.contains("--- TRAYECTORIA DEL PROYECTIL (ASCII) ---"))
    }

    @Test
    fun testCalculoPrecisoViento345GradosNpOEnSolicitarConfiguracion() {
        val lector = LectorListaConsola(listOf("2", "50.0", "60.0", "345.0", "15.0", "2.0"))
        val config = App(lector = lector, escritor = EscritorMemoriaConsola()).solicitarConfiguracion()
        assertIs<Luna>(config.fuenteGravedad)
        assertEquals(50.0, config.velocidadInicial)
        assertEquals(60.0, config.angulo)
        assertEquals(2.0, config.masaKg)
        val viento = assertNotNull(config.viento)
        assertEquals("NpO", viento.rumbo.codigo)
        assertEquals("Norte por el Oeste", viento.rumbo.nombreCompleto)
        assertEquals(348.75, viento.rumbo.anguloNominal)
        val proyectil = assertNotNull(config.proyectil)
        assertEquals(2000.0, proyectil.pesoEnGramos())
    }

    @Test
    fun testFlujoConReintentosYValidaciones() {
        val entradas = listOf("invalido", "5", "3", "-10", "20.0", "95", "30.0", "-5", "90.0", "-1", "10.0", "0", "1.5")
        val escritor = EscritorMemoriaConsola()
        val config = App(lector = LectorListaConsola(entradas), escritor = escritor).solicitarConfiguracion()
        assertIs<Mercurio>(config.fuenteGravedad)
        assertEquals(20.0, config.velocidadInicial)
        assertEquals(30.0, config.angulo)
        assertEquals("E", config.viento?.rumbo?.codigo)
        verificarMensajesError(escritor.contenidoCompleto())
    }

    private fun verificarMensajesError(salida: String) {
        assertTrue(salida.contains("Opción inválida"))
        assertTrue(salida.contains("La velocidad inicial debe ser estrictamente mayor a 0"))
        assertTrue(salida.contains("El ángulo debe estar entre 0 y 90 grados"))
        assertTrue(salida.contains("La dirección del viento debe estar entre 0.0 y 360.0"))
        assertTrue(salida.contains("La velocidad del viento debe ser mayor o igual a 0.0"))
        assertTrue(salida.contains("El peso del proyectil debe ser estrictamente mayor a 0.0 kg"))
    }
}
