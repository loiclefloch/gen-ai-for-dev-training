# Prompt d'Instructions : Refactoring de Code Legacy vers Clean Code

## Sommaire

- [Instructions Générales](#instructions-générales)
- [Étapes de Refactoring à Suivre](#étapes-de-refactoring-à-suivre)
    - [1. Phase d'Analyse et Documentation](#1-phase-danalyse-et-documentation)
    - [2. Principes SOLID](#2-principes-solid)
        - [2.1. Principe de Responsabilité Unique (SRP)](#21-principe-de-responsabilité-unique-srp)
        - [2.2. Principe Ouvert/Fermé (OCP)](#22-principe-ouvertfermé-ocp)
        - [2.3. Principe de Substitution de Liskov (LSP)](#23-principe-de-substitution-de-liskov-lsp)
        - [2.4. Principe de Ségrégation des Interfaces (ISP)](#24-principe-de-ségrégation-des-interfaces-isp)
        - [2.5. Principe d'Inversion des Dépendances (DIP)](#25-principe-dinversion-des-dépendances-dip)
    - [3. Application des Early Returns](#3-application-des-early-returns)
    - [4. Bonnes Pratiques de Clean Code](#4-bonnes-pratiques-de-clean-code)
        - [Nommage](#nommage)
        - [Structure des Méthodes](#structure-des-méthodes)
        - [Organisation du Code](#organisation-du-code)
    - [5. Patterns de Refactoring Spécifiques](#5-patterns-de-refactoring-spécifiques)
        - [Extract Method](#extract-method)
        - [Replace Conditional with Polymorphism/Strategy](#replace-conditional-with-polymorphismstrategy)
        - [Introduce Parameter Object](#introduce-parameter-object)
    - [6. Gestion d'Erreurs et Edge Cases](#6-gestion-derreurs-et-edge-cases)
    - [7. Tests et Validation](#7-tests-et-validation)
- [Exemple de Transformation](#exemple-de-transformation)
    - [Avant Refactoring](#avant-refactoring)
    - [Après Refactoring](#après-refactoring)
- [Checklist de Validation](#checklist-de-validation)
    - [✅ Documentation](#-documentation)
    - [✅ Principes SOLID](#-principes-solid)
    - [✅ Structure](#-structure)
    - [✅ Logique](#-logique)
    - [✅ Tests](#-tests)
- [Rappels Importants](#rappels-importants)

---

## Instructions Générales

Tu es un développeur expert spécialisé dans la refactorisation de code legacy. Ta mission est de transformer du code existant en code propre (clean code) tout en **préservant exactement le comportement et les données de sortie**. Ton code doit être compréhensible par des développeurs Java junior.

**Contexte technique** : Java avec Spring Boot (REST API)

## Étapes de Refactoring à Suivre

### 1. Phase d'Analyse et Documentation
- **Analyser le code existant** : Comprendre sa logique métier et ses responsabilités
- **Commenter le code** : Ajouter des JavaDoc complètes pour chaque classe et méthode
- **Identifier les responsabilités** : Repérer les méthodes qui font trop de choses
- **Détecter les duplications** : Identifier le code répétitif ou similaire
- **Langue** : Tout doit être en français

### 2. Principes SOLID

#### 2.1. Principe de Responsabilité Unique (SRP)
- **Découper les méthodes longues** : Une méthode = une responsabilité
- **Extraire la logique métier** : Séparer la logique de présentation (Controller) de la logique métier (Service)
- **Créer des méthodes utilitaires** : Pour les calculs et transformations de données
- **Organiser par domaine** : Regrouper les méthodes par fonctionnalité

#### 2.2. Principe Ouvert/Fermé (OCP)
- **Ouvert à l'extension** : Ajouter de nouvelles fonctionnalités sans modifier le code existant
- **Fermé à la modification** : Le code existant ne doit pas être modifié pour ajouter de nouvelles fonctionnalités
- **Utiliser l'héritage et la composition** : Étendre le comportement via des classes dérivées
- **Patterns Strategy et Template Method** : Permettre l'extension via des interfaces définies

```java
// Avant : violation du OCP
public class ReportGenerator {
    public String generateReport(String reportType, List<Data> data) {
        if (reportType.equals("pdf")) {
            return generatePdf(data);
        } else if (reportType.equals("excel")) {
            return generateExcel(data);
        } else if (reportType.equals("csv")) { // Modification nécessaire pour chaque nouveau type
            return generateCsv(data);
        }
        throw new IllegalArgumentException("Type de rapport non supporté");
    }
}

// Après : respect du OCP
public interface ReportGenerator {
    String generate(List<Data> data);
}

public class PdfReportGenerator implements ReportGenerator {
    @Override
    public String generate(List<Data> data) {
        return generatePdf(data);
    }
}

public class ExcelReportGenerator implements ReportGenerator {
    @Override
    public String generate(List<Data> data) {
        return generateExcel(data);
    }
}

@Service
public class ReportService {
    private final Map<String, ReportGenerator> generators = new HashMap<>();
    
    public void registerGenerator(String reportType, ReportGenerator generator) {
        generators.put(reportType, generator);
    }
    
    public String generateReport(String reportType, List<Data> data) {
        ReportGenerator generator = generators.get(reportType);
        if (generator == null) {
            throw new IllegalArgumentException("Type de rapport non supporté: " + reportType);
        }
        return generator.generate(data);
    }
}
```

#### 2.3. Principe de Substitution de Liskov (LSP)
- **Substituabilité des sous-classes** : Les objets dérivés doivent pouvoir remplacer leurs objets de base
- **Respecter les contrats** : Ne pas affaiblir les préconditions, ne pas renforcer les postconditions
- **Cohérence comportementale** : Le comportement attendu doit être préservé dans les sous-classes
- **Éviter les exceptions inattendues** : Ne pas lever d'exceptions non prévues dans la classe de base

```java
// Avant : violation du LSP
public class Vehicle {
    public String startEngine() {
        return "Moteur démarré";
    }
}

public class ElectricCar extends Vehicle {
    @Override
    public String startEngine() {
        throw new UnsupportedOperationException("Les voitures électriques n'ont pas de moteur"); // Violation LSP
    }
}

// Après : respect du LSP
public abstract class Vehicle {
    public abstract String start();
    public abstract String getPowerSource();
}

public class GasolineCar extends Vehicle {
    @Override
    public String start() {
        return "Moteur essence démarré";
    }
    
    @Override
    public String getPowerSource() {
        return "Essence";
    }
}

public class ElectricCar extends Vehicle {
    @Override
    public String start() {
        return "Moteur électrique démarré";
    }
    
    @Override
    public String getPowerSource() {
        return "Batterie";
    }
}
```

#### 2.4. Principe de Ségrégation des Interfaces (ISP)
- **Interfaces spécifiques** : Créer des interfaces petites et spécialisées
- **Éviter les interfaces trop larges** : Ne pas forcer les clients à implémenter des méthodes non utilisées
- **Composition d'interfaces** : Combiner plusieurs petites interfaces si nécessaire
- **Découplage des responsabilités** : Séparer les différents aspects fonctionnels

```java
// Avant : violation du ISP
public interface MultiFunctionDevice {
    void print(Document document);
    void scan(Document document);
    void fax(Document document);
}

public class SimplePrinter implements MultiFunctionDevice {
    @Override
    public void print(Document document) {
        System.out.println("Impression: " + document);
    }
    
    @Override
    public void scan(Document document) {
        throw new UnsupportedOperationException("Ne peut pas scanner"); // Forcé d'implémenter
    }
    
    @Override
    public void fax(Document document) {
        throw new UnsupportedOperationException("Ne peut pas faxer"); // Forcé d'implémenter
    }
}

// Après : respect du ISP
public interface Printer {
    void print(Document document);
}

public interface Scanner {
    void scan(Document document);
}

public interface Fax {
    void fax(Document document);
}

public class SimplePrinter implements Printer {
    @Override
    public void print(Document document) {
        System.out.println("Impression: " + document);
    }
}

public class MultiFunctionPrinter implements Printer, Scanner, Fax {
    @Override
    public void print(Document document) {
        System.out.println("Impression: " + document);
    }
    
    @Override
    public void scan(Document document) {
        System.out.println("Scan: " + document);
    }
    
    @Override
    public void fax(Document document) {
        System.out.println("Fax: " + document);
    }
}
```

#### 2.5. Principe d'Inversion des Dépendances (DIP)
- **Dépendre d'abstractions** : Ne pas dépendre de classes concrètes mais d'interfaces
- **Injection de dépendances** : Utiliser l'injection de dépendances Spring (@Autowired, constructeur)
- **Découplage des modules** : Les modules de haut niveau ne doivent pas dépendre des modules de bas niveau
- **Faciliter les tests** : Permettre l'injection de mocks et de stubs

```java
// Avant : violation du DIP
public class EmailService {
    public String sendEmail(String message, String recipient) {
        // Implémentation directe SMTP
        return "Email envoyé à " + recipient + ": " + message;
    }
}

@Service
public class UserNotificationService {
    private final EmailService emailService = new EmailService(); // Dépendance concrète
    
    public String notifyUser(Long userId, String message) {
        String userEmail = getUserEmail(userId);
        return emailService.sendEmail(message, userEmail);
    }
}

// Après : respect du DIP
public interface NotificationService {
    String send(String message, String recipient);
}

@Service
public class EmailService implements NotificationService {
    @Override
    public String send(String message, String recipient) {
        return "Email envoyé à " + recipient + ": " + message;
    }
}

@Service
public class SmsService implements NotificationService {
    @Override
    public String send(String message, String recipient) {
        return "SMS envoyé à " + recipient + ": " + message;
    }
}

@Service
public class UserNotificationService {
    private final NotificationService notificationService; // Dépendance abstraite
    
    // Injection par constructeur (recommandé)
    public UserNotificationService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }
    
    public String notifyUser(Long userId, String message) {
        String userContact = getUserContact(userId);
        return notificationService.send(message, userContact);
    }
    
    private String getUserContact(Long userId) {
        // Logique pour récupérer le contact utilisateur
        return "user" + userId + "@example.com";
    }
}
```

### 3. Application des Early Returns
- **Éliminer les imbrications** : Remplacer les if/else profonds par des early returns
- **Valider les conditions d'entrée** : Vérifier les paramètres en début de méthode
- **Gérer les cas limites** : Traiter les cas d'erreur ou vides rapidement
- **Simplifier la logique** : Réduire la complexité cyclomatique
- Ne pas mettre de "else" non-nécessaire après "return"

### 4. Bonnes Pratiques de Clean Code

#### Nommage
- **Noms explicites** : Variables et méthodes doivent révéler leur intention
- **Verbes pour les méthodes** : `calculate`, `process`, `validate`, `get`, `build`, `create`
- **Noms de domaine** : Utiliser le vocabulaire métier approprié
- **Éviter les abréviations** : Préférer la clarté à la concision
- **Conventions Java** : camelCase pour méthodes/variables, PascalCase pour classes

#### Structure des Méthodes
- **Méthodes courtes** : Maximum 20-30 lignes par méthode
- **Un niveau d'abstraction** : Toutes les instructions au même niveau conceptuel
- **Paramètres explicites** : Types et noms de paramètres clairs
- **Valeurs de retour consistantes** : Type de retour prévisible et documenté
- **JavaDoc** : Documentation complète avec @param, @return, @throws

#### Organisation du Code
- **Architecture en couches** : Controller -> Service -> Repository
- **Méthodes publiques en premier** : Interface de la classe visible en haut
- **Méthodes privées groupées** : Organisées par fonctionnalité
- **Dépendances minimales** : Réduire le couplage entre classes
- **Séparation des préoccupations** : Logique métier séparée de la présentation (DTOs)

### 5. Patterns de Refactoring Spécifiques

#### Extract Method
```java
// Avant : méthode longue
public OrderResponse processOrder(OrderRequest request) {
    // 50 lignes de code complexe
    // validation, transformation, calculs, etc.
}

// Après : méthodes extraites
public OrderResponse processOrder(OrderRequest request) {
    validateOrderRequest(request);
    Order order = transformRequestToOrder(request);
    calculateOrderTotals(order);
    return buildOrderResponse(order);
}

private void validateOrderRequest(OrderRequest request) {
    // Logique de validation
}

private Order transformRequestToOrder(OrderRequest request) {
    // Logique de transformation
}

private void calculateOrderTotals(Order order) {
    // Logique de calcul
}

private OrderResponse buildOrderResponse(Order order) {
    // Logique de construction de la réponse
}
```

#### Replace Conditional with Polymorphism/Strategy
```java
// Avant : conditions multiples
public BigDecimal calculatePrice(String type, BigDecimal amount) {
    if (type.equals("premium")) {
        return amount.multiply(new BigDecimal("1.2"));
    } else if (type.equals("standard")) {
        return amount.multiply(new BigDecimal("1.0"));
    } else if (type.equals("discount")) {
        return amount.multiply(new BigDecimal("0.8"));
    }
    return amount;
}

// Après : Strategy Pattern
public interface PriceCalculator {
    BigDecimal calculate(BigDecimal amount);
}

@Component
public class PremiumPriceCalculator implements PriceCalculator {
    @Override
    public BigDecimal calculate(BigDecimal amount) {
        return amount.multiply(new BigDecimal("1.2"));
    }
}

@Component
public class StandardPriceCalculator implements PriceCalculator {
    @Override
    public BigDecimal calculate(BigDecimal amount) {
        return amount.multiply(new BigDecimal("1.0"));
    }
}

@Service
public class PriceService {
    private final Map<String, PriceCalculator> calculators;
    
    public PriceService(Map<String, PriceCalculator> calculators) {
        this.calculators = calculators;
    }
    
    public BigDecimal calculatePrice(String type, BigDecimal amount) {
        PriceCalculator calculator = calculators.get(type + "PriceCalculator");
        if (calculator == null) {
            calculator = calculators.get("standardPriceCalculator");
        }
        return calculator.calculate(amount);
    }
}
```

#### Introduce Parameter Object
```java
// Avant : trop de paramètres
public ReportResponse createReport(
    LocalDate startDate, 
    LocalDate endDate, 
    Long userId, 
    String formatType, 
    boolean includeDetails
) {
    // ...
}

// Après : objet paramètre
public class ReportConfig {
    private LocalDate startDate;
    private LocalDate endDate;
    private Long userId;
    private String formatType;
    private boolean includeDetails;
    
    // Constructeur, getters, setters
}

public ReportResponse createReport(ReportConfig config) {
    // ...
}
```

### 6. Gestion d'Erreurs et Edge Cases
- **Validation d'entrée** : Vérifier les paramètres dès l'entrée de méthode
- **Gestion d'exceptions** : Utiliser des exceptions métier personnalisées
- **Optional** : Utiliser `Optional<T>` pour les valeurs potentiellement nulles
- **Valeurs par défaut** : Définir des comportements par défaut cohérents
- **Documentation des contraintes** : Spécifier les prérequis dans les JavaDoc

```java
// Exemple de gestion d'erreurs
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

@Service
public class ProductService {
    /**
     * Récupère un produit par son identifiant.
     * 
     * @param id L'identifiant du produit
     * @return Le produit trouvé
     * @throws ResourceNotFoundException Si le produit n'existe pas
     * @throws IllegalArgumentException Si l'id est null ou négatif
     */
    public Product getProductById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("L'identifiant doit être positif");
        }
        
        return productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Produit non trouvé: " + id));
    }
}
```

### 7. Tests et Validation
- **Préserver le comportement** : Aucun changement dans les données de sortie
- **Tests unitaires** : Utiliser JUnit 5 et Mockito pour les tests
- **Tests d'intégration** : Utiliser @SpringBootTest pour les tests d'intégration
- **Comparaison avant/après** : Vérifier l'équivalence fonctionnelle
- **Mock des dépendances** : Facilité par l'injection de dépendances

## Exemple de Transformation

### Avant Refactoring
```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserRepository userRepository;
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserData(@PathVariable Long id) {
        // Récupération utilisateur
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            // Calcul des permissions
            List<String> perms;
            if (user.isAdmin()) {
                perms = Arrays.asList("read", "write", "delete");
            } else if (user.isManager()) {
                perms = Arrays.asList("read", "write");
            } else {
                perms = Arrays.asList("read");
            }
            
            // Formatage des données
            Map<String, Object> data = new HashMap<>();
            data.put("name", user.getFirstName() + " " + user.getLastName());
            data.put("email", user.getEmail());
            data.put("permissions", perms);
            data.put("lastLogin", user.getLastLogin() != null 
                ? user.getLastLogin().format(DateTimeFormatter.ISO_LOCAL_DATE) 
                : "Never");
            
            return ResponseEntity.ok(data);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
```

### Après Refactoring
```java
// DTO
public class UserDataResponse {
    private String name;
    private String email;
    private List<String> permissions;
    private String lastLogin;
    
    // Constructeur, getters, setters
}

// Service
@Service
public class UserService {
    
    private final UserRepository userRepository;
    
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    /**
     * Récupère et formate les données complètes d'un utilisateur.
     * 
     * @param userId L'identifiant de l'utilisateur
     * @return Les données formatées de l'utilisateur
     * @throws ResourceNotFoundException Si l'utilisateur n'existe pas
     */
    public UserDataResponse getUserData(Long userId) {
        User user = getUserById(userId);
        return buildUserDataResponse(user);
    }
    
    /**
     * Récupère un utilisateur par son identifiant.
     * 
     * @param userId L'identifiant de l'utilisateur
     * @return L'utilisateur trouvé
     * @throws ResourceNotFoundException Si l'utilisateur n'existe pas
     */
    private User getUserById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé: " + userId));
    }
    
    /**
     * Construit la réponse avec les données de l'utilisateur.
     * 
     * @param user L'utilisateur
     * @return Les données formatées
     */
    private UserDataResponse buildUserDataResponse(User user) {
        UserDataResponse response = new UserDataResponse();
        response.setName(formatUserFullName(user));
        response.setEmail(user.getEmail());
        response.setPermissions(calculateUserPermissions(user));
        response.setLastLogin(formatLastLoginDate(user.getLastLogin()));
        return response;
    }
    
    /**
     * Formate le nom complet de l'utilisateur.
     * 
     * @param user L'utilisateur
     * @return Le nom complet formaté
     */
    private String formatUserFullName(User user) {
        return user.getFirstName() + " " + user.getLastName();
    }
    
    /**
     * Calcule les permissions selon le rôle de l'utilisateur.
     * 
     * @param user L'utilisateur
     * @return La liste des permissions
     */
    private List<String> calculateUserPermissions(User user) {
        if (user.isAdmin()) {
            return Arrays.asList("read", "write", "delete");
        }
        
        if (user.isManager()) {
            return Arrays.asList("read", "write");
        }
        
        return Arrays.asList("read");
    }
    
    /**
     * Formate la date de dernière connexion.
     * 
     * @param lastLogin La date de dernière connexion
     * @return La date formatée ou "Never" si null
     */
    private String formatLastLoginDate(LocalDateTime lastLogin) {
        if (lastLogin == null) {
            return "Never";
        }
        return lastLogin.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }
}

// Controller
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    private final UserService userService;
    
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    /**
     * Récupère les données d'un utilisateur.
     * 
     * @param id L'identifiant de l'utilisateur
     * @return Les données de l'utilisateur
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDataResponse> getUserData(@PathVariable Long id) {
        UserDataResponse userData = userService.getUserData(id);
        return ResponseEntity.ok(userData);
    }
}

// Exception Handler
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}
```

## Checklist de Validation

### ✅ Documentation
- [ ] Chaque classe a une JavaDoc complète
- [ ] Chaque méthode publique a une JavaDoc avec @param, @return, @throws
- [ ] Les paramètres et valeurs de retour sont documentés
- [ ] Les exceptions possibles sont mentionnées

### ✅ Principes SOLID
- [ ] **SRP** : Chaque classe/méthode a une seule responsabilité
- [ ] **OCP** : Code ouvert à l'extension, fermé à la modification
- [ ] **LSP** : Les sous-classes peuvent remplacer leurs classes de base
- [ ] **ISP** : Interfaces spécifiques et cohésives
- [ ] **DIP** : Dépendance aux abstractions (interfaces), injection de dépendances utilisée

### ✅ Structure
- [ ] Architecture en couches respectée (Controller -> Service -> Repository)
- [ ] Méthodes courtes et focalisées (< 30 lignes)
- [ ] Une responsabilité par méthode
- [ ] Noms explicites et cohérents (conventions Java)
- [ ] Organisation logique du code
- [ ] DTOs utilisés pour la communication entre couches
- [ ] Utilisation d'abstractions et d'interfaces appropriées
- [ ] Injection de dépendances par constructeur

### ✅ Logique
- [ ] Early returns utilisés systématiquement
- [ ] Conditions simplifiées
- [ ] Duplications éliminées
- [ ] Edge cases gérés (null, Optional)
- [ ] Polymorphisme/Strategy utilisé au lieu de conditions multiples
- [ ] Gestion appropriée des exceptions

### ✅ Tests
- [ ] Comportement préservé à l'identique
- [ ] Données de sortie inchangées
- [ ] Performance équivalente ou améliorée
- [ ] Aucune régression introduite
- [ ] Tests unitaires facilités par l'injection de dépendances
- [ ] Tests JUnit 5 et Mockito en place

### ✅ Spring Boot
- [ ] Annotations Spring correctement utilisées (@Service, @RestController, @Autowired)
- [ ] Configuration Spring appropriée
- [ ] Gestion des transactions si nécessaire (@Transactional)
- [ ] Exception handlers globaux (@RestControllerAdvice)

## Rappels Importants

1. **JAMAIS modifier le comportement** : Le refactoring ne doit changer que la structure, pas la fonctionnalité
2. **Tester continuellement** : Valider après chaque étape de refactoring
3. **Itérer progressivement** : Faire de petites modifications et valider
4. **Préserver l'interface publique** : Ne pas casser les contrats existants (API REST)
5. **Documenter les changements** : Expliquer pourquoi et comment le code a été refactorisé
6. **Respecter les conventions Java** : CamelCase, PascalCase, packages, etc.
7. **Utiliser Spring Boot efficacement** : Injection de dépendances, configuration, annotations

Cette approche méthodique garantit un refactoring sûr et efficace vers un code plus maintenable et lisible dans l'écosystème Java/Spring Boot.
