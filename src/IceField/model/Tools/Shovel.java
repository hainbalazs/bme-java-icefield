package IceField.model.Tools;

import IceField.model.Characters.Character;
import IceField.model.Characters.FriendlyCharacter;
import IceField.model.Fields.Field;
import IceField.model.Game;

import java.util.logging.Logger;

/**
 * Ásót reprezentáló osztály. Az eszköz birtokában a karakter 2 réteg havat is eltakaríthat.
 */
public class Shovel extends StandardTool {

    /**
     * Alapértelmezett konstruktor.
     * @param game A Game objektumra referencia
     */
    public Shovel(Game game) {
        super(game);
    }

    /**
     * A tárgy használatakor a karakter mezőjén eltávolít két réteg havat.
     * @param c Referencia a karakterre, aki a tárgyat birtokolja.
     */
    public void doYourThing(FriendlyCharacter c) {
        Field f = c.getField();
        f.decreaseSnow(2);
    }
    /**
     * Teszteléshez, debuggoláshoz használt függvény.
     * @return Az objektum neve
     */
    public String show(){
        return "shovel";
    }
}