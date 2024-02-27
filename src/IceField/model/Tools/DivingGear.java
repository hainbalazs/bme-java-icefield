package IceField.model.Tools;

import IceField.model.Characters.Character;
import IceField.model.Characters.FriendlyCharacter;
import IceField.model.Game;
import IceField.model.Tools.Tool;

import java.util.logging.Logger;

/**
 * A búvárruhát reprezentáló osztály. Segítségével a karakter nem fullad vízbe.
 */
public class DivingGear extends StandardTool {

    /**
     * Alapértelmezett konstruktor.
     * @param game A Game objektumra referencia
     */
    public DivingGear(Game game) {
        super(game);
    }

    /**
     * A tárgy elhelyezi magát a paraméterként kapott karakter búvárruhájaként.
     * @param c Referencia a karakterre, akihez hozzárendeljük az eszközt.
     */
    public void place(FriendlyCharacter c) {
        c.setDivingGear(this);
    }

    /**
     * Mivel a búvárruhának aktív képessége nincsen, ezért üres.
     */
    public void doYourThing(FriendlyCharacter c) {}

    /**
     * Teszteléshez, debuggoláshoz használt függvény.
     * @return Az objektum neve
     */
    public String show(){
        return "dgear";
    }

}
