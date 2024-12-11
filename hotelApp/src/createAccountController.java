import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class createAccountController {

    @FXML
    private TextField confirmEmailField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private TextField confirmUserField;

    @FXML
    private TextField cpfField;

    @FXML
    private TextField confirmCpfField;

    @FXML
    private Button createAccountButton;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField userField;

    @FXML
    void createAccountAction(ActionEvent event) throws SQLException {
        String user = userField.getText();
        String confirmUser = confirmUserField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String email = emailField.getText();
        String confirmEmail = confirmEmailField.getText(); 
        String cpf = cpfField.getText();
        String confirmCpf = confirmCpfField.getText();

    
        if (user.equals(confirmUser) && password.equals(confirmPassword) && email.equals(confirmEmail) && cpf.equals(confirmCpf)) {
            Connection conexao = null;
            PreparedStatement stmt = null;
            try {
                Class.forName("org.postgresql.Driver"); 
                conexao = DriverManager.getConnection("jdbc:postgresql://localhost:5432/PBD", "postgres", "123456");
                System.out.println("Conexão bem-sucedida!");
                String sql = "INSERT INTO usuario (nome, email, senha_hash, cpf) VALUES (?, ?, ?, ?)";
                stmt = conexao.prepareStatement(sql);
                stmt.setString(1, user);
                stmt.setString(2, email);
                stmt.setString(3, password);
                stmt.setString(4, cpf);
                stmt.executeUpdate();
                System.out.println("Usuário inserido com sucesso!");
            } catch (ClassNotFoundException ex) {
                System.out.println("Erro: driver não encontrado");
            } finally {
                if (stmt != null) {
                    stmt.close();
                }
                if (conexao != null) {
                    conexao.close();
                }
            }
        } else {
            warning("Erro", "Os dados não condizem.");
        }
    }

    private void warning(String titulo, String mensagem) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    
}
