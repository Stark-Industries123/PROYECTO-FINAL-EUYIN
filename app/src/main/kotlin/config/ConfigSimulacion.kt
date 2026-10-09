package config

import fisicas.gravedad.FuenteGravedad

/**
 * Encapsula el estado inmutable de los parámetros requeridos para la simulación de tiro.
 *
 * Cumplimiento de principios POO & SOLID:
 * - Single Responsibility Principle (SRP): Su única responsabilidad es modelar y transportar
 *   el estado de configuración del disparo, sin lógica de cálculo físico ni interacción con la UI.
 * - Dependency Inversion Principle (DIP): Depende de la abstracción [FuenteGravedad] y no de
 *   implementaciones concretas (Tierra, Luna, etc.).
 * - Open/Closed Principle (OCP) & Liskov Substitution Principle (LSP): Permite la sustitución
 *   polimórfica de cualquier fuente gravitatoria que implemente el contrato [FuenteGravedad]
 *   sin requerir modificaciones en esta clase.
 * - Inmutabilidad & Encapsulamiento (POO): Todas las propiedades son de solo lectura (`val`),
 *   asegurando que el estado no sea alterado durante el ciclo de vida de la simulación.
 *
 * @property velocidadInicial Magnitud de la velocidad inicial de lanzamiento.
 * @property angulo Ángulo de elevación del lanzamiento en grados.
 * @property fuenteGravedad Abstracción del cuerpo celeste o entorno gravitacional.
 * @property masaProyectil Masa del cuerpo lanzado (valor por defecto: 1.0).
 */
data class ConfigSimulacion(
    val velocidadInicial: Double,
    val angulo: Double,
    val fuenteGravedad: FuenteGravedad,
    val masaProyectil: Double = 1.0
)
