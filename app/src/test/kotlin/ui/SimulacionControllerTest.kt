package ui

import config.ConfigSimulacion
import fisicas.motor.CalculadorTrayectoria
import fisicas.motor.PuntoTrayectoria
import fisicas.motor.ResultadoSimulacion
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class SimulacionControllerTest {

    private val calculadorFalso = object : CalculadorTrayectoria {
        override fun calcularTrayectoria(config: ConfigSimulacion): ResultadoSimulacion {
            val puntos = listOf(
                PuntoTrayectoria(0.0, 0.0, 0.0),
                PuntoTrayectoria(1.0, 20.0, 10.0),
                PuntoTrayectoria(2.0, 40.0, 0.0)
            )
            return ResultadoSimulacion(
                tiempoVuelo = 2.0,
                distanciaMaxima = 40.0,
                alturaMaxima = 10.0,
                trayectoria = puntos,
                cantidadRebotes = 1
            )
        }
    }

    @Test
    fun testInicializarVistaYNavegacion() {
        val vista = VistaSimulacionFake()
        val controller = SimulacionController(vista, calculadorFalso)

        controller.inicializarVista()
        assertEquals(SimulacionController.VISTA_MENU_PRINCIPAL, vista.obtenerTexto("vista"))
        assertEquals("Tierra", vista.obtenerTexto("planeta"))

        controller.alAbrirConfiguracion()
        assertEquals(SimulacionController.VISTA_CONFIGURACION, vista.obtenerTexto("vista"))

        controller.alCambiarPlaneta("2")
        assertEquals("Luna", vista.obtenerTexto("planeta"))
    }

    @Test
    fun testIniciarJuegoValidoEjecutaMotorYActualizaVista() {
        val datos = DatosFormularioSimulacion("2", "40.0", "45.0", "2.0", "15.0", 90.0)
        val vista = VistaSimulacionFake(datos)
        val controller = SimulacionController(vista, calculadorFalso)

        val exito = controller.alIniciarJuego()

        assertTrue(exito)
        assertEquals(SimulacionController.VISTA_JUEGO, vista.obtenerTexto("vista"))
        assertEquals("Luna", vista.obtenerTexto("planeta"))
        assertEquals(3, vista.obtenerPuntos().size)
        assertTrue(vista.obtenerTexto("resumen")!!.contains("Planeta: Luna"))
        assertTrue(vista.obtenerTexto("resumen")!!.contains("Alcance: 40.00 m"))
    }

    @Test
    fun testVelocidadSobreLimiteReportaErrorYNoCambiaVista() {
        val datos = DatosFormularioSimulacion("1", "1500.0", "45.0", "1.0", "0.0", 0.0)
        val vista = VistaSimulacionFake(datos)
        val controller = SimulacionController(vista, calculadorFalso)
        controller.inicializarVista()

        val exito = controller.alIniciarJuego()

        assertFalse(exito)
        assertEquals(SimulacionController.VISTA_MENU_PRINCIPAL, vista.obtenerTexto("vista"))
        assertNotNull(vista.obtenerTexto("error"))
        assertTrue(vista.obtenerTexto("error")!!.contains("1000.0"))
    }

    @Test
    fun testFinalizarYCompletarAnimacionMuestraEstadisticas() {
        val vista = VistaSimulacionFake()
        val controller = SimulacionController(vista, calculadorFalso)
        controller.alIniciarJuego()

        controller.alFinalizarSimulacion()
        controller.alCompletarAnimacion()

        assertTrue(vista.fueCompletada())
        val stats = vista.obtenerTexto("estadisticas")
        assertNotNull(stats)
        assertTrue(stats!!.contains("Planeta objetivo: Tierra"))
        assertTrue(stats.contains("Cantidad de rebotes: 1"))
    }
}
