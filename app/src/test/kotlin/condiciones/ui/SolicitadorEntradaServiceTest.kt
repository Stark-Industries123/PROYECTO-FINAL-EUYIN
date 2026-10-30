package condiciones.ui

import condiciones.io.EscritorConsola
import condiciones.io.LectorConsola
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Pruebas unitarias para el servicio de captura y validación [SolicitadorEntradaService].
 */
class SolicitadorEntradaServiceTest {

    private class LectorConsolaMock(entradas: List<String>) : LectorConsola {
        private val cola = entradas.toMutableList()
        override fun leerLinea(): String? = if (cola.isNotEmpty()) cola.removeAt(0) else null
    }

    private class EscritorConsolaMock : EscritorConsola {
        val salidas = mutableListOf<String>()
        override fun escribir(texto: String) { salidas.add(texto) }
        override fun escribirLinea(texto: String) { salidas.add(texto + "\n") }
    }

    @Test
    fun testSolicitarDireccionVientoValidaDirecta() {
        val lector = LectorConsolaMock(listOf("135.0"))
        val escritor = EscritorConsolaMock()
        val solicitador = SolicitadorEntradaService(lector, escritor)

        val direccion = solicitador.solicitarDireccionViento()
        assertEquals(135.0, direccion)
    }

    @Test
    fun testSolicitarDireccionVientoConReintentosPorTextoYLimites() {
        val lector = LectorConsolaMock(listOf("no-es-numero", "-10.0", "361.0", "270.0"))
        val escritor = EscritorConsolaMock()
        val solicitador = SolicitadorEntradaService(lector, escritor)

        val direccion = solicitador.solicitarDireccionViento()
        assertEquals(270.0, direccion)
        val contenidoSalida = escritor.salidas.joinToString("")
        assertTrue(contenidoSalida.contains("Debe ingresar un valor numérico válido"))
        assertTrue(contenidoSalida.contains("debe estar entre 0.0 y 360.0"))
    }

    @Test
    fun testSolicitarVelocidadVientoValidaConReintento() {
        val lector = LectorConsolaMock(listOf("-5.0", "15.5"))
        val escritor = EscritorConsolaMock()
        val solicitador = SolicitadorEntradaService(lector, escritor)

        val velocidad = solicitador.solicitarVelocidadViento()
        assertEquals(15.5, velocidad)
        val contenidoSalida = escritor.salidas.joinToString("")
        assertTrue(contenidoSalida.contains("debe ser mayor o igual a 0.0"))
    }

    @Test
    fun testSolicitarPesoProyectilValidoConReintento() {
        val lector = LectorConsolaMock(listOf("0.0", "-2.0", "4.2"))
        val escritor = EscritorConsolaMock()
        val solicitador = SolicitadorEntradaService(lector, escritor)

        val peso = solicitador.solicitarPesoProyectil()
        assertEquals(4.2, peso)
        val contenidoSalida = escritor.salidas.joinToString("")
        assertTrue(contenidoSalida.contains("debe ser estrictamente mayor a 0.0 kg"))
    }
}
