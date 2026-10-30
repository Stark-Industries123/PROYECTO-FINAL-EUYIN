package config

import condiciones.dominio.CondicionesIniciales
import condiciones.dominio.Proyectil
import condiciones.dominio.Viento
import fisicas.gravedad.FuenteGravedad

/**
 * Encapsula el estado inmutable de los parámetros requeridos para la simulación de tiro parabólico.
 *
 * Cumplimiento de principios POO & SOLID:
 * - Single Responsibility Principle (SRP): Su única responsabilidad es modelar, validar y transportar
 *   el estado de configuración del disparo, sin lógica de cálculo físico ni interacción con la UI.
 * - Dependency Inversion Principle (DIP): Depende de la abstracción [FuenteGravedad] y no de
 *   implementaciones concretas (Tierra, Luna, etc.).
 * - Open/Closed Principle (OCP) & Liskov Substitution Principle (LSP): Permite la sustitución
 *   polimórfica de cualquier fuente gravitatoria que implemente el contrato [FuenteGravedad]
 *   sin requerir modificaciones en esta clase.
 * - Inmutabilidad & Encapsulamiento (POO): Todas las propiedades son de solo lectura (`val`),
 *   asegurando que el estado no sea alterado durante el ciclo de vida de la simulación.
 *
 * @property velocidadInicial Magnitud de la velocidad inicial de lanzamiento en m/s (debe ser >= 0).
 * @property angulo Ángulo de elevación del lanzamiento en grados (entre 0.0 y 90.0).
 * @property fuenteGravedad Abstracción del cuerpo celeste o entorno gravitacional.
 * @property masaKg Masa del cuerpo lanzado en kilogramos (debe ser > 0, valor por defecto: 1.0).
 * @property velocidadVientoKmH Velocidad del viento en km/h (debe ser >= 0, valor por defecto: 0.0).
 * @property direccionVientoGrados Dirección del viento en grados según la rosa de los vientos (0.0 a 360.0, valor por defecto: 0.0).
 * @property viento Modelo de dominio opcional de viento con rumbo exacto calculado ([Viento]).
 * @property proyectil Modelo de dominio opcional del proyectil ([Proyectil]).
 */
data class ConfigSimulacion(
    val velocidadInicial: Double,
    val angulo: Double,
    val fuenteGravedad: FuenteGravedad,
    val masaKg: Double = DEFAULT_MASA_KG,
    val velocidadVientoKmH: Double = DEFAULT_VELOCIDAD_VIENTO_KMH,
    val direccionVientoGrados: Double = DEFAULT_DIRECCION_VIENTO_GRADOS,
    val viento: Viento? = null,
    val proyectil: Proyectil? = null
) {
    /**
     * Constructor secundario que inicializa la configuración física a partir del agregado [CondicionesIniciales].
     */
    constructor(
        velocidadInicial: Double,
        angulo: Double,
        fuenteGravedad: FuenteGravedad,
        condiciones: CondicionesIniciales
    ) : this(
        velocidadInicial = velocidadInicial,
        angulo = angulo,
        fuenteGravedad = fuenteGravedad,
        masaKg = condiciones.proyectil.pesoKg,
        velocidadVientoKmH = condiciones.viento.velocidad,
        direccionVientoGrados = condiciones.viento.direccionGrados,
        viento = condiciones.viento,
        proyectil = condiciones.proyectil
    )

    /**
     * Constructor secundario que inicializa la configuración física a partir de los modelos [Proyectil] y [Viento].
     */
    constructor(
        velocidadInicial: Double,
        angulo: Double,
        fuenteGravedad: FuenteGravedad,
        proyectil: Proyectil,
        viento: Viento
    ) : this(
        velocidadInicial = velocidadInicial,
        angulo = angulo,
        fuenteGravedad = fuenteGravedad,
        condiciones = CondicionesIniciales(proyectil, viento)
    )

    init {
        require(velocidadInicial >= 0.0) {
            "La velocidad inicial debe ser mayor o igual a 0 m/s (recibido: $velocidadInicial)."
        }
        require(angulo in 0.0..90.0) {
            "El ángulo debe estar comprendido entre 0.0 y 90.0 grados inclusive (recibido: $angulo)."
        }
        require(masaKg > 0.0) {
            "La masa del proyectil debe ser estrictamente mayor a 0 kg (recibido: $masaKg)."
        }
        require(velocidadVientoKmH >= 0.0) {
            "La velocidad del viento debe ser mayor o igual a 0 km/h (recibido: $velocidadVientoKmH)."
        }
        require(direccionVientoGrados in 0.0..360.0) {
            "La dirección del viento debe estar comprendida entre 0.0 y 360.0 grados (recibido: $direccionVientoGrados)."
        }
    }

    /**
     * Retorna las condiciones iniciales completas si ambos modelos de dominio ([proyectil] y [viento]) están presentes.
     */
    val condicionesIniciales: CondicionesIniciales?
        get() = if (proyectil != null && viento != null) CondicionesIniciales(proyectil, viento) else null

    /**
     * Retorna la velocidad del viento convertida a metros por segundo (m/s).
     * Relación: 1 km/h = 1 / 3.6 m/s.
     *
     * @return Velocidad del viento en m/s.
     */
    fun velocidadVientoMS(): Double = velocidadVientoKmH / FACTOR_KMH_A_MS

    /**
     * Propiedad de conveniencia para acceder a la velocidad del viento en m/s.
     */
    val velocidadVientoMS: Double get() = velocidadVientoMS()

    /**
     * Retorna el sector cardinal de la rosa de los vientos ([DireccionVientoSector])
     * correspondiente a [direccionVientoGrados].
     */
    fun sectorViento(): DireccionVientoSector = DireccionVientoSector.desdeGrados(direccionVientoGrados)

    /**
     * Propiedad de conveniencia para acceder al sector cardinal del viento.
     */
    val sectorViento: DireccionVientoSector get() = sectorViento()

    /**
     * Alias de compatibilidad hacia atrás con versiones previas que empleaban `masaProyectil`.
     */
    val masaProyectil: Double get() = masaKg

    companion object {
        const val DEFAULT_MASA_KG: Double = 1.0
        const val DEFAULT_VELOCIDAD_VIENTO_KMH: Double = 0.0
        const val DEFAULT_DIRECCION_VIENTO_GRADOS: Double = 0.0
        const val FACTOR_KMH_A_MS: Double = 3.6
    }
}
