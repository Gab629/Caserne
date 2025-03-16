CREATE DATABASE Gestion_Caserne;
go
Use Gestion_Caserne;
GO

DROP TABLE IF EXISTS Rapporte; -- Meryem
DROP TABLE IF EXISTS RepartiteurUrgence; -- Mica
DROP TABLE IF EXISTS Couvert; -- Salla
DROP TABLE IF EXISTS equipementTeam; -- Gab
DROP TABLE IF EXISTS vehicule; -- Meryem
DROP TABLE IF EXISTS equipementPerso; -- Mica
DROP TABLE IF EXISTS Pompier; -- Salla 
DROP TABLE IF EXISTS Equipe; -- Gab
DROP TABLE IF EXISTS Caserne; -- Meryem
DROP TABLE IF EXISTS Equipement; -- Mica
DROP TABLE IF EXISTS Incident; -- Salla
DROP TABLE IF EXISTS Secteur; -- Gab

--Creation des tables

CREATE TABLE Secteur (
	idSecteur INT IDENTITY PRIMARY KEY,
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
	idEquipement int PRIMARY KEY IDENTITY(1,1),
	typeEquipement nvarchar (50),
	statutDisponibilite nvarchar (25) NOT NULL,
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
INSERT INTO Secteur (codePostal, nomSecteur, limitesGeographique) VALUES 
('J5A 1A1','Centre-Ville', 'Rue A à Rue C'), 
('J5A 1A2','Quartier Nord', 'Rue D à Rue F'), 
('J5A 1A3','Quartier Sud', 'Rue G à Rue I'),
('J5A 1A4','Quartier Est', 'Rue J à Rue L'),
('J5A 1A5','Quartier Ouest', 'Rue M à Rue O'),
('J5A 1A6','Vieux Quartier', 'Rue P à Rue R'),
('J5A 1A7','Quartier Industriel', 'Rue S à Rue U'),
('J5A 1A8','Rosemont', 'Rue V à Rue Z');
select * from Secteur;

