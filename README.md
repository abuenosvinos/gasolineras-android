
# Proyecto Gasolineras

[![Author][Author]](http://www.antoniobuenosvinos.com)
[![Software License][License]](LICENSE.md)

Este proyecto tiene como objetivo la práctica de Kotlin.

Siéntete libre para realizar las modificaciones que consideres necesarias para tu aprendizaje.

El proyecto consiste en una aplicación basado en un WebView que ofrece el contenido de http://gasolineras.tierradeatomos.com/

Este proyecto está relacionado con https://github.com/abuenosvinos/gasolineras-www 

## Pre-requisitos

Necesitas tener instalado Android Studio.


## Configuración

El fichero local.properties necesita obligatoriamente que declares los parámetros `signingConfigs.storeFile, signingConfigs.storePassword, signingConfigs.keyAlias, signingConfigs.keyPassword` que son necesarios para firmar la aplicación.

Si no quieres firmar la aplicación y solo ejecutarla en tu entorno, modifica el fichero `app/app.gradle` y quita todo lo relativo a `signingConfigs`


## Posibles mejoras

* Incluir una pantalla inicial donde indique porque es necesario dar permisos de localización

* Añadir una pantalla intermedia de Loading antes de que el WebView cargue la web para evitar el rato que la pantalla está en blanco

* Ir pasando la ubicación al WebView según el usuario se vaya desplazando. La aplicación web debería utilizar ese dato

## Contacto

Si tienes cualquier sugerencia o problema, por favor, házmelo saber a través de este [formulario de contacto][1].

Espero te sea útil.


[1]: http://www.antoniobuenosvinos.com/hablamos/


[Author]: http://img.shields.io/badge/author-@abuenosvinos-blue.svg?style=flat-square
[License]: https://img.shields.io/badge/license-MIT-blue.svg?style=flat-square
