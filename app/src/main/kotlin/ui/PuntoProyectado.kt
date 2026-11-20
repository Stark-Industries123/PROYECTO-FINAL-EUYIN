package ui

/**
 * Representa un punto de la trayectoria proyectado a coordenadas de píxeles en pantalla.
 *
 * @property x Coordenada horizontal en píxeles dentro del lienzo.
 * @property y Coordenada vertical en píxeles dentro del lienzo.
 * @property esRebote Indica si el punto corresponde a un impacto contra el suelo.
 */
data class PuntoProyectado(
    val x: Int,
    val y: Int,
    val esRebote: Boolean
) {

    /**
     * Indica si las coordenadas del punto se encuentran dentro de los límites del lienzo.
     */
    fun esVisibleEn(ancho: Int, alto: Int): Boolean = x in 0..ancho && y in 0..alto

    /**
     * Retorna una copia desplazada del punto en píxeles.
     */
    fun desplazado(dx: Int, dy: Int): PuntoProyectado = PuntoProyectado(x + dx, y + dy, esRebote)
}
