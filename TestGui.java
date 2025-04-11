import java.awt.FlowLayout;
import java.awt.event.*;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

public class TestGui extends JFrame {
	
    private JTextField tfIDEmploye, tfNom, tfPrenom, tfposte, tfnumTel, tfIdEquipe;
    private JButton btnInserer, btnSupprimer, btnModifier, btnChercher;

    // Pour les secteurs
    private JButton btnAfficherSecteurs;
    private JTextArea taSecteurs;

    // Pour les secteurs
    private JButton btnAfficherPompierParEquipe;
    private JTextArea taPompierParEquipe;
    private JTextField tfIdEquipePompier;


    private static final long serialVersionUID = -4939544011287453046L;
    private DbConnection dbConnection;
    
    
    public TestGui() {
    	
    	super("Gestion Caserne Pompier");
    	setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setSize( 600, 400 );
        this.setLocationRelativeTo( null );
        this.setResizable(false);
        

        // Le panel principal empile tout verticalement
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        this.setContentPane(panelPrincipal);  
        
        // Panel pour les pompiers 
        JPanel panelPompier = new JPanel(new FlowLayout());
        panelPompier.setLayout( new FlowLayout() );

        // Panel pour les pompiers par quipe
        JPanel panelPompierParEquipe = new JPanel(new FlowLayout(FlowLayout.CENTER));

        // Le panel des secteurs
        JPanel panelSecteurs = new JPanel(new FlowLayout(FlowLayout.CENTER));
        


        
        // Création des composants
        tfIDEmploye = new JTextField(10);
        tfNom = new JTextField(10);
        tfPrenom = new JTextField(10);
        tfposte = new JTextField(10);
        tfnumTel = new JTextField(12);
        tfIdEquipe = new JTextField(10);
        btnInserer = new JButton("Insérer");
        btnSupprimer = new JButton("Supprimer");
        btnModifier = new JButton("Modifier");
        btnChercher = new JButton("Chercher");

        // Pour les pompier par equipe
        btnAfficherPompierParEquipe = new JButton("Afficher Pompiers par équipe");
        tfIdEquipePompier = new JTextField(10);
        taPompierParEquipe = new JTextArea(10, 50);
        taPompierParEquipe.setEditable(false);
        JScrollPane scrollPompierParEquipe = new JScrollPane(taPompierParEquipe);


        // Pour les secteurs
        btnAfficherSecteurs = new JButton("Afficher Secteurs");
        taSecteurs = new JTextArea(10, 50);
        taSecteurs.setEditable(false);
        JScrollPane scrollSecteurs = new JScrollPane(taSecteurs);

        
        // Ajout des composants à la fenêtre
        panelPompier.add(new JLabel("ID de l'employé"));
        panelPompier.add(tfIDEmploye);
        panelPompier.add(new JLabel("Nom"));
        panelPompier.add(tfNom);
        panelPompier.add(new JLabel("Prénom"));
        panelPompier.add(tfPrenom);
        panelPompier.add(new JLabel("Poste"));
        panelPompier.add(tfposte);
        panelPompier.add(new JLabel("numTel"));
        panelPompier.add(tfnumTel);
        panelPompier.add(new JLabel("ID de l'équipe"));
        panelPompier.add(tfIdEquipe);
        panelPompier.add(btnInserer);
        panelPompier.add(btnSupprimer);
        panelPompier.add(btnModifier);
        panelPompier.add(btnChercher);

        //Pour les pompiers par equipe
        panelPompierParEquipe.add(new JLabel("ID de l'équipe"));
        panelPompierParEquipe.add(tfIdEquipePompier);
        panelPompierParEquipe.add(scrollPompierParEquipe);
        panelPompierParEquipe.add(btnAfficherPompierParEquipe);
        panelPompier.add(panelPompierParEquipe);


        //Pour les secteurs
        panelSecteurs.add(btnAfficherSecteurs);
        panelSecteurs.add(scrollSecteurs);
        panelPompier.add(panelSecteurs);



        // Ajout des panels au panel principal
        panelPrincipal.add(panelPompier);
        panelPrincipal.add(panelPompierParEquipe);
        panelPrincipal.add(panelSecteurs);

        
        // Gestion des événements
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                FermerApplication();
            }
        });
        
        btnInserer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                insererAction();
            }
        });
        
        btnSupprimer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                supprimerAction();
            }
        });
        
        btnModifier.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // ModifierAction();
            }
        });
        
        btnChercher.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // chercherAction();
            }
        });

        //Afficher les secteurs
        btnAfficherSecteurs.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                try {
                    String result = GestionCaserne.afficherSecteurs();
                    taSecteurs.setText(result);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Erreur lors de l'affichage des secteurs : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        //Afficher les pompiers par equipe
        btnAfficherPompierParEquipe.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                try {
                    int equipeID = Integer.parseInt(tfIdEquipePompier.getText());
                    String result = GestionCaserne.afficherPompierDansEquipe(equipeID);
                    taPompierParEquipe.setText(result);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Erreur lors de l'affichage des secteurs : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    	
    	try {
        	//commecer par créer une connexion à la BD 
        	dbConnection = DbConnection.getInstance();
        	System.out.println("Connexion à la BD ouverte avec Succés");
    	}
    	catch (SQLException e) { //une gestion simple des erreurs de la BD
	        System.err.println("Erreur de BD: " + e.getMessage());
	        e.printStackTrace();
    	}
    }
    
    private void insererAction() {
        try {
            int idEmploye = Integer.parseInt(tfIDEmploye.getText());
            String nom = tfNom.getText();
            String prenom = tfPrenom.getText();
            String poste = tfposte.getText();
            String numTel = tfnumTel.getText();
            int idEquipe = Integer.parseInt(tfIdEquipe.getText());
            if(tfIDEmploye.getText().trim().isEmpty() || nom.equals("") || prenom.equals("") || poste.equals("") || numTel.equals("") || tfIdEquipe.getText().trim().isEmpty()) {
           	 JOptionPane.showMessageDialog(this, "Vous devez remplir tous les champs pour insérer un pompier","Erreur", JOptionPane.ERROR_MESSAGE);
           }
           else {
	            GestionCaserne.InsererPompier(idEmploye ,nom, prenom, poste, numTel, idEquipe);
	            JOptionPane.showMessageDialog(this, "Employé inséré avec succès");
           }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur d'insertion : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
}
    
    private void supprimerAction() {
        try {
            int idEmploye = Integer.parseInt(tfIDEmploye.getText());
        	GestionCaserne.supprimerPompier(idEmploye);;
            JOptionPane.showMessageDialog(this, "Employé supprimé avec succès");
            
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur de suppression : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
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
    
    public static void main(String[] args) throws Exception {
        // Apply a look'n feel
        UIManager.setLookAndFeel( new NimbusLookAndFeel() );
        
        // Start my window
        TestGui myWindow = new TestGui();
        myWindow.setVisible( true );
    }
}
