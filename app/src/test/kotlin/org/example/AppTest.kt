package org.example

import condiciones.io.EscritorConsola
import condiciones.io.LectorConsola
import config.ConfigSimulacion
import fisicas.gravedad.FuenteGravedad
import fisicas.gravedad.Jupiter
import fisicas.gravedad.Luna
import fisicas.gravedad.Mercurio
import fisicas.gravedad.Tierra
import fisicas.motor.CalculadorTrayectoria
import fisicas.motor.Simulador
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Suite de pruebas de integración para la aplicación principal [App] y su flujo unificado.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Dependency Inversion Principle (DIP): Se programa contra las abstracciones [CalculadorTrayectoria],
 *   [FuenteGravedad], [LectorConsola] y [EscritorConsola].
 * - Bajo Acoplamiento: Pruebas desacopladas de `System.in` y `System.out`.
 */
class AppTest {

    private class LectorMock(entradas: List<String>) : LectorConsola {
        private val cola = entradas.toMutableList()
        override fun leerLinea(): String? = if (cola.isNotEmpty()) cola.removeAt(0) else null
    }

    private class EscritorMock : EscritorConsola {
        val lineas = mutableListOf<String>()
        override fun escribir(texto: String) { lineas.add(texto) }
        override fun escribirLinea(texto: String) { lineas.add(texto + "\n") }
        fun contenidoCompleto(): String = lineas.joinToString("")
    }

    @Test
    fun testAppInstanciacion() {
        val app = App()
        assertNotNull(app)
    }

    @Test
    fun testFlujoCompletoEjecutarConEntradaConsolaUnificada() {
        // Orden requerido:
        // 1) Entorno gravitacional (Planeta: 1 = Tierra)
        // 2) Velocidad inicial: 30.0 m/s
        // 3) Ángulo de disparo: 45.0°
        // 4) Dirección del viento: 345.0° (NpO: Norte por el Oeste)
        // 5) Velocidad del viento: 25.0 km/h
        // 6) Peso del proyectil: 4.5 kg
        val lector = LectorMock(listOf("1", "30.0", "45.0", "345.0", "25.0", "4.5"))
        val escritor = EscritorMock()
        val app = App(lector = lector, escritor = escritor)

        val resultado = app.ejecutar()

        // Verificación de métricas físicas dinámicas
        assertTrue(resultado.tiempoVuelo > 0.0, "El tiempo de vuelo debe ser mayor a 0")
        assertTrue(resultado.distanciaMaxima > 0.0, "El alcance horizontal debe ser mayor a 0")
        assertTrue(resultado.alturaMaxima > 0.0, "La altura máxima debe ser mayor a 0")
        assertTrue(resultado.desviacionLateralMaxima > 0.0, "El viento lateral en 345° debe desviar en eje Z")
        assertTrue(resultado.trayectoria.isNotEmpty(), "La trayectoria no debe estar vacía")

        val salida = escritor.contenidoCompleto()

        // 1. Verificación de la guía general limpia de cuatro renglones
        assertTrue(salida.contains("Norte: 315° a 45°"), "Debe mostrar 'Norte: 315° a 45°'")
        assertTrue(salida.contains("Este: 45° a 135°"), "Debe mostrar 'Este: 45° a 135°'")
        assertTrue(salida.contains("Sur: 135° a 225°"), "Debe mostrar 'Sur: 135° a 225°'")
        assertTrue(salida.contains("Oeste: 225° a 315°"), "Debe mostrar 'Oeste: 225° a 315°'")

        // 2. Verificación de que NO se imprimieron todos los 32 rumbos detallados en el menú interactivo
        // (es decir, no se imprime la tabla extensa de cuadrantes antes de pedir la dirección)
        assertTrue(!salida.contains("ROSA DE LOS VIENTOS (32 RUMBOS / CUARTAS - 11.25° POR PUNTO)"))

        // 3. Verificación de cálculo preciso del rumbo a 345°: NpO (Norte por el Oeste)
        assertTrue(salida.contains("NpO"), "Debe contener el código NpO")
        assertTrue(salida.contains("Norte por el Oeste"), "Debe contener el nombre 'Norte por el Oeste'")
        assertTrue(salida.contains("Sopla DESDE el rumbo NpO (Norte por el Oeste)"))

        // 4. Verificación de valores dinámicos en el reporte final
        assertTrue(salida.contains("Planeta objetivo: Tierra"), "Debe mostrar el planeta objetivo Tierra")
        assertTrue(
            salida.contains("4.50 kg") || salida.contains("4,50 kg"),
            "Debe mostrar la masa del proyectil formateada (4.50 kg)"
        )
        assertTrue(
            salida.contains("25.0 km/h") || salida.contains("25,0 km/h"),
            "Debe mostrar la velocidad del viento formateada (25.0 km/h)"
        )
        assertTrue(
            salida.contains("345.0°") || salida.contains("345,0°"),
            "Debe mostrar la dirección del viento formateada (345.0°)"
        )

        // 5. Verificación de que se imprimió el gráfico ASCII
        assertTrue(
            salida.contains("--- TRAYECTORIA DEL PROYECTIL (ASCII) ---"),
            "Debe imprimir el encabezado del gráfico ASCII"
        )
    }

    @Test
    fun testCalculoPrecisoViento345GradosNpOEnSolicitarConfiguracion() {
        val lector = LectorMock(listOf("2", "50.0", "60.0", "345.0", "15.0", "2.0"))
        val escritor = EscritorMock()
        val app = App(lector = lector, escritor = escritor)

        val config = app.solicitarConfiguracion()

        assertIs<Luna>(config.fuenteGravedad)
        assertEquals(50.0, config.velocidadInicial)
        assertEquals(60.0, config.angulo)
        assertEquals(2.0, config.masaKg)
        assertEquals(15.0, config.velocidadVientoKmH)
        assertEquals(345.0, config.direccionVientoGrados)

        val viento = config.viento
        assertNotNull(viento)
        assertEquals(345.0, viento.direccionGrados)
        assertEquals(15.0, viento.velocidad)
        assertEquals("NpO", viento.rumbo.codigo)
        assertEquals("Norte por el Oeste", viento.rumbo.nombreCompleto)
        assertEquals(348.75, viento.rumbo.anguloNominal)

        val proyectil = config.proyectil
        assertNotNull(proyectil)
        assertEquals(2.0, proyectil.pesoKg)
        assertEquals(2000.0, proyectil.pesoEnGramos())
    }

    @Test
    fun testFlujoConReintentosYValidaciones() {
        // Entradas con errores que deben reintentarse:
        // Planeta: "invalido", "5", luego "3" (Mercurio)
        // Velocidad: "-10", luego "20.0"
        // Ángulo: "95", luego "30.0"
        // Dirección viento: "-5", luego "90.0" (E / Este)
        // Velocidad viento: "-1", luego "10.0"
        // Peso proyectil: "0", luego "1.5"
        val lector = LectorMock(listOf(
            "invalido", "5", "3",
            "-10", "20.0",
            "95", "30.0",
            "-5", "90.0",
            "-1", "10.0",
            "0", "1.5"
        ))
        val escritor = EscritorMock()
        val app = App(lector = lector, escritor = escritor)

        val config = app.solicitarConfiguracion()

        assertIs<Mercurio>(config.fuenteGravedad)
        assertEquals(20.0, config.velocidadInicial)
        assertEquals(30.0, config.angulo)
        assertEquals(90.0, config.direccionVientoGrados)
        assertEquals(10.0, config.velocidadVientoKmH)
        assertEquals(1.5, config.masaKg)
        assertEquals("E", config.viento?.rumbo?.codigo)
        assertEquals("Este", config.viento?.rumbo?.nombreCompleto)

        val salida = escritor.contenidoCompleto()
        assertTrue(salida.contains("Opción inválida"))
        assertTrue(salida.contains("La velocidad inicial debe ser estrictamente mayor a 0"))
        assertTrue(salida.contains("El ángulo debe estar entre 0 y 90 grados"))
        assertTrue(salida.contains("La dirección del viento debe estar entre 0.0 y 360.0"))
        assertTrue(salida.contains("La velocidad del viento debe ser mayor o igual a 0.0"))
        assertTrue(salida.contains("El peso del proyectil debe ser estrictamente mayor a 0.0 kg"))
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
