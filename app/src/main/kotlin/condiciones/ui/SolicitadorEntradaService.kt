package condiciones.ui

import condiciones.io.EscritorConsola
import condiciones.io.LectorConsola
import fisicas.gravedad.FuenteGravedad
import ui.ValidadorEntrada

/**
 * Servicio encargado de la captura interactiva y validación robusta de datos ingresados por consola.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Responsable exclusivamente del flujo de captura de datos,
 *   parseo numérico, control de reintentos mediante bucles y gestión de excepciones ante entradas no válidas.
 * - Dependency Inversion Principle (DIP): Depende de las abstracciones [LectorConsola] y [EscritorConsola].
 * - Don't Repeat Yourself (DRY): Centraliza el bucle de validación y captura de excepciones en un método plantilla privado.
 * - Baja Complejidad Ciclomática: Métodos divididos en responsabilidades elementales.
 */
class SolicitadorEntradaService(
    private val lector: LectorConsola,
    private val escritor: EscritorConsola,
    private val validador: ValidadorEntrada = ValidadorEntrada()
) {

    /**
     * Solicita interactivamente la selección del entorno gravitacional (cuerpo celeste).
     *
     * @return [FuenteGravedad] seleccionada por el usuario.
     */
    fun solicitarPlaneta(): FuenteGravedad {
        while (true) {
            escritor.escribirLinea("\nSelecciona el entorno gravitacional:")
            escritor.escribirLinea("1. Tierra")
            escritor.escribirLinea("2. Luna")
            escritor.escribirLinea("3. Mercurio")
            escritor.escribirLinea("4. Júpiter")
            escritor.escribir("Opción (1-4): ")

            val entrada = try {
                lector.leerLinea()?.trim()
            } catch (e: Exception) {
                escritor.escribirLinea("Error de lectura: ${e.message ?: "Entrada inválida"}. Intente nuevamente.")
                continue
            }

            if (entrada == null) {
                escritor.escribirLinea("Error: Fin de la entrada de datos (EOF). No se puede continuar.")
                throw IllegalStateException("Fin de flujo de entrada alcanzado.")
            }

            val planeta = validador.parsearPlaneta(entrada)
            if (planeta != null) {
                return planeta
            }

            escritor.escribirLinea("Opción inválida. Por favor, selecciona un número del 1 al 4.")
        }
    }

    /**
     * Solicita la velocidad inicial de lanzamiento en m/s (> 0.0).
     *
     * @return Velocidad validada en m/s.
     */
    fun solicitarVelocidadInicial(): Double {
        return solicitarDoubleValido(
            mensaje = "Ingrese la velocidad inicial (m/s, > 0): "
        ) { valor ->
            if (valor <= 0.0) {
                "Error: La velocidad inicial debe ser estrictamente mayor a 0 (> 0)."
            } else {
                null
            }
        }
    }

    /**
     * Solicita el ángulo de elevación de disparo en grados sexagesimales ([0.0, 90.0]).
     *
     * @return Ángulo validado en grados.
     */
    fun solicitarAnguloDisparo(): Double {
        return solicitarDoubleValido(
            mensaje = "Ingrese el ángulo de disparo (grados, 0-90): "
        ) { valor ->
            if (valor < 0.0 || valor > 90.0) {
                "Error: El ángulo debe estar entre 0 y 90 grados inclusive."
            } else {
                null
            }
        }
    }

    /**
     * Alias de conveniencia para solicitar el ángulo de disparo.
     */
    fun solicitarAngulo(): Double = solicitarAnguloDisparo()

    /**
     * Solicita la dirección del viento en grados azimutales (rango [0.0, 360.0]),
     * imprimiendo previamente la guía general limpia de cuatro renglones.
     *
     * @param presentador Presentador opcional para delegar la impresión de la guía.
     * @return Dirección validada en grados.
     */
    fun solicitarDireccionVientoConGuia(presentador: PresentadorRosaVientosService? = null): Double {
        if (presentador != null) {
            presentador.mostrarGuiaGeneralViento()
        } else {
            escritor.escribirLinea("Norte: 315° a 45°")
            escritor.escribirLinea("Este: 45° a 135°")
            escritor.escribirLinea("Sur: 135° a 225°")
            escritor.escribirLinea("Oeste: 225° a 315°")
        }
        return solicitarDireccionViento()
    }

    /**
     * Solicita la dirección del viento en grados azimutales (rango [0.0, 360.0]).
     *
     * @return Dirección validada en grados.
     */
    fun solicitarDireccionViento(): Double {
        return solicitarDoubleValido(
            mensaje = "Ingrese la dirección del viento en grados (0-360): "
        ) { valor ->
            if (valor < 0.0 || valor > 360.0) {
                "Error: La dirección del viento debe estar entre 0.0 y 360.0 (ingresado: $valor)."
            } else {
                null
            }
        }
    }

    /**
     * Solicita la velocidad del viento (>= 0.0 km/h).
     *
     * @return Velocidad validada en km/h.
     */
    fun solicitarVelocidadViento(): Double {
        return solicitarDoubleValido(
            mensaje = "Ingrese la velocidad del viento (km/h, >= 0): "
        ) { valor ->
            if (valor < 0.0) {
                "Error: La velocidad del viento debe ser mayor o igual a 0.0 (ingresado: $valor)."
            } else {
                null
            }
        }
    }

    /**
     * Solicita el peso del proyectil en kilogramos (> 0.0 kg).
     *
     * @return Peso del proyectil validado en kg.
     */
    fun solicitarPesoProyectil(): Double {
        return solicitarDoubleValido(
            mensaje = "Ingrese el peso del proyectil en kg (> 0): "
        ) { valor ->
            if (valor <= 0.0) {
                "Error: El peso del proyectil debe ser estrictamente mayor a 0.0 kg (ingresado: $valor)."
            } else {
                null
            }
        }
    }

    /**
     * Bucle general de captura interactiva que gestiona excepciones de lectura y parseo numérico.
     *
     * @param mensaje Mensaje de indicación (prompt) mostrado al usuario.
     * @param validacionAdicional Regla de validación de negocio sobre el valor numérico obtenido; retorna mensaje de error o null si es válido.
     * @return Valor [Double] validado.
     */
    private fun solicitarDoubleValido(
        mensaje: String,
        validacionAdicional: (Double) -> String?
    ): Double {
        while (true) {
            escritor.escribir(mensaje)

            val entrada = try {
                lector.leerLinea()?.trim()
            } catch (e: Exception) {
                escritor.escribirLinea("Error de lectura: ${e.message ?: "Entrada inválida"}. Intente nuevamente.")
                continue
            }

            if (entrada == null) {
                escritor.escribirLinea("Error: Fin de la entrada de datos (EOF). No se puede continuar.")
                throw IllegalStateException("Fin de flujo de entrada alcanzado.")
            }

            val valor = try {
                entrada.toDouble()
            } catch (e: NumberFormatException) {
                escritor.escribirLinea("Error: Debe ingresar un valor numérico válido.")
                continue
            }

            val mensajeErrorValidacion = validacionAdicional(valor)
            if (mensajeErrorValidacion != null) {
                escritor.escribirLinea(mensajeErrorValidacion)
                continue
            }

            return valor
        }
    }
}
