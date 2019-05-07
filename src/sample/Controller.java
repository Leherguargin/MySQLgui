package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.awt.*;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class Controller implements Initializable {

    public TextField adresSerwera;
    public TextField nazwaBazy;
    public PasswordField hasloUzytkownika;
    public TextField loginUzytkownika;
    public Button zaloguj;
    public Button wyloguj;
    public Button dodajTabelke;
    public Button usunTabelke;
    public Button dodajKrotke;
    public Button usunKrotke;
    private ObservableList<Tabelka> listaTabelek = FXCollections.observableArrayList();
    public TableView tabelka;

    public static Connection conn = null;
    public TableView daneZtabelek;
    private ObservableList<Tabelka> daneZtabelekLista = FXCollections.observableArrayList();
    PreparedStatement ps;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dodajKrotke.setDisable(true);
        usunTabelke.setDisable(true);
        dodajTabelke.setDisable(true);
        usunKrotke.setDisable(true);

        tabelka.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                ObservableList<Tabelka> current = tabelka.getSelectionModel().getSelectedItems();
                /*try{
                    String string = "SELECT * FROM " + current.get(0).getNazwa() + ";";
                    System.out.println(string);
                    ResultSet daneZZaznaczonejTabelki = conn.prepareStatement(string).executeQuery();
                    printResult(conn.prepareStatement("describe"+current.get(0).getNazwa()+';').executeQuery());//todo to jest test xDkis
                    printResult(daneZZaznaczonejTabelki);
                    List daneZtabelekLista = zapisDoListy(daneZZaznaczonejTabelki);//arraylista z stringami
                    for(int i = 0; i < daneZtabelekLista.size(); i++){ //dodanie danych z zaznaczonej tabelki do listy observable
                        daneZtabelekLista.add(new Tabelka((String)daneZtabelekLista.get(i)));
                        //System.out.println(daneZtabelekLista.get(i).getNazwa());
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
                System.out.println(current.get(0).getNazwa());
                //i tutaj co chciałbym robić przy wyobrze elemntu(CHYBA)
                //i to w funkcji initizlize...

            }
        });
    }

    public void zaloguj(ActionEvent actionEvent) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://sql." + adresSerwera.getText() + '/' + nazwaBazy.getText();
            conn = DriverManager.getConnection(url, loginUzytkownika.getText(), hasloUzytkownika.getText() );
            ResultSet wszystkieTabelki = conn.prepareStatement("show tables;").executeQuery();
            List wszystkieTabelkiLista = zapisDoListy(wszystkieTabelki);//arraylista z stringami
            for(int i = 0; i < wszystkieTabelkiLista.size(); i++){ //dodanie wszystkich tabelek do listy observable
                listaTabelek.add(new Tabelka((String)wszystkieTabelkiLista.get(i)));
            }
            zaloguj.setDisable(true);
            wyloguj.setDisable(false);
            dodajTabelke.setDisable(false);
            usunTabelke.setDisable(false);
            dodajKrotke.setDisable(false);
            usunKrotke.setDisable(false);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Name column
        TableColumn<Tabelka, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setMinWidth(200);
        nameColumn.setCellValueFactory(new PropertyValueFactory<Tabelka,String>("nazwa"));

        //table = new TableView<>();
        tabelka.setItems(listaTabelek);

        tabelka.getColumns().addAll(nameColumn/*, priceColumn, quantityColumn*/);

    }

    public void dodajTabelke(ActionEvent actionEvent) {

        OknoDodawaniaTabelki oknoDodawaniaTabelki = new OknoDodawaniaTabelki();
        System.out.println(oknoDodawaniaTabelki.display());

        /*String nazwaTabelki = oknoDodawaniaTabelki.nazwaTabelkiLabel.getText();//todo zabezpiecz jesli nie wpisze w ktores pole
        String nazwaKolumny;
        String typKolumny;//todo mozliwosc wyboru typu, a nie wpisanie "z palca"
        String ograniczenia; //todo możliwość wyboru ogarniczeń
        String zapytankoTworzaceTabelke = "CREATE TABLE" + nazwaTabelki + "(" +
                "id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY," +
                "imie VARCHAR(30) NOT NULL," +
                "nazwisko VARCHAR(30) NOT NULL," +
                "kraj VARCHAR(30)," +
                "zarobki INTEGER" +
                ")";
        if(conn != null){
            try {
                ps = conn.prepareStatement(zapytankoTworzaceTabelke);
                ps.executeUpdate();
                System.out.println("Dodano tabelke.");
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }else{
            System.out.println("Nie połączono do bazy danych!"); //todo wyświetl bledy użytkownikowi lub wygaś przyciski jeśli nie połączono.
        }*/

    }

    public void usunTabelke(ActionEvent actionEvent) {
        if(tabelka.getSelectionModel().getSelectedItem() != null) {
            for(int i=0;i<listaTabelek.size();i++){
                if(listaTabelek.get(i) == tabelka.getSelectionModel().getSelectedItem()){
                    String zapytankoUsuwajace = "DROP TABLE " + listaTabelek.get(i).getNazwa() + ";";
                    try {
                        ps = conn.prepareStatement(zapytankoUsuwajace);
                        ps.executeUpdate();
                        System.out.println("usunieto tabelke: " + listaTabelek.get(i).getNazwa());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    System.out.println(zapytankoUsuwajace);
                    //listaTabelek.remove(i);
                    break;
                }
                listaTabelek.get(i);
            }

        }
    }

    public void dodajKrotke(ActionEvent actionEvent) {
        OknoDodawaniaKrotek oknoDodawaniaKrotek = new OknoDodawaniaKrotek();
        oknoDodawaniaKrotek.display();
    }

    public void usunKrotke(ActionEvent actionEvent) {

    }

    public static void printResult(ResultSet rs) throws Exception
    {
        while (rs.next()) {
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                if (i > 1) System.out.print(",  ");
                String columnValue = rs.getString(i);
                System.out.print(columnValue + " ");
            }
            System.out.println();
        }
    }

    public static List zapisDoListy(ResultSet rs) throws Exception
    {
        List<String> list = new ArrayList<String>();
        while (rs.next()) {
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                //if (i > 1) list.add(",  ");
                String columnValue = rs.getString(i);
                list.add(columnValue);
            }
        }
        return list;
    }

    public void wyloguj(ActionEvent actionEvent) {
        wylogujAction();
    }

    public void wylogujAction(){
        try {
            for(int i = 0; i < tabelka.getColumns().size(); i++){
                tabelka.getColumns().remove(i);
            }
            conn.close();
            wyloguj.setDisable(true);
            zaloguj.setDisable(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}


//todo po wylogowaniu i zalogowaniu ponownym mamy podwojnie wczyane nazwy tabl bazy do tabelki programu
//todo implementacja dodawania i usuwania krotek
//todo implememntacja prezentowania krotek w tabeli
//todo dodawanie tabelek ulepszyc wybieranie ogaraniczen
//todo inne mozliwosci baz danych mysql'a?
//todo generator zapytan do bazy? (a na pewno "wywoływacz" zapytań)