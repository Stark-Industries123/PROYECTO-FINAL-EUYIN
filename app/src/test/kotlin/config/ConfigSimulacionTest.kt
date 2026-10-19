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
        val config = ConfigSimulacion(
            velocidadInicial = 25.0,
            angulo = 45.0,
            fuenteGravedad = Tierra()
        )

        assertEquals(1.0, config.masaKg, "La masa por defecto debe ser 1.0 kg")
        assertEquals(0.0, config.velocidadVientoKmH, "La velocidad del viento por defecto debe ser 0.0 km/h")
        assertEquals(0.0, config.direccionVientoGrados, "La dirección del viento por defecto debe ser 0.0°")
        assertEquals(0.0, config.velocidadVientoMS(), "La velocidad del viento en m/s debe ser 0.0")
        assertEquals(DireccionVientoSector.NORTE, config.sectorViento, "El sector por defecto debe ser NORTE")
        assertEquals(config.masaKg, config.masaProyectil, "masaProyectil debe ser alias de masaKg")
    }

    @Test
    fun testConversionVelocidadVientoKmhAMs() {
        // 36 km/h = 10 m/s (36 / 3.6)
        val config36 = ConfigSimulacion(
            velocidadInicial = 20.0,
            angulo = 30.0,
            fuenteGravedad = Tierra(),
            velocidadVientoKmH = 36.0
        )
        assertEquals(10.0, config36.velocidadVientoMS(), 0.001)
        assertEquals(10.0, config36.velocidadVientoMS, 0.001)

        // 72 km/h = 20 m/s
        val config72 = ConfigSimulacion(
            velocidadInicial = 20.0,
            angulo = 30.0,
            fuenteGravedad = Tierra(),
            velocidadVientoKmH = 72.0
        )
        assertEquals(20.0, config72.velocidadVientoMS(), 0.001)
    }

    @Test
    fun testValidacionMasaMayorACero() {
        // Masa válida (> 0)
        val configValida = ConfigSimulacion(
            velocidadInicial = 10.0,
            angulo = 45.0,
            fuenteGravedad = Tierra(),
            masaKg = 0.001
        )
        assertEquals(0.001, configValida.masaKg)

        // Masa = 0 debe fallar
        assertFailsWith<IllegalArgumentException> {
            ConfigSimulacion(
                velocidadInicial = 10.0,
                angulo = 45.0,
                fuenteGravedad = Tierra(),
                masaKg = 0.0
            )
        }

        // Masa negativa debe fallar
        assertFailsWith<IllegalArgumentException> {
            ConfigSimulacion(
                velocidadInicial = 10.0,
                angulo = 45.0,
                fuenteGravedad = Tierra(),
                masaKg = -5.0
            )
        }
    }

    @Test
    fun testValidacionVelocidadVientoMayorOIgualACero() {
        // Negativa debe fallar
        assertFailsWith<IllegalArgumentException> {
            ConfigSimulacion(
                velocidadInicial = 10.0,
                angulo = 45.0,
                fuenteGravedad = Tierra(),
                velocidadVientoKmH = -0.1
            )
        }
    }

    @Test
    fun testValidacionDireccionVientoRango() {
        // Límites válidos [0.0, 360.0]
        val min = ConfigSimulacion(10.0, 45.0, Tierra(), direccionVientoGrados = 0.0)
        assertEquals(0.0, min.direccionVientoGrados)

        val max = ConfigSimulacion(10.0, 45.0, Tierra(), direccionVientoGrados = 360.0)
        assertEquals(360.0, max.direccionVientoGrados)

        // Menor a 0 debe fallar
        assertFailsWith<IllegalArgumentException> {
            ConfigSimulacion(10.0, 45.0, Tierra(), direccionVientoGrados = -0.01)
        }

        // Mayor a 360 debe fallar
        assertFailsWith<IllegalArgumentException> {
            ConfigSimulacion(10.0, 45.0, Tierra(), direccionVientoGrados = 360.01)
        }
    }

    @Test
    fun testValidacionVelocidadInicialYAngulo() {
        assertFailsWith<IllegalArgumentException> {
            ConfigSimulacion(velocidadInicial = -1.0, angulo = 45.0, fuenteGravedad = Tierra())
        }

        assertFailsWith<IllegalArgumentException> {
            ConfigSimulacion(velocidadInicial = 10.0, angulo = -0.5, fuenteGravedad = Tierra())
        }

        assertFailsWith<IllegalArgumentException> {
            ConfigSimulacion(velocidadInicial = 10.0, angulo = 90.5, fuenteGravedad = Tierra())
        }
    }
}
