package ui

import config.ConfigSimulacion
import fisicas.gravedad.*

/**
 * Interfaz de usuario por consola (CLI) encargada de la interacción con el usuario
 * para la configuración de la simulación de tiro parabólico.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Esta clase tiene una única responsabilidad:
 *   solicitar, validar y recopilar los datos requeridos por la consola para construir y retornar
 *   un objeto inmutable [ConfigSimulacion]. No contiene lógica de cálculo físico ni persistencia.
 * - Robustez y Exception-Safety: Se emplean funciones seguras de conversión como [toDoubleOrNull]
 *   y [toIntOrNull] dentro de bucles interactivos `while`, evitando caídas del programa por
 *   excepciones de formato numérico ([NumberFormatException]) ante entradas no válidas.
 * - Inversión de Dependencias (DIP) / Testabilidad: Permite la inyección opcional de lambdas
 *   de lectura y escritura ([lector], [escritor], [escritorLinea]), facilitando la realización de
 *   pruebas unitarias sin depender de la entrada/salida estándar del sistema (`System.`in` / `System.out`).
 */
class MenuConsola(
    private val lector: () -> String? = { readlnOrNull() },
    private val escritor: (String) -> Unit = { print(it) },
    private val escritorLinea: (String) -> Unit = { println(it) }
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
     * Muestra el menú numerado de planetas y solicita una opción válida (1-4).
     * Mapea la opción numérica a su respectiva instancia de [FuenteGravedad].
     *
     * @return Instancia concreta de [FuenteGravedad].
     */
    fun solicitarPlaneta(): FuenteGravedad {
        while (true) {
            escritorLinea("\nSelecciona el entorno gravitacional:")
            escritorLinea("1. Tierra")
            escritorLinea("2. Luna")
            escritorLinea("3. Mercurio")
            escritorLinea("4. Júpiter")
            escritor("Opción (1-4): ")

            val entrada = lector()?.trim()
            val opcion = entrada?.toIntOrNull()

            when (opcion) {
                1 -> return Tierra()
                2 -> return Luna()
                3 -> return Mercurio()
                4 -> return Jupiter()
                else -> escritorLinea("Opción inválida. Por favor, selecciona un número del 1 al 4.")
            }
        }
    }

    /**
     * Solicita y valida la velocidad inicial del proyectil.
     * Debe ser un número de tipo [Double] estrictamente mayor a 0 (> 0).
     *
     * @return Magnitud de la velocidad inicial en m/s.
     */
    fun solicitarVelocidadInicial(): Double {
        while (true) {
            escritor("Introduce la velocidad inicial (m/s, > 0): ")
            val entrada = lector()?.trim()
            val valor = entrada?.toDoubleOrNull()

            if (valor == null) {
                escritorLinea("Entrada inválida. Debes ingresar un número válido (ej: 20.0).")
            } else if (valor <= 0.0) {
                escritorLinea("Error: La velocidad inicial debe ser estrictamente mayor a 0 (> 0).")
            } else {
                return valor
            }
        }
    }

    /**
     * Solicita y valida el ángulo de disparo.
     * Debe ser un número de tipo [Double] comprendido entre 0° y 90° inclusive (0 <= ángulo <= 90).
     *
     * @return Ángulo de elevación en grados.
     */
    fun solicitarAngulo(): Double {
        while (true) {
            escritor("Introduce el ángulo de disparo (grados, 0-90): ")
            val entrada = lector()?.trim()
            val valor = entrada?.toDoubleOrNull()

            if (valor == null) {
                escritorLinea("Entrada inválida. Debes ingresar un número válido (ej: 45.0).")
            } else if (valor < 0.0 || valor > 90.0) {
                escritorLinea("Error: El ángulo debe estar entre 0 y 90 grados inclusive.")
            } else {
                return valor
            }
        }
    }

    companion object {
        /**
         * Función de conveniencia para solicitar la configuración utilizando la consola por defecto.
         */
        fun solicitarConfiguracion(): ConfigSimulacion = MenuConsola().solicitarConfiguracion()
    }
}
