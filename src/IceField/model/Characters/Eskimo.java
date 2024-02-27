package IceField.model.Characters;

import IceField.model.Fields.Field;
import IceField.model.Fields.Igloo;
import IceField.model.Game;

import java.util.logging.Logger;

/**
 * Ez az osztály reprezentálja a játékban az eszkimókat, akik Friendly Character-ek.
 * Képessége az igluépítés, amit csak a saját mezőjén tud végrehajtani, ha van rajta hó is éppen.
 * Maximális élete 5.
 */
public class Eskimo extends FriendlyCharacter {
    /**
     * Alapértelmezett konstruktor
     * 5 maxhp
     */
    public Eskimo(int id_, Field standingOn_, Game g_) {
        super(id_, 5, g_);
    }
    public Eskimo(int id_, Game g_, int currhp) {super(id_, g_, 5, currhp);}

    /**
     * Adott View-tól függően megjeleníti az adott karakterről a tudnivalókat. Absztrakt függvény.
     * Gui esetén kirajzolja magát
     * Promptos indításnál kiírja a tulajdonságait <char_id><field_id><in_water><health><actionpoints><tools [..]><character_type> formátumban
     */
    @Override
    public String show() {
        StringBuilder sTools = new StringBuilder();
        int cnt = 0;
        for(int i = 0; i < ktools.size(); i++, cnt++){
            sTools.append("{").append(cnt).append(",").append(ktools.get(i).show()).append("}");
            if(!(i == ktools.size() - 1))
                sTools.append(",");
        }
        cnt = 3;
        if(hasDivingGear()){
            sTools.append("{").append(cnt++).append(",").append(gear.show()).append("}");
        }
        cnt = 4;
        for (int j = 0; j < stools.size(); j++, cnt++){
            sTools.append("{").append(cnt).append(",").append(stools.get(j).show()).append("}");
            if(!(j == stools.size() - 1))
                sTools.append(",");
        }
        return "<"+ id +">"+"<"+ standingOn.getID()+">"+"<"+ health +">"+"<"+ actionPoint+">"+"<"+sTools+">"+"<eskimo>";
    }

    /**
     * Az eszkimó iglut épít a mezőre. Lehet sikeres/sikertelen, ezt a Field ellenőrzi, ha sikeres 1 munkapontba kerül.
     * Ha sikertelen a Game-et értesítjük egy hibaüzenettel.
     * @param f ezen a Field-en használjuk a képességet; muszáj az a mező legyen ahol a Karakter jelenleg áll
     */
    public void useAbility(Field f) {
        if(!f.equals(getField())) {
            gRef.showError("Az eszkimó nem a saját mezején akar iglut építeni");
            return;
        }

        boolean successfulChange = false;
        if(f.getBuilding() != null) {
            f.rmBuilding();
            successfulChange = true;
        }
        else
            successfulChange = f.addBuilding(new Igloo());
        if(successfulChange) {
            actionPoint--;
            if (actionPoint == 0)
                gRef.endTurn();
        }
        else
            gRef.showError("Az eszkimó nem tudott iglut építeni");
    }

}