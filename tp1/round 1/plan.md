# Plan de développement — Backend API E-commerce

## Phase 1 : Setup du projet

### 1.1 Initialisation Spring Boot
- [ ] Créer le projet avec Spring Initializr (Spring Web, Jackson)
- [ ] Configurer le `pom.xml` avec les dépendances
- [ ] Créer la classe `Application.java`
- [ ] Configurer `application.properties`

### 1.2 Conversion des données
- [ ] Convertir `products_dataset.csv` en `products.json`
- [ ] Placer le fichier dans `src/main/resources/data/`

**Fichiers à créer :**
```
backend/
├── pom.xml
├── src/main/java/com/ecommerce/Application.java
├── src/main/resources/
│   ├── application.properties
│   └── data/products.json
```

---

## Phase 2 : Modèle et Repository

### 2.1 Entité Product
- [ ] Créer la classe `Product.java` avec tous les champs
- [ ] Ajouter les annotations Jackson pour le mapping JSON

### 2.2 Repository
- [ ] Créer `ProductRepository.java`
- [ ] Implémenter le chargement du fichier JSON au démarrage
- [ ] Implémenter `findAll()` et `findById()`

**Fichiers à créer :**
```
src/main/java/com/ecommerce/
├── model/Product.java
└── repository/ProductRepository.java
```

---

## Phase 3 : Service métier

### 3.1 ProductService
- [ ] Créer `ProductService.java`
- [ ] Implémenter la recherche textuelle (`q`)
- [ ] Implémenter les filtres :
  - Catégorie
  - Prix min/max
  - En stock
  - Mis en avant (featured)
- [ ] Implémenter le tri (prix asc/desc, pertinence)
- [ ] Implémenter `getProductById()`

**Fichier à créer :**
```
src/main/java/com/ecommerce/service/ProductService.java
```

---

## Phase 4 : Controller REST

### 4.1 DTOs
- [ ] Créer `ProductListResponse.java` (liste + total)
- [ ] Créer `ErrorResponse.java` (message d'erreur)

### 4.2 ProductController
- [ ] Créer `ProductController.java`
- [ ] Implémenter `GET /products` avec query params
- [ ] Implémenter `GET /products/{id}`
- [ ] Gérer les erreurs (400, 404)

**Fichiers à créer :**
```
src/main/java/com/ecommerce/
├── dto/
│   ├── ProductListResponse.java
│   └── ErrorResponse.java
└── controller/ProductController.java
```

---

## Phase 5 : Tests unitaires (70% couverture)

### 5.1 Tests Service
- [ ] Créer `ProductServiceTest.java`
- [ ] Tester la recherche textuelle
- [ ] Tester chaque filtre individuellement
- [ ] Tester les filtres combinés
- [ ] Tester le tri
- [ ] Tester `getProductById()`

### 5.2 Tests Repository
- [ ] Créer `ProductRepositoryTest.java`
- [ ] Tester le chargement JSON
- [ ] Tester `findAll()` et `findById()`

**Fichiers à créer :**
```
src/test/java/com/ecommerce/
├── service/ProductServiceTest.java
└── repository/ProductRepositoryTest.java
```

---

## Phase 6 : Golden Tests API (100% couverture)

### 6.1 Setup
- [ ] Créer `ProductApiGoldenTest.java`
- [ ] Configurer MockMvc pour les tests d'intégration

### 6.2 Fichiers Golden
- [ ] `get_products_no_filter.json`
- [ ] `get_products_search_valid.json`
- [ ] `get_products_search_too_short.json`
- [ ] `get_products_filter_category.json`
- [ ] `get_products_filter_price_range.json`
- [ ] `get_products_filter_in_stock.json`
- [ ] `get_products_filter_featured.json`
- [ ] `get_products_sort_price_asc.json`
- [ ] `get_products_sort_price_desc.json`
- [ ] `get_products_combined_filters.json`
- [ ] `get_products_empty_result.json`
- [ ] `get_product_by_id_found.json`
- [ ] `get_product_by_id_not_found.json`

**Fichiers à créer :**
```
src/test/java/com/ecommerce/api/
├── ProductApiGoldenTest.java
└── golden/
    └── *.json (13 fichiers)
```

---

## Phase 7 : Docker

### 7.1 Configuration Docker
- [ ] Créer `Dockerfile` (multi-stage build)
- [ ] Créer `docker-compose.yml`
- [ ] Créer `.dockerignore`

### 7.2 Validation
- [ ] Build de l'image
- [ ] Test du conteneur
- [ ] Vérifier le healthcheck

**Fichiers à créer :**
```
backend/
├── Dockerfile
├── docker-compose.yml
└── .dockerignore
```

---

## Récapitulatif des fichiers

| Phase | Fichiers | Statut |
|-------|----------|--------|
| 1 | `pom.xml`, `Application.java`, `application.properties`, `products.json` | ⬜ |
| 2 | `Product.java`, `ProductRepository.java` | ⬜ |
| 3 | `ProductService.java` | ⬜ |
| 4 | `ProductController.java`, DTOs | ⬜ |
| 5 | Tests unitaires (2 fichiers) | ⬜ |
| 6 | Golden tests (14 fichiers) | ⬜ |
| 7 | Docker (3 fichiers) | ⬜ |

**Total : ~25 fichiers à créer**

---

## Ordre d'exécution recommandé

1. **Phase 1** → Setup projet + données JSON
2. **Phase 2** → Modèle + Repository
3. **Phase 3** → Service métier
4. **Phase 4** → Controller REST
5. ✅ **Valider manuellement** que l'API fonctionne
6. **Phase 5** → Tests unitaires
7. **Phase 6** → Golden tests
8. **Phase 7** → Docker
9. ✅ **Valider le déploiement Docker**
