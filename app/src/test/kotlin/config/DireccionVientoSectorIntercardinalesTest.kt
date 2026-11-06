package config

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Pruebas unitarias para verificar la clasificación de los sectores intercardinales
 * (Noreste, Sureste, Suroeste, Noroeste) de [DireccionVientoSector].
 */
class DireccionVientoSectorIntercardinalesTest {

    @Test
    fun testMapeoNoresteYSureste() {
        assertEquals(DireccionVientoSector.NORESTE, DireccionVientoSector.desdeGrados(30.0))
        assertEquals(DireccionVientoSector.NORESTE, DireccionVientoSector.desdeGrados(45.0))
        assertEquals(DireccionVientoSector.NORESTE, DireccionVientoSector.desdeGrados(59.9))
        assertEquals(DireccionVientoSector.SURESTE, DireccionVientoSector.desdeGrados(120.0))
        assertEquals(DireccionVientoSector.SURESTE, DireccionVientoSector.desdeGrados(135.0))
        assertEquals(DireccionVientoSector.SURESTE, DireccionVientoSector.desdeGrados(149.9))
    }

    @Test
    fun testMapeoSuroesteYNoroeste() {
        assertEquals(DireccionVientoSector.SUROESTE, DireccionVientoSector.desdeGrados(210.0))
        assertEquals(DireccionVientoSector.SUROESTE, DireccionVientoSector.desdeGrados(225.0))
        assertEquals(DireccionVientoSector.SUROESTE, DireccionVientoSector.desdeGrados(239.9))
        assertEquals(DireccionVientoSector.NOROESTE, DireccionVientoSector.desdeGrados(300.0))
        assertEquals(DireccionVientoSector.NOROESTE, DireccionVientoSector.desdeGrados(315.0))
        assertEquals(DireccionVientoSector.NOROESTE, DireccionVientoSector.desdeGrados(329.9))
    }
}
