package IceField.model.Characters;

import IceField.model.*;
import IceField.model.Fields.Field;
import IceField.view.CharacterView;

import java.util.*;

/**
 * Ez az absztrakt osztály reprezentálja a játékban szereplő mezőkre helyezhető karaktereket,
 * akik a mezőkkel interakcióba is léphetnek különböző módokon: mozoghatnak két szomszédos mező között,
 * azokról le is eshetnek a vízbe, vagy a mezőn havazás áldozatai lehetnek.
 */
public abstract class Character {
    /**
     * referencia a karakter saját nézetére
     * A leszármazottaknak nekik megfelelő specializált observer-jük van. (Bear - BearView, FriendlyCharacter - FriendlyCharacterView)
     */
    private CharacterView observer;
    /**
     * Referencia arra a mezőre, amin a karakter jelenleg áll, vagy amelyikről éppen vízbeesett
     */
    protected Field standingOn;
    /**
     * Referencia a központi Game osztályra
     * rajta keresztül történik a kommunikáció kör/játék vége, vízbeesés adminisztrációjánál
     */
    protected static Game gRef;
    /**
     * A karakter egyedi azonosítója
     * jelenleg nem használt, fenntartva későbbi fejlesztésre
     */
    protected int id;

    /**
     * Alapértelmezett konstruktor.
     * @param id_ karakternek generált azonosító szám
     */
    public Character(int id_,  Game g_) {
        id = id_;
        standingOn = null;
        gRef = g_;
    }

    /**
     * Áthelyezi a karaktert 'next' mezőre, megváltoztatja a standingOn attribútumát, majd a mező felé is jelzi, hogy egy újabb karaktert kell befogadnia
     * @param next erre a Field mezőre lép át
     */
    public void stepOn(Field next) {
        ArrayList<Field> neighbours = standingOn.getNeighbour();
        if(neighbours.contains(next)){
            standingOn.rmCharacter(this);
            next.addCharacter(this);
            standingOn = next;
            afterStep();
        }
        else
            gRef.showError("A karakter csak szomszédos mezőre léphet!");
    }

    /**
     * Akkor kerül meghívásra, ha valamilyen okból kifolyólag a karakter vízbe kerülne. Absztrakt függvény, vannak akik tudnak úszni.
     * Ha vízbekerül, jelez erről a Game felé.
     */
    public abstract void fallenin();

    /**
     * Lekérdezés, hogy a karakter melyik aktuális mezőn tartózkodik.
     * @return ezen a mezőn áll a karakter.
     */
    public Field getField() {
        return standingOn;
    }

    /**
     * Így reagál az adott Karakter a sarkalatos időjárási viszonyokra. Absztrakt függvény, egyes karakterek másként tűrik a hideget.
     * Hóesés után hívódhat.
     */
    public abstract void loseHealth();

    /**
     * Megadja az adott karakter egyedi azonosítóját
     * @return a karakter azonosítója
     */
    public int getID(){return id;}

    /**
     * Adott View-tól függően megjeleníti az adott karakterről a tudnivalókat. Absztrakt függvény.
     * Gui esetén kirajzolja magát
     * Promptos indításnál kiírja a tulajdonságait <char_id><field_id><in_water><health><actionpoints><tools [..]> formátumban
     */
    public abstract String show();

    /**
     * Ez a metódus inicializálja a karakter standingOn paraméterét, csak egyszer inicializásnál hívható.
     * @param f erre a mezőre spawnoljuk a karaktert
     * akár valami plusz tudást is bele lehetne rakni Game inithez
     */
    public void initField(Field f) {
        if(standingOn == null) {
            standingOn = f;
            standingOn.addCharacter(this);
        }
    }

    /**
     * Ez a metódus írja le minden karakterre specifikusan milyen egyéb intézkedései vannak a lépés után
     * Absztrakt metódus.
     */
    protected abstract void afterStep();

    /**
     * View objektum hozzákapcsolása az adott karakterhez
     * @param cv a megfelelő view objektum
     */
    public void addObserver(CharacterView cv){
        observer = cv;
    }
}