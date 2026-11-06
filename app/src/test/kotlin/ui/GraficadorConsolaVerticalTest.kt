package ui

import config.ConfigSimulacion
import fisicas.gravedad.Tierra
import fisicas.motor.PuntoTrayectoria
import fisicas.motor.Simulador
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Pruebas unitarias para verificar la proyección vertical (90°) en el lienzo ASCII.
 */
class GraficadorConsolaVerticalTest {

    @Test
    fun testTiroVerticalAngulo90() {
        val graficador = GraficadorConsola(filas = 5, columnas = 10, caracterProyectil = '*', caracterFondo = '.')
        val puntos = (0..4).map { PuntoTrayectoria(tiempo = it * 0.5, x = 0.0, y = it * 5.0) }
        val lienzo = graficador.renderizar(puntos)
        assertEquals(5, lienzo.size)
        for ((indice, fila) in lienzo.withIndex()) {
            assertEquals('*', fila[0], "La fila $indice debe tener un asterisco en la columna 0")
            assertTrue(fila.substring(1).all { it == '.' })
        }
    }

    @Test
    fun testTiroVerticalConSimuladorAngulo90GeneraLineaVertical() {
        val gravedad = Tierra()
        val calculador = Simulador(gravedad)
        val config = ConfigSimulacion(velocidadInicial = 20.0, angulo = 90.0, fuenteGravedad = gravedad)
        val resultado = calculador.calcularTrayectoria(config)
        val graficador = GraficadorConsola(filas = 10, columnas = 20, caracterProyectil = '*', caracterFondo = '.')
        val lienzo = graficador.renderizar(resultado.trayectoria)
        assertEquals(10, lienzo.size)
        for ((indice, fila) in lienzo.withIndex()) {
            assertEquals('*', fila[0], "La fila $indice debe contener un asterisco en la columna 0")
            assertTrue(fila.substring(1).all { it == '.' })
        }
    }
}
