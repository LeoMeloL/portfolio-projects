import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class roomController {

    static String url = "jdbc:postgresql://localhost:5432/PBD";
    static String user = "postgres";
    static String password = "123456";

    @FXML
    private Button searchBarButton;

    @FXML
    private TextField searchBarField;

    @FXML
    private ImageView standartImage;

    @FXML
    private ImageView suiteImage;

    @FXML
    private ImageView vipImage;

    @FXML
    private Label inicioLabel;

    @FXML
    void searchBarAction(ActionEvent event) {
        String text = searchBarField.getText();
        System.out.println(text);
        List<String> palavrasChaveEncontradas = null;

        if (!text.isEmpty()) {
            palavrasChaveEncontradas = buscarPalavrasChave(text);
    
            for (String palavra : palavrasChaveEncontradas) {
                System.out.println("Palavra-chave encontrada: " + palavra);
            }
        } else {
            System.out.println("Nenhum texto foi digitado no campo de pesquisa.");
        }


        openRoomListScreen(palavrasChaveEncontradas);



    }

    @FXML
    void standartAction(MouseEvent event) {
        openStandartScreen();

    }

    

    @FXML
    void standartExitAction(MouseEvent event) {
        standartImage.setEffect(null);

    }

    @FXML
    void standartPopAction(MouseEvent event) {
        DropShadow dropShadow = new DropShadow();
        standartImage.setEffect(dropShadow);

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
    void suiteAction(MouseEvent event) {
        openVipScreen();
    }

    @FXML
    void suiteExitAction(MouseEvent event) {
        suiteImage.setEffect(null);

    }

    @FXML
    void suitePopAction(MouseEvent event) {
        DropShadow dropShadow = new DropShadow();
        suiteImage.setEffect(dropShadow);

    }

    @FXML
    void vipAction(MouseEvent event) {
        openVipScreen();

    }

    @FXML
    void vipExitAction(MouseEvent event) {
        vipImage.setEffect(null);

    }

    @FXML
    void vipPopAction(MouseEvent event) {
        DropShadow dropShadow = new DropShadow();
        vipImage.setEffect(dropShadow);

    }

    private void openStandartScreen(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("standartLayout.fxml"));
            Parent root = fxmlLoader.load();
            Scene tela = new Scene(root);

            Stage primaryStage = new Stage();
            primaryStage.setTitle("standart");
            primaryStage.setScene(tela);
            primaryStage.show();
        } catch (Exception e) {
            System.out.println("Erro ao abrir a standart screen");
        }

        Stage stage = (Stage) standartImage.getScene().getWindow();
        stage.close();
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

    private void openVipScreen(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("vipLayout.fxml"));
            Parent root = fxmlLoader.load();
            Scene tela = new Scene(root);

            Stage primaryStage = new Stage();
            primaryStage.setTitle("vip");
            primaryStage.setScene(tela);
            primaryStage.show();
        } catch (Exception e) {
            System.out.println("Erro ao abrir a vip screen");
        }

        Stage stage = (Stage) standartImage.getScene().getWindow();
        stage.close();
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

    private void openRoomListScreen(List<String> palavrasChaveEncontradas){

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("roomList.fxml"));
            Parent root = fxmlLoader.load();
            roomListController controller = fxmlLoader.getController();
            controller.initData(palavrasChaveEncontradas);

            Stage primaryStage = new Stage();
            primaryStage.setTitle("list");
            Scene tela = new Scene(root);
            primaryStage.setScene(tela);
            primaryStage.show();
        } catch (Exception e) {
            System.out.println("Erro ao abrir a list screen");
        }

        Stage stage = (Stage) standartImage.getScene().getWindow();
        stage.close();

    }
    
}


