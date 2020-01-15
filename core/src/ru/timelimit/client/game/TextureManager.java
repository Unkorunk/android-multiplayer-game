package ru.timelimit.client.game;

import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;

public final class TextureManager {

    public static Texture addTexture (String textureName, String path) {
        if (textureMap.containsKey(textureName)) {
            throw new RuntimeException("Texture is already exists");
        }

        textureMap.put(textureName, new Texture(path));
        return textureMap.get(textureName);
    }

    public static Texture get(String textureName) {
        if (!textureMap.containsKey(textureName)) {
            return null;
        }

        return textureMap.get(textureName);
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
        textureMap.clear();
    }

    private static HashMap<String, Texture> textureMap = new HashMap<>();
}
