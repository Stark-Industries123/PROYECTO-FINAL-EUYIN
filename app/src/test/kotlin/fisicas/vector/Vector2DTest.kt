package fisicas.vector

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class Vector2DTest {

    private val delta = 1e-6

    @Test
    fun `suma de vectores suma componentes correctamente`() {
        val v1 = Vector2D(3.0, 4.0)
        val v2 = Vector2D(1.5, -2.0)
        val suma = v1 + v2

        assertEquals(4.5, suma.x, delta)
        assertEquals(2.0, suma.y, delta)
    }

    @Test
    fun `resta de vectores resta componentes correctamente`() {
        val v1 = Vector2D(5.0, 10.0)
        val v2 = Vector2D(2.0, 3.0)
        val resta = v1 - v2

        assertEquals(3.0, resta.x, delta)
        assertEquals(7.0, resta.y, delta)
    }

    @Test
    fun `multiplicacion por escalar escala componentes`() {
        val v = Vector2D(2.0, -3.0)
        val resultado = v * 2.5

        assertEquals(5.0, resultado.x, delta)
        assertEquals(-7.5, resultado.y, delta)
    }

    @Test
    fun `division por escalar divide componentes`() {
        val v = Vector2D(10.0, -20.0)
        val resultado = v / 2.0

        assertEquals(5.0, resultado.x, delta)
        assertEquals(-10.0, resultado.y, delta)
    }

    @Test
    fun `division por cero lanza IllegalArgumentException`() {
        val v = Vector2D(5.0, 5.0)
        assertThrows<IllegalArgumentException> {
            v / 0.0
        }
    }

    @Test
    fun `vector opuesto invierte los signos`() {
        val v = Vector2D(4.0, -8.0)
        val opuesto = -v

        assertEquals(-4.0, opuesto.x, delta)
        assertEquals(8.0, opuesto.y, delta)
    }

    @Test
    fun `magnitud calcula la norma euclidea 3-4-5`() {
        val v = Vector2D(3.0, 4.0)
        assertEquals(5.0, v.magnitud(), delta)
        assertEquals(25.0, v.magnitudCuadrada(), delta)
    }

    @Test
    fun `normalizar vector produce vector unitario`() {
        val v = Vector2D(0.0, 10.0)
        val unitario = v.normalizar()

        assertEquals(0.0, unitario.x, delta)
        assertEquals(1.0, unitario.y, delta)
        assertEquals(1.0, unitario.magnitud(), delta)
    }

    @Test
    fun `normalizar vector cero retorna vector cero sin NaN`() {
        val cero = Vector2D.CERO.normalizar()
        assertEquals(0.0, cero.x, delta)
        assertEquals(0.0, cero.y, delta)
        assertFalse(cero.x.isNaN())
        assertFalse(cero.y.isNaN())
    }

    @Test
    fun `producto punto calcula escalar correctamente`() {
        val v1 = Vector2D(1.0, 2.0)
        val v2 = Vector2D(3.0, 4.0)
        // 1*3 + 2*4 = 3 + 8 = 11
        assertEquals(11.0, v1.productoPunto(v2), delta)

        // Vectores ortogonales deben dar 0
        val ortogonal1 = Vector2D(1.0, 0.0)
        val ortogonal2 = Vector2D(0.0, 1.0)
        assertEquals(0.0, ortogonal1.productoPunto(ortogonal2), delta)
    }

    @Test
    fun `distancia calcula separacion entre puntos`() {
        val p1 = Vector2D(1.0, 1.0)
        val p2 = Vector2D(4.0, 5.0)
        // sqrt((4-1)^2 + (5-1)^2) = sqrt(9 + 16) = 5
        assertEquals(5.0, p1.distanciaA(p2), delta)
    }
}
