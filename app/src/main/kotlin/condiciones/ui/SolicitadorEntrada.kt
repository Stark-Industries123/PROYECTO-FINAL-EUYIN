package condiciones.ui

import fisicas.gravedad.FuenteGravedad

/**
 * Contrato de abstracción para el servicio coordinador de solicitud de parámetros de entrada.
 */
interface SolicitadorEntrada {

    fun solicitarPlaneta(): FuenteGravedad

    fun solicitarVelocidadInicial(): Double

    fun solicitarAnguloDisparo(): Double

    fun solicitarAngulo(): Double

    fun solicitarDireccionVientoConGuia(presentador: PresentadorRosaVientos? = null): Double

    fun solicitarDireccionViento(): Double

    fun solicitarVelocidadViento(): Double

    fun solicitarPesoProyectil(): Double
}
