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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class standartController {

    String url = "jdbc:postgresql://localhost:5432/PBD";
    String user = "postgres";
    String password = "123456";

    @FXML
    private Label duoLabel;

    @FXML
    private Label fiveLabel;

    @FXML
    private Label singleLabel;

    @FXML
    private Label trioLabel;

    @FXML
    private Label inicioLabel;

    @FXML
    private TextField duoTextField;

    @FXML
    private TextField fiveTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField singleTextField;

    @FXML
    private TextField trioTextField;


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
    void duoExitAction(MouseEvent event) {
        duoLabel.setEffect(null);
    }

    @FXML
    void duoPopAction(MouseEvent event) {
        DropShadow dropShadow = new DropShadow();
        duoLabel.setEffect(dropShadow);


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

    @FXML
    void duoRoomAction(MouseEvent event) {
        if (emailTextField.getText().isEmpty()) {
            // O TextField está vazio
            warning("Erro", "Digite seu email na parte superior direita!!");
            return;
        }

        String textoNumero = singleTextField.getText();
        int qtd = Integer.parseInt(textoNumero);
        String email = emailTextField.getText();
        int idUsuario = obterDadosUsuario(email);
        //int idQuarto = obterIdQuarto();
        //int numeroQuarto = obterNumeroQuarto(idQuarto);
        double preco = 150;

        List<Integer> quartosCategoria1 = obterQuartosCategoria(1, 2);
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
    void fiveExitAction(MouseEvent event) {
        fiveLabel.setEffect(null);
    }

    @FXML
    void fivePopAction(MouseEvent event) {
        DropShadow dropShadow = new DropShadow();
        fiveLabel.setEffect(dropShadow);

    }

    @FXML
    void fiveRoomAction(MouseEvent event) {
        if (emailTextField.getText().isEmpty()) {
            // O TextField está vazio
            warning("Erro", "Digite seu email na parte superior direita!!");
            return;
        }

        	
        String textoNumero = singleTextField.getText();
        int qtd = Integer.parseInt(textoNumero);
        String email = emailTextField.getText();
        int idUsuario = obterDadosUsuario(email);
        //int idQuarto = obterIdQuarto();
        //int numeroQuarto = obterNumeroQuarto(idQuarto);
        double preco = 300;

        List<Integer> quartosCategoria1 = obterQuartosCategoria(1, 5);
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
    void singleExitAction(MouseEvent event) {
        singleLabel.setEffect(null);

    }

    @FXML
    void singlePopAction(MouseEvent event) {
        DropShadow dropShadow = new DropShadow();
        singleLabel.setEffect(dropShadow);

    }

    @FXML
    void singleRoomAction(MouseEvent event) {
        if (emailTextField.getText().isEmpty()) {
            // O TextField está vazio
            warning("Erro", "Digite seu email na parte superior direita!!");
            return;
        }

        String textoNumero = singleTextField.getText();
        int qtd = Integer.parseInt(textoNumero);
        String email = emailTextField.getText();
        int idUsuario = obterDadosUsuario(email);
        //int idQuarto = obterIdQuarto();
        //int numeroQuarto = obterNumeroQuarto(idQuarto);
        double preco = 100;

        List<Integer> quartosCategoria1 = obterQuartosCategoria(1, 1);
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
    void trioExitAction(MouseEvent event) {
        trioLabel.setEffect(null);

    }

    @FXML
    void trioPopAction(MouseEvent event) {
        DropShadow dropShadow = new DropShadow();
        trioLabel.setEffect(dropShadow);

    }

    @FXML
    void trioRoomAction(MouseEvent event) {
        if (emailTextField.getText().isEmpty()) {
            // O TextField está vazio
            warning("Erro", "Digite seu email na parte superior direita!!");
            return;
        }

        String textoNumero = singleTextField.getText();
        int qtd = Integer.parseInt(textoNumero);
        String email = emailTextField.getText();
        int idUsuario = obterDadosUsuario(email);
        //int idQuarto = obterIdQuarto();
        //int numeroQuarto = obterNumeroQuarto(idQuarto);
        double preco = 200;

        List<Integer> quartosCategoria1 = obterQuartosCategoria(1, 3);
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

private void warning(String titulo, String mensagem) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    

}


