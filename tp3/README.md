# TP3 - Refactoring de Code Legacy avec l'IA

## 📖 Vue d'ensemble

Ce TP vous permet de pratiquer le refactoring de code legacy en utilisant l'IA comme assistant. Vous travaillerez sur une application e-commerce réaliste avec des problèmes typiques de code legacy.


## 🎯 Objectifs

- Apprendre à utiliser l'IA pour ajouter des tests sur du code existant
- Refactorer une base de code legacy pour améliorer sa maintenabilité
- Préparer le code pour de futures évolutions


## 🎯 Objectifs pédagogiques

- Identifier les code smells et anti-patterns avec l'IA
- Utiliser l'IA pour ajouter des tests sur du code existant
- Refactorer progressivement une base de code
- Appliquer les principes SOLID
- Préparer le code pour de futures évolutions

## 📂 Contenu

- **`legacy-ecommerce/`** - Application Spring Boot legacy à refactorer
  - Code sans tests
  - Architecture avec couplage fort
  - Bugs critiques à corriger
  - Bonnes pratiques non respectées
  - /!\ le README.md du projet se veut réaliste : avec des erreurs, pas mis à jour, etc

## 🚀 Pour commencer

1. Ouvrez uniquement le dossier `legacy-ecommerce/` dans votre IDE: il ne faut pas que l'IA ait accès aux autres fichiers de ce repository (instructions, etc) pour ne pas l'orienter.
2. Utilisez votre assistant IA pour vous guider

## 📚 Ressources

- [Refactoring Guru](https://refactoring.guru/)
- [SOLID Principles](https://www.digitalocean.com/community/conceptual_articles/s-o-l-i-d-the-first-five-principles-of-object-oriented-design)
- [Spring Testing Guide](https://spring.io/guides/gs/testing-web)

## 🎓 Étapes

- Mission 1 (Tests, analyse métier et listing des bugs)
- Mission 2 (Refactoring)
- Mission 3 (Évolution)

## 📦 Contexte du projet

Vous héritez d'une application e-commerce développée il y a quelques années. Le code fonctionne mais présente de nombreux problèmes :

## 🚀 Démarrage

### Prérequis

- Java 11+
- Maven 3.6+
- Un assistant IA (GitHub Copilot, Claude Code, Cursor, etc.)

### Installation

```bash
cd tp3/legacy-ecommerce
mvn clean install
mvn spring-boot:run
```

L'application démarre sur `http://localhost:8080`

### Tester l'API

```bash
# Lister les produits
curl http://localhost:8080/api/products

# Créer un panier
curl -X POST http://localhost:8080/api/orders/cart?userId=1

# Ajouter un produit au panier
curl -X POST "http://localhost:8080/api/orders/cart/1/items?productId=1&quantity=2"

# Créer une commande
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{"userId": 1, "cartId": 1, "shippingAddress": "123 Main St"}'
```


## 📝 Méthodologie

### Phase 1 : Analyse et Compréhension 🔍

**Objectif** : Comprendre le code existant et identifier les zones à risque

**Prompt suggéré pour votre IA** :
```
Analyse le code de cette application e-commerce legacy. 
Identifie :
1. Les principales violations des principes SOLID
2. Les bugs potentiels ou avérés
3. Les zones de code les plus critiques à tester en priorité
4. Les dépendances et couplages problématiques

Fournis-moi un rapport structuré avec des exemples concrets du code.
```

**Livrables** :
- Carte des dépendances
- Liste des bugs identifiés
- Priorisation des zones à refactorer

### Phase 2 : Stratégie de Refactoring 📝

**Objectif** : Définir un plan d'action structuré

**Prompt suggéré** :
```
Sur la base de l'analyse précédente, propose-moi une stratégie de refactoring en suivant ces principes :
1. Ne jamais refactorer sans tests
2. Procéder par petits incréments
3. Toujours garder le code fonctionnel
4. Prioriser les zones à fort impact

Détaille l'ordre des étapes et les patterns à appliquer.
```

**Livrables** :
- Plan de refactoring étape par étape
- Identification des patterns à appliquer
- Stratégie de tests

### Phase 3 : Implémentation 🔨

#### Étape 3.1 : Ajouter des tests

**Prompt suggéré** :
```
Commençons par ajouter des tests unitaires pour [OrderService/ProductRepository/etc.].
Utilise JUnit 5 et Mockito.
Couvre au minimum :
- Les cas nominaux
- Les cas d'erreur (null, valeurs invalides)
- Les edge cases identifiés dans l'analyse

Génère les tests pour la classe OrderService en premier.
```

#### Étape 3.2 : Refactorer avec la protection des tests

**Prompt suggéré** :
```
Maintenant que nous avons des tests, refactorons OrderService :
1. Remplace le singleton par l'injection de dépendances Spring
2. Extrais les validations dans des méthodes dédiées
3. Implémente une gestion d'erreurs avec des exceptions custom
4. Ajoute la gestion transactionnelle

Assure-toi que tous les tests passent après chaque modification.
```

#### Étape 3.3 : Appliquer les principes SOLID

**Prompt suggéré** :
```
Appliquons les principes SOLID :
1. Single Responsibility : sépare les responsabilités de OrderService
2. Open/Closed : utilise des interfaces pour les repositories
3. Liskov Substitution : assure-toi que les abstractions sont cohérentes
4. Interface Segregation : crée des interfaces spécifiques
5. Dependency Inversion : inverse les dépendances vers des abstractions

Procède classe par classe en maintenant les tests verts.
```

## 🎓 Missions du TP

### Mission 1 : Tests et correction des bugs critiques

1. **Ajouter des tests unitaires** pour `OrderService` couvrant :
    - Création de commande
    - Annulation de commande
    - Calcul du total

2. **Corriger les bugs** identifiés :
    - Restaurer le stock lors de l'annulation
    - Vérifier le stock avant création de commande
    - Gérer les cas d'erreur (cart null, produit inexistant)

3. **Ajouter des tests pour ProductRepository**

### Mission 2 : Refactoring de l'architecture

1. **Supprimer les singletons** et implémenter l'injection de dépendances Spring
2. **Créer des DTOs** pour les requêtes/réponses API
3. **Implémenter une gestion d'erreurs globale** avec `@ControllerAdvice`
4. **Ajouter de la validation** avec Bean Validation (`@Valid`, `@NotNull`, etc.)

### Mission 3 : Évolution - Système de promotions

Maintenant que le code est propre et testé, ajoutez une nouvelle fonctionnalité :

**Fonctionnalité** : Système de codes promo
- Un code promo donne un pourcentage de réduction
- Un code promo a une date d'expiration
- Un code promo peut être à usage unique ou multiple
- La réduction s'applique au total de la commande

**Approche suggérée** :
1. Utiliser Spec-Driven Development (voir TP1)
2. Créer d'abord les tests
3. Implémenter en suivant les bonnes pratiques du code refactoré

## 💡 Conseils

### Utilisation de l'IA

1. **Demandez des explications** : Ne copiez pas aveuglément, comprenez
2. **Itérez** : Affinez vos prompts selon les résultats
3. **Validez** : Testez systématiquement le code généré
4. **Apprenez** : Notez les patterns et techniques proposées

### Bonnes pratiques

- ✅ Commitez après chaque étape réussie
- ✅ Lancez les tests avant chaque commit
- ✅ Gardez les changements petits et incrémentaux
- ✅ Documentez vos décisions dans les commits

### Pièges à éviter

- ❌ Ne refactorez jamais sans tests
- ❌ Ne changez pas tout d'un coup
- ❌ Ne faites pas confiance aveuglément à l'IA
- ❌ N'oubliez pas de vérifier les edge cases

## 📚 Ressources

- [Principes SOLID](https://www.digitalocean.com/community/conceptual_articles/s-o-l-i-d-the-first-five-principles-of-object-oriented-design)
- [Refactoring patterns](https://refactoring.guru/design-patterns)
- [Spring Boot Testing](https://spring.io/guides/gs/testing-web)
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)

## En cas de blocage :

1. Relisez la phase de la méthodologie concernée
2. Demandez à votre IA de clarifier
3. Consultez les ressources fournies
4. Contactez le formateur

Bon courage ! 🚀
