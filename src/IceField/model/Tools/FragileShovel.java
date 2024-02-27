package IceField.model.Tools;

import IceField.model.Characters.FriendlyCharacter;
import IceField.model.Fields.Field;
import IceField.model.Game;

/**
 * Törékeny ásót reprezentáló osztály. Az eszköz birtokában a karakter 2 réteg havat is eltakaríthat, azonban
 * háromszori használat után eltörik, és további használatkor csak egyetlen havat tud eltávolítani.
 */

public class FragileShovel extends Shovel{

    /**
     * Az ásó tartósságga
     */
    private int durability;

    /**
     * Alapértelmezett konstruktor. A lapát tartóssága 3.
     * @param game A Game objektumra referencia
     */
    public FragileShovel(Game game){
        super(game);
        durability = 3;
    }

    /**
     * Konstruktor, amivel beállítható az ásó kezdeti tartóssága is
     * @param game A Game objektumra referencia
     * @param d A kezdeti tartósság értéke
     */
    public FragileShovel(Game game, int d){
        super(game);
        durability = d;
    }

    /**
     * A tárgy használatakor ha az ásó még nem tört el, akko a karakter mezőjén eltávolít két réteg havat.
     * Ha már eltört (durability == 0), akkor csak egy réteget.
     * @param c Referencia a karakterre, aki a tárgyat birtokolja.
     */
    @Override
    public void doYourThing(FriendlyCharacter c) {
        Field f = c.getField();
        if(durability > 0) {
            f.decreaseSnow(2);
            durability--;
        }
        else {
            f.decreaseSnow(1);
        }
    }

    /**
     * Teszteléshez, debuggoláshoz használt függvény.
     * @return Az objektum neve és tartóssága
     */
    public String show(){
        return "fshovel," + durability;
    }
}
