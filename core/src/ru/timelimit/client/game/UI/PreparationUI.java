package ru.timelimit.client.game.UI;

import com.badlogic.gdx.graphics.g2d.Sprite;
import ru.timelimit.client.game.TextureManager;
import ru.timelimit.client.game.Trap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class PreparationUI extends UI {
    @Override
    public void init() {
        btnMap = new HashMap<>();

        var menuBtn = new Button(50, 10, 10, 10, () -> {});
        menuBtn.setSprite(new Sprite(TextureManager.get("test")));
        btnMap.put("MenuBtn", menuBtn);
    }

    public void initChooser(ArrayList<Trap> trapList, Consumer<Trap> cb) {
        int startX = 130;
        for (int i = 0; i < trapList.size(); i++) {
            int finalI = i;
            var trapBtn = new Button(startX, 10, 10, 10, () -> {
                cb.accept(trapList.get(finalI));
            });
            trapBtn.setSprite(new Sprite(TextureManager.get("test")));
            btnMap.put("trapBtn" + i, trapBtn);
            startX += 40;
        }
    }


}
