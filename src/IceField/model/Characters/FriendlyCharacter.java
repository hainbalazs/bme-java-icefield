package IceField.model.Characters;

import IceField.model.Fields.Field;
import IceField.model.Game;
import IceField.model.Tools.*;
import IceField.view.CharacterView;
import IceField.view.FriendlyCharacterView;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Ez az absztrakt osztály reprezentálja a játékban szereplő olyan karaktereket, amikkel a játékosok interakcióba léphetnek.
 * Ismeri és kezeli a maga állapotát, pl. mennyi élete van, mennyi akciót tud még tenni,
 * eltárolják az általuk megszerzett eszközöket (Tool), ezeket és a saját képességüket használni is tudják.
 * Ezenkívül a karakterek a saját kezükkel egy réteg havat is le tudnak szedni az adott mezőről.
 */
public abstract class FriendlyCharacter extends Character {
    private FriendlyCharacterView observer;
    /**
     * A karakter életerőpontjai (nem negatív), maximum csak maxhealth-nyi lehet
     */
    protected int health;
    /**
     * A karakter számára maximálisan elérhető életerőpont mennyiség (típusonként eltérő), konstans
     */
    private final int maxhealth;
    /**
     * A karakter akciópontjai (nem negatív)
     * kör elején mindig 4-re visszaállítódik, 1 eszköz/képesség használata egy pontba kerül, ha elfogy a karakter befejezi a körét
     */
    protected int actionPoint;
    /**
     * A karakter ebben az attribútumban tárolja el a felvett kulcstárgyait.
     * (0-2) sorszámon tartózkodó tárgyak, olyan sorrendben tárolódnak, ahogy felvették őket.
     */
    protected ArrayList<KeyTool> ktools;
    /**
     * A karakter ebben az attribútumban tárolja el a felvett sztenderd tárgyait.
     * (3-) sorszámon tartózkodó tárgyak, olyan sorrendben tárolódnak, ahogy felvették őket.
     */
    protected ArrayList<StandardTool> stools;
    /**
     * Referencia a karakter létező vagy nem létező (null) búvárruhájára.
     */
    protected DivingGear gear;

    /**
     * Alapértelmezett konstruktor.
     * @param id_ karakternek generált azonosító szám
     * @param maxhp_ karakternek (típus szerint) kiosztott maximális életerőpont érték
     */
    public FriendlyCharacter(int id_, int maxhp_, Game g_) {
        super(id_, g_);
        health = maxhealth = maxhp_;
        actionPoint = 4;
        gear = null;

        stools = new ArrayList<>();
        ktools = new ArrayList<>();
    }
    public FriendlyCharacter(int id_, Game g_, int maxhp_, int current_health){
        super(id_, g_);
        maxhealth = maxhp_;
        health = current_health;
        actionPoint = 4;
        gear = null;

        stools = new ArrayList<>();
        ktools = new ArrayList<>();
    }

    /**
     * Akkor kerül meghívásra, ha valamilyen okból kifolyólag a FriendlyKarakter vízbe kerül.
     * Ő magától nem tud kimászni, ezért jelez erről a Game felé.
     */
    public void fallenin() {
        gRef.inWater(this);
    }

    /**
     * A karakter a képességét használja. Absztrakt metódus. 1 munkapontba kerül, ha munkapontjai elfogynak köre végetér.
     * @param f ezen a Field-en használjuk a képességet.
     */
    public abstract void useAbility(Field f);

    /**
     * A karakter t eszközt használja. 1 munkapontba kerül, ha munkapontjai elfogynak köre végetér.
     * @param t ezt az Eszközt használjuk
     */
    public void useTool(Tool t) {
        t.doYourThing(this);
        actionPoint--;

        if(actionPoint == 0)
            gRef.endTurn();
    }

    public ArrayList<ToolView> wrapTools(){
        ArrayList<ToolView> tv = new ArrayList<ToolView>();
        int cnt = 0;
        for(; cnt < ktools.size(); cnt++)
            tv.add(cnt, ktools.get(cnt).getView());
        if(hasDivingGear())
            tv.add(3, gear.getView());
        for(cnt = 4; cnt - 4 < stools.size(); cnt++)
            tv.add(cnt, stools.get(cnt-4).getView());
        return tv;
    }

    /**
     * A karakter arról a mezőről amin éppen áll kiássa a belefagyott eszközt, ha valami be van fagyva.
     * Ha sikeres volt az ásás, akkor ez 1 munkapontba kerül, ha munkapontjai elfogynak köre végetér.
     */
    public void obtainTool(){
        boolean obtained = standingOn.obtainTool(this);
        if(obtained) {
            observer.toolsUpdated(wrapTools());
            actionPoint--;
            if (actionPoint == 0)
                gRef.endTurn();
        }
    }

    /**
     * Lekérdezés, hogy a karakter melyik aktuális mezőn tartózkodik.
     * @return ezen a mezőn áll a karakter.
     */
    public Field getField() {
        return standingOn;
    }

    /**
     * Így reagál az adott Karakter a sarkalatos időjárási viszonyokra.
     * Csökkentjük a FriendlyCharacter életét 1-gyel, ha elfogy végetér a játék.
     * Hóesés után hívódhat.
     */
    public void loseHealth() {
        health--;
        observer.hpChanged(health);
        if(health == 0)
            gRef.endGame(false);
    }

    /**
     * Növeljük a karakter életét 1-gyel, nem lépheti túl a maxhealth-ben meghatározott értéket.
     * Étel elfogyasztása után hívódhat.
     */
    public void addHealth() {
        if(health < maxhealth) {
            health++;
            observer.hpChanged(health);
        }
    }

    /**
     * A karakter akciópontjainak visszaállítása 4-re.
     * Kör elején, vagy vízből kimentéskor hívódhat.
     */
    public void resetAPs() {
        actionPoint = 4;
        observer.apChanged(4);
    }

    /**
     * Megadja, hogy a karateren található-e búvárruha.
     * @return igaz, ha a karakter rendelkezik Búvárruhával, egyébként hamis
     */
    public boolean hasDivingGear() {
        return gear != null;
    }

    /**
     * Felvesz a karakter egy búvárruhát.
     * @param dg felvenni készült búvárruha
     */
    public void setDivingGear(DivingGear dg){
        gear = dg;
    }

    /**
     * A karakter eltakarít egy réteg havat arról a mezőről ahol éppen áll, 1 munkapontért cserébe.
     */
    public void digWithHands() {
        standingOn.decreaseSnow(1);
        actionPoint--;

        if(actionPoint == 0)
            gRef.endTurn();
    }

    /**
     * Visszaadja a karakterben az i. -ként eltárolt eszközt.
     * @param i az ilyen sorszámon eltárolt eszközt kérjük. (0-2) esetén KeyToolt, >2 esetén StandardToolt adunk
     * @return a kiválasztott eszköz
     */
    public Tool getToolAt(int i) {
        if(i == 3)
            return gear;
        else if(i < 3)
            return ktools.get(i);
        else
            return stools.get(i-4);
    }

    /**
     * Elhelyez a karakterben egy sztenderd eszközt.
     * @param st elhelyezendő sztenderd eszköz
     */
    public void addSTool(StandardTool st) {
        stools.add(st);
    }

    /**
     * Elhelyez a karakterben egy kulcstárgyat.
     * Elhelyezés után automatikusan megindul a keresés a többi kulcstárgyért, de ez a kulcstárgy felelőssége.
     * @param kt elhelyezendő kulcstárgy
     */
    public void addKTool(KeyTool kt) {
        ktools.add(kt);
        gRef.keytoolFound();
    }

    /**
     * Ez a metódus írja le minden karakterre specifikusan milyen egyéb intézkedései vannak a lépés után
     * A játékos által irányított karakter elhasznál egy akcióponot lépésenként, ha azok elfogynak a játék végetér.
     */
    protected void afterStep(){
        actionPoint--;
        if(actionPoint == 0)
            gRef.endTurn();
    }

    /**
     * A paraméterként kapott ételt a karakter eltünteti a saját (sztenderd) eszközei közül.
     */
    public void removeThrowable(StandardTool tool){
        StandardTool st;
        for (Iterator<StandardTool> stIt = stools.iterator(); stIt.hasNext();) {
            st = stIt.next();
            if (st.equals(tool))
                stIt.remove();
        }
        observer.toolsUpdated(wrapTools());
    }

    @Override
    public void addObserver(CharacterView cv){
        observer = (FriendlyCharacterView) cv;
    }
}
