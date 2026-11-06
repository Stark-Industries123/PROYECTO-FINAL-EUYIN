package config

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Pruebas unitarias para verificar la clasificación de los sectores cardinales
 * principales (Norte, Este, Sur, Oeste) de [DireccionVientoSector].
 */
class DireccionVientoSectorTest {

    @Test
    fun testMapeoNorteCruzandoLimiteCeroGrados() {
        assertEquals(DireccionVientoSector.NORTE, DireccionVientoSector.desdeGrados(0.0))
        assertEquals(DireccionVientoSector.NORTE, DireccionVientoSector.desdeGrados(350.0))
        assertEquals(DireccionVientoSector.NORTE, DireccionVientoSector.desdeGrados(20.0))
        assertEquals(DireccionVientoSector.NORTE, DireccionVientoSector.desdeGrados(330.0))
        assertEquals(DireccionVientoSector.NORTE, DireccionVientoSector.desdeGrados(359.9))
        assertEquals(DireccionVientoSector.NORTE, DireccionVientoSector.desdeGrados(360.0))
        assertEquals(DireccionVientoSector.NORTE, DireccionVientoSector.desdeGrados(29.99))
    }

    @Test
    fun testMapeoSectoresCardinalesEsteSurYOeste() {
        assertEquals(DireccionVientoSector.ESTE, DireccionVientoSector.desdeGrados(60.0))
        assertEquals(DireccionVientoSector.ESTE, DireccionVientoSector.desdeGrados(90.0))
        assertEquals(DireccionVientoSector.ESTE, DireccionVientoSector.desdeGrados(119.9))
        assertEquals(DireccionVientoSector.SUR, DireccionVientoSector.desdeGrados(150.0))
        assertEquals(DireccionVientoSector.SUR, DireccionVientoSector.desdeGrados(180.0))
        assertEquals(DireccionVientoSector.SUR, DireccionVientoSector.desdeGrados(209.9))
        assertEquals(DireccionVientoSector.OESTE, DireccionVientoSector.desdeGrados(240.0))
        assertEquals(DireccionVientoSector.OESTE, DireccionVientoSector.desdeGrados(270.0))
        assertEquals(DireccionVientoSector.OESTE, DireccionVientoSector.desdeGrados(299.9))
    }

    @Test
    fun testNormalizacionAngulosNegativosYMayoresA360() {
        assertEquals(DireccionVientoSector.NORTE, DireccionVientoSector.desdeGrados(720.0))
        assertEquals(DireccionVientoSector.ESTE, DireccionVientoSector.desdeGrados(450.0))
        assertEquals(DireccionVientoSector.OESTE, DireccionVientoSector.desdeGrados(-90.0))
        assertEquals(DireccionVientoSector.NORTE, DireccionVientoSector.desdeGrados(-10.0))
    }
}
