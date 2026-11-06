package fisicas.motor

import fisicas.fuerzas.CalculadorArrastreAerodinamico
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/**
 * Pruebas unitarias aisladas para el integrador numérico [MotorFisicaEuler2D] y el contrato [MotorFisica2D].
 */
class MotorFisicaEuler2DTest {

    private val delta = 1e-6

    @Test
    fun `integrarPaso actualiza velocidad y posicion mediante Euler-Cromer`() {
        val motor: MotorFisica2D = MotorFisicaEuler2D()
        val inicial = PuntoTrayectoria(tiempo = 0.0, x = 0.0, y = 10.0, vx = 5.0, vy = 0.0)

        // ax = -2.0 m/s², ay = -9.8 m/s², dt = 0.1 s
        // nuevoVx = 5.0 + (-2.0 * 0.1) = 4.8 -> nuevoX = 0.0 + 4.8 * 0.1 = 0.48
        // nuevoVy = 0.0 + (-9.8 * 0.1) = -0.98 -> nuevoY = 10.0 + (-0.98 * 0.1) = 9.902
        val siguiente = motor.integrarPaso(inicial, ax = -2.0, ay = -9.8, dt = 0.1)

        assertEquals(0.1, siguiente.tiempo, delta)
        assertEquals(4.8, siguiente.vx, delta)
        assertEquals(-0.98, siguiente.vy, delta)
        assertEquals(0.48, siguiente.x, delta)
        assertEquals(9.902, siguiente.y, delta)
    }

    @Test
    fun `avanzarEstado permite desplazamiento negativo en X con proyectil liviano y viento extremo`() {
        val calculador = CalculadorArrastreAerodinamico()
        val motor: MotorFisica2D = MotorFisicaEuler2D(calculador)

        var estado = PuntoTrayectoria(tiempo = 0.0, x = 0.0, y = 50.0, vx = 4.0, vy = 10.0)
        val masaLiviana = 0.2
        val gravedad = 9.8
        val vientoExtremoEnContra = -30.0
        val dt = 0.005

        repeat(400) {
            estado = motor.avanzarEstado(estado, masaLiviana, gravedad, vientoExtremoEnContra, dt)
        }

        assertTrue(estado.vx < 0.0, "La velocidad horizontal vx debe invertirse a valores negativos")
        assertTrue(estado.x < 0.0, "La posición X debe desplazarse hacia atrás (X < 0, actual: ${estado.x})")
    }

    @Test
    fun `variacion de distancia en X segun la masa inyectada en el motor fisico`() {
        val motor: MotorFisica2D = MotorFisicaEuler2D(CalculadorArrastreAerodinamico())
        var estadoLiviano = PuntoTrayectoria(tiempo = 0.0, x = 0.0, y = 20.0, vx = 15.0, vy = 10.0)
        var estadoPesado = estadoLiviano

        val vientoEnContra = -20.0
        val dt = 0.02

        repeat(50) {
            estadoLiviano = motor.avanzarEstado(estadoLiviano, masa = 0.2, gravedad = 9.8, vientoX = vientoEnContra, dt = dt)
            estadoPesado = motor.avanzarEstado(estadoPesado, masa = 10.0, gravedad = 9.8, vientoX = vientoEnContra, dt = dt)
        }

        assertTrue(
            estadoPesado.x > estadoLiviano.x,
            "El proyectil más pesado (${estadoPesado.x} m) debe conservar mayor avance en X que el liviano (${estadoLiviano.x} m)"
        )
    }

    @Test
    fun `valida diferencial de tiempo y masa positivos`() {
        val motor = MotorFisicaEuler2D()
        val estado = PuntoTrayectoria(0.0, 0.0, 0.0)

        assertThrows(IllegalArgumentException::class.java) {
            motor.integrarPaso(estado, 0.0, -9.8, dt = 0.0)
        }
        assertThrows(IllegalArgumentException::class.java) {
            motor.avanzarEstado(estado, masa = 0.0, gravedad = 9.8, vientoX = 0.0, dt = 0.01)
        }
    }
}
