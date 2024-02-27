package IceField.model.Tools;

import IceField.model.Characters.Character;
import IceField.model.Characters.FriendlyCharacter;
import IceField.model.Tools.Tool;
import IceField.model.Game;

import java.util.logging.Logger;

/**
 * A kulcstárgyakat reprezentáló osztály, amelyek segítségével a játék megnyerhető.
 */
public class KeyTool extends Tool {

    /**
     * A kulcstárgy típusát reprezentáló attribútum.
     */
    private int type;

    /**
     * Alapértelmezett konstruktor, amivel a kulcstárgy típusát állíthatjuk be (összesen 3).
     * 1 - Flare Gun
     * 2 - Cartridge
     * 3 - Flare
     * @param t A kulcstárgy típusa
     * @param game A Game objektumra referencia
     */
    public KeyTool(int t, Game game){
        super(game);
        this.type = t;
    }

     /**
     * A tárgy elhelyezi magát a paraméterként kapott karakter kulcstárgyai közé.
     * Ezek után szól a Game-nek, hogy őt megszerezték.
     * @param c Referencia a karakterre, akihez hozzárendeljük az eszközt.
     */
    public void place(FriendlyCharacter c) {
        c.addKTool(this);
        gRef.keytoolFound();
    }

    /**
     * A kulcstárgy használatakor megnézzük, hogy a játékos megnyerte-e a játékot.
     * A Game-n meghív egy függvényt, ami ellenőrzi a nyerési feltételeket.
     * @param c Referencia a karakterre, aki a tárgyat birtokolja.
     */
    public void doYourThing(FriendlyCharacter c) {
        gRef.searchForKeyItems();
    }

    /**
     * Teszteléshez, debuggoláshoz használt függvény.
     * @return Az objektum neve és típusa
     */
    public String show(){
        return type + ",ktool";
    }

}