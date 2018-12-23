package client.Menus.RoomMenu;

import client.Menus.MenuController;
import client.QBitzApplication;
import client.GameModels.Room;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ResourceBundle;

public class RoomMenuController extends MenuController {


    @FXML
    private TableView<Room> roomTable;

    @FXML
    private TextField roomCodeText;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Platform.runLater(() -> {
            getRoomList();
            createTable();
            addButtonToTable();
        });
    }

    private void createTable(){
        TableColumn<Room, String>  nameColumn = new TableColumn<>("Room Name");
        //nameColumn.setMinWidth(200);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Room, String>  modeColumn = new TableColumn<>("Game Mode");
        //modeColumn.setMinWidth(200);
        modeColumn.setCellValueFactory(new PropertyValueFactory<>("gamemode"));

        TableColumn<Room, String>  ownerColumn = new TableColumn<>("Owner");
        //ownerColumn.setMinWidth(200);
        ownerColumn.setCellValueFactory(new PropertyValueFactory<>("ownerName"));

        TableColumn<Room, String>  playerNoColumn = new TableColumn<>("Players");
        //playerNoColumn.setMinWidth(200);
        playerNoColumn.setCellValueFactory(c-> new SimpleStringProperty(c.getValue().getPlayers() + " / " + c.getValue().getMaxPlayers()));

        TableColumn<Room, String>  levelColumn = new TableColumn<>("Entrance Level");
        //levelColumn.setMinWidth(200);
        levelColumn.setCellValueFactory(new PropertyValueFactory<>("entranceLevel"));

        //TableColumn joinColumn = new TableColumn("Join");
        //levelColumn.setMinWidth(200);

        roomTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        roomTable.getColumns().addAll(nameColumn, modeColumn, ownerColumn,playerNoColumn,levelColumn);
        //roomTable.setPlaceholder(new Label("No room available"));
    }

    public ObservableList<Room> populateRoomList(JSONArray roomList){
        roomTable.getItems().clear();
        ObservableList<Room> rooms = FXCollections.observableArrayList();
        for (Object roomObj : roomList) {
            JSONObject roomJSON = (JSONObject) roomObj;
            rooms.add(new Room(
                    roomJSON.getString("name"),
                    roomJSON.getInt("roomID"),
                    roomJSON.getString("ownerName"),
                    roomJSON.getInt("gameMode"),
                    roomJSON.getInt("players"),
                    roomJSON.getInt("maxPlayers"),
                    roomJSON.getInt("entranceLevel")
                    )
            );
        }
        roomTable.getItems().addAll(rooms);
        return rooms;
    }

    private void getRoomList(){
        JSONObject getRoomsJSON = new JSONObject();
        getRoomsJSON.put("requestType", "displayRooms");
        QBitzApplication.getSceneController().sendMessageToServer(getRoomsJSON);
    }

    public void createRoom(ActionEvent actionEvent) {
        QBitzApplication.getSceneController().gotoMenu("RoomCreateMenu");
    }

    public void enterPrivateRoom(ActionEvent actionEvent) {
            String roomCode = roomCodeText.getText();
            JSONObject roomJSON = new JSONObject();
            roomJSON.put("requestType", "joinPrivateRoom");
            roomJSON.put("roomCode", roomCode);
            QBitzApplication.getSceneController().sendMessageToServer(roomJSON);
    }

    private void addButtonToTable() {
        TableColumn<Room, Void> colBtn = new TableColumn("Join");
        Callback<TableColumn<Room, Void>, TableCell<Room, Void>> cellFactory = new Callback<TableColumn<Room, Void>, TableCell<Room, Void>>() {
            @Override
            public TableCell<Room, Void> call(final TableColumn<Room, Void> param) {
                final TableCell<Room, Void> cell = new TableCell<Room, Void>() {

                    private Button btn = new Button("Join");
                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Room room = getTableView().getItems().get(getIndex());
                            joinRoom(room.getId());
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };

        colBtn.setCellFactory(cellFactory);

        roomTable.getColumns().add(colBtn);

    }

    private void joinRoom(int id){
        JSONObject roomJSON = new JSONObject();
        roomJSON.put("requestType", "joinRoom");
        roomJSON.put("roomID", id);
        QBitzApplication.getSceneController().sendMessageToServer(roomJSON);
    }

    @Override
    public void onMessageReceived(String message) {
        JSONObject responseJSON = new JSONObject(message);
        if(responseJSON.getString("responseType").equals("displayRooms")){
            if (responseJSON.getBoolean("result")){
                Platform.runLater(() -> {
                    populateRoomList(responseJSON.getJSONArray("roomList"));
                });
            }
        }
        else if(responseJSON.getString("responseType").equals("joinRoom")){
            int resultCode = responseJSON.getInt("result");
            if (resultCode == 0){
                Platform.runLater(() -> {
                    responseJSON.put("roomCode", "");
                    QBitzApplication.getSceneController().gotoMenu("RoomLobbyMenu", responseJSON);
                });
            }
            else if(resultCode == 1){
                System.out.println("Room is full");
            }
            else if(resultCode == 2){
                System.out.println("Entrance level :(");
            }
            else if(resultCode == 3){
                System.out.println("Room does not exist :(");
            }
        }
    }
}