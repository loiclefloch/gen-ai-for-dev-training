# TEMPLATE – Analyse générale

# 1. Synthèse générale
Décrire une brève synthèse des objectifs métier, du contexte général, du périmètre fonctionnel et de la stack principale.
(Observé / Déduit)

---

# 2. Analyse fonctionnelle

## 2.1 Objectifs métier
Décrire les besoins métier auxquels répond l’application.  
(Observé / Déduit / Non documenté)

## 2.2 Types d’utilisateurs
Lister les utilisateurs internes ou externes et leurs rôles.  
| Utilisateur | Rôle | Commentaires |
|-------------|------|--------|
| {{Profil}} | {{Description}} | {{Commentaires éventuels}} |

## 2.3 Fonctionnalités principales
| Fonctionnalité | Description | Utilisateurs | Flux associés | Source |
|----------------|-------------|--------------|---------------|--------|
| {{Nom}} | {{Description}} | {{Liste}} | API / Fichiers / Messages | Observé / Déduit / Non documenté |

## 2.4 Entités métiers
| Entité | Attributs clés | Relations | Commentaire | Source |
|--------|----------------|-----------|--------------|--------|
| {{Nom}} | {{Liste}} | {{Liens}} | {{Notes}} | Observé / Déduit / Non documenté |

## 2.5 Cas d’usage
Décrire les cas d'utilisation principaux.
| Cas d’usage | Acteur | Déclencheur | Étapes clés | Résultat attendu | Source |
|-------------|--------|-------------|-------------|------------------|--------|
| {{Nom}} | {{Utilisateur}} | {{Déclencheur}} | {{Étapes}} | {{Résultat}} | Observé / Déduit / Non documenté |

Intégrer des schémas mermaid si pertinent, en plus du tableau.

---

# 3 Stack technique

## 3.1 Langages et frameworks
Liste précise, avec versions si observables.  
(Observé / Déduit / Non documenté)

## 3.2 Bibliothèques et dépendances majeures
Indiquer les packages importants, fichiers de configuration et points d’entrée.  

| Dépendance | Version | Rôle |
|------------|---------|------|
| {{Nom}} | {{Version}} | {{Usage}} |

## 3.3 Composants d’infrastructure
Bases de données, caches, bus, proxies, scripts batch, etc.  

| Composant | Version | Usage | Source |
|-----------|---------|-------|--------|
| {{Base/Cache/Bus}} | {{Version}} | {{Rôle}} | Observé / Déduit |

---

# 4. Architecture applicative

## 4.1 Organisation du code
Description des modules, packages, couches, conventions.  
(Observé)

| Module / Package | Description | Répertoires et fichiers clés | Source |
|------------------|-------------|---------------|--------|
| {{Nom}} | {{Description}} | {{Paths}} | Observé |

## 4.2 Patterns architecturaux
Hexagonal, MVC, microservices, monolithe modulaire…  
(Observé / Déduit)

## 4.3 Points d’intégration
APIs externes, services tiers, files de messages, etc.  
(Observé / Déduit)


---

# 5. Testabilité et tests automatisés

## 5.1 Présence de tests
Types observés : unitaires, intégration, API, end-to-end…  
Répertoires et frameworks utilisés. 

| Type de test | Framework | Répertoires | Volume estimé | Source |
|--------------|-----------|-------------|----------------|--------|
| Unitaire | {{Nom}} | {{Path}} | {{Nb}} | Observé |
| Intégration | {{Nom}} | {{Path}} | {{Nb}} | Observé |
| E2E | {{Nom}} | {{Path}} | {{Nb}} | Observé |


## 5.2 Couverture perçue
Indication qualitative si l’information n’est pas mesurable. 

| Aspect | Niveau | Justification |
|--------|--------|---------------|
| Couverture globale | Faible / Moyenne / Forte | {{Notes}} |
| Testabilité du code | Faible / Moyenne / Forte | {{Notes}} |

---

# 6. Modèle de données

## 6.1 Bases et schémas
Noms des bases, schémas, fichiers de création SQL.  
(Observé)
| Base / Schéma | Description | Fichiers source | Source |
|----------------|-------------|------------------|--------|
| {{Nom}} | {{Description}} | {{Path}} | Observé |

## 6.2 Tables / collections principales
Description des tables métier et de leurs colonnes clés.  
(Observé)
| Table | Colonnes clés | Description | Source |
|-------|----------------|-------------|--------|
| {{Nom}} | {{Liste}} | {{Usage}} | Observé |

## 6.3 Relations
PK/FK, cardinalités visibles dans le SQL ou inférées par usage.  
| Origine | Destination | Type | Source |
|---------|-------------|-------|--------|
| {{Table A}} | {{Table B}} | 1-N / N-N / FK | Observé / Déduit |

---

# 7. Architecture de déploiement

## 7.1 Conteneurs et exécution
Dockerfiles, images, entrypoints, scripts de démarrage.  
(Observé)

## 7.2 Environnements

| Environnement | Spécificités | Fichiers associés | Source |
|---------------|--------------|-------------------|--------|
| Dev / Test / Preprod / Prod | {{Notes}} | {{Paths}} | Observé / Non documenté |

---

# 8. Points particuliers et observations complémentaires
Notes sur des éléments atypiques, risques, dette technique, incohérences ou patterns spécifiques.  
(Observé / Déduit)

---

# 9. Hypothèses
Lister les hypothèses formulées lorsqu’une information n’est pas totalement observable dans les artefacts analysés.  

| Hypothèse | Description | Justification | Impact potentiel | Statut |
|-----------|-------------|---------------|------------------|--------|
| {{Nom}} | {{Supposition}} | {{Raison}} | {{Impact}} | À confirmer |
| {{Nom}} | {{Supposition}} | {{Indices}} | {{Impact}} | À confirmer |

---

# 10. Annexes
Chemins de fichiers clés, extraits de code, ressources utiles.  
(Observé)
