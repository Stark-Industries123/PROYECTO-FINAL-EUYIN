package fisicas.gravedad

import kotlin.test.Test
import kotlin.test.assertTrue

/**
 * Pruebas unitarias para verificar los valores de aceleración gravitatoria
 * y nombres de las diferentes fuentes celestes (DIP / Polimorfismo).
 */
class FuentesGravedadTest {

    @Test
    fun testFuentesGravedadValoresValidos() {
        val planetas: List<FuenteGravedad> = listOf(Tierra(), Luna(), Mercurio(), Jupiter())

        for (planeta in planetas) {
            assertTrue(planeta.gravedad > 0.0, "${planeta.obtenerNombre()} debe tener una gravedad positiva")
            assertTrue(planeta.obtenerNombre().isNotBlank(), "El nombre del cuerpo celeste no debe estar vacío")
        }
    }
}
