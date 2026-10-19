package ui

import config.ConfigSimulacion
import fisicas.gravedad.FuenteGravedad
import fisicas.gravedad.Tierra
import fisicas.motor.CalculadorTrayectoria
import fisicas.motor.PuntoTrayectoria
import fisicas.motor.Simulador
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Pruebas unitarias para verificar la proyección vertical (90°) en el lienzo ASCII,
 * validando el desacoplamiento mediante abstracciones (DIP).
 */
class GraficadorConsolaVerticalTest {

    @Test
    fun testTiroVerticalAngulo90() {
        val graficador: GraficadorConsola = GraficadorConsola(filas = 5, columnas = 10, caracterProyectil = '*', caracterFondo = '.')
        val puntos = listOf(
            PuntoTrayectoria(tiempo = 0.0, x = 0.0, y = 0.0),
            PuntoTrayectoria(tiempo = 0.5, x = 0.0, y = 5.0),
            PuntoTrayectoria(tiempo = 1.0, x = 0.0, y = 10.0),
            PuntoTrayectoria(tiempo = 1.5, x = 0.0, y = 15.0),
            PuntoTrayectoria(tiempo = 2.0, x = 0.0, y = 20.0),
            PuntoTrayectoria(tiempo = 2.5, x = 0.0, y = 15.0),
            PuntoTrayectoria(tiempo = 3.0, x = 0.0, y = 10.0),
            PuntoTrayectoria(tiempo = 3.5, x = 0.0, y = 5.0),
            PuntoTrayectoria(tiempo = 4.0, x = 0.0, y = 0.0)
        )

        val lienzo = graficador.renderizar(puntos)

        assertEquals(5, lienzo.size)
        for ((indice, fila) in lienzo.withIndex()) {
            assertEquals('*', fila[0], "La fila $indice debe tener un asterisco en la columna 0")
            assertTrue(fila.substring(1).all { it == '.' }, "Las columnas > 0 en fila $indice deben ser solo fondo")
        }
    }

    @Test
    fun testTiroVerticalConSimuladorAngulo90GeneraLineaVertical() {
        val gravedad: FuenteGravedad = Tierra()
        val calculador: CalculadorTrayectoria = Simulador(gravedad)
        val config = ConfigSimulacion(velocidadInicial = 20.0, angulo = 90.0, fuenteGravedad = gravedad)
        val resultado = calculador.calcularTrayectoria(config)

        val graficador: GraficadorConsola = GraficadorConsola(filas = 10, columnas = 20, caracterProyectil = '*', caracterFondo = '.')
        val lienzo = graficador.renderizar(resultado.trayectoria)

        assertEquals(10, lienzo.size)
        for ((indice, fila) in lienzo.withIndex()) {
            assertEquals('*', fila[0], "La fila $indice debe contener un asterisco en la columna 0")
            assertTrue(fila.substring(1).all { it == '.' }, "La fila $indice no debe proyectar asteriscos en columnas > 0")
        }
    }
}
