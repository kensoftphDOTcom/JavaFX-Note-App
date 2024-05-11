package com.kensoftph.javafxnotes;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class NotesController implements Initializable {

    private Db db = new Db();

    @FXML
    private ListView<Note> listView;

    @FXML
    private TextArea textArea;

    @FXML
    private void listViewClicked(MouseEvent event) {
        if(listView.getSelectionModel().getSelectedItem() != null) {
            textArea.setText(listView.getSelectionModel().getSelectedItem().getContent());
        }
    }

    @FXML
    void btnClear(MouseEvent event) {
        if (listView.getSelectionModel().getSelectedItem() != null) {
            listView.getSelectionModel().clearSelection();
            textArea.clear();
        }
    }

    @FXML
    void btnDelete(MouseEvent event) {
        if (listView.getSelectionModel().getSelectedItem() != null) {
            db.deleteNote(listView.getSelectionModel().getSelectedItem().getId());
            refreshList();
        }
    }

    @FXML
    void btnSave(MouseEvent event) {

        String fiveWords = textArea.getText();
        String[] words = fiveWords.split("\\s");
        List<String> wordTitle = new ArrayList<>();

        for (int i = 0; i < words.length && i < 5; i++) {
            wordTitle.add(words[i]);
        }

        String title = String.join(" ", wordTitle);



        if(listView.getSelectionModel().getSelectedItem() != null && !textArea.getText().trim().isEmpty() ){
            db.updateNote(listView.getSelectionModel().getSelectedItem().getId(), title, textArea.getText());
            refreshList();
            return;
        }

        if(!textArea.getText().trim().isEmpty()){
            db.insertNote(title, textArea.getText());
            refreshList();
        }

    }

    private void refreshList() {
        List<Note> note = db.readNotes();
        listView.getItems().clear();
        textArea.clear();
        listView.getItems().addAll(note);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        refreshList();
    }
}