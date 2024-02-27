package IceField.model;

import IceField.controller.SchLogger;
import IceField.controller.SchParser;
import IceField.model.Characters.Bear;
import IceField.model.Characters.Character;
import IceField.model.Characters.FriendlyCharacter;
import IceField.model.Fields.Field;
import IceField.model.Fields.StableField;
import IceField.model.Tools.KeyTool;
import IceField.model.Tools.StandardTool;
import IceField.model.Tools.Tool;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author sch413
 */

/**
 * A Modell architektúra motorját megvalósító osztály. Tárolja a játék elemeit.
 * Kiszolgálja a kontroller által beérkező hívásokat, számon tartja, ki a soron következő karakter,
 * interakcióba lép vele. Menedzseli a köröket, a fordulókat és azok végén bekövetkező eseményeket
 * (havazás, vízben lévő játékosok számontartása), ellenőrzi, hogy meghaltak-e a karakterek.
 * Kötéllel való mentésnél menedzseli a választást a kimenthető karakterek közül.
 * Tárolja az eddig megtalált kulcstárgyak számát, így azok használatakor el tudja dönteni,
 * hogy nyert-e a játékos. Elindítja és befejezi a játékot. Egyetlen példány létezhet.
 */
public class  Game {

    /**
     * A parseren keresztül tud a game interfacelni az I/O elemekkel
     */
    SchParser p;

    /**
     * Referencia a központi Logger-re
     * csak fejlesztési célokra használt (szkeleton, debug, teszt)
     */
    static final public Logger loggr = Logger.getLogger("sch413logger");

    /**
     * Ez a számláló tartja számon, eddig hány kulcstárgyat találtak meg a karakterek.
     */
    private int ktCounter;

    /**
     * Számontartja a játék állapotát c: folyamatban, w= nyert, l= vesztett
     */
    private char gameState;

    /**
     * A karaktereket tároló lista
     */
    private ArrayList<FriendlyCharacter> characters;

    /**
     * A mezőket tároló lista
     */
    private ArrayList<Field> fields;

    /**
     * A karaktereket tároló lista
     */
    private ArrayList<Bear> bears;

    /**
     * Azt a karaktert tárolja, aminek épp a köre van
     */
    private FriendlyCharacter activeCharacter;

    /**
     * Számláló, amely megmondja, hogy még hány karakter jöhet, mielőt vihar törne ki.
     */
    private int blizzardcounter;

    /**
     * Egy kulcs-érték adatstruktúra, amely eltárolja, hogy az egyes karakterek vízbe
     * esése óta hány karakter köre ment le.
     */
    private HashMap<FriendlyCharacter, Integer> timesinwater;

    /**
     * Ha 0, akkor random értékeket kap a blizzardCounter, ha pozitív akkor mindig azt az értéket kapja vihar után.
     */
    private int defaultBlizzard;

    /**
     * Globális random változó, a játékmenet bekövetkező véletlenszerű
     * eseményekhez ezt használjuk.
     */
    static Random rand;

    /**
     * Tárolja, hogy engedélyezettek-e a random események.
     */
    private boolean randstate;


    /**
     *
     *Inicializálja random változót, a blizzardCountert, a defaultBlizzardot, a randstate-et, a ktCountert, a karakter és mező tömböket, a timesinwatert, a parsert és a gameState-et.
     * @param dbc defaultBlizzard értéke
     * @param rands engedélyezettek-e a véletlen események
     */
    public Game(int dbc, boolean rands, SchParser _p) {
        rand = new Random();
        blizzardcounter = dbc;
        randstate = rands;
        if (randstate == false) defaultBlizzard = dbc;
        else defaultBlizzard = 0;
        gameState = 'c';
        ktCounter = 0;
        characters = new ArrayList<FriendlyCharacter>();
        fields = new ArrayList<Field>();
        bears = new ArrayList<Bear>();
        timesinwater = new HashMap<>();
        p = _p;
    }


    /**
     * Visszaadja az éppen aktív karaktert.
     * @return Az aktuális karakter.
     */
    public FriendlyCharacter getActiveChar() {
        return activeCharacter;
    }


    /**
     * Kör végén esedékes események kezelését végzi: új aktív karakter beállítása, ellenőrzi,
     * hogy a vízbeesett karakterek meghalnak-e, ha igen, meghívja az endGame-et. Ha lejár a blizzardCounter,
     * azaz esedékes a vihar, meghívja a blizzard() függvényt. Emellett lép a pályán lévő medvékkel, a fieldeken meghívja az endturneventet
     */
    public void endTurn() {
        //előző karakter pontjainak visszaállítása
        activeCharacter.resetAPs();

        //blizzardot meghívjuk, ha kell
        blizzardcounter--;
        if(blizzardcounter == 0){
            blizzard();
            if(randstate == false) blizzardcounter = defaultBlizzard;
            else blizzardcounter = 1+rand.nextInt(characters.size());
        }

        //lépünk a medvékkel, de közben ellenőizni kell, hogy van-e medve már azon a mezőn
        //ha van, akkor nem lép oda a medve
        for (Bear b : bears) {
            Field f = null;
            if(randstate == false){
                int i = 0;
                boolean found = false;
                while( i<b.getField().getNeighbour().size() && found == false) {
                    found = true;
                    for(Bear be : bears){
                       if(be != b && be.getField() == b.getField().getNeighbour().get(i)){
                           found = false;
                       }
                    }
                    if(found == false) i++;
                }
                if(i<b.getField().getNeighbour().size())
                f = b.getField().getNeighbour().get(i);
            }
            else{
                ArrayList<Field> fl = new ArrayList<Field>();
                for(Field g: b.getField().getNeighbour()){
                    boolean free = true;
                    for(Bear be : bears){
                        if(be != b && be.getField() == g){
                            free = false;
                        }
                    }
                    if(free) fl.add(g);
                }
                if(fl.size() != 0)
                f = fl.get(rand.nextInt(fl.size()));
            }
            if(f != null) b.stepOn(f);
        }

        for (Field f: fields) {
            f.endTurnEvent();
        }

        //ellenőrizzük, hogy valaki vízbefullt-e, illetve mindenkinek csökkentjük a hátralévő idejét
        for(FriendlyCharacter c: characters){
            if(timesinwater.containsKey(c)){
                int a = timesinwater.get(c);
                a--;
                if(a <= 0 && c.hasDivingGear() == false)  endGame(false);
                timesinwater.replace(c, a);
            }
        }

        //beállítjuk a következő karaktert aktívnak
        int nextindex = characters.indexOf(activeCharacter)+1;
        if(nextindex == characters.size()) nextindex = 0;
        activeCharacter = characters.get(nextindex);
        activeCharacter.resetAPs();
    }

    /**
     * Ez a függvény hívódik meg vihar esetén. Véletlenszerűen kiválaszt a jégmezőről mezőket,
     * ezeknek növeli a hó szintjét, illetve levon egy életet a nem igluban lévő karakterektől
     */
    public void blizzard() {
        for(Field f: fields){
            if(randstate == false || rand.nextInt(2) == 1){
                f.increaseSnow();
                if(f.getBuilding()==null || (f.getBuilding() != null  && f.getBuilding().protectFromSnow()==false)){
                    for(Character c: f.getCharacters()){
                        c.loseHealth();
                    }
                }
            }
        }
    }

    /**
     * A játék végét kezelő függvény, közli a felhasználóval, hogy véget ért a játék, és hogy milyen eredménnyel.
     * Implementációja kontroller függő. Konzolos környezetben csak kiírja, hogy nyert vagy vesztett a felhasználó.
     * Megváltoztatja a gameState attribútum értékét az inputnak megfelelően.
     * @param result Ha nyert a játékos true, egyébként false
     */
    public void endGame(boolean result) {
        if(result == true){
            gameState = 'w';
            return;
        }
      gameState = 'l';

    }

    /**
     *  Néhány interakció csak adott körülmények között engedélyezettek, ha ezek nem állnak fenn, a tevékenység nem végrehajtható,
     *  erről értesítést kap a Game-en keresztül a felhasználó. Konzolos környezetben csak kiírja a kapott stringet.
     * @param s a kiírandó üzenet
     */
    public void showError(String s) {
        p.print(s);
    }

    /**
     *  A Game értesül, hogy c karakter vízekerült, eszerint frissíti timesInWater-t, azaz felveszi bele a karaktert,
     *  és értéknek a karakterek számát rendeli hozzá.
     *  @param c a vízbesett karakter
     */
    public void inWater(FriendlyCharacter c) {
        timesinwater.put(c, characters.size());
    }


    /**
     * A felhasználó ezzel választhatja ki, hogy a paraméterül kapott karakter szomszédos mezőin
     * lévő játékosok közül kit akar kimenteni a vízből. Ehhez megkeresi a szomszédos, vízben lévő
     * karaktereket, majd a felhasználó választ egyet közölük. Interfacenek a parser print és chooseRescued függvényét használjuk
     * @param c A kimentő karakter.
     * @return A kimentendő karakter.
     */
    public Character chooseCharacter(FriendlyCharacter c) {
        boolean succes = false;
        while(succes == false){
            p.print("Enter the ID of the character that you wish to rescue!");
            for(Field f : c.getField().getNeighbour()){
                for(Character c2: f.getCharacters()){
                    if(timesinwater.containsKey(c2))
                        p.print(((Integer)c2.getID()).toString());
                }
            }
            int i = p.chooseRescued();
            for(FriendlyCharacter c3: characters){
                if(c3.getID() == i){
                    succes = true;
                    return c3;
                }
            }
            p.print("Rossz karakter id!");
        }
        return null;
    }

    /**
     * Híváskor a Game tudni fogja, hogy még egy kulcstárgyat megtaláltak,
     * növeli a megfelelő számlálót.
     */
    public void keytoolFound() {
        ktCounter++;
    }

    /**
     * Kiírja a játék attribútumainak értékét, a fields, characters és bears lista elemeire meghívja a listaelemek kiíró függvényeit a kimeneti nyelvnek megfelelően.
     */
    public String show(){
        String res = fields.size() + "\n" + characters.size() + "\n" + activeCharacter.getID() +"\n" + blizzardcounter + "\n" +gameState + "\n<";
        String w = "";
        for(FriendlyCharacter c : characters){
            if(timesinwater.containsKey(c))  w += (c.getID() + ",");
        }
        if(w.length()>0) w = w.substring(0, w.length()-1);
        res+= (w+">");
        for(FriendlyCharacter c : characters){
            res+=("\n" +c.show());
        }
        for(Bear b : bears){
            res+=("\n"+b.show());
        }
        for(Field f: fields){
            res+=("\n"+f.show());
        }

        return res;
    }

    /**
     * Ellenőrzi, hogy minden követelmény fennáll-e a játék megnyeréséhez.
     * Ez abból áll, hogy ellenőrizzük, hogy minden karakter egy mezőn áll-e,
     * és hogy megtaláltuk-e már az összes kulcstárgyat. Ha igen, vége a játéknak.
     * @return Ha megnyertük a játékot true, egyébként false.
     */
    public boolean searchForKeyItems() {
        if(ktCounter < 3){
            showError("The character failed to assemble the flare gun: missing some parts!");
            return false;
        }
        Field f = characters.get(0).getField();
        for(FriendlyCharacter c: characters){
            if(c.getField() != f){
                showError("The character failed to assemble the flare gun: each character must be on the same field!");
                return false;
            }
        }
        endGame(true);
        return true;
    }

    //A játékállapot kialakításához szükséges függvények

    /**
     * Visszatér az adott id-val rendelkező mezővel.
     * @param id a visszaadott mező azonosítója
     */
    public Field getField(int id){
        for(Field f : fields){
            if(f.getID() == id) return f;
        }
        showError("This field id does not exist!");
        return null;
    }

    /**
     * Meghívja az adott id-val rendelkező mező show függvényét.
     * @param id a mező azonosítója
     */
    public String showField(int id){
        for(Field f: fields){
            if(f.getID() == id){
                return  (f.show());
            }
        }
        showError("This field id does not exist!");
        return null;
    }

    /**
     * Meghívja az adott id-val rendelkező karakter vagy medve show függvényét.
     * @param id a karakter azonosítója
     */
    public String showChar(int id){
        for(FriendlyCharacter c: characters){
            if(c.getID() == id){
                return (c.show());
            }
        }
        for(Bear b: bears){
            if(b.getID() == id){
                return (b.show());
            }
        }
        showError("This character id does not exist!");
        return null;
    }

    /**
     * Hozzáad egy karaktert a characters listához. Ha ez az első karakter, akkor beállítja activeCharacternek
     * @param c
     */
    public void addChar(FriendlyCharacter c){
        characters.add(c);
        if(characters.size()==1) activeCharacter = c;
    }

    /**
     * Az adott azonosítójú karaktert kikeressük a characters listából és a megfelelő értékkel hozzáadjuk a timesInWaterhez.
     * @param id a karakter azonosítója
     * @param timeleft ameddig még életben maradhat a vízben
     */
    public void drowning(int id, int timeleft){
        FriendlyCharacter c = null;
        for(FriendlyCharacter c2: characters){
            if(c2.getID() == id) c = c2;
        }
        if(c == null) {showError("This character id does not exist!"); return; }
        if(timesinwater.containsKey(c)) timesinwater.replace(c, timeleft);
        else timesinwater.put(c, timeleft);
    }

    /**
     * Hozzáadjuk a medvét a bears listához.
     * @param b a játékhoz adandó medve
     */
    public void addBear(Bear b){
        bears.add(b);
    }

    /**
     * Beállítja a szomszédságot két mező között.
     * @param id1 az egyik mező azonosítója
     * @param id2 a másik mező azonosítója
     */
    public void addNeighbour(int id1, int id2){
        Field f1 = null;
        Field f2 = null;
        for(Field f: fields){
            if(f.getID() == id1) f1 = f;
        }
        for(Field f: fields){
            if(f.getID() == id2) f2 = f;
        }
        if(f1 == null || f2 == null) {showError("This field id does not exist!"); return;}

        f1.AddNeighbour(f2);
        f2.AddNeighbour(f1);
    }

    /**
     * Hozzáadja a mezőt a fields listához
     * @param f a hozzáadandó mező
     */
    public void addField(Field f){
        fields.add(f);
    }

    /**
     * Berakja a kapott tárgyat az idhoz tartozó karakter inventoryjába.
     * @param t a tárgy
     * @param id a karakter azonosítója
     */
    public void addstdtool(StandardTool t, int id){
        for(FriendlyCharacter c: characters){
            if(c.getID() == id){
                c.addSTool(t);
                return;
            }
        }
        showError("This character id does not exist!");
    }

    /**
     * Berakja a kapott tárgyat az idhoz tartozó karakter inventoryjába.
     * @param t a tárgy
     * @param id a karakter azonosítója
     */
    public void addktool(KeyTool t, int id){
        for(FriendlyCharacter c: characters){
            if(c.getID() == id){
                c.addKTool(t);
                return;
            }
        }
        showError("This character id does not exist!");
    }
}