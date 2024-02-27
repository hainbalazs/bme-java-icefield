package IceField.model.Tools;

import IceField.model.Characters.Character;
import IceField.model.Characters.FriendlyCharacter;
import IceField.model.Game;

import java.util.logging.Logger;

/**
 * A játék eszközeit összefoglaló absztrakt osztály.
 */
public abstract class Tool {
    /**
     * Referencia a játék Game objektumára.
     */
    protected static Game gRef;

    /**
     * Alapértelmezett konstruktor.
     * @param game A Game objektumra referencia
     */
    public Tool(Game game) {
        gRef = game;
    }

    /**
     * A tárgyon meghívott függvény absztrakt váza, amikor a tárgy cselekvését szeretnénk kiváltni.
     * @param c Referencia a karakterre, aki a tárgyat birtokolja.
     */
    public abstract void doYourThing(FriendlyCharacter c);

    /**
     * Absztrakt függvény. A tárgy elhelyezi magát a paraméterként kapott karakter tárgyai közé.
     * @param c Referencia a karakterre, akihez hozzárendeljük az eszközt.
     */
    public abstract void place(FriendlyCharacter c);

    /**
     * Absztrakt függvény. Teszteléshez, debuggoláshoz használt függvény.
     * @return Az objektum neve
     */
    public abstract String show();

}