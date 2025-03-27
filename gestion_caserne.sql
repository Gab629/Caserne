/*CREATE DATABASE Gestion_Caserne;
go*/
Use Gestion_Caserne;
GO

DROP TABLE IF EXISTS Rapporte; 
DROP TABLE IF EXISTS RepartiteurUrgence; 
DROP TABLE IF EXISTS Couvert; 
DROP TABLE IF EXISTS equipementTeam; 
DROP TABLE IF EXISTS vehicule; 
DROP TABLE IF EXISTS equipementPerso; 
DROP TABLE IF EXISTS Pompier; 
DROP TABLE IF EXISTS Equipe; 
DROP TABLE IF EXISTS Caserne; 
DROP TABLE IF EXISTS Equipement; 
DROP TABLE IF EXISTS Incident; 
DROP TABLE IF EXISTS Secteur; 

--Creation des tables

CREATE TABLE Secteur (
	idSecteur int IDENTITY PRIMARY KEY,
	codePostal char (7) CHECK (codePostal LIKE '[A-Z][0-9][A-Z] [0-9][A-Z][0-9]'),
	nomSecteur nvarchar (50) NOT NULL, --Limite le char au texte
	limitesGeographique text
	);

CREATE TABLE Incident (
	incidentID int IDENTITY (1,1),
	dateIncident date,
	adresseIncident varchar (100),
	idSecteur int,
	typeIncident nvarchar (30),
	gravite int NOT NULL, --gravite sur 3 niveaux, niveau 1: petit incident, niveau 2: incident avec blesses, niveau 3: incident sur plusieurs choses et des blesses graves
	Foreign key(idSecteur) references Secteur(idSecteur),
	PRIMARY KEY (incidentID, dateIncident, adresseIncident)
	);

CREATE TABLE Equipement (
	idEquipement int PRIMARY KEY,
	typeEquipement nvarchar(50),
	statutDisponibilite nvarchar(50) NOT NULL,
	date_expiration date
	);

CREATE TABLE Caserne (
	idCaserne int PRIMARY KEY,
	nomCaserne nvarchar(100),
	numeroTelephone varchar (12) NOT NULL CHECK (numeroTelephone LIKE '[0-9][0-9][0-9]-[0-9][0-9][0-9]-[0-9][0-9][0-9][0-9]'),
	adresse varchar(100),
	);
	

CREATE TABLE Couvert( --ajout de secteur couvert pour avoir plus d'infos sur les casernes
	idSecteur int,
	idCaserne int,
	PRIMARY KEY (idSecteur, idCaserne),
	FOREIGN KEY (idSecteur) REFERENCES Secteur(idSecteur) on delete cascade,
	FOREIGN KEY (idCaserne) references Caserne(idCaserne) on delete cascade
	);
	
CREATE TABLE Equipe (
	idEquipe int PRIMARY KEY,
	nomEquipe nvarchar (30),
	horaire nvarchar (30) CHECK (horaire in ('matin','apres-midi','soir','nuit')),
	idCaserne int,
	FOREIGN KEY (idCaserne) REFERENCES Caserne(idCaserne) on delete cascade
	);
	
CREATE TABLE RepartiteurUrgence (
	numEmploye int PRIMARY KEY identity(1,1),
	nom nvarchar (50) NOT NULL,
	prenom nvarchar (50) NOT NULL
	);

CREATE TABLE Rapporte (
	incidentId int,
	dateIncident date,
	adresseIncident varchar (100),
	numEmploye int,
	idCaserne int,
	CaserneRenfort int,
	PRIMARY KEY (incidentId, numEmploye, idCaserne),
	FOREIGN KEY (incidentId, dateIncident, adresseIncident) REFERENCES Incident(incidentId, dateIncident, adresseIncident) on delete cascade,
	FOREIGN KEY (numEmploye) REFERENCES RepartiteurUrgence(numEmploye) on delete cascade,
	FOREIGN KEY (idCaserne) REFERENCES Caserne(idCaserne),
	FOREIGN KEY (CaserneRenfort) References Caserne(idCaserne)
	);

CREATE TABLE equipementTeam (
	idEquipement int,
	idEquipe int,
	PRIMARY KEY (idEquipement, idEquipe),
	FOREIGN KEY (idEquipement) REFERENCES Equipement(idEquipement) on delete cascade,
	FOREIGN KEY (idEquipe) REFERENCES Equipe(idEquipe) on delete cascade
	);

CREATE TABLE Vehicule (
	idVehicule int PRIMARY KEY,
	typeVehicule nvarchar (30),
	plaque_immatriculation varchar (7) NOT NULL check (plaque_immatriculation LIKE '[A-Z][0-9][0-9] [A-Z][A-Z][A-Z]'),
	idEquipe int,
	FOREIGN KEY (idEquipe) REFERENCES Equipe(idEquipe) on delete cascade
	);

CREATE TABLE Pompier (
	idEmploye int PRIMARY KEY identity(1,1),
	nom varchar (50) NOT NULL,
	prenom varchar (50) NOT NULL,
	poste varchar (50),
	numTel varchar(12) NOT NULL CHECK (numTel LIKE '[0-9][0-9][0-9]-[0-9][0-9][0-9]-[0-9][0-9][0-9][0-9]'),
	idEquipe int,
	FOREIGN KEY (idEquipe) REFERENCES Equipe(idEquipe) on delete cascade
	);

CREATE TABLE equipementPerso(
	idEquipement int,
	idEmploye int,
	PRIMARY KEY (idEquipement),
	FOREIGN KEY (idEquipement) REFERENCES Equipement(idEquipement) on delete cascade,
	FOREIGN KEY (idEmploye) REFERENCES Pompier(idEmploye) on delete cascade
	);

--insertion de valeurs dans les tables

INSERT INTO Secteur (codePostal, nomSecteur, limitesGeographique) 
VALUES ('J5A 1A1','Centre-Ville', 'Rue A à Rue C'), 
('J5A 1A2','Quartier Nord', 'Rue D à Rue F'), 
('J5A 1A3','Quartier Sud', 'Rue G à Rue I'),
('J5A 1A4','Quartier Est', 'Rue J à Rue L'),
('J5A 1A5','Quartier Ouest', 'Rue M à Rue O'),
('J5A 1A6','Vieux Quartier', 'Rue P à Rue R'),
('J5A 1A7','Quartier Industriel', 'Rue S à Rue U'),
('J5A 1A8','Rosemont', 'Rue V à Rue Z');
select * from Secteur;

SET IDENTITY_INSERT Incident ON;
INSERT INTO Incident (incidentID, typeIncident, dateIncident, gravite, adresseIncident, idSecteur)
VALUES 
(1, 'Incendie', '2025-03-15', 3, '500 Rue Saint-Antoine O, Montréal, QC', 3),
(2, 'Accident de la route', '2025-03-16', 2, 'Avenue du Parc, Montréal, QC', 5),
(3, 'Inondation', '2025-03-17', 1, '1500 Boulevard de Maisonneuve O, Montréal, QC', 2),
(4, 'Feu de forêt', '2025-03-18', 3, 'Parc du Mont-Royal, Montréal, QC', 7),
(5, 'Gaz toxique', '2025-03-19', 1, '30 Rue Saint-Denis, Montréal, QC', 4),
(6, 'Accident industriel', '2025-03-20', 2, 'Rue Jean-Talon O, Montréal, QC', 6),
(7, 'Feu', '2025-03-21', 2, 'Rue Saint-Laurent, Montréal, QC', 1),
(8, 'Explosion', '2025-03-22', 1, '1500 Boulevard René-Lévesque O, Montréal, QC', 8);
select * from Incident;
SET IDENTITY_INSERT Incident OFF;


INSERT INTO Equipement(idEquipement, typeEquipement, statutDisponibilite, date_expiration) 
VALUES  (1,'Equipe', 'Disponible','2027-10-31'),
	    (2,'Equipe', 'Disponible','2027-10-31'),
		(3,'Equipe', 'Indisponible','2027-12-31'),
		(4,'Equipe', 'Disponible','2030-10-31'),
		(5,'Equipe', 'En réparation','2027-10-31'),
		(6,'Equipe', 'En utilisation','2025-10-31'),
		(7,'Equipe', 'A renouveler','2025-04-01'),
		(8,'Equipe', 'Disponible','2027-07-31'),
		(11,'Personnel', 'Disponible','2035-10-31'),
		(12,'Personnel', 'Disponible','2028-08-31'),
		(13,'Personnel', 'A nettoyer','2027-10-31'),
		(14,'Personnel', 'Indisponible','2027-02-28'),
		(15,'Personnel', 'Indisponible','2025-11-30'),
		(16,'Personnel', 'En utilisation','2027-12-31'),
		(17,'Personnel', 'En utilisation','2027-12-31'),
		(18,'Personnel', 'En utilisation','2027-12-31')
;
select * from Equipement;


INSERT INTO Caserne (idCaserne, nomCaserne, numeroTelephone, adresse) VALUES 
(1, 'Caserne Centrale', '514-123-4567', '123 Rue Centrale'),
(2, 'Caserne Quartier Nord', '514-234-5678', '456 Rue Nord'),
(3, 'Caserne Quartier Sud', '514-345-6789', '789 Rue Sud'),
(4, 'Caserne Quartier Est', '514-456-7890', '101 Rue Est'),
(5, 'Caserne Quartier Ouest', '514-567-8901', '202 Rue Ouest'),
(6, 'Caserne Vieux Quartier', '514-678-9012', '303 Rue Vieux'),
(7, 'Caserne Industrielle', '514-789-0123', '404 Rue Industrie'),
(8, 'Caserne Rosemont', '514-890-1234', '505 Rue Rosemont');
select * from Caserne;


INSERT INTO Equipe (idEquipe ,nomEquipe, horaire, idCaserne) VALUES 
(1 , 'Alpha','nuit', '1'), 
(2, 'Delta','matin', '1'), 
(3, 'Romeo','apres-midi', '1'),
(4, 'Charlie','soir', '2'),
(5, 'Bravo','nuit', '2'),
(6, 'Echo','matin', '2'),
(7, 'Zero','apres-midi', '3'),
(8, 'Triolet','soir', '3');
select * from Equipe;



SET IDENTITY_INSERT Pompier ON;
INSERT INTO Pompier (idEmploye, nom, prenom, numTel, poste, idEquipe)
VALUES 
(1, 'Dupont', 'Jean', '061-234-5678', 'Chef de Corps', 1),
(2, 'Leclerc', 'Marie', '062-345-6789', 'Officier de garde', 2),
(3, 'Martin', 'Pierre', '063-456-7890', 'Sapeur-Pompier', 3),
(4, 'Moreau', 'Sophie', '064-567-8901', 'Sapeur-Pompier', 4),
(5, 'Lemoine', 'Alexandre', '065-678-9012', 'Sapeur-Pompier', 5),
(6, 'Bernard', 'Claire', '066-789-0123', 'Chef de garde', 6),
(7, 'Garnier', 'David', '067-890-1234', 'Sapeur-Pompier', 7),
(8, 'Robert', 'Chloé', '068-901-2345', 'Officier de garde', 8);
select * from Pompier;
SET IDENTITY_INSERT Pompier OFF;

INSERT INTO equipementPerso(idEquipement, idEmploye)
VALUES (11, 1),
	   (12,5),
	   (13,4),
	   (14,8),
	   (15,6),
	   (16,7),
	   (17,3),
	   (18,2)
;
SELECT * FROM equipementPerso

INSERT INTO Vehicule (idVehicule, typeVehicule, plaque_immatriculation, idEquipe) VALUES 
(1, 'Camion-Pompe', 'A12 ABC', 2),
(2, 'Échelle', 'B34 DEF', 3),
(3, 'Unité de Secours', 'C56 GHI', 4),
(4, 'Camion-Citerne', 'D78 JKL', 5),
(5, 'Camion-Pompe', 'E90 MNO', 6),
(6, 'Unité de Commandement', 'F12 PQR', 7),
(7, 'Camion-Pompe', 'G34 STU', 8),
(8, 'Échelle', 'H56 VWX', 8),
(9, 'Camion-Citerne', 'I78 YZA', 2);
select * from Vehicule;

INSERT INTO equipementTeam (idEquipement, idEquipe) VALUES 
('1','1'), 
('2','1'), 
('3','1'),
('4','2'),
('5','2'),
('6','2'),
('7','3'),
('8','3');
select * from equipementTeam;

INSERT INTO Couvert (idSecteur, idCaserne)
VALUES 
(1, 1),
(2, 1),
(3, 2),
(4, 3),
(5, 4),
(6, 4),
(7, 5),
(8, 6),
(1, 7),
(3, 8);
select * from Couvert;


INSERT INTO RepartiteurUrgence(nom, prenom)
VALUES  ('Senecal', 'Patrick'),
		('Musso', 'Guillaume'),
		('Quentin', 'Philippe'),
		('De la Cruz', 'Pénéloppe'),
		('Levac', 'Catherine'),
		('Brown', 'Charlie'),
		('Bercie', 'Audrey'),
		('Cariere','Kassandra');
select * from RepartiteurUrgence;


INSERT INTO Rapporte (incidentId, dateIncident, adresseIncident, numEmploye, idCaserne, CaserneRenfort) VALUES 
(1, '2025-03-15', '500 Rue Saint-Antoine O, Montréal, QC', 1, 1, NULL),
(2, '2025-03-16', 'Avenue du Parc, Montréal, QC', 2, 2, 1),
(3, '2025-03-17', '1500 Boulevard de Maisonneuve O, Montréal, QC', 3, 3, NULL),
(4, '2025-03-18', 'Parc du Mont-Royal, Montréal, QC', 4, 4, 2),
(5, '2025-03-19', '30 Rue Saint-Denis, Montréal, QC', 5, 5, NULL),
(6, '2025-03-20', 'Rue Jean-Talon O, Montréal, QC', 6, 6, 3),
(7, '2025-03-21', 'Rue Saint-Laurent, Montréal, QC', 7, 7, NULL),
(8, '2025-03-22', '1500 Boulevard René-Lévesque O, Montréal, QC', 8, 8, 5);
select * from Rapporte;


--Lister les noms et prenoms des pompiers avec leur équipe, caserne et le secteur couvert par leur caserne

Select Pompier.nom, Pompier.prenom, Equipe.nomEquipe, Caserne.nomCaserne, Secteur.nomSecteur
FROM Pompier, Caserne, Equipe, Secteur, Couvert
WHERE Pompier.idEquipe = Equipe.idEquipe
AND Equipe.idCaserne = Caserne.idCaserne
AND Caserne.idCaserne = Couvert.idCaserne
AND Couvert.idSecteur = Secteur.idSecteur;

--Trouver les véhicules associés à chaque caserne et équipe Afficher le nom de la caserne, le nom de l’équipe et le type de véhicule.

SELECT Vehicule.typeVehicule, Caserne.nomCaserne, Equipe.nomEquipe
FROM Caserne, Equipe, Vehicule
WHERE Equipe.idCaserne = Caserne.idCaserne
AND Vehicule.idEquipe = Equipe.idEquipe;

--Lister les incidents où une caserne de renfort a été appelée, avec les répartiteurs responsables

SELECT Incident.typeIncident, caserne1.nomCaserne, caserne2.nomCaserne, RepartiteurUrgence.nom
FROM Rapporte
JOIN Incident ON Rapporte.incidentId = Incident.incidentId
JOIN Caserne caserne1 ON Rapporte.idCaserne = caserne1.idCaserne
JOIN Caserne caserne2 ON Rapporte.CaserneRenfort = caserne2.idCaserne
JOIN RepartiteurUrgence ON Rapporte.numEmploye = RepartiteurUrgence.numEmploye
WHERE Rapporte.CaserneRenfort IS NOT NULL;


--Afficher les incidents survenus après le 15 mars 2025
SELECT * FROM Incident
WHERE dateIncident > '2025-03-18';

--Liste des pompiers d'une équipe spécifique
SELECT Pompier.nom, Pompier.prenom, Pompier.poste
FROM Pompier 
JOIN Equipe ON Pompier.idEquipe = Equipe.idEquipe
WHERE Equipe.nomEquipe = 'Triolet';

--Liste des incidents avec leur secteur et la caserne qui les couvre
SELECT Incident.incidentID,Incident.typeIncident, Secteur.nomSecteur, Caserne.nomCaserne
FROM Incident 
JOIN Secteur ON Incident.idSecteur = Secteur.idSecteur
JOIN Couvert ON Secteur.idSecteur = Couvert.idSecteur
JOIN Caserne ON Couvert.idCaserne = Couvert.idCaserne;


--Lister les casernes ayant au moins un incident de niveau 3 (sous-requete avce ANY)
SELECT nomCaserne
FROM Caserne
WHERE idCaserne = ANY (
    SELECT idCaserne
    FROM Rapporte
    JOIN Incident ON Rapporte.incidentId = Incident.incidentID
    WHERE gravite = 3
);

--Lister les secteurs n’ayant jamais eu d’incidents grave (sous-requete avec IN)
SELECT nomSecteur
FROM Secteur
WHERE idSecteur NOT IN (
    SELECT idSecteur
    FROM Incident
    WHERE gravite = 3
);

--Lister les chefs de garde qui ont effectué des interventions avec des véhicules de type "Camion-Pompe" 
SELECT Pompier.nom, Pompier.prenom, Pompier.poste, Vehicule.typeVehicule, Incident.incidentID, Incident.typeIncident, Incident.dateIncident
FROM Pompier
JOIN Equipe ON Pompier.idEquipe = Equipe.idEquipe
JOIN Vehicule ON Vehicule.idEquipe = Equipe.idEquipe
JOIN Caserne ON Equipe.idCaserne = Caserne.idCaserne
JOIN Couvert ON Caserne.idCaserne = Couvert.idCaserne
JOIN Incident ON Incident.idSecteur = Couvert.idSecteur
WHERE Pompier.poste = 'Chef de garde'
  AND Vehicule.typeVehicule = 'Camion-Pompe';




