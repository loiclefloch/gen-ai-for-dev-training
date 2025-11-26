# Spécification fonctionnelle — Page de recherche produits

## 1. Description générale de la page
La page permet à un utilisateur de rechercher un produit dans un catalogue et d’en afficher une liste de résultats. L’utilisateur peut saisir un mot-clé, appliquer quelques filtres basiques (catégorie, fourchette de prix, disponibilité), puis consulter la liste des produits correspondants. L’objectif est de permettre un accès rapide aux produits et de faciliter la navigation vers la fiche détaillée.

## 2. Cas d’usages nominaux

### CU1 – Recherche par mot-clé
L’utilisateur saisit un mot-clé dans un champ et lance la recherche. Le système affiche la liste des produits correspondant au mot-clé.

### CU2 – Application de filtres
L'utilisateur peut affiner les résultats avec :
- une ou plusieurs catégories,
- un prix minimum et/ou maximum,
- la disponibilité (en stock uniquement).

### CU3 – Consultation des résultats
Pour chaque produit, la liste affiche : image, nom, catégorie, prix TTC, disponibilité, délai de livraison. L’utilisateur peut cliquer sur un produit pour afficher sa fiche.

### CU4 – Tri simple
L’utilisateur peut trier les résultats par pertinence (par défaut), prix croissant ou prix décroissant.

## 3. Cas d’erreurs ou limites
- Aucun produit ne correspond au mot-clé.
- Aucun produit ne correspond après application des filtres.
- L’utilisateur saisit un mot-clé inférieur à 2 caractères.
- Prix minimum supérieur au prix maximum.
- Erreur technique lors de la récupération des produits.

## 4. Interactions utilisateurs
- Champ texte pour saisir un mot-clé.
- Bouton Rechercher (ou touche Entrée).
- Filtres : catégories, prix min/max, "En stock uniquement".
- Menu de tri.
- Liste de résultats cliquables, pour ouvrir le détail d'un produit.
- Bouton Réinitialiser.

## 5. Règles de gestion
- La recherche nécessite min. 2 caractères.
- Les filtres s'appliquent en plus du mot-clé.
- Le prix est exprimé TTC en euros, formaté à deux décimales.
- Un produit est "en stock" si stock > 0.
- Le tri s’applique sur le résultat filtré.
- Si prix min > prix max, afficher erreur et ne pas interroger le backend.

## 6. Gestion des erreurs
### Erreur utilisateur
- Mot-clé trop court → "Saisissez au moins 2 caractères."
- Prix min > prix max → "Le prix minimum doit être inférieur ou égal au prix maximum."

### Aucun résultat
- "Aucun produit ne correspond à votre recherche."

### Erreur technique
- "Une erreur est survenue lors de la recherche. Veuillez réessayer plus tard."

## 7. Critères d’acceptation
1. Une recherche avec un mot-clé valide retourne des produits.
2. Moins de 2 caractères → message d’erreur, aucune recherche lancée.
3. Les filtres réduisent correctement les résultats.
4. Filtre "en stock" → seuls les produits disponibles apparaissent.
5. Le tri réordonne correctement la liste.
6. Liste vide → message dédié affiché.
