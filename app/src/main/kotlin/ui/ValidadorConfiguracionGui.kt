package ui

import config.ConfigSimulacion

/**
 * Contrato para la validación y actualización de parámetros ingresados en el formulario Swing.
 *
 * Desacopla la ejecución de la simulación de los controles concretos del formulario (DIP/ISP).
 */
interface ValidadorConfiguracionGui {

    /**
     * Sincroniza la selección inicial del formulario con la gravedad configurada y retorna su nombre.
     */
    fun sincronizarPlanetaInicial(): String

    /**
     * Actualiza la gravedad activa según el índice seleccionado y retorna el nombre del planeta.
     */
    fun actualizarPlanetaPorIndice(indice: Int): String

    /**
     * Actualiza los valores de texto del formulario y opcionalmente la dirección del viento en grados.
     */
    fun actualizarEntradas(valores: Map<String, String>, direccionVientoGrados: Double? = null)

    /**
     * Valida y construye la [ConfigSimulacion] actual; ante un error notifica mediante [alError].
     */
    fun construirConfiguracion(alError: (String) -> Unit): ConfigSimulacion?
}
