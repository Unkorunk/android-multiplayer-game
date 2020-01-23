package ru.timelimit.client.game.UI;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import ru.timelimit.client.game.GameClient;
import ru.timelimit.client.game.ResourceManager;
import ru.timelimit.client.game.Trap;

import java.util.ArrayList;
import java.util.function.Consumer;

public final class PreparationUI extends UI {
    private Label moneyDisplay;

    public void updateMoney(int money) {
        moneyDisplay.setText("$" + money);
    }

    @Override
    public void init() {
        super.init();

        var width = cameraInst.viewportWidth;
        var height = cameraInst.viewportHeight;

        errorLabel = new Label(100, height - 20, 0, 0, "");
        errorLabel.background = null;

        var menuBtn = new Button(width - 40, height - 50, 40, 40, () -> {
            GameClient.sendDisconnect();
        });
        menuBtn.setSprite(new Sprite(ResourceManager.getTexture("BtnExit")));

        var title = new Label(cameraInst.viewportWidth / 2,
                cameraInst.viewportHeight - 50,
                100,10, "PREPARATION STAGE");

        moneyDisplay = new Label(width - 70 - 20, height - 50 - 20, 0, 0, "$0");

        addElement("ErrorLabel", errorLabel, true);
        addElement("MenuBtn", menuBtn, true);
        addElement("Title", title, true);
        addElement("MoneyDisplay", moneyDisplay, true);
    }

    public void initChooser(ArrayList<Trap> trapList, Consumer<Integer> cb) {
        int startX = 130;
        for (int i = 0; i < trapList.size(); i++) {
            int finalI = i;
            var trapBtn = new Button(startX, 10, 40, 40, () -> cb.accept(finalI));
            trapBtn.setSprite(new Sprite(trapList.get(finalI).getSprite().getTexture()));
            btnMap.put("trapBtn" + i, trapBtn);
            startX += 50;
        }
    }


}
