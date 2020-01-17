package ru.timelimit.client.game.UI;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import ru.timelimit.client.game.GameClient;

import java.util.HashMap;


public class MenuUI extends UI {
    @Override
    public void init() {
        var table = new Table();

        btnMap = new HashMap<String, UIElement>();

        var cameraWidth = GameClient.instance.sceneManager.currentScene.getCamera().viewportWidth;
        var cameraHeight = GameClient.instance.sceneManager.currentScene.getCamera().viewportHeight;

    /*    var title = new Label((float)(cameraWidth * 0.02),
                cameraHeight - 50,
                100,10, "KILL YOUR FRIENDS");*/
        //title.setBackground(new Sprite(TextureManager.getTexture("BtnEmpty")));
        var createLobbyBtn = new Button((float)(cameraWidth * 0.07), (float)(cameraHeight * 0.8),
                (float)(cameraWidth * 0.3), (float)(cameraHeight * 0.1),  () -> { });

       // title.background = null;

       createLobbyBtn.background = null;

        var createLobbyLbl = new Label((float)(cameraWidth * 0.02) + (float)(cameraWidth * 0.4) / 2,
                (float)(cameraHeight * 0.8) + (float)(cameraHeight * 0.1) / 2,
                (float)(cameraWidth * 0.3), (float)(cameraHeight * 0.1), "Create Lobby");

        var exitBtn = new Button((float)(cameraWidth * 0.8), (float)(cameraHeight * 0.8),
                (float)(cameraWidth * 0.1), (float)(cameraHeight * 0.1),  () -> { });

        exitBtn.setBackground(new Sprite(Objects.requireNonNull(TextureManager.getTexture("ExitBtn"))));

        btnMap.put("CLobbyLbl", createLobbyLbl);
        btnMap.put("createLobbyBtn", createLobbyBtn);
        btnMap.put("ExitBtn",exitBtn);
       // btnMap.put("MainTitle", title);

       //var exitBtn = new Button("Exit", btnBackground);

    }
}
