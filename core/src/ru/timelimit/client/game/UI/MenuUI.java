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

        var title = new Label(cameraWidth / 2,
                cameraHeight - 50,
                100,10, "KILL YOUR FRIENDS");

        var createLobbyBtn = new Button((float)(cameraWidth * 0.15), (float)(cameraHeight * 0.4),
                (float)(cameraWidth * 0.3), (float)(cameraHeight * 0.3),  () -> { });

        //createLobbyBtn.setSprite(new Sprite(Objects.requireNonNull(TextureManager.getTexture("BtnEmpty"))));
      //  btnMap.keySet().add("createLobbyBtn");

       /* var CreateLobbyLbl = new Label((float)(cameraWidth * 0.15), (float)(cameraHeight * 0.4),
                (float)(cameraWidth * 0.3), (float)(cameraHeight * 0.3), "Create Lobby");*/


        var joinLobbyBtn = new Button((float)(cameraWidth * 0.15) + (float)(cameraWidth * 0.3) + (float)( cameraWidth * 0.1), (float)(cameraHeight * 0.4),
                (float)(cameraWidth * 0.3), (float)(cameraHeight * 0.3), () -> {});
        //joinLobbyBtn.setSprite(new Sprite(Objects.requireNonNull(TextureManager.getTexture("BtnEmpty"))));

       /* var joinLobbyLbl = new Label((float)(cameraWidth * 0.15) + (float)(cameraWidth * 0.3) + (float)( cameraWidth * 0.1), (float)(cameraHeight * 0.4),
                (float)(cameraWidth * 0.3), (float)(cameraHeight * 0.3), "Join Lobby");*/

        btnMap.put("createLobbyBtn", createLobbyBtn);
        //btnMap.put("CreateLobbyLbl", CreateLobbyLbl);
        btnMap.put("MainTitle", title);
        btnMap.put("joinLobbyBtn", joinLobbyBtn);
        //btnMap.put("joinLobbyLbl", joinLobbyLbl);

        //var settingsBtn = new Button("Settings", btnBackground);
       // var exitBtn = new Button("Exit", btnBackground);

    }
}
