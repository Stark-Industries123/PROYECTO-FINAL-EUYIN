package ui

/**
 * Representa una marca graduada de la regla vertical de altura HUD en coordenadas de pantalla.
 *
 * @property posicionY Posición vertical en píxeles de la marca sobre el lienzo.
 * @property alturaMetros Altura física en metros asociada a la marca.
 */
data class MarcaAlturaHud(
    val posicionY: Int,
    val alturaMetros: Int
) {

    /**
     * Retorna la etiqueta formateada en metros para renderizar junto a la marca.
     */
    fun etiqueta(): String = "$alturaMetros m"

    /**
     * Indica si la marca cae dentro del tramo vertical comprendido entre [topeY] y [sueloY].
     */
    fun estaEnRango(topeY: Int, sueloY: Int): Boolean = posicionY in topeY..sueloY
}
