# 🔧 Guide de Configuration pour le Projet Java + SQL Server

---

## ✅ Prérequis

- **Java 21 ou plus récent** doit être installé sur votre ordinateur.

---

## 🌱 Variables d’environnement

### 1. Ajouter Java à vos variables d’environnement

- **Nom de la variable** : `JAVA_HOME`  
- **Chemin de la variable** :  
  ```
  C:\Program Files\Java\jdk-23\bin
  ```
  *(Ce chemin peut varier selon votre version de Java et votre système.)*

---

## 🛠️ Configuration de SQL Server

### 1. Ouvrir SQL Server Configuration Manager

- Ouvrir l’explorateur de fichiers.
- Copier-coller le chemin suivant dans la barre d'adresse :
  ```
  C:\Windows\SysWOW64\SQLServerManager16.msc
  ```
- Appuyer sur Entrée.

### 2. Activer le protocole TCP/IP

- Suivez les instructions du module *« Créer une app Java et SQL dans AliveCode »* pour activer le **protocole de connexion TCP/IP** ou allez sur le web à cette adresse [Lien TCPIP](https://manifold.net/doc/mfd9/enable_tcp_ip_for_sql_server.htm).

### 3. Redémarrer SQL Server

- Après avoir activé le TCP/IP, redémarrer SQL Server via le gestionnaire, ou simplement redémarrer votre ordinateur.

---

## 💻 Connexion avec Azure & VS Code

### 1. Connexion dans l’extension Azure de VS Code

- **Type de connexion** : `Microsoft SQL Server`  
- **Serveur** : `localhost`  
- **Base de données** : `Gestion_Caserne`  
- **Trust server certificate** : `True`

---

## 📦 Ouvrir et exécuter le projet dans VS Code

### Étapes :

1. Ouvrir le dossier **Caserne** dans VS Code.
2. Cliquer sur le fichier **`Caserne.code-workspace`**.
3. Sélectionner **Open Workspace**.
4. Ouvrir le fichier **`TestCli.java`**.
5. Compiler le projet :
   - Raccourci : `Ctrl + Shift + B`  
   - Ou via la palette de commandes : `Ctrl + Shift + P` → `Task: Run Build Task`
6. Lancer le programme Java avec **Run Java**.
