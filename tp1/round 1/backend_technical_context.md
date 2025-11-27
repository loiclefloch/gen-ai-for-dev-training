# Contexte technique — Backend API E-commerce

## 1. Vue d'ensemble

API REST pour un mini site e-commerce. Le backend expose les données produits en lecture seule.

## 2. Stack technique

- **Langage** : Java 17+
- **Framework** : Spring Boot 3.x
- **Build** : Maven
- **Données** : Fichier JSON statique (converti depuis `products_dataset.csv`)
- **Tests** : JUnit 5 + Mockito
- **Conteneurisation** : Docker

## 3. Endpoints API

### `GET /products`

Retourne une liste de produits avec filtres optionnels.

**Query parameters :**

| Paramètre   | Type      | Description                                      | Obligatoire |
|-------------|-----------|--------------------------------------------------|-------------|
| `q`         | string    | Mot-clé de recherche (min 2 caractères)          | Non         |
| `category`  | string    | Filtre par catégorie                             | Non         |
| `priceMin`  | number    | Prix minimum TTC                                 | Non         |
| `priceMax`  | number    | Prix maximum TTC                                 | Non         |
| `inStock`   | boolean   | `true` = uniquement produits en stock            | Non         |
| `featured`  | boolean   | `true` = uniquement produits mis en avant        | Non         |
| `sort`      | string    | Tri : `price_asc`, `price_desc`, `relevance`     | Non         |

**Réponse succès (200) :**

```json
{
  "products": [
    {
      "id": "string",
      "name": "string",
      "description": "string",
      "price": "number",
      "category": "string",
      "image_url": "string",
      "stock_quantity": "number",
      "is_featured": "boolean"
    }
  ],
  "total": "number"
}
```

**Réponse erreur (400) — recherche trop courte :**

```json
{
  "error": "Le terme de recherche doit contenir au moins 2 caractères"
}
```

---

### `GET /products/:id`

Retourne le détail d'un produit par son identifiant.

**Paramètres de chemin :**

| Paramètre | Type   | Description          |
|-----------|--------|----------------------|
| `id`      | string | Identifiant produit  |

**Réponse succès (200) :**

```json
{
  "id": "string",
  "name": "string",
  "description": "string",
  "price": "number",
  "category": "string",
  "image_url": "string",
  "stock_quantity": "number",
  "is_featured": "boolean"
}
```

**Réponse erreur (404) :**

```json
{
  "error": "Produit non trouvé"
}
```

## 4. Règles métier

### Recherche (`q`)
- Minimum 2 caractères requis
- Recherche insensible à la casse
- Recherche dans les champs `name` et `description`
- Tri par pertinence par défaut

### Filtrage
- Les filtres sont cumulatifs (AND)
- `priceMin` et `priceMax` sont inclusifs
- `inStock: true` → `stock_quantity > 0`
- `featured: true` → `is_featured === true`

### Tri (`sort`)
- `relevance` : par défaut si recherche active, sinon ordre naturel
- `price_asc` : prix croissant
- `price_desc` : prix décroissant

## 5. Structure du projet

```
tp1/round 1/backend/
├── src/main/java/com/ecommerce/
│   ├── Application.java              # Point d'entrée Spring Boot
│   ├── controller/
│   │   └── ProductController.java    # Endpoints REST
│   ├── service/
│   │   └── ProductService.java       # Logique métier
│   ├── model/
│   │   └── Product.java              # Entité produit
│   ├── dto/
│   │   ├── ProductListResponse.java  # DTO réponse liste
│   │   └── ErrorResponse.java        # DTO erreur
│   └── repository/
│       └── ProductRepository.java    # Accès données JSON
├── src/main/resources/
│   ├── application.properties        # Configuration
│   └── data/
│       └── products.json             # Données produits
├── src/test/java/com/ecommerce/
│   ├── controller/
│   │   └── ProductControllerTest.java
│   └── service/
│       └── ProductServiceTest.java
├── pom.xml
├── Dockerfile
└── README.md
```

## 6. Commandes Maven

```bash
./mvnw spring-boot:run    # Démarre le serveur (port 8080)
./mvnw test               # Lance les tests
./mvnw package            # Build le JAR
```

## 7. Variables d'environnement

| Variable      | Description         | Défaut |
|---------------|---------------------|--------|
| `SERVER_PORT` | Port du serveur     | 8080   |

## 8. Codes HTTP

| Code | Signification                          |
|------|----------------------------------------|
| 200  | Succès                                 |
| 400  | Requête invalide (ex: recherche < 2 chars) |
| 404  | Produit non trouvé                     |
| 500  | Erreur serveur                         |

## 9. Stratégie de tests

### Tests API — Golden Tests (100% couverture)

Tests d'intégration end-to-end validant les contrats API avec des snapshots JSON.

**Fichiers :**
```
src/test/java/com/ecommerce/api/
├── ProductApiGoldenTest.java
└── golden/
    ├── get_products_no_filter.json
    ├── get_products_search_valid.json
    ├── get_products_search_too_short.json
    ├── get_products_filter_category.json
    ├── get_products_filter_price_range.json
    ├── get_products_filter_in_stock.json
    ├── get_products_filter_featured.json
    ├── get_products_sort_price_asc.json
    ├── get_products_sort_price_desc.json
    ├── get_products_combined_filters.json
    ├── get_products_empty_result.json
    ├── get_product_by_id_found.json
    └── get_product_by_id_not_found.json
```

**Cas couverts :**

| Endpoint | Scénario | Fichier golden |
|----------|----------|----------------|
| `GET /products` | Sans filtre | `get_products_no_filter.json` |
| `GET /products` | Recherche valide (`q=...`) | `get_products_search_valid.json` |
| `GET /products` | Recherche trop courte (400) | `get_products_search_too_short.json` |
| `GET /products` | Filtre catégorie | `get_products_filter_category.json` |
| `GET /products` | Filtre prix min/max | `get_products_filter_price_range.json` |
| `GET /products` | Filtre inStock=true | `get_products_filter_in_stock.json` |
| `GET /products` | Filtre featured=true | `get_products_filter_featured.json` |
| `GET /products` | Tri prix croissant | `get_products_sort_price_asc.json` |
| `GET /products` | Tri prix décroissant | `get_products_sort_price_desc.json` |
| `GET /products` | Filtres combinés | `get_products_combined_filters.json` |
| `GET /products` | Résultat vide | `get_products_empty_result.json` |
| `GET /products/:id` | Produit trouvé | `get_product_by_id_found.json` |
| `GET /products/:id` | Produit non trouvé (404) | `get_product_by_id_not_found.json` |

**Librairie :** JSONassert pour la comparaison des snapshots

### Données
Le fichier de produit csv doit être transformé en json. ça sera la base de données du backend.

### Tests unitaires — Backend (70% couverture)

Tests unitaires sur la logique métier du service.

**Fichiers :**
```
src/test/java/com/ecommerce/
├── service/
│   └── ProductServiceTest.java
└── repository/
    └── ProductRepositoryTest.java
```

**Couverture ciblée :**

| Classe | Méthode | Couverture |
|--------|---------|------------|
| `ProductService` | `searchProducts()` | ✅ |
| `ProductService` | `filterByCategory()` | ✅ |
| `ProductService` | `filterByPriceRange()` | ✅ |
| `ProductService` | `filterByStock()` | ✅ |
| `ProductService` | `filterByFeatured()` | ✅ |
| `ProductService` | `sortProducts()` | ✅ |
| `ProductService` | `getProductById()` | ✅ |
| `ProductRepository` | `loadProducts()` | ✅ |
| `ProductRepository` | `findAll()` | ✅ |
| `ProductRepository` | `findById()` | ✅ |

**Outils :**
- JUnit 5
- Mockito (mocks)
- JaCoCo (rapport de couverture)

### Configuration JaCoCo

Ajouter dans `pom.xml` :

```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.11</version>
    <executions>
        <execution>
            <goals>
                <goal>prepare-agent</goal>
            </goals>
        </execution>
        <execution>
            <id>report</id>
            <phase>test</phase>
            <goals>
                <goal>report</goal>
            </goals>
        </execution>
    </executions>
    <configuration>
        <rules>
            <rule>
                <element>BUNDLE</element>
                <limits>
                    <limit>
                        <counter>LINE</counter>
                        <value>COVEREDRATIO</value>
                        <minimum>0.70</minimum>
                    </limit>
                </limits>
            </rule>
        </rules>
    </configuration>
</plugin>
```

### Commandes de test

```bash
./mvnw test                          # Tous les tests
./mvnw test -Dtest=*GoldenTest       # Golden tests uniquement
./mvnw test -Dtest=*ServiceTest      # Tests unitaires uniquement
./mvnw jacoco:report                 # Génère le rapport de couverture
```

## 10. Déploiement Docker

### Dockerfile

```dockerfile
# Build stage
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .
RUN chmod +x mvnw && ./mvnw dependency:go-offline
COPY src src
RUN ./mvnw package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### docker-compose.yml

```yaml
version: '3.8'

services:
  api:
    build:
      context: .
      dockerfile: Dockerfile
    container_name: ecommerce-api
    ports:
      - "8080:8080"
    environment:
      - SERVER_PORT=8080
      - SPRING_PROFILES_ACTIVE=prod
    healthcheck:
      test: ["CMD", "wget", "-q", "--spider", "http://localhost:8080/products"]
      interval: 30s
      timeout: 10s
      retries: 3
      start_period: 40s
    restart: unless-stopped
```

### Commandes Docker

```bash
# Build de l'image
docker build -t ecommerce-api .

# Lancer le conteneur
docker run -d -p 8080:8080 --name ecommerce-api ecommerce-api

# Avec docker-compose
docker-compose up -d          # Démarrer
docker-compose down           # Arrêter
docker-compose logs -f api    # Voir les logs
docker-compose up --build     # Rebuild et démarrer
```

### .dockerignore

```
target/
.git/
.gitignore
*.md
.idea/
*.iml
```

### Variables d'environnement Docker

| Variable | Description | Défaut |
|----------|-------------|--------|
| `SERVER_PORT` | Port du serveur | 8080 |
| `SPRING_PROFILES_ACTIVE` | Profil Spring (`dev`, `prod`) | `prod` |
