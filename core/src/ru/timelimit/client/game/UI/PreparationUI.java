package ru.timelimit.client.game.UI;

import com.badlogic.gdx.graphics.g2d.Sprite;
import ru.timelimit.client.game.TextureManager;

import java.util.ArrayList;
import java.util.HashMap;

public class PreparationUI extends UI {
    @Override
    public void init() {
        btnMap = new HashMap<>();

        var menuBtn = new Button(50, 10, 10, 10, () -> {});
        menuBtn.setSprite(new Sprite(TextureManager.get("test")));
        btnMap.put("MenuBtn", menuBtn);
    }


    // TODO: Finish when traps would be released
//    public void initChooser(ArrayList<Trap> trapList) {
//        int startX = 130;
//        for (int i = 0; i < btnMap.size(); i++) {
//            var trapBtn = new Button(startX, 10, 10, 10, () -> {});
//            btnMap.put("trapBtn" + i, trapBtn);
//        }
//    }


}
