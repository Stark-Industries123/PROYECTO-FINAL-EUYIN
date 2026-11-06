package condiciones.ui

import condiciones.io.EscritorMemoriaConsola
import condiciones.io.LectorListaConsola
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Pruebas unitarias para el servicio de captura y validación [SolicitadorEntradaService].
 */
class SolicitadorEntradaServiceTest {

    @Test
    fun testSolicitarDireccionVientoValidaDirecta() {
        val lector = LectorListaConsola(listOf("135.0"))
        val escritor = EscritorMemoriaConsola()
        val solicitador = SolicitadorEntradaService(lector, escritor)
        val direccion = solicitador.solicitarDireccionViento()
        assertEquals(135.0, direccion)
    }

    @Test
    fun testSolicitarDireccionVientoConReintentosPorTextoYLimites() {
        val lector = LectorListaConsola(listOf("no-es-numero", "-10.0", "361.0", "270.0"))
        val escritor = EscritorMemoriaConsola()
        val solicitador = SolicitadorEntradaService(lector, escritor)
        val direccion = solicitador.solicitarDireccionViento()
        assertEquals(270.0, direccion)
        val contenidoSalida = escritor.contenidoCompleto()
        assertTrue(contenidoSalida.contains("Debe ingresar un valor numérico válido"))
        assertTrue(contenidoSalida.contains("debe estar entre 0.0 y 360.0"))
    }

    @Test
    fun testSolicitarVelocidadVientoValidaConReintento() {
        val lector = LectorListaConsola(listOf("-5.0", "15.5"))
        val escritor = EscritorMemoriaConsola()
        val solicitador = SolicitadorEntradaService(lector, escritor)
        val velocidad = solicitador.solicitarVelocidadViento()
        assertEquals(15.5, velocidad)
        assertTrue(escritor.contenidoCompleto().contains("debe ser mayor o igual a 0.0"))
    }

    @Test
    fun testSolicitarPesoProyectilValidoConReintento() {
        val lector = LectorListaConsola(listOf("0.0", "-2.0", "4.2"))
        val escritor = EscritorMemoriaConsola()
        val solicitador = SolicitadorEntradaService(lector, escritor)
        val peso = solicitador.solicitarPesoProyectil()
        assertEquals(4.2, peso)
        assertTrue(escritor.contenidoCompleto().contains("debe ser estrictamente mayor a 0.0 kg"))
    }
}
