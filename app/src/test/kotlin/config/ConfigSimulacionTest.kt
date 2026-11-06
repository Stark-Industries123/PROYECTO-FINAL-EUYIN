package config

import fisicas.gravedad.Tierra
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/**
 * Pruebas unitarias para validar las reglas de negocio y restricciones
 * del modelo de configuración [ConfigSimulacion] (SRP).
 */
class ConfigSimulacionTest {

    @Test
    fun testValoresPorDefecto() {
        val config = ConfigSimulacion(velocidadInicial = 25.0, angulo = 45.0, fuenteGravedad = Tierra())
        assertEquals(1.0, config.masaKg)
        assertEquals(0.0, config.velocidadVientoKmH)
        assertEquals(0.0, config.direccionVientoGrados)
        assertEquals(0.0, config.velocidadVientoMS())
        assertEquals(DireccionVientoSector.NORTE, config.sectorViento)
        assertEquals(config.masaKg, config.masaProyectil)
    }

    @Test
    fun testConversionVelocidadVientoKmhAMs() {
        val config36 = ConfigSimulacion(20.0, 30.0, Tierra(), velocidadVientoKmH = 36.0)
        assertEquals(10.0, config36.velocidadVientoMS(), 0.001)
        assertEquals(10.0, config36.velocidadVientoMS, 0.001)
        val config72 = ConfigSimulacion(20.0, 30.0, Tierra(), velocidadVientoKmH = 72.0)
        assertEquals(20.0, config72.velocidadVientoMS(), 0.001)
    }

    @Test
    fun testValidacionMasaMayorACero() {
        val configValida = ConfigSimulacion(10.0, 45.0, Tierra(), masaKg = 0.001)
        assertEquals(0.001, configValida.masaKg)
        assertFailsWith<IllegalArgumentException> { ConfigSimulacion(10.0, 45.0, Tierra(), masaKg = 0.0) }
        assertFailsWith<IllegalArgumentException> { ConfigSimulacion(10.0, 45.0, Tierra(), masaKg = -5.0) }
    }

    @Test
    fun testValidacionVelocidadVientoMayorOIgualACero() {
        assertFailsWith<IllegalArgumentException> {
            ConfigSimulacion(10.0, 45.0, Tierra(), velocidadVientoKmH = -0.1)
        }
    }

    @Test
    fun testValidacionDireccionVientoRango() {
        val min = ConfigSimulacion(10.0, 45.0, Tierra(), direccionVientoGrados = 0.0)
        assertEquals(0.0, min.direccionVientoGrados)
        val max = ConfigSimulacion(10.0, 45.0, Tierra(), direccionVientoGrados = 360.0)
        assertEquals(360.0, max.direccionVientoGrados)
        assertFailsWith<IllegalArgumentException> { ConfigSimulacion(10.0, 45.0, Tierra(), direccionVientoGrados = -0.01) }
        assertFailsWith<IllegalArgumentException> { ConfigSimulacion(10.0, 45.0, Tierra(), direccionVientoGrados = 360.01) }
    }

    @Test
    fun testValidacionVelocidadInicialYAngulo() {
        assertFailsWith<IllegalArgumentException> { ConfigSimulacion(-1.0, 45.0, Tierra()) }
        assertFailsWith<IllegalArgumentException> { ConfigSimulacion(10.0, -0.5, Tierra()) }
        assertFailsWith<IllegalArgumentException> { ConfigSimulacion(10.0, 90.5, Tierra()) }
    }
}
