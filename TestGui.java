import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;


public class TestGui extends JFrame {

    // === Champs texte pour les pompiers ===
    private JTextField tfIDEmploye, tfNom, tfPrenom, tfposte, tfnumTel, tfIdEquipe;
    private JButton btnInsererPompier, btnSupprimerPompier;

    // === Secteurs ===
    private JButton btnAfficherSecteurs;
    private JTextArea taSecteurs;

    // === Pompiers par équipe ===
    private JButton btnAfficherPompierParEquipe;
    private JTextArea taPompierParEquipe;
    private JTextField tfIdEquipePompier;

    // === Incidents ===
    private JTextField tfIDSecteur;
    private JButton btnButtonCalculerNbIncident;

    // === Nouveauté : Affichage des équipements expirés ===
    private JButton btnAfficherEquipementsExpirés;
    private JTextArea taEquipements;

    // === Gestion de la base de données ===
    private static final long serialVersionUID = -4939544011287453046L;
    private DbConnection dbConnection;

    // === Couleurs et polices pour une interface améliorée ===
    private final Color rougeBordeaux = new Color(128, 0, 32);
    private final Color grisClair = new Color(245, 245, 245);
    private final Font fontTitre = new Font("Arial", Font.BOLD, 14);
    private final Font fontTexte = new Font("Arial", Font.PLAIN, 12);

    public TestGui() {
        super("🚒 Gestion de la caserne de pompiers");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(900, 900);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setBackground(Color.WHITE);
        setContentPane(panelPrincipal);

        Border bordureRouge = BorderFactory.createLineBorder(rougeBordeaux, 2);
        EmptyBorder margeInterne = new EmptyBorder(10, 10, 10, 10);

        // === Panel pour gestion des pompiers ===
        JPanel panelPompier = new JPanel(new GridLayout(4, 4, 10, 10));
        panelPompier.setBorder(BorderFactory.createTitledBorder(bordureRouge, "Gestion des pompiers", TitledBorder.LEFT, TitledBorder.TOP, fontTitre, rougeBordeaux));
        panelPompier.setBackground(grisClair);

        tfIDEmploye = new JTextField(10); tfNom = new JTextField(10); tfPrenom = new JTextField(10); tfposte = new JTextField(10);
        tfnumTel = new JTextField(12); tfIdEquipe = new JTextField(10);

        btnInsererPompier = new JButton("+ Insérer");
        btnSupprimerPompier = new JButton("Supprimer");
        styliserBouton(btnInsererPompier);
        styliserBouton(btnSupprimerPompier);

        panelPompier.add(new JLabel("ID Employé:")); panelPompier.add(tfIDEmploye);
        panelPompier.add(new JLabel("Nom:")); panelPompier.add(tfNom);
        panelPompier.add(new JLabel("Prénom:")); panelPompier.add(tfPrenom);
        panelPompier.add(new JLabel("Poste:")); panelPompier.add(tfposte);
        panelPompier.add(new JLabel("Numéro Tel:")); panelPompier.add(tfnumTel);
        panelPompier.add(new JLabel("ID Équipe:")); panelPompier.add(tfIdEquipe);
        panelPompier.add(btnInsererPompier); panelPompier.add(btnSupprimerPompier);

        // === Panel Pompiers par Équipe ===
        JPanel panelPompierParEquipe = new JPanel(new BorderLayout());
        panelPompierParEquipe.setBorder(BorderFactory.createTitledBorder(bordureRouge, "Pompiers par Équipe", TitledBorder.LEFT, TitledBorder.TOP, fontTitre, rougeBordeaux));
        panelPompierParEquipe.setBackground(Color.WHITE);

        JPanel ligneRechercheEquipe = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ligneRechercheEquipe.setBackground(Color.DARK_GRAY);
        ligneRechercheEquipe.setBorder(margeInterne);
        JLabel labelEquipe = new JLabel("ID Équipe:");
        labelEquipe.setForeground(Color.WHITE);
        tfIdEquipePompier = new JTextField(10);
        btnAfficherPompierParEquipe = new JButton("Afficher");
        styliserBouton(btnAfficherPompierParEquipe);
        ligneRechercheEquipe.add(labelEquipe);
        ligneRechercheEquipe.add(tfIdEquipePompier);
        ligneRechercheEquipe.add(btnAfficherPompierParEquipe);

        taPompierParEquipe = new JTextArea(8, 50);
        taPompierParEquipe.setFont(fontTexte); taPompierParEquipe.setEditable(false);
        JScrollPane scrollPompierParEquipe = new JScrollPane(taPompierParEquipe);
        panelPompierParEquipe.add(ligneRechercheEquipe, BorderLayout.NORTH);
        panelPompierParEquipe.add(scrollPompierParEquipe, BorderLayout.CENTER);

        // === Panel Secteurs ===
        JPanel panelSecteurs = new JPanel(new BorderLayout());
        panelSecteurs.setBorder(BorderFactory.createTitledBorder(bordureRouge, "Affichage des Secteurs", TitledBorder.LEFT, TitledBorder.TOP, fontTitre, rougeBordeaux));
        panelSecteurs.setBackground(Color.WHITE);

        btnAfficherSecteurs = new JButton("Afficher Secteurs");
        styliserBouton(btnAfficherSecteurs);
        taSecteurs = new JTextArea(5, 50);
        taSecteurs.setFont(fontTexte); taSecteurs.setEditable(false);
        JScrollPane scrollSecteurs = new JScrollPane(taSecteurs);
        panelSecteurs.add(btnAfficherSecteurs, BorderLayout.NORTH);
        panelSecteurs.add(scrollSecteurs, BorderLayout.CENTER);

        // === Panel Incidents ===
        JPanel panelIncidents = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelIncidents.setBorder(BorderFactory.createTitledBorder(bordureRouge, "Incidents", TitledBorder.LEFT, TitledBorder.TOP, fontTitre, rougeBordeaux));
        panelIncidents.setBackground(grisClair);

        tfIDSecteur = new JTextField(10);
        btnButtonCalculerNbIncident = new JButton("Calculer");
        styliserBouton(btnButtonCalculerNbIncident);
        panelIncidents.add(new JLabel("ID Secteur:"));
        panelIncidents.add(tfIDSecteur);
        panelIncidents.add(btnButtonCalculerNbIncident);

        // === Panel Équipements Expirés ===
        JPanel panelEquipements = new JPanel(new BorderLayout());
        panelEquipements.setBorder(BorderFactory.createTitledBorder(bordureRouge, "Équipements Expirés", TitledBorder.LEFT, TitledBorder.TOP, fontTitre, rougeBordeaux));
        panelEquipements.setBackground(Color.WHITE);

        btnAfficherEquipementsExpirés = new JButton("Afficher");
        styliserBouton(btnAfficherEquipementsExpirés);
        taEquipements = new JTextArea(5, 50);
        taEquipements.setFont(fontTexte); taEquipements.setEditable(false);
        JScrollPane scrollEquipements = new JScrollPane(taEquipements);
        panelEquipements.add(btnAfficherEquipementsExpirés, BorderLayout.NORTH);
        panelEquipements.add(scrollEquipements, BorderLayout.CENTER);

        // === Ajout des panels au panel principal ===
        panelPrincipal.add(panelPompier);
        panelPrincipal.add(panelPompierParEquipe);
        panelPrincipal.add(panelSecteurs);
        panelPrincipal.add(panelIncidents);
        panelPrincipal.add(panelEquipements);

        // === Événements ===
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                FermerApplication();
            }
        });

        btnInsererPompier.addActionListener(e -> insererPompierAction());
        btnSupprimerPompier.addActionListener(e -> supprimerPompierAction());
        btnAfficherSecteurs.addActionListener(e -> AfficherSecteurs());
        btnAfficherPompierParEquipe.addActionListener(e -> AfficherPompierParEquipe());
        btnButtonCalculerNbIncident.addActionListener(e -> calculerNombreIncident());
        btnAfficherEquipementsExpirés.addActionListener(e -> {
            try {
                taEquipements.setText(GestionCaserne.afficherEquipementsExpirés());
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage());
            }
        });

        try {
            dbConnection = DbConnection.getInstance();
            System.out.println("Connexion à la BD ouverte avec Succès");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Connexion échouée : " + e.getMessage());
        }

        pack();
        setVisible(true);
    }

    // === Style pour les boutons ===
    private void styliserBouton(JButton bouton) {
        bouton.setBackground(rougeBordeaux);
        bouton.setForeground(Color.WHITE);
        bouton.setFocusPainted(false);
        bouton.setFont(new Font("Arial", Font.BOLD, 12));
        bouton.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
    }

    private void FermerApplication() {
    	try {
        	//fermer la connexion à la BD
        	dbConnection = DbConnection.getInstance();
        	System.out.println("Connexion à la BD fermée avec Succés");
    	}
    	catch (SQLException e) { //une gestion simple des erreurs de la BD
	        System.err.println("Erreur de BD: " + e.getMessage());
	        e.printStackTrace();
    	}
    	//dispose pour le frame
    	dispose();
		
	}


    private void insererPompierAction() {
        try {
            int idEmploye = Integer.parseInt(tfIDEmploye.getText());
            String nom = tfNom.getText(), prenom = tfPrenom.getText(), poste = tfposte.getText(), numTel = tfnumTel.getText();
            int idEquipe = Integer.parseInt(tfIdEquipe.getText());
            if (nom.isEmpty() || prenom.isEmpty() || poste.isEmpty() || numTel.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tous les champs doivent être remplis", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            GestionCaserne.InsererPompier(idEmploye, nom, prenom, poste, numTel, idEquipe);
            JOptionPane.showMessageDialog(this, "Employé inséré avec succès");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage());
        }
    }

    private void supprimerPompierAction() {
        try {
            int id = Integer.parseInt(tfIDEmploye.getText());
            GestionCaserne.supprimerPompier(id);
            JOptionPane.showMessageDialog(this, "Employé supprimé avec succès");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage());
        }
    }

    private void AfficherSecteurs() {
        try {
            taSecteurs.setText(GestionCaserne.afficherSecteurs());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage());
        }
    }

    private void AfficherPompierParEquipe() {
        try {
            int id = Integer.parseInt(tfIdEquipePompier.getText());
            taPompierParEquipe.setText(GestionCaserne.afficherPompierDansEquipe(id));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage());
        }
    }

    private void calculerNombreIncident() {
        try {
            int idSecteur = Integer.parseInt(tfIDSecteur.getText());
            JOptionPane.showMessageDialog(this, GestionCaserne.afficherNbIncidentsDansSecteurs(idSecteur));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage());
        }
    }

    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(new NimbusLookAndFeel());
        new TestGui(); // pas besoin d’appeler setVisible ici déjà fait dans le constructeur
    }

}
