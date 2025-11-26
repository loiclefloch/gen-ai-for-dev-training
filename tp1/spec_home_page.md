# Spécification fonctionnelle — Home page du site e-commerce

## 1. Description générale de la page
La home page affiche une barre de recherche en haut de la page, suivie d'une liste de produits mis en avant (par exemple des promotions ou nouveautés). Tant qu’aucune recherche n’est saisie, ces produits restent visibles. Dès qu’une recherche est lancée, la zone de mise en avant est remplacée par les résultats de recherche.

## 2. Cas d’usages nominaux

### CU1 — Affichage de la home page
L’utilisateur arrive sur la page. La barre de recherche s’affiche en haut. En dessous, une liste de produits mis en avant apparaît.

### CU2 — Consultation des produits mis en avant
L’utilisateur peut parcourir les produits mis en avant et cliquer sur un produit pour accéder à sa fiche détaillée.

### CU3 — Saisie d'une recherche
Lorsque l’utilisateur saisit un mot-clé valide et lance la recherche, la zone des produits mis en avant disparaît et laisse place à la liste des résultats.

### CU4 — Navigation vers un produit
Depuis la mise en avant ou les résultats, l’utilisateur peut cliquer sur un produit pour accéder à la page de consultation.

### CU5 — Retour à la home sans recherche
Si l’utilisateur revient sur la home et que le champ de recherche est vide, la liste de mise en avant est affichée à nouveau.

## 3. Cas d’erreurs ou limites
- Aucune donnée de mise en avant.
- Aucun résultat pour la recherche.
- Mot-clé trop court (< 2 caractères).
- Erreur technique lors du chargement de la mise en avant ou de la recherche.

## 4. Interactions utilisateurs
- Champ de recherche.
- Bouton Rechercher (ou touche Entrée).
- Liste des produits mis en avant.
- Liste des résultats en cas de recherche.
- Produits cliquables ouvrant la fiche produit.
- Effacement du mot-clé permettant de revenir à la mise en avant.

## 5. Règles de gestion
- La barre de recherche est toujours affichée.
- Recherche valide = minimum 2 caractères.
- Champ vide → affichage de la mise en avant.
- Recherche valide → affichage des résultats uniquement.
- En absence de produits mis en avant, afficher un message simple.
- Les produits mis en avant affichent image, nom, prix TTC, disponibilité.
- Aucun détail technique ne doit être affiché en cas d’erreur.

## 6. Gestion des erreurs

### Erreur utilisateur
"Veuillez saisir au moins 2 caractères."

### Aucun produit mis en avant
"Aucun produit mis en avant actuellement."

### Aucun résultat de recherche
"Aucun produit ne correspond à votre recherche."

### Erreur technique
- Mise en avant : "Impossible de charger les produits mis en avant."
- Recherche : "Une erreur est survenue lors de la recherche. Veuillez réessayer plus tard."

## 7. Critères d’acceptation
1. La barre de recherche est visible en permanence.
2. Les produits mis en avant s’affichent s’il en existe.
3. Une recherche valide remplace la mise en avant par les résultats.
4. Aucune recherche valide → la mise en avant reste affichée.
5. Mot-clé < 2 caractères → message d’erreur, aucun appel backend.
6. Aucun résultat → message dédié.
7. Un clic sur un produit ouvre la fiche produit.
