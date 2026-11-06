package ui

import fisicas.gravedad.FuenteGravedad

/**
 * Contrato de abstracción para la validación y parseo de entradas de usuario en consola.
 */
interface ValidadorEntradaContract {

    /**
     * Parsea una opción numérica de texto al cuerpo celeste correspondiente.
     */
    fun parsearPlaneta(entrada: String?): FuenteGravedad?

    /**
     * Valida y parsea la magnitud de la velocidad inicial (> 0 m/s).
     */
    fun parsearVelocidad(entrada: String?): ResultadoValidacion<Double>

    /**
     * Valida y parsea el ángulo de elevación (0° a 90° inclusive).
     */
    fun parsearAngulo(entrada: String?): ResultadoValidacion<Double>

    /**
     * Valida y parsea la masa del proyectil (> 0 kg).
     */
    fun parsearMasa(entrada: String?): ResultadoValidacion<Double>

    /**
     * Valida y parsea la velocidad del viento (>= 0 km/h).
     */
    fun parsearViento(entrada: String?): ResultadoValidacion<Double>

    /**
     * Valida y parsea la dirección azimutal del viento en grados (0° a 360°).
     */
    fun parsearDireccion(entrada: String?): ResultadoValidacion<Double>
}
