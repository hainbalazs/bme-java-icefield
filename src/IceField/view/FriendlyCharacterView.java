package IceField.view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * FriendlyCharacter állapotát a grafikus felületen karbantartó és megjelenítő osztály.
 * Eltárolja a reprezentált karakter sorszámát, testhőjét, aktuális akciópontjait,
 * tudja, hogy vízben van-e vagy nem, ismeri a hordozotteszközöket (sorrendben).
 * Képes megjeleníteni a felső panelen a hozzátartozó karaktert reprezentáló ikont,
 * és az alsó panelen a hozzátartozó karakter által hordozott eszközöket,
 * továbbá a mezőn a jelenlétét reprezentáló karikát. Ismeri a kirajzolandó célterületet.
 */
public class FriendlyCharacterView extends CharacterView {
    /**
     * a megjelenítendő karakter sorszáma
     */
    private int id;
    /**
     * a megjelenítendő karakter életpontjai
     */
    private int health;
    /**
     * a megjelenítendő karakter akciópontjai
     */
    private int actionpoints;
    /**
     * akkor igaz, ha megjelenítendő karakter éppen aktív
     */
    private boolean active;
    /**
     * a megjelenítendő karakterben tárolt eszközök nézetei (megfelelő sorrendben)
     */
    private ArrayList<ToolView> tools;
    /**
     * a megjelenítendő karakterhez tartozó ikon színe
     */
    private Color iColor;
    /**
     * referencia arra a konténerre ahova a megjelenítendő karakter ikonját kell rajzolni
     */
    private JPanel playerIconP;
    /**
     * referencia arra a konténerre ahova a megjelenítendő karakter által hordozott eszközöket kell rajzolni,
     * statikus attribútum
     */
    private static JPanel inventoryP;
    /**
     * referencia arra a címkére ahova a megjelenítendő karakter akciópontjait ki kell írni,
     * statikus attribútum
     */
    private static JLabel actionpoinstL;
    /**
     * referencia arra a címkére ahova a megjelenítendő karakter életpontjait ki kell írni,
     * statikus attribútum
     */
    private static JLabel healthL;

    /**
     * az osztály alapértelmezett konstruktora
     * LILA színű karakterként fog megjelenni a mezőn
     */
    public FriendlyCharacterView() {
        super(Color.PINK);
    }

    /**
     * Ez a metódus akkor hívódik meg ha a modellben változik a karakter testhője,
     * absztrakt metódus, mivel nem minden karakternek kell rá reagálni.
     *
     * @param nv erre az értékre változott a modellben a karakter testhőjének értéke
     */
    @Override
    public void hpChanged(int nv) {
        health = nv;
        healthL.setText(String.valueOf(health));
    }

    /**
     * Ez a metódus akkor hívódik meg ha a modellben változik a karakter testhője,
     * ilyenkor frissítjük a felületen az új értéket.
     * @param nv erre az értékre változott a modellben a karakter akciópontjainak értéke
     */
    public void apChanged(int nv){
        actionpoints = nv;
        actionpoinstL.setText(String.valueOf(actionpoints));
    }

    /**
     * Ez a metódus akkor hívódik meg ha a modellben új eszköz kerül felvételre a karakterhez, vagy kerül ki.
     * Újra kirajzolódik az eszköztár.
     * @param tv frissült eszköznézeteket tartalmazó tömb
     */
    public void toolsUpdated(ArrayList<ToolView> tv){
        tools = tv;
        drawInventory();
    }

    /**
     * absztrakt metódus,
     * kirajzolja lila színnel, jobb-középső pozícióra
     * a karakter jelenlétét reprezentáló kört a mezőre.
     *
     * @param fieldP erre a mezőt befoglaló panelre rajzolódik ki a kör
     */
    @Override
    public void drawDot(JPanel fieldP) {}

    /**
     * Kirajzolódik a teljes nézet. Absztrakt metódus.
     */
    @Override
    public void draw() {}

    /**
     * A megfelelő állapot szerint (újra) kirajzolásra kerül az karakterhez tartozó eszköztár.
     */
    private void drawInventory(){}

    /**
     * Beállítja a nézethez tartozó statikus címkét,
     * ahova a megjelenítendő karakter akciópontjait ki kell írni
     * @param apL az új beállítandó címke
     */
    public static void setApLabel(JLabel apL){
        actionpoinstL = apL;
    }

    /**
     * Beállítja a nézethez tartozó statikus címkét,
     * ahova a megjelenítendő karakter életpontjait ki kell írni
     * @param hpL az új beállítandó címke
     */
    public static void setHpLabel(JLabel hpL){
        healthL = hpL;
    }

    /**
     * Beállítja a nézethez tartozó statikus panelt,
     * ahova a megjelenítendő karakter eszköztárát kell kirajzolni
     * @param invP az új beállítandó panel
     */
    public static void setInventoryPanel(JPanel invP){
        inventoryP = invP;
    }


}
