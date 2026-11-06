package fisicas.motor

import config.ConfigSimulacion
import fisicas.gravedad.Tierra
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Suite de pruebas unitarias para el motor físico [Simulador] y el contrato [CalculadorTrayectoria].
 */
class SimuladorFisicoTest {

    private fun crearTierra() = Tierra()

    @Test
    fun testRequisitoA_MapeoGradosASectoresDireccionViento() {
        val tierra = crearTierra()
        assertEquals("Norte", ConfigSimulacion(20.0, 45.0, tierra, direccionVientoGrados = 0.0).sectorViento.descripcion)
        assertEquals("Norte", ConfigSimulacion(20.0, 45.0, tierra, direccionVientoGrados = 350.0).sectorViento.descripcion)
        assertEquals("Norte", ConfigSimulacion(20.0, 45.0, tierra, direccionVientoGrados = 20.0).sectorViento.descripcion)
        assertEquals("Este", ConfigSimulacion(20.0, 45.0, tierra, direccionVientoGrados = 90.0).sectorViento.descripcion)
        assertEquals("Sur", ConfigSimulacion(20.0, 45.0, tierra, direccionVientoGrados = 180.0).sectorViento.descripcion)
        assertEquals("Oeste", ConfigSimulacion(20.0, 45.0, tierra, direccionVientoGrados = 270.0).sectorViento.descripcion)
    }

    @Test
    fun testRequisitoB_ProyectilMasPesadoRecorreMasDistanciaConVientoEnContraQueUnoLiviano() {
        val tierra = crearTierra()
        val simulador = Simulador(tierra)
        val configPesado = ConfigSimulacion(30.0, 45.0, tierra, masaKg = 10.0, velocidadVientoKmH = 40.0, direccionVientoGrados = 0.0)
        val configLiviano = ConfigSimulacion(30.0, 45.0, tierra, masaKg = 0.5, velocidadVientoKmH = 40.0, direccionVientoGrados = 0.0)
        val resultadoPesado = simulador.calcularTrayectoria(configPesado)
        val resultadoLiviano = simulador.calcularTrayectoria(configLiviano)
        assertTrue(resultadoPesado.distanciaMaxima > resultadoLiviano.distanciaMaxima)
    }

    @Test
    fun testDIP_AbstraccionCalculadorTrayectoria() {
        val tierra = crearTierra()
        val calculador: CalculadorTrayectoria = Simulador(tierra)
        val config = ConfigSimulacion(velocidadInicial = 15.0, angulo = 45.0, fuenteGravedad = tierra)
        val resultado = calculador.calcularTrayectoria(config)
        assertTrue(resultado.distanciaMaxima > 0.0)
        assertTrue(resultado.tiempoVuelo > 0.0)
    }

    @Test
    fun testPuntoTrayectoriaEstructura3DYVelocidades() {
        val punto = PuntoTrayectoria(1.5, 12.0, 8.5, -1.2, 8.0, -2.5, -0.8)
        assertEquals(1.5, punto.tiempo)
        assertEquals(1.5, punto.t)
        assertEquals(12.0, punto.x)
        assertEquals(8.5, punto.y)
        assertEquals(-1.2, punto.z)
        assertEquals(8.0, punto.vx)
        assertEquals(-2.5, punto.vy)
        assertEquals(-0.8, punto.vz)
    }
}
