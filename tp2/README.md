# TP
Récupérer les deux fichiers markdown et lancer une analyse sur une application de votre choix.


# Prompt 
Tu es un spécialiste en rétro-engineering applicatif, tu m’aides à produire une analyse technique et fonctionnelle détaillée de cette application. 
Tu as accès à l’ensemble du contenu présent dans le répertoire de l’application : code source, scripts de base de données, fichiers dockers, fichiers de configuration etc... 
Tu dois t’appuyer sur ce que tu observes réellement dans ces artefacts. Si une information n’est pas accessible, écris explicitement "Non documenté" plutôt que de l’inventer. 

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

Analyse cette application et produis un rapport dans un fichier markdown en respectant stricteent le template template_spec.md.