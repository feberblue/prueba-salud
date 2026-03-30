#!/bin/bash

echo "======================================"
echo "Iniciando Backend Gradle (Puerto 8080)"
echo "======================================"

# Limpiar builds anteriores
echo "Limpiando builds anteriores..."
rm -rf build/

# Compilar sin tests para evitar problemas
echo "Compilando proyecto..."
gradle build -x test

# Ejecutar aplicación
echo "Iniciando aplicación Spring Boot..."
gradle bootRun --args='--spring.profiles.active=dev'
