# Spécification fonctionnelle — Page de consultation d’un produit

## 1. Description générale de la page
La page de consultation d’un produit affiche toutes les informations essentielles d’un produit sélectionné depuis la recherche. Elle permet à l’utilisateur de comprendre les caractéristiques du produit, vérifier son prix, sa disponibilité, consulter la description, visualiser des images et revenir aux résultats.

## 2. Cas d’usages nominaux

### CU1 — Affichage du produit
L’utilisateur ouvre la page via la liste de recherche. Les informations du produit sont affichées : nom, prix, disponibilité, images, catégorie, description, délai de livraison.

### CU2 — Consultation des images
L’utilisateur voit une image principale. Si plusieurs images sont disponibles, il peut en sélectionner une autre via des miniatures.

### CU3 — Lecture des informations détaillées
L’utilisateur consulte : description longue, catégorie, prix TTC, stock, délai de livraison.

### CU4 — Retour à la recherche
Un bouton permet de revenir à la liste des résultats.

## 3. Cas d’erreurs ou limites
- Produit inexistant.
- Produit indisponible (stock = 0).
- Pas d'image disponible.
- Erreur technique lors du chargement.

## 4. Interactions utilisateurs
- Image principale et suivantes sous forme de caroussel.
- Informations : nom, prix TTC, disponibilité, catégorie, délai.
- Description longue.
- Bouton "Retour aux résultats".
- Bouton "Ajouter au panier".

## 5. Règles de gestion
- Le produit est identifié par un ID dans l’URL.
- Prix TTC en euros.
- Stock > 0 → "En stock", sinon "Indisponible".
- Si produit indisponible : masquer le bouton "Ajouter au panier".
- Afficher une image générique si aucune image n’existe.

## 6. Gestion des erreurs

### Produit introuvable
"Produit introuvable." + bouton retour.

### Erreur technique
"Impossible de charger le produit. Veuillez réessayer plus tard."

### Produit indisponible
"Ce produit n’est actuellement pas disponible." et bouton d’ajout masqué.

## 7. Critères d’acceptation
1. Produit valide → informations affichées + bouton d’ajout visible.
2. Stock = 0 → message d’indisponibilité + pas de bouton d’ajout.
3. Aucune image → image générique affichée.
4. ID invalide → message "Produit introuvable".
5. Retour aux résultats fonctionnel.
6. Carousel présent si plusieurs images sont disponibles.
