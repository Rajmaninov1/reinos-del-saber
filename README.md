# Reinos del Saber

Juego educativo de estrategia por turnos para 3 participantes, desarrollado en Java con JavaFX. Cada jugador conquista territorio respondiendo preguntas de matemáticas, inglés y vocabulario.

## Documentación

- [Propuesta de juego](<docs/Reinos del Saber propuesta de juego.md>): presentación, alcance y reglas.
- [Guía técnica del equipo](<docs/Reinos del Saber guía técnica del equipo.md>): diseño, pruebas, riesgos y plan.
- [Enunciado del proyecto](<docs/vv2a5mfvXuI28r8c_I03kWeF0-TgDYwjZ-juego-20-arcade-20-desarrollado-20-en-20-java-20-utilizando-20-la-20-plataforma-20-java-fx.pdf>): entregas exigidas por el módulo.

## Requisitos

| Herramienta | Versión |
| --- | --- |
| JDK | 21 |
| Maven | 3.9 o superior |

Las demás dependencias, como JavaFX y JUnit, las descarga Maven la primera vez que se usa el proyecto, así que se necesita conexión a internet.

## Instalación

### Linux y macOS

Con [SDKMAN](https://sdkman.io/), que no requiere permisos de administrador:

```bash
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
sdk install java 21.0.13-tem
sdk install maven
```

Si `21.0.13-tem` ya no aparece, busca la versión 21 más reciente de Temurin con `sdk list java`. También sirve el gestor de paquetes del sistema, siempre que instale el JDK 21 y Maven 3.9 o superior.

### Windows

Ejecuta estos comandos en PowerShell y luego abre una terminal nueva:

```powershell
winget install EclipseAdoptium.Temurin.21.JDK

$v = "3.10.0"
$zip = "$env:TEMP\maven.zip"
$destino = "$env:LOCALAPPDATA\Programs\Maven"
Invoke-WebRequest "https://repo1.maven.org/maven2/org/apache/maven/apache-maven/$v/apache-maven-$v-bin.zip" -OutFile $zip
Expand-Archive $zip -DestinationPath $destino -Force
$path = [Environment]::GetEnvironmentVariable("Path", "User")
[Environment]::SetEnvironmentVariable("Path", "$path;$destino\apache-maven-$v\bin", "User")
```

### Comprobar la instalación

```bash
java -version
mvn -version
```

`java -version` debe mostrar la versión 21, y `mvn -version` debe indicar `Java version: 21`.

## Ejecutar el juego

Abre una terminal en la carpeta del proyecto y ejecuta:

```bash
mvn javafx:run
```

## Otros comandos

| Comando | Qué hace |
| --- | --- |
| `mvn test` | Ejecuta las pruebas |
| `mvn compile` | Compila sin ejecutar |
| `mvn javadoc:javadoc` | Genera la documentación en `target/reports/apidocs/index.html` |
| `mvn clean` | Borra los archivos generados |

## Problemas frecuentes

| Mensaje | Causa | Solución |
| --- | --- | --- |
| `release version 21 not supported` | Maven está usando un JDK anterior | Revisa `mvn -version` y corrige `JAVA_HOME` |
| El sistema no reconoce `mvn` | Maven no está en el `PATH` | Abre una terminal nueva o revisa la instalación |
