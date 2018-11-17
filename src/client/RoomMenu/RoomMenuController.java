package client.RoomMenu;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import server.models.Room;


import java.net.URL;
import java.util.ResourceBundle;

public class RoomMenuController implements Initializable {


    @FXML
    private TableView<Room> roomTable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        TableColumn<Room, String>  nameColumn = new TableColumn<>("Room Name");
        //nameColumn.setMinWidth(200);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Room, String>  modeColumn = new TableColumn<>("Game Mode");
        //modeColumn.setMinWidth(200);
        modeColumn.setCellValueFactory(new PropertyValueFactory<>("gamemode"));

        TableColumn<Room, String>  ownerColumn = new TableColumn<>("Owner");
        //ownerColumn.setMinWidth(200);
        ownerColumn.setCellValueFactory(new PropertyValueFactory<>("ownerid"));

        TableColumn<Room, String>  playerNoColumn = new TableColumn<>("Players");
        //playerNoColumn.setMinWidth(200);
        playerNoColumn.setCellValueFactory(new PropertyValueFactory<>("players"));

        TableColumn<Room, String>  levelColumn = new TableColumn<>("Entrance Level");
        //levelColumn.setMinWidth(200);
        levelColumn.setCellValueFactory(new PropertyValueFactory<>("entranceLevel"));

        roomTable.setItems(getRooms());
        roomTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        roomTable.getColumns().addAll(nameColumn, modeColumn, ownerColumn,playerNoColumn,levelColumn);

    }

    public ObservableList<Room> getRooms(){
        ObservableList<Room> rooms = FXCollections.observableArrayList();
        rooms.add(new Room("A", 0, 12345, 6, 5 ));
        rooms.add(new Room("B", 1, 15454, 3, 4 ));
        rooms.add(new Room("C", 2, 12342, 2, 3 ));
        rooms.add(new Room("D", 3, 12343, 7, 4 ));
        rooms.add(new Room("E", 0, 15435, 1, 7 ));
        return rooms;
    }

    public void createRoom(ActionEvent actionEvent) {
    }

    public void enterPrivateRoom(ActionEvent actionEvent) {
    }
}
