package IceField.model.Tools;

import IceField.model.Characters.FriendlyCharacter;
import IceField.model.Fields.DeployedTent;
import IceField.model.Fields.Field;
import IceField.model.Game;

/**
 * A sátort, mint tárgyat reprezentáló osztály.
 */

public class TentItem extends StandardTool {

    /**
     * Alapértelmezett konstruktor.
     * @param game A Game objektumra referencia
     */
    public TentItem(Game game){
        super(game);
    }

    /**
     * Használatkor a birtokos karakter mezőjére egy sátrat rakunk, és kivesszük a karakter eszköztárából.
     * @param c Referencia a karakterre, aki a tárgyat birtokolja.
     */
    public void doYourThing(FriendlyCharacter c) {
        Field f = c.getField();
        f.addBuilding(new DeployedTent());
        c.removeThrowable(this);
    }

    /**
     * Teszteléshez, debuggoláshoz használt függvény.
     * @return Az objektum neve
     */
    public String show(){
        return "tent";
    }
}
