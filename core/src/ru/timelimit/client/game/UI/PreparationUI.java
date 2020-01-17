package ru.timelimit.client.game.UI;

import com.badlogic.gdx.graphics.g2d.Sprite;
import ru.timelimit.client.game.GameClient;
import ru.timelimit.client.game.ResourceManager;
import ru.timelimit.client.game.Trap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

public final class PreparationUI extends UI {
    @Override
    public void init() {
        var width = GameClient.instance.sceneManager.currentScene.getCamera().viewportWidth;
        var height = GameClient.instance.sceneManager.currentScene.getCamera().viewportWidth;

        btnMap = new HashMap<>();

        errorLabel = new Label(100, GameClient.instance.sceneManager.currentScene.getCamera().viewportHeight - 20, 0, 0, "");
        errorLabel.background = null;
        btnMap.put("errorLabel", errorLabel);

        var menuBtn = new Button(width - 40, height - 50, 40, 40, () -> {
            GameClient.instance.sceneManager.currentScene.setState(1);
        });
        menuBtn.setSprite(new Sprite(ResourceManager.getTexture("BtnExit")));

        var title = new Label(GameClient.instance.sceneManager.currentScene.getCamera().viewportWidth / 2,
                GameClient.instance.sceneManager.currentScene.getCamera().viewportHeight - 50,
                100,10, "PREPARATION STAGE");

        btnMap.put("MenuBtn", menuBtn);
        btnMap.put("Title", title);
    }

    public void initChooser(ArrayList<Trap> trapList, Consumer<Trap> cb) {
        int startX = 130;
        for (int i = 0; i < trapList.size(); i++) {
            int finalI = i;
            var trapBtn = new Button(startX, 10, 40, 40, () -> {
                cb.accept(trapList.get(finalI));
            });
            trapBtn.setSprite(new Sprite(trapList.get(finalI).getSprite().getTexture()));
            btnMap.put("trapBtn" + i, trapBtn);
            startX += 50;
        }
    }


}
