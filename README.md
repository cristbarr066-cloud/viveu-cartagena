# 🏠 ViveU – Alojamientos Universitarios en Cartagena

> **Plataforma PropTech de Catálogo Inteligente y Comparación de Habitaciones para Estudiantes de Educación Superior en Cartagena.**  
> *Proyecto desarrollado para la asignatura Desarrollo del Espíritu Emprendedor – Universidad de Cartagena (Administración Industrial, 2026).*

---

## 👥 Equipo Emprendedor
* **Verónica Arrieta**
* **Maydee Hernández**
* **Luisa Cervantes**

---

## 🌟 Propuesta de Valor y Características Tecnológicas
* **Búsqueda Filtrada por Sedes Universitarias de Cartagena:** Filtros específicos para Sede Zaragocilla (Medicina/Salud), Sede Piedra de Bolívar (Ingenierías), Claustro San Agustín (Centro) y Sede San Pablo.
* **Geolocalización y Mapas Interactivos:** 
  * Los propietarios pueden escribir la dirección y ajustar libremente su punto arrastrando el pin en el mapa de Cartagena.
  * **Cálculo Automático de Tiempo y Distancia:** El sistema calcula matemáticamente la distancia real y los minutos a pie hacia la sede de la Universidad de Cartagena seleccionada.
  * Los estudiantes pueden ver el mapa interactivo con la ruta hacia la universidad directamente en el modal de **Ver Detalles**.
* **Comparador Paralelo Interactivo:** Permite seleccionar hasta 3 habitaciones y compararlas lado a lado analizando precio, servicios públicos incluidos, climatización (Aire/Abanico), distancia a pie y normas de convivencia.
* **Simulador de Presupuesto Real (Arriendo + Transcaribe):** Permite calcular el costo total de vida mensual ingresando libremente los viajes diarios y días de desplazamiento con la tarifa oficial actualizada de Transcaribe ($3.900 COP).
* **Asistente IA Estudiantil ("ViveU AI Advisor"):** Motor semántico interactivo que comprende lenguaje natural (ej. *"Estudio medicina en Zaragocilla y tengo 400 mil"*) y sugiere las opciones óptimas.
* **Modelo de Negocio & Monetización:**
  * Plan Gratuito para propietarios.
  * Plan **Destacado VIP ($15.000 COP)** para validar la hipótesis de ingresos del Taller 2 ($10.000 - $20.000 COP).
* **Sello "Verificado ViveU":** Resuelve la problemática central de los anuncios informales y estafas en grupos de Facebook/WhatsApp.

---

## 🚀 Instrucciones de Ejecución

### Requisitos
* **Java 21** o superior instalado.
* **Apache Maven 3.9+** instalado.

### Comandos de Ejecución
Para iniciar el servidor en local:
```bash
mvn spring-boot:run
```

Una vez que la consola indique `Started ViveUApplication`:
1. **Catálogo Principal y Comparador:** [http://localhost:8080](http://localhost:8080)
2. **Formulario de Publicación de Propietarios:** [http://localhost:8080/publicar](http://localhost:8080/publicar)
3. **Dossier Ejecutivo y Modelo de Negocio:** [http://localhost:8080/modelo-negocio](http://localhost:8080/modelo-negocio)
4. **Consola H2 Database (en memoria):** [http://localhost:8080/h2-console](http://localhost:8080/h2-console) (JDBC URL: `jdbc:h2:mem:viveudb`, Usuario: `sa`, Contraseña en blanco).

---

## 🎙️ Pitch Oficial ViveU (135 Palabras - Máx 150)
> *"¿Eres estudiante universitario en Cartagena y te cuesta encontrar una habitación económica, segura y cercana a tu sede? Para solucionar las 2 semanas de búsqueda informal en redes y el riesgo de estafas, creamos **ViveU**: una plataforma inteligente diseñada para estudiantes de educación superior. Centralizamos habitaciones verificadas con precios transparentes ($300.000 a $500.000 COP), servicios incluidos y proximidad real a las sedes de la Universidad de Cartagena. Nuestra propuesta de valor ahorra tiempo mediante comparación técnica simultánea y recomendaciones con Inteligencia Artificial. A diferencia de grupos dispersos de Facebook, ofrecemos datos auditados y normas claras. Monetizamos mediante publicaciones destacadas ($15.000 COP) y planes de visibilidad para propietarios interesados en arrendatarios estudiantiles responsables. ViveU transforma la incertidumbre de mudarse en una experiencia ágil y segura."*

---

## 🛠️ Stack Tecnológico
* **Backend:** Spring Boot 3.2.5, Java 21, Spring Web MVC & REST, Spring Data JPA.
* **Base de Datos:** H2 Database (En memoria, inicializada con 8 alojamientos representativos en Zaragocilla, Los Cerros, Amberes, Manga, Pie de la Popa, Blas de Lezo y Centro).
* **Frontend:** Thymeleaf, HTML5 Semántico, CSS3 Puro (Diseño Claro Tecnológico 'Tech Clean', sombras multidimensionales, paleta vibrante en azules eléctricos y púrpuras, responsive, CSS Grid, variables de tokens), JavaScript moderno reactivo.
