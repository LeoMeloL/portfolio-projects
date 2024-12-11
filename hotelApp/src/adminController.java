import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class adminController {
    static String url = "jdbc:postgresql://localhost:5432/PBD";
    static String user = "postgres";
    static String password = "123456";

    @FXML
    private Label removeLabel;

    @FXML
    private Label addLabel;

    @FXML
    private Label inicioLabel;

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
    void removeAction(MouseEvent event) {
        String texto = addTextField.getText();
        List<String> keywords = buscarPalavrasChave(texto);
        int categoria = -1;
        int capacidade = -1;

        
        for (String palavra : keywords) {
        switch(palavra) {
            case "standart":
            categoria = 1;
            break;

            case "vip":
            categoria = 2;
            break;

            case "suite":
            categoria = 3;

            case "1 pessoa":
            capacidade = 1;
            break;

            case "2 pessoas":
            capacidade = 2;
            break;

            case "3 pessoas":
            capacidade = 3;
            break;

            case "5 pessoas":
            capacidade = 5;
            break;
            }
        }

        if (categoria == -1 || capacidade == -1) {
            System.out.println("Parametros faltando");
        }
    
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String sql = "DELETE FROM quarto WHERE ID_categoria = ? AND capacidade = ? AND ocupado = false;";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, categoria);
                preparedStatement.setInt(2, capacidade);
                preparedStatement.executeUpdate();
                System.out.println("Quarto removido");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    @FXML
    void removeExitAction(MouseEvent event) {
        removeLabel.setEffect(null);
    }

    @FXML
    void removePopAction(MouseEvent event) {
        DropShadow dropShadow = new DropShadow();
        removeLabel.setEffect(dropShadow);

    }

    @FXML
    private TextField addTextField;

    @FXML
    void addAction(MouseEvent event) {
        String texto = addTextField.getText();
        List<String> keywords = buscarPalavrasChave(texto);
        int categoria = -1;
        int capacidade = -1;

        
        for (String palavra : keywords) {
        switch(palavra) {
            case "standart":
            categoria = 1;
            break;

            case "vip":
            categoria = 2;
            break;

            case "suite":
            categoria = 3;

            case "1 pessoa":
            capacidade = 1;
            break;

            case "2 pessoas":
            capacidade = 2;
            break;

            case "3 pessoas":
            capacidade = 3;
            break;

            case "5 pessoas":
            capacidade = 5;
            break;
            }
        }

        if (categoria == -1 || capacidade == -1) {
            System.out.println("Parametros faltando");
        }
    
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String sql = "INSERT INTO quarto (id_categoria, capacidade, ocupado) VALUES (?, ?, false)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, categoria);
                preparedStatement.setInt(2, capacidade);
                preparedStatement.executeUpdate();
                System.out.println("Quarto adicionado");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void addExitAction(MouseEvent event) {
        addLabel.setEffect(null);
    }

    @FXML
    void addPopAction(MouseEvent event) {
        DropShadow dropShadow = new DropShadow();
        addLabel.setEffect(dropShadow);

    }

    public static List<String> buscarPalavrasChave(String texto) {
        List<String> palavrasChaveEncontradas = new ArrayList<>();
    
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT words FROM keywords WHERE ? LIKE CONCAT('%', words, '%')";
            PreparedStatement statement = connection.prepareStatement(sql);
    
            // Para cada palavra-chave no banco de dados
            try (Statement keywordsStatement = connection.createStatement();
                 ResultSet keywordsResultSet = keywordsStatement.executeQuery("SELECT words FROM keywords")) {
                while (keywordsResultSet.next()) {
                    String palavraChave = keywordsResultSet.getString("words");
                    statement.setString(1, texto);
                    
                    // Se o texto contém a palavra-chave completa, adicione-a à lista
                    if (texto.contains(palavraChave)) {
                        palavrasChaveEncontradas.add(palavraChave);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return palavrasChaveEncontradas;
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

}
