package IceField.model.Characters;

import IceField.model.Game;
import IceField.view.BearView;

/**
 * Ez az osztály reprezentálja a játékban a jegesmedvéket, olyan karakter,
 * aki körönként lép egyet (a Game által véletlenszerűen megadott) irányba.
 * Ha közös jégtáblán áll egy barátságos karakterrel aki nincs védett helyen, akkor azt elkapja, és vége a játéknak.
 * Vízbeesés nem befolyásolja, mert ki tud úszni egy másik jégtáblára, és a vízben is el tudja kapni a többi barátságos karaktert.
 * Hóesés sem befolyásolja.
 */
public class Bear extends Character{
    /**
     * Alapértelmezett konstruktor.
     * @param id_ karakternek generált azonosító szám
     */
    public Bear(int id_, Game g_) {
        super(id_, g_);
    }

    /**
     * A medve minden lépése után meghívásra kerülő függvény, ami megöli a rajta álló, nem védett (akár vízben lévő) játékosokat.
     * Ha valaki meghal, a játék végetér.
     * A Game felelőssége ezt meghívni.
     * ebben a vezióban a Game-nek kell figyelni hogy 2 medve ne lépjen 1 helyre
     */
    public void kill(){
        if(standingOn.getCharacters().size() > 1 && !(standingOn.getBuilding() != null && standingOn.getBuilding().protectFromBear()))
            gRef.endGame(false);
    }
    /**
     * Akkor kerül meghívásra, ha valamilyen okból kifolyólag a karakter vízbe kerülne.
     * A jegesmedve tud úszni, őt nem kell számontartani. Direkt üres metódus.
     */
    @Override
    public void fallenin() {}

    /**
     * Így reagál az adott Karakter a sarkalatos időjárási viszonyokra.
     * A jegesmedve megszokta a havazást nem veszít testhőt.
     * Hóesés után hívódhat. Direkt üres metódus.
     */
    @Override
    public void loseHealth() {}

    /**
     * Adott View-tól függően megjeleníti az adott karakterről a tudnivalókat. Absztrakt függvény.
     * Gui esetén kirajzolja magát
     * Promptos indításnál kiírja a tulajdonságait <char_id><field_id><character_type> formátumban
     */
    @Override
    public String show() {
        return "<"+ id +">"+"<"+ standingOn.getID()+">"+"<bear>";
    }

    /**
     * Ez a metódus írja le minden karakterre specifikusan milyen egyéb intézkedései vannak a lépés után
     * A medvének meg kell probálnia ölni lépésenként.
     */
    @Override
    protected void afterStep() {
        kill();
    }
}
