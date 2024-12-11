import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


public class LoginController {

    enum UserType {
        ADMINISTRADOR,
        USUARIO_COMUM,
        DESCONHECIDO
    }

    @FXML
    private Button enterButton;

    @FXML
    private Button createAccountButton;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField userField;

    @FXML
    void loginAction(ActionEvent event) throws SQLException{
        String user = userField.getText();
        String password = passwordField.getText();
    
        UserType userType = getUserType(user, password);
    
        if (userType == UserType.ADMINISTRADOR) {
            System.out.println("Login bem-sucedido como administrador");
            openMainScreen();
            ((Stage) enterButton.getScene().getWindow()).close();
        } else if (userType == UserType.USUARIO_COMUM) {
            System.out.println("Login bem-sucedido como usuário comum");
            ((Stage) enterButton.getScene().getWindow()).close();
            openMainScreen();
        } else {
            System.out.println("Usuário não encontrado ou credenciais inválidas");
            warning("Erro", "As credenciais estão incorretas.");
        }
    }

    UserType getUserType(String username, String password) throws SQLException {
        Connection conexao = null;
        PreparedStatement stmtUsuario = null;
        PreparedStatement stmtAdministrador = null;
        ResultSet rsUsuario = null;
        ResultSet rsAdministrador = null;
        UserType userType = UserType.DESCONHECIDO;
    
        try {
            Class.forName("org.postgresql.Driver");
            conexao = DriverManager.getConnection("jdbc:postgresql://localhost:5432/PBD", "postgres", "123456");
            System.out.println("Conexão bem-sucedida!");
    
            String sqlUsuario = "SELECT COUNT(*) FROM usuario WHERE nome = ? AND senha_hash = ?";
            stmtUsuario = conexao.prepareStatement(sqlUsuario);
            stmtUsuario.setString(1, username);
            stmtUsuario.setString(2, password);
            rsUsuario = stmtUsuario.executeQuery();
    
            if (rsUsuario.next()) {
                int count = rsUsuario.getInt(1);
                if (count > 0) {
                    userType = UserType.USUARIO_COMUM;
                }
            }
    
            String sqlAdministrador = "SELECT COUNT(*) FROM administrador WHERE nome = ? AND senha_hash = ?";
            stmtAdministrador = conexao.prepareStatement(sqlAdministrador);
            stmtAdministrador.setString(1, username);
            stmtAdministrador.setString(2, password);
            rsAdministrador = stmtAdministrador.executeQuery();
    
            if (rsAdministrador.next()) {
                int count = rsAdministrador.getInt(1);
                if (count > 0) {
                    userType = UserType.ADMINISTRADOR;
                }
            }
        } catch (ClassNotFoundException ex) {
            System.out.println("Erro: driver não encontrado");
        } finally {
            if (rsUsuario != null) {
                rsUsuario.close();
            }
            if (stmtUsuario != null) {
                stmtUsuario.close();
            }
            if (rsAdministrador != null) {
                rsAdministrador.close();
            }
            if (stmtAdministrador != null) {
                stmtAdministrador.close();
            }
            if (conexao != null) {
                conexao.close();
            }
        }
    
        return userType;
    }


    @FXML
    void createAccountAction(ActionEvent event) {
        openCreateAccountScreen();
    }

    private void openCreateAccountScreen() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("createAccountLayout.fxml"));
            Parent root = fxmlLoader.load();
            Scene tela = new Scene(root);

            Stage secondaryStage = new Stage();
            secondaryStage.setTitle("Create Account");
            secondaryStage.setScene(tela);
            secondaryStage.show();
        } catch (Exception e) {
            System.out.println("Erro ao abrir a create account screen");
        }
    }

    private void openMainScreen(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main.fxml"));
            Parent root = fxmlLoader.load();
            Scene tela = new Scene(root);

            Stage thirdStage = new Stage();
            thirdStage.setTitle("Main Screen");
            thirdStage.setScene(tela);
            thirdStage.show();
        }catch (Exception e) {
            System.out.println("Erro ao abrir a main screen");
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

