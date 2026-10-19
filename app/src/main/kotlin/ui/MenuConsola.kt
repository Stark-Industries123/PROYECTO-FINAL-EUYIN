package ui

import config.ConfigSimulacion
import fisicas.gravedad.FuenteGravedad

/**
 * Interfaz de usuario por consola (CLI) encargada de la interacción con el usuario
 * para la configuración de la simulación de tiro parabólico.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Esta clase tiene una única responsabilidad:
 *   coordinar el flujo de entrada y salida (I/O) en consola. Delega las reglas de validación
 *   y conversión a [ValidadorEntrada].
 * - Baja Complejidad Ciclomática: Cada método está desacoplado y cuenta con una complejidad ciclomática
 *   reducida (<= 2 caminos condicionales por método).
 * - Inversión de Dependencias (DIP) / Testabilidad: Permite la inyección opcional de dependencias
 *   ([lector], [escritor], [escritorLinea], [validador]), facilitando la realización de
 *   pruebas unitarias sin depender de la entrada/salida estándar del sistema (`System.`in` / `System.out`).
 * - Encapsulación: Todo el estado interno y dependencias son estrictamente `private`.
 */
class MenuConsola(
    private val lector: () -> String? = LECTOR_DEFECTO,
    private val escritor: (String) -> Unit = ESCRITOR_DEFECTO,
    private val escritorLinea: (String) -> Unit = ESCRITOR_LINEA_DEFECTO,
    private val validador: ValidadorEntrada = VALIDADOR_DEFECTO
) {

    /**
     * Coordina el flujo de solicitud y validación de todos los parámetros de la simulación,
     * retornando una instancia lista para usar de [ConfigSimulacion].
     *
     * @return Configuración validada de la simulación.
     */
    fun solicitarConfiguracion(): ConfigSimulacion {
        escritorLinea("=== CONFIGURACIÓN DE LA SIMULACIÓN DE TIRO PARABÓLICO ===")
        val planeta = solicitarPlaneta()
        val velocidad = solicitarVelocidadInicial()
        val angulo = solicitarAngulo()

        return ConfigSimulacion(
            velocidadInicial = velocidad,
            angulo = angulo,
            fuenteGravedad = planeta
        )
    }

    /**
     * Muestra el menú de planetas y solicita una opción válida.
     *
     * @return Instancia concreta de [FuenteGravedad].
     */
    fun solicitarPlaneta(): FuenteGravedad {
        while (true) {
            mostrarMenuPlanetas()
            val planeta = validador.parsearPlaneta(lector())
            if (planeta != null) {
                return planeta
            }
            escritorLinea("Opción inválida. Por favor, selecciona un número del 1 al 4.")
        }
    }

    private fun mostrarMenuPlanetas() {
        escritorLinea("\nSelecciona el entorno gravitacional:")
        escritorLinea("1. Tierra")
        escritorLinea("2. Luna")
        escritorLinea("3. Mercurio")
        escritorLinea("4. Júpiter")
        escritor("Opción (1-4): ")
    }

    /**
     * Solicita y valida la velocidad inicial del proyectil (> 0 m/s).
     *
     * @return Magnitud de la velocidad inicial en m/s.
     */
    fun solicitarVelocidadInicial(): Double {
        while (true) {
            escritor("Introduce la velocidad inicial (m/s, > 0): ")
            when (val res = validador.parsearVelocidad(lector())) {
                is ResultadoValidacion.Exito -> return res.valor
                is ResultadoValidacion.Error -> escritorLinea(res.mensaje)
            }
        }
    }

    /**
     * Solicita y valida el ángulo de disparo (0° a 90° inclusive).
     *
     * @return Ángulo de elevación en grados.
     */
    fun solicitarAngulo(): Double {
        while (true) {
            escritor("Introduce el ángulo de disparo (grados, 0-90): ")
            when (val res = validador.parsearAngulo(lector())) {
                is ResultadoValidacion.Exito -> return res.valor
                is ResultadoValidacion.Error -> escritorLinea(res.mensaje)
            }
        }
    }

    /**
     * Solicita y valida la masa del proyectil (> 0 kg).
     *
     * @return Masa del proyectil en kilogramos.
     */
    fun solicitarMasa(): Double {
        while (true) {
            escritor("Introduce la masa del proyectil (kg, > 0): ")
            when (val res = validador.parsearMasa(lector())) {
                is ResultadoValidacion.Exito -> return res.valor
                is ResultadoValidacion.Error -> escritorLinea(res.mensaje)
            }
        }
    }

    /**
     * Solicita y valida la velocidad del viento (>= 0 km/h).
     *
     * @return Velocidad del viento en km/h.
     */
    fun solicitarViento(): Double {
        while (true) {
            escritor("Introduce la velocidad del viento (km/h, >= 0): ")
            when (val res = validador.parsearViento(lector())) {
                is ResultadoValidacion.Exito -> return res.valor
                is ResultadoValidacion.Error -> escritorLinea(res.mensaje)
            }
        }
    }

    /**
     * Solicita y valida la dirección azimutal del viento (0° a 360°).
     *
     * @return Dirección del viento en grados.
     */
    fun solicitarDireccion(): Double {
        while (true) {
            escritor("Introduce la dirección del viento (grados, 0-360): ")
            when (val res = validador.parsearDireccion(lector())) {
                is ResultadoValidacion.Exito -> return res.valor
                is ResultadoValidacion.Error -> escritorLinea(res.mensaje)
            }
        }
    }

    companion object {
        val LECTOR_DEFECTO: () -> String? = { readlnOrNull() }
        val ESCRITOR_DEFECTO: (String) -> Unit = { print(it) }
        val ESCRITOR_LINEA_DEFECTO: (String) -> Unit = { println(it) }
        val VALIDADOR_DEFECTO: ValidadorEntrada = ValidadorEntrada()

        /**
         * Función de conveniencia para solicitar la configuración utilizando la consola por defecto.
         */
        fun solicitarConfiguracion(): ConfigSimulacion = MenuConsola().solicitarConfiguracion()
    }
}
