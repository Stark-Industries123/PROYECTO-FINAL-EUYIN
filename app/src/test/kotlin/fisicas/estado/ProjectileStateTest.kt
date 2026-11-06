package fisicas.estado

import fisicas.vector.Vector2D
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ProjectileStateTest {

    private val delta = 1e-6

    @Test
    fun testRapidezCalculaMagnitudDeVelocidad() {
        val estado = ProjectileState(Vector2D(0.0, 10.0), Vector2D(3.0, 4.0), tiempo = 1.0)
        assertEquals(5.0, estado.rapidez, delta)
        assertFalse(estado.estaEnSuelo)
        assertFalse(estado.estaEnReposo)
    }

    @Test
    fun testEstaEnSueloDetectaCotaCeroONegativa() {
        val estadoEnAire = ProjectileState(Vector2D(5.0, 0.1 + delta), Vector2D(1.0, 0.0))
        assertFalse(estadoEnAire.estaEnSuelo)
        val estadoEnSuelo = ProjectileState(Vector2D(5.0, 0.0), Vector2D(1.0, 0.0))
        assertTrue(estadoEnSuelo.estaEnSuelo)
        val estadoBajoSuelo = ProjectileState(Vector2D(5.0, -0.05), Vector2D(1.0, 0.0))
        assertTrue(estadoBajoSuelo.estaEnSuelo)
    }

    @Test
    fun testAvanzarProduceNuevoEstadoInmutable() {
        val inicial = ProjectileState(Vector2D(0.0, 0.0), Vector2D(10.0, 20.0), tiempo = 0.0)
        val siguiente = inicial.avanzar(Vector2D(1.0, 2.0), Vector2D(10.0, 18.0), deltaTiempo = 0.1)
        assertEquals(1.0, siguiente.posicion.x, delta)
        assertEquals(2.0, siguiente.posicion.y, delta)
        assertEquals(10.0, siguiente.velocidad.x, delta)
        assertEquals(18.0, siguiente.velocidad.y, delta)
        assertEquals(0.1, siguiente.tiempo, delta)
        assertEquals(0.0, inicial.tiempo, delta)
    }

    @Test
    fun testDetenerAnulaVelocidadYFijaReposo() {
        val enMovimiento = ProjectileState(Vector2D(15.0, -0.2), Vector2D(2.0, -1.0), tiempo = 3.5)
        val detenido = enMovimiento.detener()
        assertEquals(15.0, detenido.posicion.x, delta)
        assertEquals(0.0, detenido.posicion.y, delta)
        assertEquals(0.0, detenido.velocidad.x, delta)
        assertEquals(0.0, detenido.velocidad.y, delta)
        assertEquals(3.5, detenido.tiempo, delta)
        assertTrue(detenido.estaEnReposo)
    }
}
