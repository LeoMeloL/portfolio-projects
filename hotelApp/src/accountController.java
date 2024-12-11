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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


public class accountController {

    String textoDigitado;
    String url = "jdbc:postgresql://localhost:5432/PBD";
    String user = "postgres";
    String password = "123456";
    String nome;
    String cpf;
    String senha;
    int id;
    String email;
    String userType;
    int avaliation;

    @FXML
    private Label nameLabel;

    @FXML
    private Label inicioLabel;

    @FXML
    private Label spentLabel;

    @FXML
    private Label adminLabel;


    @FXML
    private Label reservasLabel;

    @FXML
    private Label reservasQtdLabel;

    @FXML
    private TextField emailTextField;

    @FXML
    private Label emailLabel;

    @FXML
    private Label cpfLabel;

    @FXML
    private Label senhaLabel;

    @FXML
    private Label checkoutLabel;

    @FXML
    private Label faturaLabel;

        @FXML
    private ImageView fiveStar;

    @FXML
    private ImageView fourStar;

    @FXML
    private ImageView oneStar;

    @FXML
    private Button sendButton;

    @FXML
    private TextField textField;

    @FXML
    private ImageView threeStar;

    @FXML
    private ImageView twoStar;

    @FXML
    void fiveStarAction(MouseEvent event) {
        avaliation = 5;
        DropShadow dropShadow = new DropShadow();
        fiveStar.setEffect(dropShadow);

    }

    @FXML
    void fiveStarExit(MouseEvent event) {
        fiveStar.setEffect(null);

    }

    @FXML
    void fiveStarPop(MouseEvent event) {
        DropShadow dropShadow = new DropShadow();
        fiveStar.setEffect(dropShadow);

    }

    @FXML
    void fourStarAction(MouseEvent event) {
        DropShadow dropShadow = new DropShadow();
        fourStar.setEffect(dropShadow);
        avaliation = 4;

    }

    @FXML
    void fourStarExit(MouseEvent event) {
        fourStar.setEffect(null);

    }

    @FXML
    void fourStarPop(MouseEvent event) {
        DropShadow dropShadow = new DropShadow();
        fourStar.setEffect(dropShadow);

    }

    @FXML
    void oneStarAction(MouseEvent event) {
        avaliation = 1;
        DropShadow dropShadow = new DropShadow();
        oneStar.setEffect(dropShadow);


    }

    @FXML
    void oneStarExit(MouseEvent event) {
        oneStar.setEffect(null);

    }

    @FXML
    void oneStarPop(MouseEvent event) {
        DropShadow dropShadow = new DropShadow();
        oneStar.setEffect(dropShadow);

    }

    @FXML
    void sendButtonAction(ActionEvent event) {
        String text = textField.getText();
        inserirComentarioAvaliacao(id,text, avaliation);

    }

    @FXML
    void threeStarAction(MouseEvent event) {
        DropShadow dropShadow = new DropShadow();
        threeStar.setEffect(dropShadow);
        avaliation = 3;

    }

    @FXML
    void threeStarExit(MouseEvent event) {
        threeStar.setEffect(null);

    }

    @FXML
    void threeStarPop(MouseEvent event) {
        DropShadow dropShadow = new DropShadow();
        threeStar.setEffect(dropShadow);

    }

    @FXML
    void twoStarAction(MouseEvent event) {
        DropShadow dropShadow = new DropShadow();
        twoStar.setEffect(dropShadow);
        avaliation = 2;

    }

    @FXML
    void twoStarExit(MouseEvent event) {
        twoStar.setEffect(null);
    }

    @FXML
    void twoStarPop(MouseEvent event) {
        DropShadow dropShadow = new DropShadow();
        twoStar.setEffect(dropShadow);

    }

    @FXML
    void adminAction(MouseEvent event) {
        openAdminScreen();

    }

    @FXML
    void adminExitAction(MouseEvent event) {
        adminLabel.setEffect(null);

    }

    @FXML
    void adminPopAction(MouseEvent event) {
        DropShadow dropShadow = new DropShadow();
        adminLabel.setEffect(dropShadow);

    }

    @FXML
    void inicioAction(MouseEvent event) {
        openMainScreen();

    }

    @FXML
    void inicioExitAction(MouseEvent event) {
        inicioLabel.setEffect(null);

    }

    @FXML
    void inicioPopAction(MouseEvent event) {
        DropShadow dropShadow = new DropShadow();
        inicioLabel.setEffect(dropShadow);

    }

    @FXML
    void checkoutAction(MouseEvent event) {
        String text = spentLabel.getText();

        if (!text.equals("0")) {
            warning("Erro", "Voce deve pagar sua fatura primeiro!!!");
            return;
        }
        String sql = "DELETE FROM reserva WHERE id_usuario = ?";
    try (Connection connection = DriverManager.getConnection(url, user, password);
         PreparedStatement statement = connection.prepareStatement(sql)) {
        statement.setInt(1, id);
        int linhasAfetadas = statement.executeUpdate();
        System.out.println(linhasAfetadas + " reserva(s) do usuário " + id + " foi(foram) apagada(s).");
    } catch (SQLException e) {
        e.printStackTrace();
    }

    reservasQtdLabel.setText("0");
    spentLabel.setText("0");

    warning("SUCESSO!!!", "Seu checkout foi realizado com sucesso!!!");
    }

    @FXML
    void checkoutExitAction(MouseEvent event) {
        checkoutLabel.setEffect(null);

    }

    @FXML
    void checkoutPopAction(MouseEvent event) {
        DropShadow dropShadow = new DropShadow();
        checkoutLabel.setEffect(dropShadow);
    }

    @FXML
    void faturaAction(MouseEvent event) {
        String sql = "DELETE FROM reserva WHERE id_usuario = ?";
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            int linhasAfetadas = statement.executeUpdate();
            System.out.println(linhasAfetadas + " reserva(s) do usuário " + id + " foi(foram) apagada(s).");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        reservasQtdLabel.setText("0");
        spentLabel.setText("0");
        warning("SUCESSO!!", "Sua fatura foi paga com sucesso!!!");
        

    }

    @FXML
    void faturaExitAction(MouseEvent event) {
        faturaLabel.setEffect(null);

    }

    @FXML
    void faturaPopAction(MouseEvent event) {
        DropShadow dropShadow = new DropShadow();
        faturaLabel.setEffect(dropShadow);
        

    }

    @FXML
    void emailEnterAction(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {

            email = emailTextField.getText();
            obterDadosUsuario();
            calcularGastoTotalDoUsuario(id);
        }
    }

    public int obterDadosUsuario() {
        String sqlUsuarioComum = "SELECT * FROM usuario WHERE email = ?";
        String sqlAdministrador = "SELECT * FROM administrador WHERE email = ?";
        Connection connection;
    
        try {
            connection = DriverManager.getConnection(url, user, password);
            PreparedStatement statement = connection.prepareStatement(sqlUsuarioComum);
            statement.setString(1, email);
            ResultSet result = statement.executeQuery();
    
            if (result.next()) {
                // O usuário foi encontrado na tabela de usuários comuns
                nome = result.getString("nome");
                senha = result.getString("senha_hash");
                cpf = result.getString("cpf");
                id = result.getInt("id");
                userType = "Usuário comum";
            } else {
                // O usuário não foi encontrado na tabela de usuários comuns, então verifica na tabela de administradores
                statement = connection.prepareStatement(sqlAdministrador);
                statement.setString(1, email);
                result = statement.executeQuery();
    
                if (result.next()) {
                    // O usuário foi encontrado na tabela de administradores
                    nome = result.getString("nome");
                    senha = result.getString("senha_hash");
                    id = result.getInt("id");
                    userType = "Administrador";
                    adminLabel.setVisible(true);

                } else {
                    // O usuário não foi encontrado em nenhuma das tabelas
                    System.out.println("Usuário não encontrado.");
                    return 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        nameLabel.setText(nome);
        cpfLabel.setText(cpf);
        senhaLabel.setText(senha);
        emailLabel.setText(email);
    
        return 1;
    }
    

    public double calcularGastoTotalDoUsuario(int idUsuario) {
        double gastoTotal = 0.0;
        int quantidadeReservas = 0;
        String sql = "SELECT COUNT(*) AS total_reservas, SUM(preco) AS total_gasto FROM reserva WHERE id_usuario = ?";
        Connection connection = null;
    
        try {
            connection = DriverManager.getConnection(url, user, password);
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, idUsuario);
            ResultSet result = statement.executeQuery();
    
            if (result.next()) {
                quantidadeReservas = result.getInt("total_reservas");
                gastoTotal = result.getDouble("total_gasto");
                System.out.println("Quantidade de reservas: " + quantidadeReservas);
                System.out.println("Gasto total do usuário: " + gastoTotal);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        
        reservasQtdLabel.setText(Integer.toString(quantidadeReservas)); // Atualiza a label com o gasto total
        spentLabel.setText("R$" + Double.toString(gastoTotal));
        return quantidadeReservas;
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

        Stage stage = (Stage) inicioLabel.getScene().getWindow();
        stage.close();

        }

        private void openAdminScreen(){
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("adminLayout.fxml"));
                Parent root = fxmlLoader.load();
                Scene tela = new Scene(root);
    
                Stage thirdStage = new Stage();
                thirdStage.setTitle("Admin Screen");
                thirdStage.setScene(tela);
                thirdStage.show();
            }catch (Exception e) {
                System.out.println("Erro ao abrir a Admin screen");
            }
    
            Stage stage = (Stage) inicioLabel.getScene().getWindow();
            stage.close();
    
            }

    private void warning(String titulo, String mensagem) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    public void inserirComentarioAvaliacao(int idUsuario, String comentario, int pontuacao) {
        String sql = "INSERT INTO comentarioAvaliacao (id_usuario, texto_comentario, pontuacao) VALUES (?, ?, ?)";
        
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            
            preparedStatement.setInt(1, idUsuario);
            preparedStatement.setString(2, comentario);
            preparedStatement.setInt(3, pontuacao);
            
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Comentário e pontuação inseridos com sucesso!");
            } else {
                System.out.println("Erro ao inserir comentário e pontuação.");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
}

