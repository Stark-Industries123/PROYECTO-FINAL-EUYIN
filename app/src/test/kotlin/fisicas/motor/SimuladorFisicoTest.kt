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
 *
 * Verifica los requisitos del Issue 1.1:
 * a) Mapeo correcto de grados a sectores de dirección del viento (incluyendo Norte en 0°, 350° y 20°).
 * b) Un proyectil más pesado recorre más distancia con viento en contra que uno liviano.
 * c) Un viento a 90° (Este) genera desviación en la coordenada Z.
 * d) El proyectil realiza rebotes reduciendo su altura máxima en Y en cada pique.
 */
class SimuladorFisicoTest {

    private val tierra = Tierra()

    @Test
    fun testRequisitoA_MapeoGradosASectoresDireccionViento() {
        // Validación del sector Norte en 0°, 350° y 20°
        val config0 = ConfigSimulacion(20.0, 45.0, tierra, direccionVientoGrados = 0.0)
        assertEquals(DireccionVientoSector.NORTE, config0.sectorViento, "0° debe pertenecer al sector NORTE")

        val config350 = ConfigSimulacion(20.0, 45.0, tierra, direccionVientoGrados = 350.0)
        assertEquals(DireccionVientoSector.NORTE, config350.sectorViento, "350° debe pertenecer al sector NORTE")

        val config20 = ConfigSimulacion(20.0, 45.0, tierra, direccionVientoGrados = 20.0)
        assertEquals(DireccionVientoSector.NORTE, config20.sectorViento, "20° debe pertenecer al sector NORTE")

        // Verificación de los demás sectores cardinales a través de la configuración
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

        // Configuración con viento en contra frontal (0.0° = Norte) de 40 km/h
        val configPesado = ConfigSimulacion(
            velocidadInicial = 30.0,
            angulo = 45.0,
            fuenteGravedad = tierra,
            masaKg = 10.0, // Proyectil pesado: 10 kg
            velocidadVientoKmH = 40.0,
            direccionVientoGrados = 0.0
        )

        val configLiviano = ConfigSimulacion(
            velocidadInicial = 30.0,
            angulo = 45.0,
            fuenteGravedad = tierra,
            masaKg = 0.5, // Proyectil liviano: 0.5 kg
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

        // Disparo con viento a 90° (Este) a 35 km/h
        val configConVientoEste = ConfigSimulacion(
            velocidadInicial = 25.0,
            angulo = 45.0,
            fuenteGravedad = tierra,
            masaKg = 1.0,
            velocidadVientoKmH = 35.0,
            direccionVientoGrados = 90.0 // 90° = Este
        )

        // Disparo idéntico pero sin viento para comparación de línea base
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

        // Sin viento, todas las coordenadas Z deben permanecer en 0.0
        assertTrue(
            resultadoSinViento.trayectoria.all { abs(it.z) < 1e-9 },
            "Sin viento, todas las coordenadas Z deben ser 0.0"
        )
        assertEquals(0.0, resultadoSinViento.desviacionLateralMaxima, 1e-9)

        // Con viento a 90° (Este), debe producirse desviación apreciable en Z
        assertTrue(
            resultadoConViento.desviacionLateralMaxima > 0.5,
            "El viento a 90° debe generar una desviación lateral en Z significativa (> 0.5 m, obtenido: ${resultadoConViento.desviacionLateralMaxima} m)"
        )
        assertTrue(
            resultadoConViento.trayectoria.any { abs(it.z) > 0.1 },
            "La trayectoria debe registrar puntos con coordenada Z diferente de cero"
        )
    }

    @Test
    fun testRequisitoD_RebotesReducenAlturaMaximaEnCadaPique() {
        // Coeficiente de restitución por defecto 0.75 (pierde energía en cada rebote)
        val simulador = Simulador(
            gravedadFoco = tierra,
            coeficienteRestitucion = 0.75
        )

        val config = ConfigSimulacion(
            velocidadInicial = 20.0,
            angulo = 60.0,
            fuenteGravedad = tierra,
            masaKg = 2.0
        )

        val resultado = simulador.calcularTrayectoria(config)

        // Debe haber realizado al menos 2 rebotes
        assertTrue(
            resultado.cantidadRebotes >= 2,
            "El proyectil debe rebotar al menos 2 veces (obtenido: ${resultado.cantidadRebotes})"
        )

        val piques = resultado.alturasMaximasPorPique
        assertTrue(
            piques.size >= 3,
            "Deben registrarse al menos 3 arcos de altura máxima (lanzamiento + piques, obtenido: ${piques.size})"
        )

        // Cada pique debe tener una altura máxima estrictamente menor que el pique anterior
        for (i in 1 until piques.size) {
            val alturaAnterior = piques[i - 1]
            val alturaActual = piques[i]
            assertTrue(
                alturaActual < alturaAnterior,
                "La altura máxima del pique $i ($alturaActual m) debe ser menor que la del pique anterior ($alturaAnterior m)"
            )
        }
    }

    @Test
    fun testEfectoMasaEnDesviacionLateralPorViento() {
        val simulador = Simulador(tierra)

        // A menor masa, mayor desviación por el viento (A = F / m)
        val configLiviano = ConfigSimulacion(
            velocidadInicial = 25.0,
            angulo = 45.0,
            fuenteGravedad = tierra,
            masaKg = 0.2,
            velocidadVientoKmH = 30.0,
            direccionVientoGrados = 90.0
        )

        val configPesado = ConfigSimulacion(
            velocidadInicial = 25.0,
            angulo = 45.0,
            fuenteGravedad = tierra,
            masaKg = 5.0,
            velocidadVientoKmH = 30.0,
            direccionVientoGrados = 90.0
        )

        val resLiviano = simulador.calcularTrayectoria(configLiviano)
        val resPesado = simulador.calcularTrayectoria(configPesado)

        assertTrue(
            resLiviano.desviacionLateralMaxima > resPesado.desviacionLateralMaxima,
            "El proyectil liviano debe desviarse lateralmente más que el pesado ante el mismo viento lateral"
        )
    }

    @Test
    fun testRestitucionCeroNoRealizaRebotes() {
        val simuladorSinRebote = Simulador(
            gravedadFoco = tierra,
            coeficienteRestitucion = 0.0
        )

        val config = ConfigSimulacion(
            velocidadInicial = 20.0,
            angulo = 45.0,
            fuenteGravedad = tierra
        )

        val resultado = simuladorSinRebote.calcularTrayectoria(config)
        assertEquals(0, resultado.cantidadRebotes, "Con restitución 0.0, no debe haber rebotes")
        assertEquals(1, resultado.alturasMaximasPorPique.size, "Solo debe haber un arco de altura máxima")
    }

    @Test
    fun testDIP_AbstraccionCalculadorTrayectoria() {
        // Demuestra que se puede programar contra la interfaz CalculadorTrayectoria
        val calculador: CalculadorTrayectoria = Simulador(tierra)
        val config = ConfigSimulacion(velocidadInicial = 15.0, angulo = 45.0, fuenteGravedad = tierra)
        val resultado = calculador.calcularTrayectoria(config)

        assertTrue(resultado.distanciaMaxima > 0.0)
        assertTrue(resultado.tiempoVuelo > 0.0)
    }

    @Test
    fun testPuntoTrayectoriaEstructura3DYVelocidades() {
        val punto = PuntoTrayectoria(
            tiempo = 1.5,
            x = 12.0,
            y = 8.5,
            z = -1.2,
            vx = 8.0,
            vy = -2.5,
            vz = -0.8
        )

        assertEquals(1.5, punto.tiempo)
        assertEquals(1.5, punto.t, "El alias t debe coincidir con tiempo")
        assertEquals(12.0, punto.x)
        assertEquals(8.5, punto.y)
        assertEquals(-1.2, punto.z)
        assertEquals(8.0, punto.vx)
        assertEquals(-2.5, punto.vy)
        assertEquals(-0.8, punto.vz)
    }
}
