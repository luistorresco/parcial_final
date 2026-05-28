# 🏛️ Sistema Catastral de Antioquia

El **Sistema Catastral de Antioquia** es una aplicación de escritorio de alto rendimiento desarrollada en **Java** nativo bajo la arquitectura **MVC (Modelo-Vista-Controlador)**. Está diseñada para procesar, ordenar y realizar búsquedas ultra-rápidas sobre aproximadamente **50.000 registros catastrales** almacenados en un archivo CSV.

La aplicación utiliza algoritmos de ordenación a medida, búsquedas indexadas personalizadas y cumple rigurosamente con las mejores prácticas de programación limpia y principios de diseño **S.O.L.I.D.**

---

## 🎨 Características Principales

* **Interfaz Gráfica de Usuario (GUI)** Swing premium:
  * Visualización tabular completa y fluida (`JTable`) de los registros catastrales.
  * Selector dinámico de criterios de ordenación (NPN, Municipio, Dirección y Ficha).
  * Cronómetro de precisión en tiempo real (`HH:mm:ss.SSS`) que mide los milisegundos y nanosegundos empleados en cada proceso.
  * Buscador rápido con caja de texto e ícono de lupa.
  * **Selección y Auto-desplazamiento**: Al encontrar coincidencias, la tabla ilumina el rango completo de filas coincidentes en azul y se desplaza automáticamente (scroll) para posicionar la primera coincidencia visible en pantalla.
* **Carga de Datos Inteligente**:
  * Carga y validación automática del archivo catastral local `predios.csv` de manera transparente y ultra-rápida (menos de 400 ms).
  * Manejo riguroso de errores y validación de permisos de lectura para evitar cierres inesperados.
* **Algoritmos a Medida Avanzados**:
  * **QuickSort Hoare**: Implementación a medida del algoritmo QuickSort utilizando el esquema de partición de Hoare con pivote central (mediana), optimizado para evitar el peor caso y resistir grandes cantidades de registros repetidos sin degradar rendimiento.
  * **Búsqueda Binaria de Rango $O(\log N + K)$**: Búsqueda binaria indexada combinada con expansión bidireccional paralela. Permite búsquedas parciales (por prefijos) y retorna los límites exactos `[inicio, fin]` contiguos.
* **Resiliencia de Espacios**:
  * Normalización automática de espacios múltiples consecutivos a espacios simples tanto en la barra de búsqueda del usuario como en los datos almacenados de la base catastral catastral (ej. `"La     estrella"` matches `"La estrella"`).

---

## 🏗️ Estructura del Proyecto

El código fuente sigue un patrón de desacoplamiento estricto por paquetes:

```text
final/ (Raíz del proyecto)
│
├── predios.csv                  # Archivo de datos con ~50.000 registros catastrales.
├── .gitignore                   # Exclusiones de control de versiones Git.
├── README.md                    # Este archivo de documentación del proyecto.
│
├── bin/                         # Archivos ejecutables de Java compilados (.class).
│
└── src/
    ├── model/
    │   └── Predio.java          # [MODELO] Representación de un registro de catastro.
    │
    ├── view/
    │   └── GuiView.java         # [VISTA] Interfaz gráfica basada en Swing.
    │
    ├── controller/
    │   └── PredioGuiController.java # [CONTROLADOR] Enlace EDT entre el Modelo y la Vista.
    │
    ├── util/
    │   ├── CsvReader.java           # [UTIL] Validador y procesador BufferedReader del CSV.
    │   ├── QuickSortHoare.java      # [UTIL] Algoritmo QuickSort Hoare genérico.
    │   ├── BinarySearchUtil.java    # [UTIL] Búsqueda binaria y OCP normalizadora.
    │   └── TimerUtil.java           # [UTIL] Cronómetro de alta resolución.
    │
    └── main/
        └── Main.java                # [BOOTSTRAP] Hilo seguro EDT Swing de entrada.
```

---

## 🛠️ Requisitos del Sistema

* **Kit de Desarrollo de Java (JDK)**: Versión 8 o superior.
* **Sistema Operativo**: Multiplataforma (Linux, Windows o macOS).

---

## 💻 Instrucciones para Compilar y Ejecutar en Visual Studio Code

Abre la terminal integrada en VS Code en la raíz de tu proyecto (`/home/dainkor/Documentos/logica de programacion 2/final`) y sigue los siguientes comandos:

### 1. Limpieza y Compilación de Clases
Compila los archivos `.java` colocándolos en la carpeta binaria `bin/`:
```bash
rm -rf bin
mkdir -p bin
javac -d bin src/model/Predio.java src/util/TimerUtil.java src/util/CsvReader.java src/util/QuickSortHoare.java src/util/BinarySearchUtil.java src/view/GuiView.java src/controller/PredioGuiController.java src/main/Main.java
```

### 2. Ejecutar la Aplicación
Inicia la máquina virtual de Java ejecutando la clase bootstrap principal:
```bash
java -cp bin main.Main
```

---

## 📐 Principios S.O.L.I.D. Aplicados

1. **Responsabilidad Única (SRP)**: Cada clase tiene una sola tarea concreta (los algoritmos no dibujan pantallas y las vistas no ordenan datos).
2. **Abierto/Cerrado (OCP)**: Los ordenamientos y las búsquedas se realizan inyectando comportamientos funcionales (`Comparator<Predio>` y `Function<Predio, String>`), permitiendo agregar nuevos criterios sin alterar el código algorítmico básico.
3. **Sustitución de Liskov (LSP)**: Ausencia de jerarquías forzadas de herencia, comunicando capas exclusivamente mediante tipos estandarizados e inmutables.
4. **Segregación de Interfaces (ISP)**: Uso estratégico de interfaces funcionales de Java ligeras y enfocadas.
5. **Inversión de Dependencias (DIP)**: Desacoplamiento total mediante la delegación del comportamiento y de la lógica en firmas genéricas.
