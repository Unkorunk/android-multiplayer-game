package ru.timelimit.client.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import java.util.HashMap;

public final class TextureManager {

    public static Texture addTexture (String textureName, String path) {
        if (textureMap.containsKey(textureName)) {
            throw new RuntimeException("Texture is already exists");
        }

        textureMap.put(textureName, new Texture(path));
        return textureMap.get(textureName);
    }

    public static Texture getTexture(String textureName) {
        if (!textureMap.containsKey(textureName)) {
            return null;
        }

        return textureMap.get(textureName);
    }

    public static BitmapFont getFont(String fontName) {
        if (!fonts.containsKey(fontName)) {
            return null;
        }

        return fonts.get(fontName);
    }

    public static void dispose(String textureName) {
        if (textureMap.containsKey(textureName)) {
            textureMap.get(textureName).dispose();
            textureMap.remove(textureName);
        }
    }

    public static void disposeAll() {
        for (HashMap.Entry<String, Texture> it : textureMap.entrySet()) {
            it.getValue().dispose();
        }
        for (HashMap.Entry<String, BitmapFont> it : fonts.entrySet()) {
            it.getValue().dispose();
        }
        textureMap.clear();
    }

    public static void addFont(String name, BitmapFont font){
        fonts.put(name, font);
    }

    private static HashMap<String, Texture> textureMap = new HashMap<>();

    private static HashMap<String, BitmapFont> fonts = new HashMap<>();
}
