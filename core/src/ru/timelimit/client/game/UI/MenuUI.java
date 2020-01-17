package ru.timelimit.client.game.UI;

import com.badlogic.gdx.graphics.g2d.Sprite;
import ru.timelimit.client.game.GameClient;
import ru.timelimit.client.game.ResourceManager;

import java.util.HashMap;


public class MenuUI extends UI {
    @Override
    public void init() {
        btnMap = new HashMap<>();

        var cameraWidth = GameClient.instance.sceneManager.currentScene.getCamera().viewportWidth;
        var cameraHeight = GameClient.instance.sceneManager.currentScene.getCamera().viewportHeight;

        var title = new Label(cameraWidth  / 2, cameraHeight - 50,
                100,10, "KILL YOUR FRIENDS");
        title.setBackground(new Sprite(ResourceManager.getTexture("BtnEmpty")));
        var createLobbyBtn = new Button(cameraWidth / 2 - 75, cameraHeight - 150,
                150, 40,  () -> GameClient.instance.sceneManager.currentScene.setState(2));

        var createLobbyLbl = new Label(cameraWidth / 2,cameraHeight - 150 + 20, 0, 0, "Create Lobby");
        createLobbyLbl.background = null;
        createLobbyBtn.addChildren(createLobbyLbl);

        var btnExit = new Button(cameraWidth - 60, cameraHeight - 60,
                40, 40,  () -> System.exit(0));

        btnExit.setBackground(new Sprite(ResourceManager.getTexture("BtnExit")));

        btnMap.put("createLobbyBtn", createLobbyBtn);
        btnMap.put("ExitBtn", btnExit);
        btnMap.put("MainTitle", title);
    }
}
