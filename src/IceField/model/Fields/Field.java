package IceField.model.Fields;

import IceField.model.Characters.Character;
import IceField.model.Characters.FriendlyCharacter;
import IceField.model.Game;
import IceField.model.Tools.*;

import java.util.logging.Logger;
import java.util.ArrayList;

/**
 * Field osztály. Ez az absztrakt osztály reprezentálja a jégmezőt alkotó jégtáblákat. Ismeri és kezeli a maga állapotát, pl. van-e rajta hó, el van-e ásva rajta eszköz, építettek-e rá iglut stb. A Field-ek ismerik a szomszédos Field-eket, azokat le is lehet kérdezni.
 * @author sch413
 */
public abstract class Field {


    /**
     * A mező egyedi azonosítója
     */
    private int id;
    /**
     * A mezőn lévő építmény tartalmazó attribútum
     */
    private Building building;
    /**
     * A mezőn lévő hó mennyiséget tároló változó
     */
    private int snowlevel;
    /**
     * Annak a tényét tárolja, hogy ennek a mezőnek a kapacitását ismeri-e már a játékos
     */
    protected boolean exploredCapacity;
    /**
     * Az adott mező szomszédait tároló attribútum
     */
    private ArrayList<Field> neighbours;
    /**
     * Az adott mezőbe befagyott tárgyat tároló attribútum
     */
    private Tool frozentool;
    /**
     * Az adott mezőn álló karakterek kollekciója
     */
    private ArrayList<Character> characters;
    /**
     * Absztrakt fgv. ld. megvalósítás.
     */
    public abstract int getCapacity();
    /**
     * Absztrakt fgv. ld. megvalósítás.
     */
    public abstract void addCharacter(Character c);

    /**
     *  A field konstrukora, inicializálja a karakterek és szomszédok tárolásához használt kollekciókat
     *  generál egy véletlen mennyiségű havat a mezőre és elrejti a mező kapacitását
     */
    public Field(int id, int capacity, int snowlevel, boolean explored, Building building, Tool tool) {
        characters = new ArrayList<Character>();
        this.snowlevel = snowlevel;
        exploredCapacity = explored;
        this.id = id;
        this.building = building;
        this.frozentool = tool;
        neighbours = new ArrayList<Field>();
    }

    /**
     * Szintén inicializálásra szolgáló függvény, a pálya generálását követően ezzel a függvénnyel
     * állítódik be, hogy melyik field-ek lesznek szomszédosak
     */
    public void AddNeighbour(Field f){
        neighbours.add(f);
    }

    /**
     * Getter az id-re
     */
    public int getID(){
        return id;
    }



    /**
     * Kitörli a paraméterül kapott karaktert a karakterek kollekciójából. Akkor használjuk amikor ellép innen valaki,
     * vagy kihúzzák kötélel.
     */
    public void rmCharacter(Character c) {
        characters.remove(c);
    }


    /**
     * Viszzad egy stringben egy riportot az adott mezőről. Teszteléshez, meg grafikus megjelenítéshez használatos.
     */
    public String show (){
        String str = new String();
        str = str.concat("<" + id + ">" + "<" + exploredCapacity + ">"  + "<" + this.getCapacity() + ">");
        str = str.concat("<");
        for (int i = 0; i < characters.size(); i++){
            str = str.concat("" + characters.get(i).getID());
            if (i != characters.size() - 1)
                str = str.concat(",");
        }
        str = str.concat(">");
        str = str.concat("<");
        for (int i = 0; i < neighbours.size(); i++){
            str = str.concat("" + neighbours.get(i).getID());
            if (i != neighbours.size() - 1)
                str = str.concat(",");
        }
        str = str.concat(">");
        if (frozentool != null){
            str = str.concat("<" + frozentool.show() + ">");
        }
        else{
            str = str.concat("<null>");
        }
        if (building != null){
            str = str.concat(building.show());
        }
        else {
            str = str.concat("<null>");
        }
        str = str.concat("<" + snowlevel + ">");
        return str;
    }

    /**
     * Visszaadja a karakterek listáját.
     */
    public ArrayList<Character> getCharacters() {
        return characters;
    }

    /**
     * Visszaadja az ezzel a mezővel szomszédos mezők listáját.
     */
    public ArrayList<Field> getNeighbour() {
        return neighbours;
    }

    /**
     * A mezőn lévő jégbefagyott tárgynak szól, hogy helyezze magát a paraméterül kapott karakter tárgyai közé.
     * Ezután kitörli a tárgyat, hogy többször ne lehessen felszedni.
     * Vissza azt adja, hogy sikeres volt-e a tárgy felvétele.
     */
    public boolean obtainTool(FriendlyCharacter c) {
        if(frozentool == null)
            return false;
        frozentool.place(c);
        frozentool = null;
        return true;
    }

    /**
     * A specifikáció szerint épületet helyez a mezőre, majd visszadja, hogy ez sikeres volt-e
     * @param building
     * @return
     */
    public boolean addBuilding(Building building){
        if (this.building == null){
            if (building.protectFromBear()){
                if (snowlevel > 0){
                    this.building = building;
                    snowlevel = 0;
                    return true;
                }
                else    return false;
            }
            else
            {
                this.building = building;
                return true;
            }
        }
        return false;

    }

    /**
     * Megnöveli a hószintet eggyel a mezőn. Ez hóvihar esetén következhet be.
     */
    public void increaseSnow() {
        if (snowlevel < 6)
            snowlevel++;
    }

    /**
     * Csökkenti a mezőn lévő hószintet a megadott mennyiséggel, de minimum 0-ig. Ez a mennyiség lehet 1(kézzel),
     * vagy 2(lapáttal).
     */
    public void decreaseSnow(int amount) {
        snowlevel -= amount;
        if (snowlevel < 0)
            snowlevel = 0;
    }

    /**
     * Visszaadja a mezőn lévő épületet.
     * @return
     */
    public Building getBuilding(){
        return this.building;
    }

    /**
     * Kitörli a mezőn lévő épületet
     */
    public void rmBuilding(){
        building = null;
    }

    /**
     * Végrehajtja azokat az eseményeket, amiknek a specifikáció szerint a köt végén kell történjenek
     */
    public void endTurnEvent(){
        if (this.building != null)
            if (this.building.Destroy() == true){
                this.building = null;
            }
    }

    /**
     * Akkor hívódik meg, ha egy sarkkutató felkutatja a mező kapacitását, ekkor ez ennek megfelelő attribútumot true-ra
     * állítja, így a felhasználói felület le tudja majd kérdezni, hogy mely mezőknek a kapacitását kell kimutatnia.
     */
    public void exploredField(){
        exploredCapacity = true;
    }
}