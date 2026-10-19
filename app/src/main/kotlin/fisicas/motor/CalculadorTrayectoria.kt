package fisicas.motor

import config.ConfigSimulacion

/**
 * Contrato de abstracción para el cálculo de trayectorias físicas de proyectiles.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Dependency Inversion Principle (DIP): Los módulos de alto nivel (como la aplicación o la interfaz
 *   de usuario) dependen de esta abstracción y no de una implementación física concreta.
 * - Open/Closed Principle (OCP): Permite incorporar nuevos motores de cálculo o integradores numéricos
 *   (e.g., Runge-Kutta de orden 4, Verlet, modelos balísticos avanzados) sin modificar el código consumidor.
 * - Clean Architecture: Define el caso de uso del dominio físico puro sin dependencias de infraestructura ni UI.
 */
interface CalculadorTrayectoria {

    /**
     * Calcula la trayectoria completa del proyectil a partir de la configuración física proporcionada.
     *
     * @param config Configuración inmutable con los parámetros cinemáticos, gravitatorios, eólicos y de masa.
     * @return [ResultadoSimulacion] con las métricas finales y la serie temporal discreta de puntos en 3D.
     */
    fun calcularTrayectoria(config: ConfigSimulacion): ResultadoSimulacion
}
