package IceField.model.Tools;

import IceField.model.Characters.Character;
import IceField.model.Characters.FriendlyCharacter;
import IceField.model.Game;
import IceField.model.Tools.Tool;

import java.util.logging.Logger;

/**
 * Olyan megszerezhető eszközt reprezentáló absztrakt osztály, amik nem kulcstárgyak,
 */
public abstract class StandardTool extends Tool {

    /**
     * Alapértelmezett konstruktor.
     * @param game A Game objektumra referencia
     */
    public StandardTool(Game game) {
        super(game);
    }

    /**
     * A tárgyon meghívott függvény absztrakt váza, amikor a tárgy cselekvését szeretnénk kiváltni.
     * @param c Referencia a karakterre, aki a tárgyat birtokolja.
     */
    public abstract void doYourThing(FriendlyCharacter c);

    /**
     * A tárgy elhelyezi magát a paraméterként kapott karakter sztenderd eszközeihez.
     * @param c Referencia a karakterre, akihez hozzárendeljük az eszközt.
     */
    public void place(FriendlyCharacter c) {
        c.addSTool(this);
    }

    /**
     * Absztrakt függvény. Teszteléshez, debuggoláshoz használt függvény.
     * @return Az objektum neve
     */
    public abstract String show();

}