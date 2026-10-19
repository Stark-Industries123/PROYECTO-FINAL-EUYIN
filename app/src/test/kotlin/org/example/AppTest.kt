package org.example

import config.ConfigSimulacion
import fisicas.gravedad.FuenteGravedad
import fisicas.gravedad.Tierra
import fisicas.motor.CalculadorTrayectoria
import fisicas.motor.Simulador
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Suite de pruebas de integración para la aplicación y orquestación general.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Dependency Inversion Principle (DIP): Se programa contra las abstracciones [CalculadorTrayectoria]
 *   y [FuenteGravedad], desacoplando los tests de implementaciones concretas directas.
 * - Bajo Acoplamiento (CBO): Se reducen las referencias a tipos concretos externos.
 */
class AppTest {

    @Test
    fun testAppInstanciacion() {
        val app = App()
        assertNotNull(app)
    }

    @Test
    fun testCalculoTrayectoriaTierraConAbstracciones() {
        val gravedad: FuenteGravedad = Tierra()
        val calculador: CalculadorTrayectoria = Simulador(gravedad)
        val config = ConfigSimulacion(velocidadInicial = 20.0, angulo = 45.0, fuenteGravedad = gravedad)

        val resultado = calculador.calcularTrayectoria(config)

        assertTrue(resultado.tiempoVuelo > 0.0, "El tiempo de vuelo debe ser mayor que 0")
        assertTrue(resultado.distanciaMaxima > 0.0, "La distancia máxima debe ser mayor que 0")
        assertTrue(resultado.alturaMaxima > 0.0, "La altura máxima debe ser mayor que 0")
        assertTrue(resultado.trayectoria.isNotEmpty(), "La lista trayectoria no debe estar vacía")

        val primerPunto = resultado.trayectoria.first()
        assertEquals(0.0, primerPunto.tiempo, 0.001)
        assertEquals(0.0, primerPunto.x, 0.001)
        assertEquals(0.0, primerPunto.y, 0.001)
    }

    @Test
    fun testCalculoTrayectoriaAnguloCeroConAbstracciones() {
        val gravedad: FuenteGravedad = Tierra()
        val calculador: CalculadorTrayectoria = Simulador(gravedad)
        val config = ConfigSimulacion(velocidadInicial = 15.0, angulo = 0.0, fuenteGravedad = gravedad)

        val resultado = calculador.calcularTrayectoria(config)

        assertEquals(0.0, resultado.tiempoVuelo, 0.001)
        assertEquals(0.0, resultado.distanciaMaxima, 0.001)
        assertEquals(0.0, resultado.alturaMaxima, 0.001)
        assertTrue(resultado.trayectoria.isNotEmpty())
    }

    @Test
    fun testCalculoTrayectoriaConConfiguracionCompleta() {
        val gravedad: FuenteGravedad = Tierra()
        val calculador: CalculadorTrayectoria = Simulador(gravedad)
        val config = ConfigSimulacion(
            velocidadInicial = 25.0,
            angulo = 45.0,
            fuenteGravedad = gravedad,
            masaKg = 2.5,
            velocidadVientoKmH = 30.0,
            direccionVientoGrados = 90.0
        )

        val resultado = calculador.calcularTrayectoria(config)

        assertTrue(resultado.tiempoVuelo > 0.0)
        assertTrue(resultado.distanciaMaxima > 0.0)
        assertTrue(resultado.alturaMaxima > 0.0)
        assertTrue(resultado.desviacionLateralMaxima > 0.0, "Debe registrar desviación lateral en Z")
    }
}
