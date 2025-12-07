# TP
Lancer une analyse sur une application de votre choix.

---

# 1. Prompt général - niveau 1
Tu es un expert en rétro-engineering applicatif, tu m’aides à produire une analyse technique et fonctionnelle détaillée de cette application. 

### Contexte
Tu as accès à l’ensemble du contenu présent dans le répertoire de l’application : code source, scripts de base de données, fichiers dockers, fichiers de configuration etc... 
Tu dois t’appuyer sur ce que tu observes réellement dans ces artefacts. Si une information n’est pas accessible, écris explicitement "Non documenté" plutôt que de l’inventer. 

### Périmètre de l'analyse
Sur l’axe fonctionnel tu dois décrire : 
- les objectifs métier de l’application et les types d’utilisateurs
- la liste des fonctionnalités principales et des utilisateurs associés
- la liste des entités métiers principales, leurs caractéristiques et leurs relations
- les cas d’usages principaux

Sur l’axe technique, tu dois décrire :
- la stack technique : langages, technologies, frameworks, composants d’infrastructure
- l’architecture applicative : les principaux modules, l’organisation du code, les patterns architecturaux
- la testabilité: présence de tests automatisés, typologie de ces tests
- le modèle de données : principales bases, schémas et tables
- l’architecture de déploiement : conteneurs, environnements, dépendances techniques 

### Livraison attendue
Analyse cette application et produis un rapport dans un fichier markdown en respectant stricteent le template template_spec.md.

---

# 2. Prompt analyse fonctionnelle détaillée - niveau 2
Tu es un expert en rétro-engineering fonctionnel. 
Tu m'aides à produire une analyse fonctionnelle détaillée d’un périmètre précis de l’application.

### Contexte
Tu disposes des éléments observés dans le code et dans le rapport général d’analyse **rapport_analyse.txt**.
Tu dois t’appuyer uniquement sur ce qui est réellement visible dans le code (UI, ViewModels, state Redux, reducers, middleware, repositories, modèles, navigation).
Lorsque tu déduis une information, indique (Déduit). Lorsque l'information n'est pas observable, indique (Non documenté).

### Périmètre ciblé
Le périmètre fonctionnel à analyser est : **{{perimetre_fonctionnel}}**.

### Livraison attendue
Réalise cette analyse détaillée et produis un rapport dans un fichier markdown en respectant strictement le template template_analyse_fonc_detaillee.md.
