import java.awt.FlowLayout;
import java.awt.event.*;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

public class TestGui extends JFrame {
	
    private JTextField tfIDEmploye, tfNom, tfPrenom, tfposte, tfnumTel, tfIdEquipe;
    private JButton btnInserer, btnSupprimer, btnModifier, btnChercher;
    private static final long serialVersionUID = -4939544011287453046L;
    private DbConnection dbConnection;
    
    
    public TestGui() {
    	
    	super("Gestion Caserne Pompier");
    	setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        //this.setSize( 600, 400 );
        this.setLocationRelativeTo( null );
        this.setResizable(false);
        
        JPanel contentPane = (JPanel) this.getContentPane();
        contentPane.setLayout( new FlowLayout() );

        
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
        
        
        
        // Ajout des composants à la fenêtre
        contentPane.add(new JLabel("ID de l'employé"));
        contentPane.add(tfIDEmploye);
        contentPane.add(new JLabel("Nom"));
        contentPane.add(tfNom);
        contentPane.add(new JLabel("Prénom"));
        contentPane.add(tfPrenom);
        contentPane.add(new JLabel("Poste"));
        contentPane.add(tfposte);
        contentPane.add(new JLabel("numTel"));
        contentPane.add(tfnumTel);
        contentPane.add(new JLabel("ID de l'équipe"));
        contentPane.add(tfIdEquipe);
        contentPane.add(btnInserer);
        contentPane.add(btnSupprimer);
        contentPane.add(btnModifier);
        contentPane.add(btnChercher);
        
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
    
//     private void ModifierAction() {
//    	 try {
//             int id = Integer.parseInt(tfId.getText());
//             String nom = tfNom.getText();
//             String prenom = tfPrenom.getText();
//             if(nom.equals("") || prenom.equals("") ) {
//             	 JOptionPane.showMessageDialog(this, "Vous devez remplir tous les champs pour modifier un employé","Erreur", JOptionPane.ERROR_MESSAGE);
//             }
//             else {
//             	EmployeDAO.modifierEmploye(id, nom, prenom);
//             }
//         } catch (SQLException ex) {
//             ex.printStackTrace();
//             JOptionPane.showMessageDialog(this, "Erreur de recherche : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
//         }
//    }
    
//     private void chercherAction() {
//     	 try {
//              int id = Integer.parseInt(tfId.getText());
//              JOptionPane.showMessageDialog(this, "Vous devez remplir le champ id pour chercher un employé","Erreur", JOptionPane.ERROR_MESSAGE);
//              EmployeDAO.AfficheEmployeGui(id, tfNom, tfPrenom);
//          } catch (SQLException ex) {
//              ex.printStackTrace();
//              JOptionPane.showMessageDialog(this, "Erreur de recherche : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
//          }
//     }
    
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
