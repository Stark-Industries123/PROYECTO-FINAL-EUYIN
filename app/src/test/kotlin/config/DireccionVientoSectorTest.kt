package config

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Pruebas unitarias para verificar la correcta clasificación y mapeo azimutal
 * del enum [DireccionVientoSector] según la rosa de los vientos.
 */
class DireccionVientoSectorTest {

    @Test
    fun testMapeoNorteCruzandoLimiteCeroGrados() {
        // El enunciado requiere explícitamente validar Norte en 0°, 350° y 20°
        assertEquals(DireccionVientoSector.NORTE, DireccionVientoSector.desdeGrados(0.0), "0° debe ser NORTE")
        assertEquals(DireccionVientoSector.NORTE, DireccionVientoSector.desdeGrados(350.0), "350° debe ser NORTE")
        assertEquals(DireccionVientoSector.NORTE, DireccionVientoSector.desdeGrados(20.0), "20° debe ser NORTE")

        // Límites exactos del sector Norte [330° a 30°)
        assertEquals(DireccionVientoSector.NORTE, DireccionVientoSector.desdeGrados(330.0), "330° (inicio exacto) debe ser NORTE")
        assertEquals(DireccionVientoSector.NORTE, DireccionVientoSector.desdeGrados(359.9), "359.9° debe ser NORTE")
        assertEquals(DireccionVientoSector.NORTE, DireccionVientoSector.desdeGrados(360.0), "360.0° (equivalente a 0°) debe ser NORTE")
        assertEquals(DireccionVientoSector.NORTE, DireccionVientoSector.desdeGrados(29.99), "29.99° debe ser NORTE")
    }

    @Test
    fun testMapeoTodosLosSectoresCardinales() {
        // NORESTE (30° a 60°)
        assertEquals(DireccionVientoSector.NORESTE, DireccionVientoSector.desdeGrados(30.0))
        assertEquals(DireccionVientoSector.NORESTE, DireccionVientoSector.desdeGrados(45.0))
        assertEquals(DireccionVientoSector.NORESTE, DireccionVientoSector.desdeGrados(59.9))

        // ESTE (60° a 120°)
        assertEquals(DireccionVientoSector.ESTE, DireccionVientoSector.desdeGrados(60.0))
        assertEquals(DireccionVientoSector.ESTE, DireccionVientoSector.desdeGrados(90.0))
        assertEquals(DireccionVientoSector.ESTE, DireccionVientoSector.desdeGrados(119.9))

        // SURESTE (120° a 150°)
        assertEquals(DireccionVientoSector.SURESTE, DireccionVientoSector.desdeGrados(120.0))
        assertEquals(DireccionVientoSector.SURESTE, DireccionVientoSector.desdeGrados(135.0))
        assertEquals(DireccionVientoSector.SURESTE, DireccionVientoSector.desdeGrados(149.9))

        // SUR (150° a 210°)
        assertEquals(DireccionVientoSector.SUR, DireccionVientoSector.desdeGrados(150.0))
        assertEquals(DireccionVientoSector.SUR, DireccionVientoSector.desdeGrados(180.0))
        assertEquals(DireccionVientoSector.SUR, DireccionVientoSector.desdeGrados(209.9))

        // SUROESTE (210° a 240°)
        assertEquals(DireccionVientoSector.SUROESTE, DireccionVientoSector.desdeGrados(210.0))
        assertEquals(DireccionVientoSector.SUROESTE, DireccionVientoSector.desdeGrados(225.0))
        assertEquals(DireccionVientoSector.SUROESTE, DireccionVientoSector.desdeGrados(239.9))

        // OESTE (240° a 300°)
        assertEquals(DireccionVientoSector.OESTE, DireccionVientoSector.desdeGrados(240.0))
        assertEquals(DireccionVientoSector.OESTE, DireccionVientoSector.desdeGrados(270.0))
        assertEquals(DireccionVientoSector.OESTE, DireccionVientoSector.desdeGrados(299.9))

        // NOROESTE (300° a 330°)
        assertEquals(DireccionVientoSector.NOROESTE, DireccionVientoSector.desdeGrados(300.0))
        assertEquals(DireccionVientoSector.NOROESTE, DireccionVientoSector.desdeGrados(315.0))
        assertEquals(DireccionVientoSector.NOROESTE, DireccionVientoSector.desdeGrados(329.9))
    }

    @Test
    fun testNormalizacionAngulosNegativosYMayoresA360() {
        // Grados > 360
        assertEquals(DireccionVientoSector.NORTE, DireccionVientoSector.desdeGrados(720.0))
        assertEquals(DireccionVientoSector.ESTE, DireccionVientoSector.desdeGrados(450.0)) // 450 - 360 = 90

        // Grados negativos
        assertEquals(DireccionVientoSector.OESTE, DireccionVientoSector.desdeGrados(-90.0)) // -90 -> 270
        assertEquals(DireccionVientoSector.NORTE, DireccionVientoSector.desdeGrados(-10.0)) // -10 -> 350
    }
}
