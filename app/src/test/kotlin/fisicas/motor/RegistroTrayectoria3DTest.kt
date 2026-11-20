package fisicas.motor

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Pruebas unitarias para [RegistroTrayectoria3D].
 */
class RegistroTrayectoria3DTest {

    @Test
    fun testConstruyeResultadoConRebotesYPiques() {
        val registro = RegistroTrayectoria3D()
        registro.registrarPunto(PuntoTrayectoria(tiempo = 0.0, x = 0.0, y = 0.0, z = 0.0))
        registro.registrarSubPaso(alturaActual = 10.0, codigoContacto = 0)
        registro.registrarSubPaso(alturaActual = 0.0, codigoContacto = 1)
        registro.registrarSubPaso(alturaActual = 5.0, codigoContacto = 0)
        registro.registrarSubPaso(alturaActual = 0.0, codigoContacto = -1)
        registro.registrarPunto(PuntoTrayectoria(tiempo = 2.4, x = 25.0, y = 0.0, z = -3.5))

        val resultado = registro.construirResultado()
        assertEquals(2.4, resultado.tiempoVuelo, 1e-9)
        assertEquals(25.0, resultado.distanciaMaxima, 1e-9)
        assertEquals(10.0, resultado.alturaMaxima, 1e-9)
        assertEquals(1, resultado.cantidadRebotes)
        assertEquals(3.5, resultado.desviacionLateralMaxima, 1e-9)
        assertEquals(listOf(10.0, 5.0), resultado.alturasMaximasPorPique)
    }

    @Test
    fun testSinRebotesConservaAlturaMaximaComoUnicoPiqueYAlcanceNegativo() {
        val registro = RegistroTrayectoria3D()
        registro.registrarPunto(PuntoTrayectoria(tiempo = 0.0, x = 0.0, y = 0.0))
        registro.registrarSubPaso(alturaActual = 4.2, codigoContacto = 0)
        registro.registrarPunto(PuntoTrayectoria(tiempo = 1.0, x = -8.5, y = 0.0))

        val resultado = registro.construirResultado()
        assertEquals(0, resultado.cantidadRebotes)
        assertEquals(-8.5, resultado.distanciaMaxima, 1e-9)
        assertEquals(listOf(4.2), resultado.alturasMaximasPorPique)
    }
}
