package ru.timelimit.client.game.UI;

import com.badlogic.gdx.graphics.g2d.Sprite;
import ru.timelimit.client.game.GameClient;
import ru.timelimit.client.game.ResourceManager;
import ru.timelimit.client.game.Trap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

public final class PreparationUI extends UI {
    private Label moneyDisplay;

    public void updateMoney(int money) {
        moneyDisplay.setText("$" + money);
    }

    @Override
    public void init() {
        var width = GameClient.instance.sceneManager.currentScene.getCamera().viewportWidth;
        var height = GameClient.instance.sceneManager.currentScene.getCamera().viewportHeight;

        btnMap = new HashMap<>();

        var menuBtn = new Button(10, GameClient.instance.sceneManager.currentScene.getCamera().viewportHeight - 50, 40, 40, () -> {});
        menuBtn.setSprite(new Sprite(ResourceManager.getTexture("BtnExit")));

        var title = new Label(GameClient.instance.sceneManager.currentScene.getCamera().viewportWidth / 2,
                GameClient.instance.sceneManager.currentScene.getCamera().viewportHeight - 50,
                100,10, "PREPARATION STAGE");

        moneyDisplay = new Label(width - 70 - 20, height - 50 - 20, 0, 0, "$0");

        btnMap.put("MenuBtn", menuBtn);
        btnMap.put("Title", title);
        btnMap.put("MoneyDisplay", moneyDisplay);
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
