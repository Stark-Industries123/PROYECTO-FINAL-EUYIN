package fisicas.motor

import config.ConfigSimulacion
import config.DireccionVientoSector
import fisicas.gravedad.Tierra
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Suite de pruebas unitarias para el motor físico [Simulador] y el contrato [CalculadorTrayectoria].
 */
class SimuladorFisicoTest {

    private val tierra = Tierra()

    @Test
    fun testRequisitoA_MapeoGradosASectoresDireccionViento() {
        val config0 = ConfigSimulacion(20.0, 45.0, tierra, direccionVientoGrados = 0.0)
        assertEquals(DireccionVientoSector.NORTE, config0.sectorViento, "0° debe pertenecer al sector NORTE")

        val config350 = ConfigSimulacion(20.0, 45.0, tierra, direccionVientoGrados = 350.0)
        assertEquals(DireccionVientoSector.NORTE, config350.sectorViento, "350° debe pertenecer al sector NORTE")

        val config20 = ConfigSimulacion(20.0, 45.0, tierra, direccionVientoGrados = 20.0)
        assertEquals(DireccionVientoSector.NORTE, config20.sectorViento, "20° debe pertenecer al sector NORTE")

        val configEste = ConfigSimulacion(20.0, 45.0, tierra, direccionVientoGrados = 90.0)
        assertEquals(DireccionVientoSector.ESTE, configEste.sectorViento, "90° debe pertenecer al sector ESTE")

        val configSur = ConfigSimulacion(20.0, 45.0, tierra, direccionVientoGrados = 180.0)
        assertEquals(DireccionVientoSector.SUR, configSur.sectorViento, "180° debe pertenecer al sector SUR")

        val configOeste = ConfigSimulacion(20.0, 45.0, tierra, direccionVientoGrados = 270.0)
        assertEquals(DireccionVientoSector.OESTE, configOeste.sectorViento, "270° debe pertenecer al sector OESTE")
    }

    @Test
    fun testRequisitoB_ProyectilMasPesadoRecorreMasDistanciaConVientoEnContraQueUnoLiviano() {
        val simulador = Simulador(tierra)

        val configPesado = ConfigSimulacion(
            velocidadInicial = 30.0,
            angulo = 45.0,
            fuenteGravedad = tierra,
            masaKg = 10.0,
            velocidadVientoKmH = 40.0,
            direccionVientoGrados = 0.0
        )

        val configLiviano = ConfigSimulacion(
            velocidadInicial = 30.0,
            angulo = 45.0,
            fuenteGravedad = tierra,
            masaKg = 0.5,
            velocidadVientoKmH = 40.0,
            direccionVientoGrados = 0.0
        )

        val resultadoPesado = simulador.calcularTrayectoria(configPesado)
        val resultadoLiviano = simulador.calcularTrayectoria(configLiviano)

        assertTrue(
            resultadoPesado.distanciaMaxima > resultadoLiviano.distanciaMaxima,
            "El proyectil pesado (${resultadoPesado.distanciaMaxima} m) debe recorrer más distancia frontal en X " +
                "que el proyectil liviano (${resultadoLiviano.distanciaMaxima} m) ante viento en contra"
        )
    }

    @Test
    fun testRequisitoC_Viento90EsteGeneraDesviacionEnCoordenadaZ() {
        val simulador = Simulador(tierra)

        val configConVientoEste = ConfigSimulacion(
            velocidadInicial = 25.0,
            angulo = 45.0,
            fuenteGravedad = tierra,
            masaKg = 1.0,
            velocidadVientoKmH = 35.0,
            direccionVientoGrados = 90.0
        )

        val configSinViento = ConfigSimulacion(
            velocidadInicial = 25.0,
            angulo = 45.0,
            fuenteGravedad = tierra,
            masaKg = 1.0,
            velocidadVientoKmH = 0.0,
            direccionVientoGrados = 0.0
        )

        val resultadoConViento = simulador.calcularTrayectoria(configConVientoEste)
        val resultadoSinViento = simulador.calcularTrayectoria(configSinViento)

        assertTrue(
            resultadoSinViento.trayectoria.all { abs(it.z) < 1e-9 },
            "Sin viento, todas las coordenadas Z deben ser 0.0"
        )
        assertEquals(0.0, resultadoSinViento.desviacionLateralMaxima, 1e-9)

        assertTrue(
            resultadoConViento.desviacionLateralMaxima > 0.5,
            "El viento a 90° debe generar una desviación lateral en Z significativa"
        )
    }

    @Test
    fun testRequisitoD_RebotesReducenAlturaMaximaEnCadaPique() {
        val simulador = Simulador(gravedadFoco = tierra, coeficienteRestitucion = 0.75)
        val config = ConfigSimulacion(velocidadInicial = 20.0, angulo = 60.0, fuenteGravedad = tierra, masaKg = 2.0)

        val resultado = simulador.calcularTrayectoria(config)

        assertTrue(resultado.cantidadRebotes >= 2)
        val piques = resultado.alturasMaximasPorPique
        assertTrue(piques.size >= 3)

        for (i in 1 until piques.size) {
            assertTrue(piques[i] < piques[i - 1])
        }
    }

    @Test
    fun testEfectoMasaEnDesviacionLateralPorViento() {
        val simulador = Simulador(tierra)
        val configLiviano = ConfigSimulacion(25.0, 45.0, tierra, masaKg = 0.2, velocidadVientoKmH = 30.0, direccionVientoGrados = 90.0)
        val configPesado = ConfigSimulacion(25.0, 45.0, tierra, masaKg = 5.0, velocidadVientoKmH = 30.0, direccionVientoGrados = 90.0)

        val resLiviano = simulador.calcularTrayectoria(configLiviano)
        val resPesado = simulador.calcularTrayectoria(configPesado)

        assertTrue(resLiviano.desviacionLateralMaxima > resPesado.desviacionLateralMaxima)
    }

    @Test
    fun testRestitucionCeroNoRealizaRebotesYCalculadorTrayectoriaDIP() {
        val calculador: CalculadorTrayectoria = Simulador(gravedadFoco = tierra, coeficienteRestitucion = 0.0)
        val config = ConfigSimulacion(velocidadInicial = 20.0, angulo = 45.0, fuenteGravedad = tierra)

        val resultado = calculador.calcularTrayectoria(config)
        assertEquals(0, resultado.cantidadRebotes)
        assertEquals(1, resultado.alturasMaximasPorPique.size)
        assertTrue(resultado.distanciaMaxima > 0.0)
    }
}
