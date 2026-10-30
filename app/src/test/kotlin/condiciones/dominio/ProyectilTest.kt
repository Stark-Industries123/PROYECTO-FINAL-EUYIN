package condiciones.dominio

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

/**
 * Pruebas unitarias para el modelo de dominio [Proyectil].
 */
class ProyectilTest {

    @Test
    fun testCreacionValidaProyectil() {
        val proyectil = Proyectil(pesoKg = 5.0)
        assertEquals(5.0, proyectil.pesoKg)
        assertEquals(5000.0, proyectil.pesoEnGramos(), 0.001)
        assertEquals(11.0231, proyectil.pesoEnLibras(), 0.01)
    }

    @Test
    fun testValidacionPesoEstrictamentePositivo() {
        assertFailsWith<IllegalArgumentException> {
            Proyectil(pesoKg = 0.0)
        }
        assertFailsWith<IllegalArgumentException> {
            Proyectil(pesoKg = -2.5)
        }
    }

    @Test
    fun testCalculoEnergiaCinetica() {
        val proyectil = Proyectil(pesoKg = 2.0)
        // Ec = 0.5 * 2.0 * 10^2 = 100 J
        val ec = proyectil.calcularEnergiaCinetica(10.0)
        assertEquals(100.0, ec, 0.001)
    }

    @Test
    fun testDescripcionFormateada() {
        val proyectil = Proyectil(pesoKg = 1.5)
        val descripcion = proyectil.descripcionFormateada()
        assertTrue(descripcion.contains("1.50 kg"))
        assertTrue(descripcion.contains("1500.0 g"))
    }
}
