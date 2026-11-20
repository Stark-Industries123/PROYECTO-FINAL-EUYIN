package fisicas.motor

import config.ConfigSimulacion
import fisicas.gravedad.Tierra
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Pruebas unitarias para [IntegradorCinematica3D] verificando la inversión de dependencias
 * mediante [FabricaEstadoCinematica] y la agrupación en [ParametrosIntegracion].
 */
class IntegradorCinematica3DTest {

    @Test
    fun testIntegradorDelegaEnFabricaAbstraccionYDetieneAlLlegarAReposo() {
        val parametros = ParametrosIntegracion(pasoTiempoMuestreo = 0.1, tiempoMaximo = 5.0)
        val fabricaFake = FabricaEstadoCinematicaFake(subPasosHastaReposo = 150)
        val integrador = IntegradorCinematica3D(parametros = parametros, fabricaEstado = fabricaFake)
        val config = ConfigSimulacion(velocidadInicial = 20.0, angulo = 45.0, fuenteGravedad = Tierra())

        val resultado = integrador.simular(config)

        assertEquals(1, fabricaFake.obtenerLlamadasCrear())
        assertEquals(150, fabricaFake.obtenerSubPasosEjecutados())
        assertEquals(listOf(0.0, 0.1, 0.2), fabricaFake.obtenerTiemposMuestreados())
        assertEquals(0.2, resultado.tiempoVuelo, 1e-9)
    }

    @Test
    fun testIntegradorRespetaLimiteDeTiempoMaximo() {
        val parametros = ParametrosIntegracion(pasoTiempoMuestreo = 0.5, tiempoMaximo = 1.0)
        val fabricaFake = FabricaEstadoCinematicaFake(subPasosHastaReposo = Int.MAX_VALUE)
        val integrador = IntegradorCinematica3D(parametros = parametros, fabricaEstado = fabricaFake)
        val config = ConfigSimulacion(velocidadInicial = 20.0, angulo = 45.0, fuenteGravedad = Tierra())

        val resultado = integrador.simular(config)

        assertEquals(listOf(0.0, 0.5, 1.0), fabricaFake.obtenerTiemposMuestreados())
        assertEquals(1.0, resultado.tiempoVuelo, 1e-9)
    }

    @Test
    fun testFabricaIntegradorCinematica3DEnsamblaSimulacionReal() {
        val integrador = FabricaIntegradorCinematica3D().crear(
            ParametrosIntegracion(coeficienteRestitucion = 0.5, coeficienteArrastre = 0.05, pasoTiempoMuestreo = 0.1)
        )
        val config = ConfigSimulacion(velocidadInicial = 20.0, angulo = 45.0, fuenteGravedad = Tierra())

        val resultado = integrador.simular(config)

        assertTrue(resultado.tiempoVuelo > 0.0)
        assertTrue(resultado.distanciaMaxima > 0.0)
        assertTrue(resultado.alturaMaxima > 0.0)
        assertTrue(resultado.cantidadRebotes >= 1)
    }
}
