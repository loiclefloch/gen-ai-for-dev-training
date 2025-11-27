# Règles techniques pour l’agent de code

## 1. Rôle de l'agent
Tu génères le code d’un mini site de e-commerce à partir des spécifications fonctionnelles fournies.  
Tu dois respecter strictement les specs métier.


## 2. Architecture et stack attendues
- Le front est une **application web en SPA** (Single Page Application).
- Le code doit être organisé par **pages / composants** (home, recherche, consultation produit).
- Le back est une API HTTP

**Exigences générales :**

- Séparer clairement **front** et **backend** (dossiers distincts).

## 3. Accessibilité (A11y)

Pour toutes les pages, tu dois :

- Utiliser des **balises HTML sémantiques** (`<header>`, `<main>`, `<nav>`, `<section>`, `<button>`, etc.).
- Fournir des **aria-labels** pertinents pour :
  - la barre de recherche,
  - les boutons de pagination,
  - les boutons “Voir le détail”, “Ajouter au panier”,
  - les carousels d’images.

## 4. Stratégie et couverture de tests

Tu dois mettre en place des tests automatisés.

## 5. Règles de code

Quand tu génères du code, applique ces règles :
- Extraire la **logique métier** (filtres, tri) dans des fonctions pures testables.
- Ajouter des **commentaires courts** pour chaque fonction et chaque classe.

## 6. Déploiement via Docker

Le projet doit pouvoir être lancé avec Docker.
Documenter dans le `README` comment lancer le projet avec Docker.

## 7. Données de test

- Les données produits sont stockées dans un **fichier JSON** unique côté backend.
- Le backend ne doit **jamais modifier ce fichier** en écriture.