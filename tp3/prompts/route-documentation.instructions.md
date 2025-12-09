# Instructions pour la documentation des routes/API

## Objectif

Pour chaque fonctionnalité ou endpoint testé, créer un fichier de documentation dans `docs/[controller]/[feature].md` qui synthétise les spécifications de la route et les résultats des tests. Cette documentation doit servir de référence technique complète pour les développeurs et de guide de maintenance pour l'équipe.

Toute section obligatoire manquante, déplacée ou renommée rend la documentation non conforme. L'ordre et les titres doivent être strictement respectés.
On peux ajouter des sections optionnelles si nécessaire, mais les sections obligatoires doivent toujours être présentes et dans le bon ordre. Si des sections optionnelles sont ajoutées, on doit se poser la question de si on devrait pas les rendre obligatoires pour toutes les documentations, en mettant à jour ces instructions.

Toute modification des instructions doit être datée, versionnée et accompagnée d'un changelog en début de fichier.

En cas d'ambiguïté ou d'instruction contradictoire, l'IA doit privilégier la conformité à l'exemple fourni et signaler le cas dans le chat.

## Structure du fichier de documentation

Le fichier doit contenir les sections suivantes dans cet ordre exact :

### 1. En-tête avec métadonnées
- **Titre descriptif** : Nom de la fonctionnalité ou comportement déjà testé (ex: "API GET Liste des Produits", "Endpoint Création de Commande")
- **Tableau des métadonnées** (obligatoire pour les APIs et endpoints) :
  ```markdown
  | Type | Méthode | Endpoint               | authentifié | rôle requis |
  |------|---------|------------------------|-------------|-------------|
  | API REST | GET/POST/PUT/PATCH/DELETE | /api/chemin/vers/endpoint | [x] ou [ ] | [nom du rôle] ou [] |
  ```

### 2. Résumé
Décrire en 2-4 phrases concises le comportement attendu du code. Inclure :
- Ce que fait l'API ou l'endpoint
- Dans quels cas elle est appelée
- Le format de la réponse (pour les APIs)
- Les contraintes d'accès principales

### 3. Format de Requête et Réponse (pour les APIs)
Documenter les formats d'entrée et de sortie :
```markdown
### Requête
**Méthode** : `GET/POST/PUT/PATCH/DELETE`
**URL** : `/api/endpoint/path`
**Content-Type** : `application/json` (si applicable)

**Corps de la requête** (si applicable) :
```json
{
  "champ": "valeur"
}
```

### Réponses
#### Succès (200/201/204)
```json
{
  "resultat": "format de réponse"
}
```

#### Erreurs
**400 Bad Request** : Description des cas d'erreur
**401/403** : Problèmes d'authentification/autorisation
**404** : Ressource non trouvée
**500** : Erreur serveur interne
```

### 4. Problèmes identifiés
Structure hiérarchique avec numérotation :
```markdown
### 1. Nom du problème principal

**Problème** : Description courte du problème.

**Détails** : Explication détaillée du problème avec contexte technique.

**Impact** : Conséquences concrètes du problème (sécurité, performance, maintenance).
```

### 5. Comportement confirmé/testé

- Glossaire (obligatoire si l'API manipule des codes, statuts ou valeurs métier spécifiques)
  Inclure un glossaire listant et expliquant tous les codes, statuts, types ou valeurs métier utilisés dans les requêtes/réponses ou dans les exemples JSON. Exemples : codes de statut de commande, types de produit, états de paiement, etc.

- Règles métier (obligatoire si l'API applique ou expose des règles de gestion)
  Lister de façon exhaustive toutes les règles métier applicables à la route/API: champs obligatoires, contraintes de cohérence, droits d'accès, cas particuliers par code, gestion des doublons, états autorisés, etc. Préciser si certaines règles ne sont pas implémentées côté backend.

- Section avec sous-sections utilisant des émojis de validation :

```markdown
## Comportement [Fonctionnalité] Confirmé

### ✅ Aspect 1

Description du comportement vérifié avec points clés :
- Point 1 avec détails techniques
- Point 2 avec validation métier
- Point 3 avec contrôles de sécurité

### ✅ Aspect 2

**Confirmé/Vérifiée** : Description avec détails techniques et mécanismes internes.
```

### 6. Avantages identifiés (si applicable)
Liste numérotée des bénéfices du système testé :
1. **Nom de l'avantage** : Description avec bénéfice concret
2. **Sécurité/Performance/Maintenabilité** : Impact positif identifié
3. **Intégration système** : Cohérence avec l'architecture existante

### 7. Recommandations
Liste numérotée avec recommandations concrètes et priorisées :
1. **Action prioritaire** : Description de l'action avec justification
2. **Amélioration sécurité** : Mesures de sécurisation recommandées
3. **Optimisation performance** : Améliorations techniques suggérées
4. **Maintenance/évolution** : Pistes d'amélioration à long terme

- Pour chaque problème identifié, référencer explicitement les tests qui le révèlent dans la section "Problèmes identifiés"

## Exigences de style et format

### Formatage
- **Titres de tests en gras** : `**NomDeLaClasseDeTest**`
- **Émojis de validation** : ✅ pour les succès, ⚠️ pour les problèmes identifiés
- **Structure hiérarchique** : Numérotation pour problèmes et recommandations
- **Mots-clés en gras** : Pour les concepts importants (ex: **Problème**, **Détails**, **Impact**)
- **Tableaux de métadonnées** : Obligatoires avec format standardisé
- **Blocs de code** : JSON formaté avec indentation pour les APIs
- **Citations de code** : Utiliser des backticks pour les noms de fichiers, endpoints, classes, méthodes
- **Espaces** : Pas espaces insécables

### Ton et langue
- Documentation rédigée en français
- Ton professionnel et technique mais accessible
- Focus sur les aspects métier, sécurité et maintenabilité
- Éviter le jargon technique excessif, expliquer les concepts complexes
- Utiliser un vocabulaire précis et cohérent
- Éviter tout anglicisme ou abréviation non explicitée dans le glossaire
- Phrases courtes, à la voix active. Éviter les doubles négations.
- Éviter les formulations ouvertes ("si nécessaire", "si applicable") sans préciser les critères d'application.

### Contenu et structure
- **Exhaustivité** : Couvrir tous les aspects testés et identifiés
- **Problèmes identifiés** : Porter sur la logique du code, pas sur les tests eux-mêmes
- **Recommandations** : Concrètes, applicables et priorisées
- **Tests** : Inclure tous les scénarios testés, même ceux révélant des problèmes
- **Format de réponse** : Détaillé pour les APIs avec exemples JSON réels

## Exemple de structure complète

```markdown
# [Nom de la fonctionnalité]

| Type | Méthode | Endpoint | authentifié | rôle requis |
|------|---------|----------|-------------|-------------|
| API REST | DELETE | /api/products/{id} | [x] | [ADMIN] |

## Résumé

[Description concise du comportement]

## Format de Requête et Réponse

### Requête
**Méthode** : `DELETE`
**URL** : `/api/products/{id}`
**Content-Type** : `application/json`

### Réponses
#### Succès (204)
Aucune réponse (No Content)

#### Erreurs
**400 Bad Request** : Identifiant invalide
**401** : Non authentifié
**403** : Rôle insuffisant (ADMIN requis)
**404** : Produit non trouvé

## Glossaire

- Statut : PENDING = En attente, VALIDATED = Validé, REJECTED = Rejeté
- Type : PHYSICAL = Produit physique, DIGITAL = Produit numérique

## Règles métier

- Seul un administrateur peut supprimer un produit
- Impossible de supprimer un produit lié à une commande en cours
- Les produits supprimés sont marqués comme inactifs, pas physiquement supprimés

## Problèmes identifiés

### 1. Problème de sécurité confirmé

**Problème** : Description courte.

**Détails** : Explication détaillée.

**Impact** : Conséquences.

## Comportement [Fonctionnalité] Confirmé

### ✅ Mécanisme Principal

Description avec points clés :
- Point 1
- Point 2

### ✅ Aspect Secondaire

**Confirmé** : Description détaillée.

## Tests [Type] à réaliser

| Test | Objectif | Résultat |
|------|----------|----------|
| **ProductControllerTest** | Objectif du test | ✅ Résultat détaillé |

## Avantages [Fonctionnalité] Identifiés

1. **Avantage 1** : Description
2. **Avantage 2** : Description

## Recommandations

1. **Action 1** : Description de l'action recommandée
2. **Action 2** : Description de l'action recommandée
```

## Plan de rédaction pour chaque endpoint

1. **Analyser** : Identifier le comportement principal et les cas d'usage
2. **Structurer** : Organiser les problèmes par ordre d'importance (sécurité > fonctionnel > performance)
3. **Documenter les formats** : Inclure les formats de requête/réponse avec exemples JSON réels
4. **Valider** : Lister tous les comportements confirmés avec émojis ✅
5. **Tester** : Documenter tous les tests avec résultats précis et impact métier
6. **Identifier les points forts** : Identifier les avantages et points forts du système
7. **Recommander** : Proposer des actions concrètes et priorisées
8. **Réviser** : Vérifier la cohérence du format et du ton

## Bonnes pratiques observées

### Pour les contrôleurs REST
- ✅ **Documentation exhaustive des APIs** avec formats de requête/réponse détaillés
- ✅ **Problèmes techniques concrets** (pagination, structure JSON, filtrage, validation)
- ✅ **Tests complets** couvrant tous les scénarios (succès, erreurs, sécurité)
- ✅ **Recommandations techniques** précises et applicables

### Pour l'architecture globale
- ✅ **Vue d'ensemble du contrôleur** avec tableau récapitulatif des endpoints
- ✅ **Identification des problèmes transversaux** (duplication, permissions, gestion d'erreurs)
- ✅ **Recommandations structurelles** pour améliorer l'architecture
- ✅ **Focus métier** sur les fonctionnalités et leur utilisation

## Points d'amélioration identifiés

1. **Uniformisation requise** : Certaines documentations manquent de sections (formats de réponse, avantages)
2. **Exemples JSON** : Toujours inclure des exemples concrets tirés des tests
3. **Priorisation des recommandations** : Distinguer les actions urgentes des améliorations long terme
4. **Traçabilité des problèmes** : Lier les problèmes identifiés aux tests qui les révèlent

## Critères de qualité

Une documentation est considérée comme complète si elle :
- ✅ Respecte la structure en 7 sections obligatoires
- ✅ Inclut des exemples JSON réels pour les APIs
- ✅ Documente tous les tests réalisés avec leurs résultats
- ✅ Propose des recommandations concrètes et priorisées
- ✅ Utilise un formatage cohérent avec émojis et tableaux
