package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;

//--module-path "C:\Users\Maciej\Downloads\openjfx-12.0.1_windows-x64_bin-sdk\javafx-sdk-12.0.1" --add-modules javafx.controls,javafx.fxml,javafx.base,javafx.graphics,javafx.media,javafx.swing


public class Main extends Application {
    //nowy kod xD
    Stage window;
    @Override
    public void start(Stage primaryStage) throws Exception{
        window = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        window.setTitle("Hello World");
        window.setScene(new Scene(root, 1000, 500));
        // Controller controller = new Controller();
        // controller.getMain(this);
        window.setOnCloseRequest(e->{
            e.consume();//dzięki temu jeśli byśmy zrobili okeinko w stylu "czy na pewno chcssz wyjść" to będzie działać poprawnie
            closeProgram();
        });
        window.show();

    }

    /*    @Override
        public void stop() {
            System.out.println("Zakonczono");
            try {
                Controller.conn.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }*/
    private void closeProgram(){
        try {
            if(Controller.conn != null){
                if(!Controller.conn.isClosed()){
                    Controller.conn.close();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        window.close();
    }
    public static void main(String[] args) {
        launch(args);
        /*OknoDodawaniaTabelki oknoDodawaniaTabelki = new OknoDodawaniaTabelki();
        oknoDodawaniaTabelki.display();*/
    }
}
