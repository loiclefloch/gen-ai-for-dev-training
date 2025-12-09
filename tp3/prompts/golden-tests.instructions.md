# Instructions pour les Golden Tests - Java Spring Boot

## Contexte

Ce document fournit des instructions pour créer des **golden tests** (tests d'approbation) pour une application Spring Boot. Les golden tests capturent le comportement actuel du code legacy avant le refactoring, permettant de s'assurer que les modifications ne changent pas le comportement existant de manière non intentionnelle.

## Objectifs des Golden Tests

1. **Capturer le comportement actuel** : Enregistrer les sorties du système tel qu'il existe aujourd'hui
2. **Sécuriser le refactoring** : Détecter automatiquement tout changement de comportement
3. **Documentation vivante** : Les tests servent de documentation du comportement actuel
4. **Filet de sécurité** : Permettre un refactoring en confiance

## Structure des Tests dans Spring Boot

### Organisation des fichiers de test

```
src/
├── main/
│   └── java/
│       └── com/ecommerce/
│           ├── controller/
│           ├── service/
│           ├── model/
│           └── repository/
└── test/
    ├── java/
    │   └── com/ecommerce/
    │       ├── controller/
    │       │   └── ProductControllerGoldenTest.java
    │       ├── service/
    │       │   └── OrderServiceGoldenTest.java
    │       └── integration/
    │           └── EcommerceIntegrationGoldenTest.java
    └── resources/
        └── golden/
            ├── product-api-responses/
            │   ├── get-all-products.json
            │   ├── get-product-by-id.json
            │   └── search-products.json
            └── order-service-outputs/
                ├── create-order.json
                └── calculate-revenue.json
```

## Types de Golden Tests

### 1. Tests des Contrôleurs REST (API)

Utilisez `MockMvc` pour tester les endpoints REST et capturer les réponses JSON.

**Exemple : ProductControllerGoldenTest.java**

```java
package com.ecommerce.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerGoldenTest {

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private static final String GOLDEN_DIR = "src/test/resources/golden/product-api-responses/";
    
    @BeforeEach
    void setUp() {
        // Initialiser l'état de test si nécessaire
    }
    
    @Test
    void testGetAllProducts_capturesGoldenResponse() throws Exception {
        // Exécuter la requête
        MvcResult result = mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andReturn();
        
        String actualResponse = result.getResponse().getContentAsString();
        
        // Comparer avec le fichier golden
        String goldenFile = GOLDEN_DIR + "get-all-products.json";
        compareWithGolden(goldenFile, actualResponse);
    }
    
    @Test
    void testGetProductById_capturesGoldenResponse() throws Exception {
        Long productId = 1L;
        
        MvcResult result = mockMvc.perform(get("/api/products/{id}", productId))
                .andExpect(status().isOk())
                .andReturn();
        
        String actualResponse = result.getResponse().getContentAsString();
        String goldenFile = GOLDEN_DIR + "get-product-by-id-1.json";
        
        compareWithGolden(goldenFile, actualResponse);
    }
    
    @Test
    void testSearchProducts_capturesGoldenResponse() throws Exception {
        String query = "laptop";
        
        MvcResult result = mockMvc.perform(get("/api/products/search")
                .param("query", query))
                .andExpect(status().isOk())
                .andReturn();
        
        String actualResponse = result.getResponse().getContentAsString();
        String goldenFile = GOLDEN_DIR + "search-products-laptop.json";
        
        compareWithGolden(goldenFile, actualResponse);
    }
    
    private void compareWithGolden(String goldenPath, String actualResponse) throws Exception {
        File goldenFile = new File(goldenPath);
        
        if (!goldenFile.exists()) {
            // Mode création : enregistrer la sortie actuelle comme golden
            goldenFile.getParentFile().mkdirs();
            Files.write(Paths.get(goldenPath), actualResponse.getBytes());
            System.out.println("✓ Golden file created: " + goldenPath);
        } else {
            // Mode vérification : comparer avec le golden
            String expectedResponse = new String(Files.readAllBytes(Paths.get(goldenPath)));
            
            // Normaliser les JSON pour comparaison
            Object expectedJson = objectMapper.readValue(expectedResponse, Object.class);
            Object actualJson = objectMapper.readValue(actualResponse, Object.class);
            
            String expectedNormalized = objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(expectedJson);
            String actualNormalized = objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(actualJson);
            
            assertEquals(expectedNormalized, actualNormalized,
                    "Response differs from golden file: " + goldenPath);
        }
    }
}
```

### 2. Tests des Services (Logique Métier)

Testez les services en isolant la logique métier et en capturant les résultats.

**Exemple : OrderServiceGoldenTest.java**

```java
package com.ecommerce.service;

import com.ecommerce.model.Cart;
import com.ecommerce.model.Order;
import com.ecommerce.model.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderServiceGoldenTest {

    private OrderService orderService;
    private ObjectMapper objectMapper;
    
    private static final String GOLDEN_DIR = "src/test/resources/golden/order-service-outputs/";
    
    @BeforeEach
    void setUp() {
        orderService = OrderService.getInstance();
        objectMapper = new ObjectMapper();
        // Réinitialiser l'état si nécessaire
        orderService.resetCounters();
    }
    
    @Test
    void testCreateOrder_capturesGoldenOutput() throws Exception {
        // Préparer les données de test
        Long userId = 1L;
        Cart cart = orderService.createCart(userId);
        
        Product product = new Product();
        product.id = 1L;
        product.name = "Test Product";
        product.price = 99.99;
        product.stock = 10;
        
        orderService.addToCart(cart, product, 2);
        
        // Exécuter l'opération
        Order order = orderService.createOrder(userId, cart.id, "123 Test St");
        
        // Capturer le résultat
        String actualOutput = objectMapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(order);
        
        String goldenFile = GOLDEN_DIR + "create-order-standard.json";
        compareWithGolden(goldenFile, actualOutput);
    }
    
    @Test
    void testCalculateTotalRevenue_capturesGoldenValue() throws Exception {
        // Créer plusieurs commandes de test
        setupTestOrders();
        
        // Calculer le revenu
        double revenue = orderService.calculateTotalRevenue();
        
        // Capturer le résultat
        String actualOutput = String.format("%.2f", revenue);
        String goldenFile = GOLDEN_DIR + "total-revenue.txt";
        
        compareWithGolden(goldenFile, actualOutput);
    }
    
    @Test
    void testGetOrdersByUser_capturesGoldenList() throws Exception {
        Long userId = 1L;
        setupTestOrdersForUser(userId);
        
        List<Order> orders = orderService.getOrdersByUser(userId);
        
        String actualOutput = objectMapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(orders);
        
        String goldenFile = GOLDEN_DIR + "orders-by-user-1.json";
        compareWithGolden(goldenFile, actualOutput);
    }
    
    private void setupTestOrders() {
        // Créer des données de test reproductibles
        // ...
    }
    
    private void setupTestOrdersForUser(Long userId) {
        // Créer des données de test pour un utilisateur spécifique
        // ...
    }
    
    private void compareWithGolden(String goldenPath, String actualOutput) throws Exception {
        File goldenFile = new File(goldenPath);
        
        if (!goldenFile.exists()) {
            goldenFile.getParentFile().mkdirs();
            Files.write(Paths.get(goldenPath), actualOutput.getBytes());
            System.out.println("✓ Golden file created: " + goldenPath);
        } else {
            String expectedOutput = new String(Files.readAllBytes(Paths.get(goldenPath)));
            assertEquals(expectedOutput.trim(), actualOutput.trim(),
                    "Output differs from golden file: " + goldenPath);
        }
    }
}
```

### 3. Tests d'Intégration End-to-End

Testez des scénarios complets avec plusieurs composants.

```java
@SpringBootTest
@AutoConfigureMockMvc
class EcommerceIntegrationGoldenTest {

    @Autowired
    private MockMvc mockMvc;
    
    @Test
    void testCompleteOrderFlow_capturesGoldenScenario() throws Exception {
        // 1. Créer un panier
        // 2. Ajouter des produits
        // 3. Créer une commande
        // 4. Vérifier le statut
        // Capturer chaque étape comme golden
    }
}
```

## Utilisation de la bibliothèque ApprovalTests

Alternative plus simple avec ApprovalTests :

```java
import org.approvaltests.Approvals;
import org.junit.jupiter.api.Test;

class ProductControllerApprovalTest {

    @Test
    void testGetAllProducts() throws Exception {
        String response = callApiAndGetResponse("/api/products");
        
        // ApprovalTests gère automatiquement les fichiers golden
        Approvals.verify(response);
    }
    
    private String callApiAndGetResponse(String endpoint) {
        // Logique pour appeler l'API
        return "...";
    }
}
```

## Bonnes Pratiques

### 1. Isolation des Tests

- Réinitialiser l'état avant chaque test
- Utiliser des données de test reproductibles
- Nettoyer après les tests si nécessaire

```java
@BeforeEach
void setUp() {
    // Réinitialiser les singletons
    OrderService.getInstance().resetCounters();
    ProductRepository.getInstance().clear();
}

@AfterEach
void tearDown() {
    // Nettoyer si nécessaire
}
```

### 2. Normalisation des Données

- Normaliser les timestamps
- Trier les listes pour avoir un ordre déterministe
- Ignorer les champs non-déterministes (IDs auto-générés, dates)

```java
private String normalizeJson(String json) throws Exception {
    ObjectNode node = (ObjectNode) objectMapper.readTree(json);
    
    // Remplacer les timestamps par des valeurs fixes
    if (node.has("createdAt")) {
        node.put("createdAt", "2025-01-01T00:00:00Z");
    }
    
    return objectMapper.writerWithDefaultPrettyPrinter()
            .writeValueAsString(node);
}
```

### 3. Gestion des Fichiers Golden

```
src/test/resources/golden/
├── README.md                    # Documentation des golden files
├── product-api-responses/       # Réponses des APIs produits
├── order-service-outputs/       # Sorties du service commandes
└── integration-scenarios/       # Scénarios end-to-end
```

### 4. Documentation des Golden Tests

Créer un `README.md` dans le répertoire golden :

```markdown
# Golden Test Files

Ces fichiers contiennent les sorties attendues du système legacy.

## Comment mettre à jour les golden files

1. Supprimer le fichier golden existant
2. Relancer le test
3. Le nouveau fichier sera créé automatiquement
4. Vérifier manuellement que le nouveau comportement est correct
5. Committer le nouveau fichier

## Organisation

- `product-api-responses/` : Réponses JSON des endpoints produits
- `order-service-outputs/` : Résultats des opérations de commande
```

## Workflow de Création des Golden Tests

### Phase 1 : Capture initiale

```bash
# 1. Écrire les tests sans fichiers golden
mvn test -Dtest=ProductControllerGoldenTest

# 2. Les fichiers golden sont créés automatiquement
# 3. Vérifier manuellement les fichiers créés
# 4. Committer les fichiers golden

git add src/test/resources/golden/
git commit -m "Add golden tests for ProductController"
```

### Phase 2 : Refactoring

```bash
# 1. Faire le refactoring
# 2. Exécuter les golden tests
mvn test -Dtest=*GoldenTest

# 3. Si les tests échouent, analyser les différences
# 4. Décider si les changements sont intentionnels ou non
```

### Phase 3 : Mise à jour intentionnelle

```bash
# Si le nouveau comportement est correct :
# 1. Supprimer les anciens golden files
rm src/test/resources/golden/product-api-responses/get-all-products.json

# 2. Régénérer
mvn test -Dtest=ProductControllerGoldenTest#testGetAllProducts

# 3. Vérifier et committer
git add src/test/resources/golden/
git commit -m "Update golden test after refactoring"
```

## Commandes Maven Utiles

```bash
# Exécuter tous les golden tests
mvn test -Dtest=*GoldenTest

# Exécuter un test spécifique
mvn test -Dtest=ProductControllerGoldenTest

# Exécuter avec plus de logs
mvn test -Dtest=*GoldenTest -X

# Nettoyer et reconstruire
mvn clean test -Dtest=*GoldenTest
```

## Checklist pour Créer un Golden Test

- [ ] Identifier la fonctionnalité à tester
- [ ] Créer la classe de test avec le suffixe `GoldenTest`
- [ ] Ajouter les annotations Spring appropriées (`@SpringBootTest`, `@AutoConfigureMockMvc`)
- [ ] Préparer les données de test reproductibles
- [ ] Implémenter la méthode `compareWithGolden()`
- [ ] Écrire le test qui capture la sortie actuelle
- [ ] Exécuter le test pour générer le fichier golden
- [ ] Vérifier manuellement le fichier golden généré
- [ ] Documenter le test (commentaires, README)
- [ ] Committer le test et le fichier golden

## Exemples de Scénarios à Tester

### Pour ProductController
- ✓ GET tous les produits
- ✓ GET produit par ID
- ✓ GET produits par catégorie
- ✓ POST création de produit
- ✓ PUT mise à jour de produit
- ✓ DELETE suppression de produit
- ✓ GET recherche de produits
- ✓ POST mise à jour du stock

### Pour OrderService
- ✓ Création de panier
- ✓ Ajout d'articles au panier
- ✓ Création de commande
- ✓ Calcul du revenu total
- ✓ Récupération des commandes par utilisateur
- ✓ Annulation de commande
- ✓ Mise à jour du statut de commande

## Debugging des Golden Tests

### Test échoue : différence détectée

```java
// Activer les logs détaillés
@Test
void testWithDetailedLogging() throws Exception {
    String actual = getActualOutput();
    String expected = getExpectedFromGolden();
    
    if (!actual.equals(expected)) {
        System.out.println("=== EXPECTED ===");
        System.out.println(expected);
        System.out.println("=== ACTUAL ===");
        System.out.println(actual);
        System.out.println("=== DIFF ===");
        // Afficher les différences ligne par ligne
    }
    
    assertEquals(expected, actual);
}
```

### Fichier golden non trouvé

Vérifier que :
- Le chemin du fichier est correct
- Le répertoire existe
- Le nom du fichier correspond au test

## Conclusion

Les golden tests sont un outil puissant pour sécuriser le refactoring du code legacy. En capturant le comportement actuel du système, ils permettent de détecter immédiatement tout changement non intentionnel lors du refactoring.

**Points clés à retenir :**
- Créer les golden tests AVANT le refactoring
- Utiliser des données de test reproductibles
- Normaliser les sorties pour éviter les faux positifs
- Documenter les fichiers golden
- Mettre à jour les golden files de manière intentionnelle uniquement
