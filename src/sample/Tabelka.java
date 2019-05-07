package sample;

import javafx.beans.property.SimpleStringProperty;

public class Tabelka {
    SimpleStringProperty nazwa;

    public Tabelka(String nazwa) {
        this.nazwa = new SimpleStringProperty(nazwa);
    }

    public String getNazwa() {
        return nazwa.get();
    }

    public SimpleStringProperty nazwaProperty() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa.set(nazwa);
    }
}