import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


public class roomListController{

    int categoria;
    int preco;
    int pessoas;

    String url = "jdbc:postgresql://localhost:5432/PBD";
    String user = "postgres";
    String password = "123456";

    @FXML
    private Label anotherLabel;

    @FXML
    private Label buyLabel;

    @FXML
    private TextField buyTextField;

    @FXML
    private Label inicioLabel;

    @FXML
    private Label precoLabel;

    @FXML
    private ImageView roomImage;

    @FXML
    private Label roomNameLabel;

    @FXML
    private TextField emailTextField;

    @FXML
    void anotherAction(MouseEvent event) {
        openRoomScreen();

    }

    @FXML
    void anotherExitAction(MouseEvent event) {
        anotherLabel.setEffect(null);

    }

    @FXML
    void anotherPopAction(MouseEvent event) {
        DropShadow dropShadow = new DropShadow();
        anotherLabel.setEffect(dropShadow);

    }

    @FXML
    void buyExitAction(MouseEvent event) {
        buyLabel.setEffect(null);
    }

    @FXML
    void buyPopAction(MouseEvent event) {
        DropShadow dropShadow = new DropShadow();
        buyLabel.setEffect(dropShadow);

    }

    @FXML
    void buyRoomAction(MouseEvent event) {
        String textoNumero = buyTextField.getText();
        int qtd = Integer.parseInt(textoNumero);
        String email = emailTextField.getText();
        int idUsuario = obterDadosUsuario(email);
        //int idQuarto = obterIdQuarto();
        //int numeroQuarto = obterNumeroQuarto(idQuarto)

        List<Integer> quartosCategoria1 = obterQuartosCategoria(categoria, pessoas);
        boolean quartosDisponiveis = false;

        for (int idQuarto : quartosCategoria1) {
            if (!quartoEstaOcupado(idQuarto)) {
                quartosDisponiveis = true;
                break;
            }
        }

        if (!quartosDisponiveis) {
            // Se não houver quartos disponíveis, exiba uma mensagem de erro
            System.out.println("Nao ha quartos disponiveis");
            return; // Saia do método, pois não há quartos disponíveis para reserva
        }

        for (int idQuarto : quartosCategoria1) {
            int numeroQuarto = obterNumeroQuarto(idQuarto);
            adicionarReserva(idUsuario, idQuarto, preco, numeroQuarto);
            setOcupado(idQuarto);
            qtd--;
            if (qtd == 0) {
                break; // Parar se a quantidade desejada de reservas for atingida
            }
        }


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

    private void openRoomScreen(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("roomLayout.fxml"));
            Parent root = fxmlLoader.load();
            Scene tela = new Scene(root);
    
            Stage thirdStage = new Stage();
            thirdStage.setTitle("room screen");
            thirdStage.setScene(tela);
            thirdStage.show();
        }catch (Exception e) {
            System.out.println("Erro ao abrir a room screen");
        }
    
        Stage stage = (Stage) buyLabel.getScene().getWindow();
        stage.close();
    
        }

    private boolean quartoEstaOcupado(int idQuarto) {
        boolean ocupado = false;

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT ocupado FROM Quarto WHERE id = ?";
            
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, idQuarto);
                ResultSet resultSet = preparedStatement.executeQuery();
                
                if (resultSet.next()) {
                    ocupado = resultSet.getBoolean("ocupado");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return ocupado;


    }

    public void setOcupado(int idQuarto){
        String sql = "UPDATE quarto SET ocupado = TRUE WHERE id = ?";
        Connection connection;

        try {
            connection = DriverManager.getConnection(url, user, password);
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, idQuarto);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public int obterNumeroQuarto(int idQuarto){

        int numeroQuarto = -1;
        String sql = "SELECT numero_quarto FROM quarto WHERE id = ?";
        Connection connection;

        try {
            connection = DriverManager.getConnection(url, user, password);
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, idQuarto);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                numeroQuarto = result.getInt("numero_quarto");
                return numeroQuarto;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return numeroQuarto;


    }

    public int obterIdQuarto(){
        int idQuarto = -1;
        String sql = "SELECT id FROM quarto WHERE ocupado = FALSE ORDER BY id LIMIT 1";
        Connection connection;

        try {
            connection = DriverManager.getConnection(url, user, password);
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                idQuarto = result.getInt("id");
                return idQuarto;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return idQuarto;

    }

    public int obterDadosUsuario(String email) {
        String sql = "SELECT * FROM usuario WHERE email = ?";
        Connection connection;

        

        try {
            connection = DriverManager.getConnection(url, user, password);
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, email);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                int id = result.getInt("id");
                String nome = result.getString("nome");
                System.out.println("ID: " + id);
                System.out.println("Nome: " + nome);

                return id;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 1;

    }

    private List<Integer> obterQuartosCategoria(int categoria, int pessoas) {
        List<Integer> quartosCategoria = new ArrayList<>();
    
        String sql = "SELECT id FROM Quarto WHERE id_categoria = ? AND capacidade >= ?";
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, categoria);
            preparedStatement.setInt(2, pessoas);
            ResultSet resultSet = preparedStatement.executeQuery();
    
            while (resultSet.next()) {
                int idQuarto = resultSet.getInt("id");
                quartosCategoria.add(idQuarto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return quartosCategoria;
    }
    

public void initData(List<String> palavrasChaveEncontradas) {

    System.out.println("Parâmetro recebido na tela History: " + palavrasChaveEncontradas);

    int qtd = 0;
    int categoria = 0;
    int preco = 0;
    String tipo = null;
    System.out.println(palavrasChaveEncontradas);
    
    if (palavrasChaveEncontradas != null) {
        for (String palavra : palavrasChaveEncontradas) {
            switch (palavra) {
                case "1 pessoa":
                case "solteiro":
                    qtd = 1;
                    categoria = 1;
                    tipo = "standart";
                    preco = 100;
                    System.out.println("chegou no standart");
                    break;
                case "2 pessoas":
                case "casal":
                    qtd = 2;
                    categoria = 1;
                    tipo = "standart";
                    preco = 150;
                    break;
                case "3 pessoas":
                    qtd = 3;
                    categoria = 1;
                    tipo = "standart";
                    preco = 200;
                    break;
                case "5 pessoas":
                case "familia":
                    qtd = 5;
                    categoria = 1;
                    tipo = "standart";
                    preco = 300;
                    break;
                case "vip":
                    System.out.println("chegou no vip");
                    categoria = 2;
                    preco = 500;
                    tipo = "vip";
                    qtd = 2;
                    break;
                case "suite":
                    categoria = 3;
                    preco = 700;
                    tipo = "suite";
                    qtd = 2;
                    break;
            }
        }
        
    if (tipo.equals("standart")){
    roomNameLabel.setText("Apartamento " + tipo + qtd + " pessoas");
    } 
    if (tipo.equals("vip") || tipo.equals("suite")) {
        roomNameLabel.setText("Apartamento " + tipo);
        Image novaImagem = new Image("file:./src/img/pexels-photo-164595.jpg");
        roomImage.setImage(novaImagem);
    }
    precoLabel.setText("R$" + preco);
    }

    this.categoria = categoria;
    this.pessoas = qtd;
    this.preco = preco;
}

private void adicionarReserva(int idUsuario, int idQuarto, double preco, int numeroQuarto) {

    try (Connection connection = DriverManager.getConnection(url, user, password)) {
        String sql = "INSERT INTO reserva (id_usuario, id_quarto, preco, numero_quarto) " +
                     "VALUES (?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idUsuario);
            preparedStatement.setInt(2, idQuarto);
            preparedStatement.setDouble(3, preco);
            preparedStatement.setInt(4, numeroQuarto);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Reserva adicionada com sucesso!");
            } else {
                System.out.println("Erro ao adicionar a reserva.");
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}



    

}
