package ru.timelimit.client.game.UI;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import ru.timelimit.client.game.GameClient;
import ru.timelimit.client.game.ResourceManager;

import java.util.ArrayList;
import java.util.HashMap;

public class MenuUI extends UI {
    public ArrayList<Integer> lobbyList = new ArrayList<>();
    int curLobby = 0;

    private Label lobbyChooserLabel;

    public enum State {
        JOINING, LOGIN, LOGOUT
    }

    private State status = State.LOGOUT;

    public void setState(State state) {
        switch (state) {
            case LOGOUT:
                hideElement("LobbyChooseTitle");
                hideElement("LobbyChooser");
                hideElement("prevLobby");
                hideElement("nextLobby");
                hideElement("createLobbyBtn");
                hideElement("mmrlabel");
                break;
            case LOGIN:
                showElement("LobbyChooseTitle");
                showElement("LobbyChooser");
                showElement("prevLobby");
                showElement("nextLobby");
                showElement("createLobbyBtn");
                showElement("mmrlabel");
                break;
            case JOINING:
                break;
            default:
                break;
        }
        status = state;
    }

    //private int startTimer = -1;

    public TextFieldWrapper activeField = null;
    private int MMR = -1;

    public void updateMMR(int mmr) {
        MMR = mmr;
        MMRLabel.setText("Score: " + mmr);
    }

    private Label MMRLabel;
    private TextFieldWrapper nicknameInput;
    private TextFieldWrapper passwordInput;

    public void updateLobbyList(ru.timelimit.network.Lobby[] lobbies) {
        lobbyList.clear();
        for (int i = 0; i < lobbies.length; i++) {
            lobbyList.add(lobbies[i].lobbyId);
        }
        if (curLobby >= lobbyList.size())
            curLobby = lobbyList.size() - 1;
        updateLobbyChooser();
    }

    private void updateLobbyChooser() {
        if (lobbyList.size() == 0) {
            lobbyChooserLabel.setText("No lobbies :(");
        } else {
            System.out.println("Lobby count: " + lobbyList.size());
            System.out.println("Cur lobby in chooser: " + lobbyList.get(curLobby));
            lobbyChooserLabel.setText("Lobby #" + lobbyList.get(curLobby) + ". Click to join!");
        }
    }

    private void lobbyChooserInit() {
        var cameraWidth = cameraInst.viewportWidth;
        var cameraHeight = cameraInst.viewportHeight;

        var lobbyChooserTitle = new Label(cameraWidth / 2, cameraHeight - 250, 0, 0, "Choose Game to Join");

        var lobbyChooserButton = new Button(cameraWidth / 2 - 150, cameraHeight - 320, 300, 40, () -> {
            if (lobbyList.size() != 0) {
                System.out.println("MenuScene: Connecting to lobby " + lobbyList.get(curLobby));
                GameClient.sendJoin(lobbyList.get(curLobby));
            } else {
                System.out.println("MenuScene: No lobbies to connect :(");
            }
        });
        lobbyChooserLabel = new Label(cameraWidth / 2, cameraHeight - 320 + 20, 0, 0, "No lobbies :(");
        lobbyChooserLabel.background = null;
        lobbyChooserButton.addChildren(lobbyChooserLabel);

        var prevLobby = new Button(cameraWidth / 2 - 200, cameraHeight - 320, 40, 40, () -> {
            if (curLobby > 0){
                curLobby--;
                updateLobbyChooser();
            }
        });
        prevLobby.setSprite(new Sprite(ResourceManager.getTexture("BtnUp")));
        prevLobby.getSprite().rotate90(false);

        var nextLobby = new Button(cameraWidth / 2 + 160, cameraHeight - 320, 40, 40, () -> {
            if (curLobby < lobbyList.size() - 1){
                curLobby++;
                updateLobbyChooser();
            }
        });
        nextLobby.setSprite(new Sprite(ResourceManager.getTexture("BtnUp")));
        nextLobby.getSprite().rotate90(true);

        btnMap.put("LobbyChooseTitle", lobbyChooserTitle);
        btnMap.put("LobbyChooser", lobbyChooserButton);
        btnMap.put("prevLobby", prevLobby);
        btnMap.put("nextLobby", nextLobby);
    }

    @Override
    public void init() {
        UI.currentUI = this;
        btnMap = new HashMap<>();
        btnSettings = new HashMap<>();

        var cameraWidth = cameraInst.viewportWidth;
        var cameraHeight = cameraInst.viewportHeight;

        errorLabel = new Label( 100, cameraHeight - 20, 0, 0, "");
        errorLabel.background = null;
        btnMap.put("errorLabel", errorLabel);

        MMRLabel = new Label(50, cameraHeight - 100, 0, 0, "Score: ");

        var title = new Label(cameraWidth  / 2, cameraHeight - 40,
                100,10, "KILL YOUR FRIENDS");
        title.setBackground(new Sprite(ResourceManager.getTexture("BtnEmpty")));
        var createLobbyBtn = new Button(cameraWidth / 2 - 75, cameraHeight - 220,
                150, 40,  () -> {
            if (status == State.JOINING) {
                GameClient.sendDisconnect();
                setState(State.LOGIN);
            } else if (status == State.LOGIN) {
                GameClient.sendCreateLobby();
            }

        });

        var createLobbyLbl = new Label(cameraWidth / 2,cameraHeight - 220 + 20, 0, 0, "Create Lobby");
        createLobbyLbl.background = null;
        createLobbyBtn.addChildren(createLobbyLbl);

        var btnExit = new Button(cameraWidth - 60, cameraHeight - 50,
                40, 40,  () -> System.exit(0));

        btnExit.setBackground(new Sprite(ResourceManager.getTexture("BtnExit")));

        var textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = ResourceManager.getFont("defaultFont");
        textFieldStyle.fontColor = new Color(1f, 1f, 1f, 1f);
        textFieldStyle.background = new SpriteDrawable(new Sprite(ResourceManager.getTexture("TextField")));

        nicknameInput = new TextFieldWrapper(new TextField("", textFieldStyle), "Your Nickname");
        nicknameInput.origin.setPosition(cameraWidth / 2 - 125, cameraHeight - 120);
        nicknameInput.origin.setWidth(250);
        nicknameInput.origin.setAlignment(Align.center);

        passwordInput = new TextFieldWrapper(new TextField("", textFieldStyle), "Password");
        passwordInput.origin.setPosition(cameraWidth / 2 - 125, cameraHeight - 170);
        passwordInput.origin.setWidth(250);
        passwordInput.origin.setAlignment(Align.center);

        var loginBtn = new Button(cameraWidth / 2f + 140, cameraHeight - 170, 40, 40, () -> {
            GameClient.sendConnect(nicknameInput.origin.getText(), passwordInput.origin.getText());
        });
        loginBtn.setSprite(new Sprite(ResourceManager.getTexture("BtnUp")));

        addElement("createLobbyBtn", createLobbyBtn, true);
        addElement("ExitBtn", btnExit, true);
        addElement("MainTitle", title, true);
        addElement("LoginBtn", loginBtn, true);
        addElement("nicknameInput", nicknameInput, true);
        addElement("passwordInput", passwordInput, true);
        addElement("mmrlabel", MMRLabel, true);

        lobbyChooserInit();

        if (GameClient.token != null) {
            System.out.println("SEND MMR");
            GameClient.sendMMR();
        } else {
            setState(State.LOGOUT);
        }
    }
}
