# NexaDelivery --- Backend

------------------------------------------------------------------------

# 1. Nom du projet

**Nom du projet :** NexaDelivery --- API REST de gestion et de suivi des
livraisons

### Dépôt du projet

-   **Backend (API REST)** :
    https://github.com/zakariaoutla/NexaDelivery_backend.git

------------------------------------------------------------------------

# 2. Présentation du projet

NexaDelivery est une **API REST de gestion et de suivi des livraisons**
développée avec Java 17 et Spring Boot.

La plateforme met en relation trois acteurs principaux : **ADMIN**,
**MERCHANT** et **DRIVER**. Les commerçants peuvent créer et suivre
leurs demandes de livraison, les livreurs peuvent consulter leurs
missions et mettre à jour leur statut, tandis que l'administrateur
supervise la plateforme et gère ses ressources.

Le backend centralise notamment la gestion des utilisateurs, des
livraisons, des véhicules, des points de collecte, des positions des
livreurs, des évaluations, des notifications et des statistiques.

------------------------------------------------------------------------

# 3. Problématique

La gestion des livraisons peut devenir complexe lorsqu'elle repose sur
des processus dispersés ou non centralisés. Le suivi des commandes,
l'affectation des livreurs, la communication des changements de statut
et la supervision des différentes ressources nécessitent une solution
structurée.

NexaDelivery répond à ce besoin avec une **API REST sécurisée par Spring
Security et JWT**, une gestion des rôles, un suivi des livraisons, des
notifications via WebSocket, un suivi de localisation des livreurs et
des statistiques adaptées aux différents utilisateurs.

------------------------------------------------------------------------

# 4. Fonctionnalités principales

-   **Authentifier les utilisateurs** avec JWT et gérer les rôles
    `ADMIN`, `MERCHANT` et `DRIVER`.
-   **Gérer les livraisons** : création, consultation, modification,
    affectation d'un livreur, changement de statut, annulation, rejet et
    suivi par code.
-   **Gérer les livreurs et commerçants** : profils, statuts et
    ressources associées.
-   **Gérer les véhicules et points de collecte** utilisés dans le
    processus de livraison.
-   **Suivre la position des livreurs** et récupérer leur dernière
    localisation.
-   **Gérer les notifications en temps réel** et le compteur de
    notifications non lues.
-   **Gérer les évaluations** associées aux livraisons.
-   **Consulter les statistiques** selon le rôle : administrateur,
    commerçant ou livreur.
-   **Consulter publiquement une livraison** grâce à son code de suivi.

------------------------------------------------------------------------

# 5. Technologies utilisées

Technologie           Utilisation dans le projet
  --------------------- ---------------------------------------------
Java 17               Langage principal du backend
Spring Boot           Framework backend
Spring Web / MVC      Création de l'API REST
Spring Data JPA       Accès et persistance des données
Hibernate             ORM
Spring Security       Sécurisation des endpoints
JWT (JJWT 0.11.5)     Authentification stateless
MySQL                 Base de données relationnelle
Flyway                Versionnement et migrations SQL
MapStruct 1.6.3       Mapping entre entités et DTOs
Lombok                Réduction du code répétitif
Bean Validation       Validation des données entrantes
WebSocket             Notifications et communication temps réel
Swagger / OpenAPI     Documentation interactive de l'API
Maven                 Gestion des dépendances et build
JUnit / Spring Test   Tests automatisés
Docker                Conteneurisation du backend
Docker Compose        Orchestration du backend, MySQL et frontend
GitHub Actions        Build et tests automatiques
Git & GitHub          Versionnement du code

------------------------------------------------------------------------

# 6. Architecture du backend

Le projet est organisé en plusieurs couches afin de séparer les
responsabilités :

``` text
Client
  │
  ▼
Controller
  │
  ▼
Service
  │
  ▼
Repository
  │
  ▼
MySQL
```

Structure principale :

``` text
src/main/java/org/laicose/nexadelivery/
├── auth/
├── configuration/
├── controller/
├── dto/
│   ├── request/
│   └── response/
├── Enum/
├── exceptions/
├── mapper/
├── model/
├── repository/
└── service/
```

Les principales entités du projet sont :

-   `User`
-   `Merchant`
-   `Driver`
-   `Vehicle`
-   `Delivery`
-   `CollectionPoint`
-   `DriverLocation`
-   `Notification`
-   `Rating`

------------------------------------------------------------------------

# 7. Sécurité et rôles

NexaDelivery utilise **Spring Security** avec une authentification **JWT
stateless**.

Les routes `/api/auth/**`, `/api/public/**`, Swagger et WebSocket sont
autorisées selon la configuration de sécurité. Les autres endpoints
nécessitent une authentification.

Les permissions métier sont contrôlées avec `@PreAuthorize`.

  -----------------------------------------------------------------------
Rôle         Responsabilités principales
  ------------ ----------------------------------------------------------
`ADMIN`      Gérer les utilisateurs, véhicules, livraisons et
statistiques globales

`MERCHANT`   Gérer son profil, ses points de collecte, créer et suivre
ses livraisons

`DRIVER`     Gérer son profil et statut, consulter ses livraisons et
mettre à jour leur état/localisation
  -----------------------------------------------------------------------

Exemple d'en-tête pour une route protégée :

``` http
Authorization: Bearer <JWT_TOKEN>
```

------------------------------------------------------------------------

# 8. Principaux endpoints

## Authentification

``` http
POST /api/auth/login
POST /api/auth/register/driver
POST /api/auth/register/merchant
```

## Livraisons

``` http
GET    /api/delivery
GET    /api/delivery/{id}
GET    /api/delivery/{id}/me
POST   /api/delivery
PUT    /api/delivery/{id}
GET    /api/delivery/tracking/{trackingCode}
PUT    /api/delivery/{deliveryId}/driver/{driverId}
PUT    /api/delivery/{id}/status
GET    /api/delivery/my-deliveries
GET    /api/delivery/driver
PUT    /api/delivery/{id}/my-status
PUT    /api/delivery/{id}/cancel
POST   /api/delivery/{deliveryId}/auto-assign
PUT    /api/delivery/{id}/reject
DELETE /api/delivery/{id}
```

## Livreurs

``` http
GET    /api/driver
GET    /api/driver/{id}
GET    /api/driver/me
PUT    /api/driver/{id}
PUT    /api/driver/me
PUT    /api/driver/me/status
DELETE /api/driver/{id}
PUT    /api/driver/{driverId}/vehicle/{vehicleId}
DELETE /api/driver/{driverId}/vehicle
```

## Commerçants

``` http
GET    /api/merchant
GET    /api/merchant/{id}
GET    /api/merchant/me
PUT    /api/merchant/me
PUT    /api/merchant/{id}
DELETE /api/merchant/{id}
```

## Véhicules

``` http
GET    /api/vehicle
GET    /api/vehicle/{id}
GET    /api/vehicle/available
POST   /api/vehicle
PUT    /api/vehicle/{id}
DELETE /api/vehicle/{id}
```

## Points de collecte

``` http
POST   /api/collection-point
GET    /api/collection-point
GET    /api/collection-point/{id}
GET    /api/collection-point/me
PUT    /api/collection-point/{id}
DELETE /api/collection-point/{id}
```

## Localisation des livreurs

``` http
POST   /api/driver-location
GET    /api/driver-location
GET    /api/driver-location/{id}
GET    /api/driver-location/delivery/{deliveryId}/latest
GET    /api/driver-location/driver/{driverId}
GET    /api/driver-location/me
GET    /api/driver-location/driver/{driverId}/latest
GET    /api/driver-location/latest
DELETE /api/driver-location/{id}
```

## Notifications

``` http
GET /api/notifications/me
PUT /api/notifications/{id}/read
GET /api/notifications/me/unread-count
```

## Évaluations

``` http
POST   /api/rating/{id}
GET    /api/rating
GET    /api/rating/{id}
GET    /api/rating/delivery/{deliveryId}
PUT    /api/rating/{id}
DELETE /api/rating/{id}
```

## Statistiques

``` http
GET /api/statistics/dashboard
GET /api/statistics/merchant
GET /api/statistics/driver
```

## Suivi public

``` http
GET /api/public/tracking/{trackingCode}
```

------------------------------------------------------------------------

# 9. Installation et lancement

## 9.1 Prérequis

Pour lancer le backend localement :

-   Java 17
-   Maven ou Maven Wrapper
-   MySQL 8
-   Git
-   Docker et Docker Compose si vous souhaitez utiliser les conteneurs

------------------------------------------------------------------------

## 9.2 Cloner le dépôt

``` bash
git clone https://github.com/zakariaoutla/NexaDelivery_backend.git
cd NexaDelivery_backend
```

------------------------------------------------------------------------

## 9.3 Configuration de la base de données

La configuration locale utilise MySQL.

Exemple :

``` properties
spring.datasource.url=jdbc:mysql://localhost:3306/nexadelivery_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD
```

> Ne publiez jamais vos mots de passe, secrets JWT ou autres
> identifiants sensibles dans GitHub.

------------------------------------------------------------------------

## 9.4 Configuration JWT

Configurez un secret JWT et sa durée d'expiration :

``` properties
app.jwt.secret=YOUR_STRONG_JWT_SECRET
app.jwt.expiration=86400000
```

------------------------------------------------------------------------

## 9.5 Migrations Flyway

Flyway est activé dans le projet.

Les migrations se trouvent dans :

``` text
src/main/resources/db/migration/
├── V1___init.sql
├── V2__add_foreign_keys.sql
├── V3__create_notifications_table.sql
├── V4__rename_notification_read_column.sql
└── V5__remove_zone.sql
```

------------------------------------------------------------------------

## 9.6 Installer et tester le projet

Avec Maven Wrapper sous Windows :

``` powershell
.\mvnw.cmd clean test
```

Linux / macOS :

``` bash
./mvnw clean test
```

------------------------------------------------------------------------

## 9.7 Lancer le backend

Windows :

``` powershell
.\mvnw.cmd spring-boot:run
```

Linux / macOS :

``` bash
./mvnw spring-boot:run
```

L'API est ensuite disponible sur :

``` text
http://localhost:8080
```

------------------------------------------------------------------------

## 9.8 Swagger

La configuration du projet autorise Swagger UI et OpenAPI.

``` text
http://localhost:8080/swagger-ui/index.html
http://localhost:8080/v3/api-docs
```

------------------------------------------------------------------------

# 10. Docker

Le backend contient un `Dockerfile` multi-stage :

1.  build avec Maven et Java 17 ;
2.  exécution avec Eclipse Temurin 17 JRE Alpine.

Pour construire l'image du backend :

``` bash
docker build -t nexadelivery-backend .
```

Pour lancer l'environnement Docker Compose :

``` bash
docker compose up --build
```

Le fichier `docker-compose.yml` définit actuellement :

-   le backend Spring Boot sur le port `8080` ;
-   MySQL 8 ;
-   le frontend servi séparément ;
-   un réseau Docker commun ;
-   un volume persistant pour MySQL ;
-   un healthcheck MySQL.

Pour arrêter les conteneurs :

``` bash
docker compose down
```

------------------------------------------------------------------------

# 11. Tests et intégration continue

Le projet contient des tests pour plusieurs parties du backend :

``` text
AuthenticationTest
DriverServiceTest
MerchantServiceTest
DeliveryServiceTest
VehicleServiceTest
RatingServiceTest
NotificationServiceTest
CollectionPointTest
DriverLocationServiceTest
StatisticsServiceTest
NexaDeliveryApplicationTests
```

Un workflow **GitHub Actions** est déclenché sur les `push` et
`pull_request` vers `main`.

Pipeline actuel :

``` text
Push / Pull Request
        │
        ▼
Checkout
        │
        ▼
Java 17
        │
        ▼
MySQL 8
        │
        ▼
mvn test
        │
        ▼
mvn package -DskipTests
```

------------------------------------------------------------------------

# 12. Captures et diagrammes

## Diagramme de classes

Le dépôt contient le diagramme de classes du projet :



![Diagram de class.jpg](Diagram%20de%20class.jpg)

------------------------------------------------------------------------

## Diagramme de cas d'utilisation



![UseCaseDiagram.jpg](UseCaseDiagram.jpg)

------------------------------------------------------------------------

## Diagramme de séquence


![GET — Consulter ses livraisons.jpg](GET%20%E2%80%94%20Consulter%20ses%20livraisons.jpg)

------------------------------------------------------------------------

# 13. Contribution personnelle

Ma contribution a porté sur la conception et le développement du backend
NexaDelivery avec **Spring Boot**.

J'ai travaillé sur la modélisation des entités, les DTOs, les mappers
MapStruct, les repositories, les services et les controllers REST. J'ai
également mis en place l'authentification JWT avec Spring Security et la
gestion des autorisations selon les rôles `ADMIN`, `MERCHANT` et
`DRIVER`.

J'ai développé les fonctionnalités liées aux livraisons, aux
commerçants, aux livreurs, aux véhicules, aux points de collecte, à la
localisation, aux évaluations, aux notifications et aux statistiques.

J'ai aussi travaillé sur les migrations de base de données avec Flyway,
les tests automatisés, la documentation Swagger, WebSocket,
Docker/Docker Compose et le pipeline GitHub Actions.

------------------------------------------------------------------------

# 14. Difficultés rencontrées

## Difficulté 1 --- Spring Security et autorisations

### Problème rencontré

Certaines requêtes vers des endpoints protégés retournaient une erreur
**403 Forbidden** malgré l'utilisation d'un token JWT.

### Recherches / Tests

J'ai vérifié la génération et la validation du token, le filtre JWT, la
configuration de `SecurityFilterChain`, les rôles présents dans
l'authentification ainsi que les annotations `@PreAuthorize`.

### Solution

J'ai structuré la sécurité autour d'un filtre JWT exécuté avant
`UsernamePasswordAuthenticationFilter`, activé la sécurité au niveau des
méthodes et défini les permissions des endpoints selon les rôles.

### Ce que j'ai appris

Cette difficulté m'a permis de mieux comprendre la différence entre
**authentification** et **autorisation**, le fonctionnement d'un filtre
JWT et la gestion des rôles avec Spring Security.

------------------------------------------------------------------------

## Difficulté 2 --- Docker et communication avec MySQL

### Problème rencontré

Le backend devait pouvoir démarrer correctement avec MySQL dans Docker
sans essayer de se connecter à la base avant qu'elle soit disponible.

### Recherches / Tests

J'ai travaillé sur le réseau Docker, les variables d'environnement, le
nom du service MySQL et le healthcheck de la base de données.

### Solution

Le fichier Docker Compose utilise un service `db`, un healthcheck MySQL
et `depends_on` avec `condition: service_healthy`. Le backend utilise
l'adresse `db:3306` à l'intérieur du réseau Docker.

### Ce que j'ai appris

Cette partie m'a permis de mieux comprendre la communication entre
conteneurs, les réseaux Docker, les healthchecks et la différence entre
les ports internes et les ports exposés sur la machine.

------------------------------------------------------------------------

## Difficulté 3 --- Notifications en temps réel

### Problème rencontré

Le projet nécessitait un mécanisme permettant d'envoyer des mises à jour
aux utilisateurs sans dépendre uniquement de requêtes HTTP répétées.

### Recherches / Tests

J'ai intégré et configuré WebSocket dans le backend, avec une
configuration dédiée et un intercepteur d'authentification WebSocket.

### Solution

Le backend contient une configuration WebSocket ainsi qu'un service de
notifications pour gérer les événements et les notifications destinés
aux utilisateurs.

### Ce que j'ai appris

Cette fonctionnalité m'a permis de mieux comprendre la différence entre
une API REST classique et une communication temps réel avec WebSocket.

------------------------------------------------------------------------

# 15. Améliorations possibles

Dans une prochaine version, il serait possible de :

-   renforcer la couverture des tests d'intégration ;
-   externaliser complètement les secrets et identifiants dans des
    variables d'environnement ;
-   améliorer la supervision et la journalisation de l'application ;
-   préparer un processus de déploiement automatisé vers un
    environnement de production.

### Conclusion

Ces améliorations permettraient de rendre le backend encore plus
robuste, sécurisé et adapté à un déploiement en production.

------------------------------------------------------------------------

# 16. Checklist finale

## Présentation

-   [x] Le nom du projet est clair.
-   [x] Le besoin et l'objectif sont expliqués.
-   [x] Les utilisateurs principaux sont identifiés.

## Fonctionnalités

-   [x] Les fonctionnalités correspondent au code du backend.
-   [x] Les trois rôles sont présentés.
-   [x] Les principaux endpoints sont documentés.

## Technologies

-   [x] Les technologies principales sont indiquées.
-   [x] Leur utilisation dans le projet est expliquée.

## Installation

-   [x] Les prérequis sont indiqués.
-   [x] Le dépôt Git est indiqué.
-   [x] Les commandes Maven sont présentes.
-   [x] Docker est documenté.
-   [x] Les données sensibles sont remplacées par des placeholders dans
    ce README.

## Documentation

-   [x] Swagger / OpenAPI est indiqué.
-   [x] Les diagrammes présents dans le dépôt sont référencés.
-   [x] Les captures présentes dans le dépôt sont référencées.

## Contribution

-   [x] La contribution personnelle est expliquée.
-   [x] Les principales parties développées sont identifiées.

## Difficultés

-   [x] Les problèmes sont expliqués.
-   [x] Les recherches et solutions sont décrites.
-   [x] Les apprentissages sont présentés.

------------------------------------------------------------------------

# Auteur

**Zakaria OUTLA**

Projet réalisé dans le cadre de la formation en développement Full
Stack.
