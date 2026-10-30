package condiciones.servicio

import condiciones.dominio.CuadranteRosa
import condiciones.dominio.PuntoRosaVientos

/**
 * Contrato de abstracción para el servicio de cálculo y resolución de rumbos en la rosa de los vientos.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Open/Closed Principle (OCP): Define una abstracción que permite incorporar o variar
 *   la resolución de rumbos (ej: rosas de 8, 16 o 32 rumbos) sin modificar a las clases clientes.
 * - Dependency Inversion Principle (DIP): Los módulos de alto nivel (vistas, orquestadores)
 *   dependen exclusivamente de esta interfaz.
 */
interface CalculadorRumboService {

    /**
     * Calcula y retorna el rumbo exacto de la rosa de los vientos correspondiente a un ángulo en grados.
     *
     * @param grados Dirección en grados sexagesimales (0.0 a 360.0).
     * @return [PuntoRosaVientos] correspondiente al sector angular.
     */
    fun calcularRumbo(grados: Double): PuntoRosaVientos

    /**
     * Retorna la lista ordenada e inmutable de todos los puntos de la rosa de los vientos.
     *
     * @return Colección de [PuntoRosaVientos].
     */
    fun obtenerPuntos(): List<PuntoRosaVientos>

    /**
     * Retorna los puntos organizados por su cuadrante correspondiente.
     *
     * @return Mapa de cuadrante a lista de puntos asociados.
     */
    fun obtenerPuntosPorCuadrante(): Map<CuadranteRosa, List<PuntoRosaVientos>>
}
