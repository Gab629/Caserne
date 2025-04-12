import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

public class TestGui extends JFrame {

    // Champs pour les informations d’un pompier
    private JTextField tfIDEmploye, tfNom, tfPrenom, tfposte, tfnumTel, tfIdEquipe;
    private JButton btnInsererPompier, btnSupprimerPompier;

    // Secteurs
    private JButton btnAfficherSecteurs;
    private JTextArea taSecteurs;

    // Affichage des pompiers par équipe
    private JButton btnAfficherPompierParEquipe;
    private JTextArea taPompierParEquipe;
    private JTextField tfIdEquipePompier;

    // Incidents
    private JTextField tfIDSecteur;
    private JButton btnButtonCalculerNbIncident;

    // Équipements expirés
    private JButton btnAfficherEquipementsExpirés;
    private JTextArea taEquipements;

    // Incidents avec renfort, équipements et véhicules d’équipe
    private JButton btnIncidentsAvecRenfort, btnEquipementsEquipe, btnVehiculesEquipe;
    private JTextArea taRapports, taEquipementsEquipe, taVehiculesEquipe;
    private JTextField tfEquipeEquipement, tfEquipeVehicule;

    private static final long serialVersionUID = -4939544011287453046L;
    private DbConnection dbConnection;

    // Style
    private final Color rougeBordeaux = new Color(153, 0, 0);
    private final Color grisClair = new Color(245, 245, 245);
    private final Font fontTitre = new Font("Arial", Font.BOLD, 14);
    private final Font fontTexte = new Font("Arial", Font.PLAIN, 12);

    public TestGui() {
        super("\uD83D\uDEA8 Gestion de la caserne de pompiers");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(900, 900);
        setLocationRelativeTo(null);
        setResizable(true);

        // Panel principal avec scroll vertical
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(panelPrincipal, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setPreferredSize(new Dimension(880, 750));
        setContentPane(scrollPane);

        Border bordureRouge = BorderFactory.createLineBorder(rougeBordeaux, 2);

        // === Panel Pompiers ===
        JPanel panelPompier = new JPanel(new GridLayout(4, 4, 10, 10));
        panelPompier.setBorder(BorderFactory.createTitledBorder(bordureRouge, "Gestion des pompiers", TitledBorder.LEFT, TitledBorder.TOP, fontTitre, rougeBordeaux));
        panelPompier.setBackground(grisClair);
        tfIDEmploye = new JTextField(10); tfNom = new JTextField(10); tfPrenom = new JTextField(10); tfposte = new JTextField(10);
        tfnumTel = new JTextField(12); tfIdEquipe = new JTextField(10);
        btnInsererPompier = new JButton("+ Insérer"); styliserBouton(btnInsererPompier);
        btnSupprimerPompier = new JButton("Supprimer"); styliserBouton(btnSupprimerPompier);
        panelPompier.add(new JLabel("ID Employé:")); panelPompier.add(tfIDEmploye);
        panelPompier.add(new JLabel("Nom:")); panelPompier.add(tfNom);
        panelPompier.add(new JLabel("Prénom:")); panelPompier.add(tfPrenom);
        panelPompier.add(new JLabel("Poste:")); panelPompier.add(tfposte);
        panelPompier.add(new JLabel("Numéro Tel:")); panelPompier.add(tfnumTel);
        panelPompier.add(new JLabel("ID Équipe:")); panelPompier.add(tfIdEquipe);
        panelPompier.add(btnInsererPompier); panelPompier.add(btnSupprimerPompier);

        // === Panel Pompiers par équipe ===
        JPanel panelPompierParEquipe = new JPanel(new BorderLayout());
        panelPompierParEquipe.setBorder(BorderFactory.createTitledBorder(bordureRouge, "Pompiers par Équipe", TitledBorder.LEFT, TitledBorder.TOP, fontTitre, rougeBordeaux));
        panelPompierParEquipe.setBackground(Color.WHITE);
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.setBackground(Color.DARK_GRAY);
        top.setBorder(new EmptyBorder(10, 10, 10, 10));
        tfIdEquipePompier = new JTextField(10);
        btnAfficherPompierParEquipe = new JButton("Afficher"); styliserBouton(btnAfficherPompierParEquipe);
        JLabel lbl = new JLabel("ID Équipe:"); lbl.setForeground(Color.WHITE);
        top.add(lbl); top.add(tfIdEquipePompier); top.add(btnAfficherPompierParEquipe);
        taPompierParEquipe = new JTextArea(8, 50); taPompierParEquipe.setFont(fontTexte); taPompierParEquipe.setEditable(false);
        panelPompierParEquipe.add(top, BorderLayout.NORTH);
        panelPompierParEquipe.add(new JScrollPane(taPompierParEquipe), BorderLayout.CENTER);

        // === Panel Secteurs ===
        JPanel panelSecteurs = new JPanel(new BorderLayout());
        panelSecteurs.setBorder(BorderFactory.createTitledBorder(bordureRouge, "Affichage des Secteurs", TitledBorder.LEFT, TitledBorder.TOP, fontTitre, rougeBordeaux));
        panelSecteurs.setBackground(Color.WHITE);
        btnAfficherSecteurs = new JButton("Afficher Secteurs"); styliserBouton(btnAfficherSecteurs);
        taSecteurs = new JTextArea(5, 50); taSecteurs.setFont(fontTexte); taSecteurs.setEditable(false);
        panelSecteurs.add(btnAfficherSecteurs, BorderLayout.NORTH);
        panelSecteurs.add(new JScrollPane(taSecteurs), BorderLayout.CENTER);

        // === Panel Incidents ===
        JPanel panelIncidents = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelIncidents.setBorder(BorderFactory.createTitledBorder(bordureRouge, "Incidents", TitledBorder.LEFT, TitledBorder.TOP, fontTitre, rougeBordeaux));
        panelIncidents.setBackground(grisClair);
        tfIDSecteur = new JTextField(10);
        btnButtonCalculerNbIncident = new JButton("Calculer"); styliserBouton(btnButtonCalculerNbIncident);
        panelIncidents.add(new JLabel("ID Secteur:"));
        panelIncidents.add(tfIDSecteur);
        panelIncidents.add(btnButtonCalculerNbIncident);

        // === Panel Équipements expirés ===
        JPanel panelEquipements = new JPanel(new BorderLayout());
        panelEquipements.setBorder(BorderFactory.createTitledBorder(bordureRouge, "Équipements Expirés", TitledBorder.LEFT, TitledBorder.TOP, fontTitre, rougeBordeaux));
        panelEquipements.setBackground(Color.WHITE);
        btnAfficherEquipementsExpirés = new JButton("Afficher"); styliserBouton(btnAfficherEquipementsExpirés);
        taEquipements = new JTextArea(5, 50); taEquipements.setFont(fontTexte); taEquipements.setEditable(false);
        panelEquipements.add(btnAfficherEquipementsExpirés, BorderLayout.NORTH);
        panelEquipements.add(new JScrollPane(taEquipements), BorderLayout.CENTER);

        // === Panel Incidents avec renfort ===
        JPanel panelRapports = new JPanel(new BorderLayout());
        panelRapports.setBorder(BorderFactory.createTitledBorder(bordureRouge, "Incidents avec caserne de renfort", TitledBorder.LEFT, TitledBorder.TOP, fontTitre, rougeBordeaux));
        panelRapports.setBackground(grisClair);
        btnIncidentsAvecRenfort = new JButton("Afficher les incidents avec renfort"); styliserBouton(btnIncidentsAvecRenfort);
        taRapports = new JTextArea(5, 50); taRapports.setEditable(false); taRapports.setFont(fontTexte);
        panelRapports.add(btnIncidentsAvecRenfort, BorderLayout.NORTH);
        panelRapports.add(new JScrollPane(taRapports), BorderLayout.CENTER);

        // === Panel Équipements d'une équipe ===
        JPanel panelEquipementsEquipe = new JPanel(new BorderLayout());
        panelEquipementsEquipe.setBorder(BorderFactory.createTitledBorder(bordureRouge, "Équipements d'une équipe", TitledBorder.LEFT, TitledBorder.TOP, fontTitre, rougeBordeaux));
        panelEquipementsEquipe.setBackground(Color.WHITE);
        JPanel topEquip = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topEquip.setBackground(Color.DARK_GRAY);
        topEquip.setBorder(new EmptyBorder(10, 10, 10, 10));
        tfEquipeEquipement = new JTextField(5);
        btnEquipementsEquipe = new JButton("Afficher équipements"); styliserBouton(btnEquipementsEquipe);
        JLabel lblEquip = new JLabel("ID Équipe:"); lblEquip.setForeground(Color.WHITE);
        topEquip.add(lblEquip); topEquip.add(tfEquipeEquipement); topEquip.add(btnEquipementsEquipe);
        taEquipementsEquipe = new JTextArea(5, 50); taEquipementsEquipe.setEditable(false); taEquipementsEquipe.setFont(fontTexte);
        panelEquipementsEquipe.add(topEquip, BorderLayout.NORTH);
        panelEquipementsEquipe.add(new JScrollPane(taEquipementsEquipe), BorderLayout.CENTER);

        // === Panel Véhicules d'une équipe ===
        JPanel panelVehiculesEquipe = new JPanel(new BorderLayout());
        panelVehiculesEquipe.setBorder(BorderFactory.createTitledBorder(bordureRouge, "Véhicules d'une équipe", TitledBorder.LEFT, TitledBorder.TOP, fontTitre, rougeBordeaux));
        panelVehiculesEquipe.setBackground(Color.WHITE);
        JPanel topVeh = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topVeh.setBackground(Color.DARK_GRAY);
        topVeh.setBorder(new EmptyBorder(10, 10, 10, 10));
        tfEquipeVehicule = new JTextField(5);
        btnVehiculesEquipe = new JButton("Afficher véhicules"); styliserBouton(btnVehiculesEquipe);
        JLabel lblVeh = new JLabel("ID Équipe:"); lblVeh.setForeground(Color.WHITE);
        topVeh.add(lblVeh); topVeh.add(tfEquipeVehicule); topVeh.add(btnVehiculesEquipe);
        taVehiculesEquipe = new JTextArea(5, 50); taVehiculesEquipe.setEditable(false); taVehiculesEquipe.setFont(fontTexte);
        panelVehiculesEquipe.add(topVeh, BorderLayout.NORTH);
        panelVehiculesEquipe.add(new JScrollPane(taVehiculesEquipe), BorderLayout.CENTER);

        // Ajout des panels au panel principal
        panelPrincipal.add(panelPompier);
        panelPrincipal.add(panelPompierParEquipe);
        panelPrincipal.add(panelSecteurs);
        panelPrincipal.add(panelIncidents);
        panelPrincipal.add(panelEquipements);
        panelPrincipal.add(panelRapports);
        panelPrincipal.add(panelEquipementsEquipe);
        panelPrincipal.add(panelVehiculesEquipe);

        // Fermeture sécurisée de la fenêtre
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                FermerApplication();
            }
        });

        // Actions des boutons
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

        btnIncidentsAvecRenfort.addActionListener(e -> {
            try {
                taRapports.setText(GestionCaserne.incidentsAvecRenfort());
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage());
            }
        });

        btnEquipementsEquipe.addActionListener(e -> {
            try {
                int id = Integer.parseInt(tfEquipeEquipement.getText());
                taEquipementsEquipe.setText(GestionCaserne.equipementsParEquipe(id));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage());
            }
        });

        btnVehiculesEquipe.addActionListener(e -> {
            try {
                int id = Integer.parseInt(tfEquipeVehicule.getText());
                taVehiculesEquipe.setText(GestionCaserne.vehiculesParEquipe(id));
            } catch (Exception ex) {
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

    // Style visuel pour tous les boutons
    private void styliserBouton(JButton bouton) {
        bouton.setBackground(rougeBordeaux);
        bouton.setForeground(Color.WHITE);
        bouton.setFocusPainted(false);
        bouton.setFont(new Font("Arial", Font.BOLD, 12));
        bouton.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
    }

    // Fermeture de l'application avec déconnexion propre
    private void FermerApplication() {
        try {
            dbConnection = DbConnection.getInstance();
            System.out.println("Connexion à la BD fermée avec Succés");
        } catch (SQLException e) {
            System.err.println("Erreur de BD: " + e.getMessage());
            e.printStackTrace();
        }
        dispose();
    }

    // Action pour insérer un pompier
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

    // Action pour supprimer un pompier
    private void supprimerPompierAction() {
        try {
            int id = Integer.parseInt(tfIDEmploye.getText());
            GestionCaserne.supprimerPompier(id);
            JOptionPane.showMessageDialog(this, "Employé supprimé avec succès");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage());
        }
    }

    // Affichage des secteurs disponibles
    private void AfficherSecteurs() {
        try {
            taSecteurs.setText(GestionCaserne.afficherSecteurs());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage());
        }
    }

    // Affichage des pompiers selon une équipe
    private void AfficherPompierParEquipe() {
        try {
            int id = Integer.parseInt(tfIdEquipePompier.getText());
            taPompierParEquipe.setText(GestionCaserne.afficherPompierDansEquipe(id));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage());
        }
    }

    // Calcul du nombre d'incidents pour un secteur
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
        new TestGui();
    }
}
