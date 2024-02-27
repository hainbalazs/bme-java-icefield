package IceField.model.Tools;

import IceField.model.Characters.Character;
import IceField.model.Characters.FriendlyCharacter;
import IceField.model.Fields.Field;
import IceField.model.Game;

import java.util.logging.Logger;

/**
 * A kötelet reprezentáló osztály. Segítségével egy karakter megmentheti vízbe esett társát.
 */
public class Rope extends StandardTool {

    /**
     * Alapértelmezett konstruktor.
     * @param game A Game objektumra referencia
     */
    public Rope(Game game) {
        super(game);
    }

    /**
     * A kötél használatakor meghívott függvény.
     * Ekkor a Game objektumtól kér egy válaszott karaktert, aki aztán a birtokló karakter mezőjére helyez.
     * @param c Referencia a karakterre, aki a tárgyat birtokolja.
     */
    public void doYourThing(FriendlyCharacter c) {
        Character r = gRef.chooseCharacter(c);
        if(r != null) {
            Field f = c.getField();
            r.stepOn(f);
        }
    }

    /**
     * Teszteléshez, debuggoláshoz használt függvény.
     * @return Az objektum neve
     */
    public String show(){
        return "rope";
    }

}