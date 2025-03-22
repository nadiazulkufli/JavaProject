import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class Signup extends JDialog{
    private JTextField tfName;
    private JTextField tfEmail;
    private JButton btnSignUp;
    private JButton btnCancel;
    private JPanel SignUpPanel;
    private JPasswordField PASSWORDPasswordField;
    private JPasswordField CONFIRMPASSWORDPasswordField;


    public Signup(JFrame parent){
        super(parent);
        setTitle("Create a new account");
        setContentPane(SignUpPanel);
        setMinimumSize(new Dimension(450,474));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);


        btnSignUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                signupUser();
            }
        });
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        setVisible(true);
    }

    private void signupUser() {
        String name = tfName.getText();
        String email = tfEmail.getText();
        String password = String.valueOf(PASSWORDPasswordField.getPassword());
        String confirmPassword = String.valueOf(CONFIRMPASSWORDPasswordField.getPassword());

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() ){
            JOptionPane.showMessageDialog(this,
                    "Please enter all fields",
                    "Try Again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)){
            JOptionPane.showMessageDialog(this,
                    "Confirm Password does not match",
                    "Please Try Again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        user = addUsertoDatabase(name,email,password);
        if (user != null){
            dispose();
        }
        else {
            JOptionPane.showMessageDialog(this,
                    "Failed to register new user",
                    "try again",
                    JOptionPane.ERROR_MESSAGE);
        }

    }
    public User user;
    private User addUsertoDatabase(String name,String email, String password){
        User user = null;
        final String DB_URL = "jdbc:mysql://localhost/storedata";
        final String USERNAME = "root";
        final String PASSWORD = "";

        try{
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            //connect to database succesfully..

            Statement stmt = conn.createStatement();
            String sql = "INSERT INTO users (name, email,password)" +
                    "VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);

            int addedRows = preparedStatement.executeUpdate();
            if (addedRows > 0){
                user = new User();
                user.name = name;
                user.email = email;
                user.password = password;
            }

            stmt.close();
            conn.close();

        }catch(Exception e){
            e.printStackTrace();
        }
        return user;
    }

    public static void main (String[] args){
        Signup myform = new Signup(null);
        User user = myform.user;
        if (user != null){
            System.out.println("Sucessful Regisration of: " + user.name );
        }
        else {
            System.out.println("Regisration canceled");
        }
    }
}
