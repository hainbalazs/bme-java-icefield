package IceField.model.Tools;

import IceField.model.Characters.Character;
import IceField.model.Characters.FriendlyCharacter;
import IceField.model.Game;
import IceField.model.Tools.StandardTool;

import java.util.logging.Logger;

/**
 * Ételt reprezentáló osztály. Segítségével a karakter növelheti a testhőmérsékletét.
 */
public class Food extends StandardTool {

    /**
     * Alapértelmezett konstruktor.
     * @param game A Game objektumra referencia
     */
    public Food(Game game) {
        super(game);
    }

    /**
     * Használatkor a birtokos karakter életét növeli 1-el, és kivesszük a karakter eszköztárából.
     * @param c Referencia a karakterre, aki a tárgyat birtokolja.
     */
    public void doYourThing(FriendlyCharacter c) {
        c.addHealth();
        c.removeThrowable(this);
    }

    /**
     * Teszteléshez, debuggoláshoz használt függvény.
     * @return Az objektum neve
     */
    public String show(){
        return "food";
    }
}