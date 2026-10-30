package condiciones

import condiciones.dominio.CondicionesIniciales
import condiciones.dominio.Proyectil
import condiciones.dominio.Viento
import condiciones.io.EscritorConsola
import condiciones.io.EscritorConsolaEstandar
import condiciones.io.LectorConsola
import condiciones.io.LectorConsolaEstandar
import condiciones.servicio.CalculadorRumboRosa32Service
import condiciones.servicio.CalculadorRumboService
import condiciones.ui.PresentadorRosaVientosService
import condiciones.ui.SolicitadorEntradaService

/**
 * Orquestador principal de la aplicación para configurar las condiciones iniciales de un tiro parabólico.
 *
 * Cumplimiento de principios POO y SOLID:
 * - 100% Orientado a Objetos: Todo el comportamiento y punto de entrada residen dentro de clases;
 *   no existen funciones sueltas a nivel de módulo.
 * - Single Responsibility Principle (SRP): Orquesta el flujo de interacción entre la interfaz de usuario,
 *   los servicios de cálculo angular y la generación del modelo de dominio.
 * - Open/Closed Principle (OCP): Permite extender o personalizar el lector, escritor, calculador de rumbos
 *   o presentador mediante inyección de dependencias sin modificar esta clase.
 * - Dependency Inversion Principle (DIP): Depende de las abstracciones [LectorConsola], [EscritorConsola]
 *   y [CalculadorRumboService].
 *
 * Flujo de ejecución del programa:
 * 1. Imprime la lista descriptiva de rangos de la rosa de los vientos (32 puntos y 4 cuadrantes).
 * 2. Solicita interactivamente los 3 datos: dirección del viento (0-360°), velocidad y peso del proyectil.
 * 3. Calcula matemáticamente el rumbo exacto del viento (32 puntos a 11.25° por punto).
 * 4. Imprime el reporte final con el rumbo exacto de origen, velocidad del viento y peso del proyectil.
 */
class ConfiguradorCondicionesApp(
    private val lector: LectorConsola = LECTOR_DEFECTO,
    private val escritor: EscritorConsola = ESCRITOR_DEFECTO,
    private val calculadorRumbo: CalculadorRumboService = CALCULADOR_DEFECTO,
    private val presentador: PresentadorRosaVientosService = PresentadorRosaVientosService(escritor, calculadorRumbo),
    private val solicitador: SolicitadorEntradaService = SolicitadorEntradaService(lector, escritor)
) {

    /**
     * Ejecuta el programa de configuración completo.
     *
     * @return Instancia inmutable de [CondicionesIniciales] configuradas.
     */
    fun ejecutar(): CondicionesIniciales {
        // Paso 1: Imprimir la lista descriptiva de rangos de la rosa de los vientos
        presentador.mostrarRosaDeLosVientos()

        // Paso 2: Solicitar los tres datos al usuario
        escritor.escribirLinea("--- INGRESO DE CONDICIONES INICIALES ---")
        val direccionGrados = solicitador.solicitarDireccionViento()
        val velocidadViento = solicitador.solicitarVelocidadViento()
        val pesoProyectil = solicitador.solicitarPesoProyectil()
        escritor.escribirLinea()

        // Paso 3: Calcular matemáticamente el rumbo exacto del viento (32 puntos, 11.25° c/u)
        val rumboCalculado = calculadorRumbo.calcularRumbo(direccionGrados)
        val viento = Viento(
            direccionGrados = direccionGrados,
            velocidad = velocidadViento,
            rumbo = rumboCalculado
        )
        val proyectil = Proyectil(pesoKg = pesoProyectil)
        val condiciones = CondicionesIniciales(proyectil = proyectil, viento = viento)

        // Paso 4: Imprimir por consola el reporte final
        presentador.mostrarReporteFinal(condiciones)

        return condiciones
    }

    companion object {
        val LECTOR_DEFECTO: LectorConsola = LectorConsolaEstandar()
        val ESCRITOR_DEFECTO: EscritorConsola = EscritorConsolaEstandar()
        val CALCULADOR_DEFECTO: CalculadorRumboService = CalculadorRumboRosa32Service()

        /**
         * Punto de entrada de la aplicación desde línea de comandos.
         */
        @JvmStatic
        fun main(args: Array<String> = emptyArray()) {
            ConfiguradorCondicionesApp().ejecutar()
        }
    }
}
