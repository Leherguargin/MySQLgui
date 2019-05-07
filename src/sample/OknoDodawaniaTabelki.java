package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class OknoDodawaniaTabelki {
    private static TableView table = new TableView();
    static PreparedStatement ps;


    public static String display() { //co z tym static???
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Dodaj tabelke...");
        window.setMinWidth(150);
        window.setMinHeight(200);

        //top
        TextField nazwaTabelki = new TextField("nazwa tabelki");

        //center
        TextField nazwaKolumny = new TextField("nazwa kolumny");
        TextField typKolumny  = new TextField("typ kolumny");
        TextField ograniczenia1  = new TextField("ograniczenia1");
        TextField ograniczenia2  = new TextField("ograniczenia2");

        HBox hBoxTabelkaIInne = new HBox();
        hBoxTabelkaIInne.getChildren().addAll(nazwaKolumny, typKolumny, ograniczenia1, ograniczenia2);

        table.setEditable(true);

        TableColumn nazwaCol = new TableColumn("nazwa");
        TableColumn typCol = new TableColumn("typ");
        TableColumn ograniczenia1Col = new TableColumn("ograniczenia1");
        TableColumn ograniczenia2Col = new TableColumn("ograniczenia2");

        table.getColumns().addAll(nazwaCol, typCol, ograniczenia1Col, ograniczenia2Col);

        final ObservableList<Kolumna> data = FXCollections.observableArrayList(
                //new Kolumna("tabelka", "int", "not null", "auto_increment")
        );
        nazwaCol.setCellValueFactory(
                new PropertyValueFactory<Kolumna,String>("nazwa")
        );
        typCol.setCellValueFactory(
                new PropertyValueFactory<Kolumna,String>("typ")
        );
        ograniczenia1Col.setCellValueFactory(
                new PropertyValueFactory<Kolumna,String>("ograniczenia1")
        );
        ograniczenia2Col.setCellValueFactory(
                new PropertyValueFactory<Kolumna,String>("ograniczenia2")
        );

        VBox glownyVboxOkna = new VBox();
        glownyVboxOkna.getChildren().addAll(hBoxTabelkaIInne, table);

//        ChoiceBox<String> notNuLL = new ChoiceBox<>();
//        ObservableList<String> ts = FXCollections.observableArrayList();
//        ts.addAll("NULL", "not NULL");//todo zamien to na zaznaczanie czy tak czy nie, oraz dodaj typy w tabelce do wyboeru
//        notNuLL.setItems(ts);//todo ograniczenia jako hbox chekboxów i tam wybierasz jakie mają być .



        //right
        Button dodajKolumne = new Button("Dodaj kolumne");
        dodajKolumne.setOnAction(e -> {//todo dodac sprawdzanie czy nazwa jest poprawna, przerowbic reszte na pola wyboru
            String nK = nazwaKolumny.getText();
            String tK = typKolumny.getText();
            String og1 = ograniczenia1.getText();
            String og2 = ograniczenia2.getText();
            data.addAll(new Kolumna(nK, tK, og1, og2));
            table.setItems(data);
        });

        Button usunKolumne = new Button("Usuń kolumne");
        usunKolumne.setOnAction(e -> {
            System.out.println();
            for(int i=0;i<data.size();i++){
                if(data.get(i) == table.getSelectionModel().getSelectedItem()){
                    data.remove(i);
                    break;
                }
            }
            table.refresh();
        });

        Button anuluj = new Button("Anuluj");
        anuluj.setOnAction(e -> {

            window.close();
        });

        Button utworzTabele = new Button("Utwórz tabelę");
        utworzTabele.setOnAction(e -> {
            String zapytankoTworzaceTabelke = "CREATE TABLE " + nazwaTabelki.getText() + " (\n";
                //"id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY," +
                //"imie VARCHAR(30) NOT NULL," +
                //"nazwisko VARCHAR(30) NOT NULL," +
                //"kraj VARCHAR(30)," +
                //"zarobki INTEGER" +
                //" )";
            for(Kolumna it:data){
                zapytankoTworzaceTabelke += it.getNazwa() + " " + it.getTyp() + " " + it.getOgraniczenia1() + " " + it.getOgraniczenia2() + ",\n";
            }
            zapytankoTworzaceTabelke = zapytankoTworzaceTabelke.substring(0, zapytankoTworzaceTabelke.length()-2);
            zapytankoTworzaceTabelke += "\n);";
            System.out.println(zapytankoTworzaceTabelke);

            if(Controller.conn != null){
                try {
                    ps = Controller.conn.prepareStatement(zapytankoTworzaceTabelke);
                    ps.executeUpdate();
                    System.out.println("Dodano tabelke.");
                } catch (SQLException ef) {
                    ef.printStackTrace();
                }
            }else{
                System.out.println("Nie połączono do bazy danych!"); //todo wyświetl bledy użytkownikowi lub wygaś przyciski jeśli nie połączono.
            }
        });

        VBox vBoxPrzyciskow = new VBox();
        vBoxPrzyciskow.getChildren().addAll(dodajKolumne, usunKolumne, utworzTabele, anuluj);

        //panel glowny
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(nazwaTabelki);
        borderPane.setCenter(glownyVboxOkna);
        borderPane.setRight(vBoxPrzyciskow);

        Scene scene = new Scene(borderPane);
        window.setScene(scene);
        window.showAndWait();
        return "dodawanie zakończone :)";
    }

    public static class Kolumna {
        private final SimpleStringProperty nazwa;
        private final SimpleStringProperty typ;
        private final SimpleStringProperty ograniczenia1;
        private final SimpleStringProperty ograniczenia2;

        public Kolumna(String nazwa, String typ, String ograniczenia1, String ograniczenia2) {
            this.nazwa = new SimpleStringProperty(nazwa);
            this.typ = new SimpleStringProperty(typ);
            this.ograniczenia1 = new SimpleStringProperty(ograniczenia1);
            this.ograniczenia2 = new SimpleStringProperty(ograniczenia2);
        }

        public String getNazwa() {
            return nazwa.get();
        }

        public void setNazwa(String fName) {
            nazwa.set(fName);
        }

        public String getTyp() {
            return typ.get();
        }

        public void setTyp(String fName) {
            typ.set(fName);
        }

        public String getOgraniczenia1() {
            return ograniczenia1.get();
        }

        public void setOgraniczenia1(String fName) {
            ograniczenia1.set(fName);
        }

        public String getOgraniczenia2() {
            return ograniczenia2.get();
        }

        public void setOgraniczenia2(String fName) {
            ograniczenia2.set(fName);
        }
    }
}

