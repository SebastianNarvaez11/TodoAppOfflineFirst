# 📝 Todo App - Offline-First Architecture

Aplicación de tareas implementando estrategia offline-first con sincronización reactiva.

## 🏗️ Stack Técnico
- **Architecture:** MVVM + Repository Pattern
- **UI:** Jetpack Compose
- **Local DB:** Room con TypeConverters
- **DI:** Hilt
- **Reactive:** Kotlin Flow + StateFlow
- **Network:** Retrofit (con manejo offline)

## 🎯 Features
✅ CRUD completo de tareas
✅ Funciona 100% offline
✅ Sincronización automática cuando hay conexión
✅ Estados reactivos con Flow
✅ Validaciones de formulario
✅ Manejo de errores robusto
✅ UI responsive con Compose

## 🔄 Flujo Offline-First
1. Todas las operaciones primero en Room
2. UI se actualiza inmediatamente (optimistic updates)
3. Sincronización con backend en background
4. Manejo de conflictos de sincronización
