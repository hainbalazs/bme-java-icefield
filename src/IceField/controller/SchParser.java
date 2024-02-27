package IceField.controller;

import IceField.model.Characters.*;
import IceField.model.Characters.Character;
import IceField.model.Fields.*;
import IceField.model.Game;
import IceField.model.Tools.*;

import java.io.*;
import java.util.ArrayList;

/**
 * Ez az osztály felel a prototípusban a beérkező üzenetek helyes feldolgozásáért,
 * illetve a megfelelő folyamatok elindításáért.
 * Továbbá előre megadott tesztesetek ellenőrzésére is lehetőséget ad, futtatás után pontosan
 * meg lehet határozni vele, hogy melyik objetum/változó vett fel hibás értéket.
 * Kívülről a getInput metódusán lehet elérni őt, illetve a print és chooserescued metódusokon
 * keresztül, amiket a game hívhat meg rajta, ha interakcióba akar lépni a külvilággal,
 * a többi metódusa privát.
 */

public class SchParser {
    /**
     * Ha a futtatást egy fájlba is ki szeretnénk írni, akkor azt
     * majd ez a PrintWriter fogja kezelni az összes kiírást használó
     * metódusban.
     */
    private PrintWriter pw;
    /**
     * Ez az objektum csak akkor aktív, ha előre megadott tesztesetet vizsgálunk
     * Ide töltjük be azt a fájlt, ami a helyes kimeneteket tartalmazza
     */
    private BufferedReader test_cmp;

    /**
     * Ez az objektum csak akkor aktív, ha előre megadott tesztesetet vizsgálunk
     * Innen töltjük be a bemeneteket tartalmazó fájlt
     */
    private BufferedReader test_read;
    /**
     * Egészen addig igaz értéket vesz fel, amíg vagy vége van az előre megadott
     * teszteset ellenőrzésének, vagy a játék be nem fejeződött "freeplay" módban
     */
    boolean gameon = true;
    /**
     * Ez a boolean jelzi, hogy lett-e már megadva kimeneti fájl
     */
    boolean ofile = false;
    /**
     * Ez a boolean akkor vált true-ra, ha beállítjuk, hogy előre megadott tesztesetet
     * futtassunk, hiszen ilyenkor külön ellenőrző lépésekre is szükség van
     */
    boolean test_mode = false;
    /**
     * Ez a Game objektum tartalmazza az éppen aktív játékot.
     * Rajta fogunk dolgozni az egyes parancsok kapcsán.
     */
    Game g = null;
    /**
     * Ez a gyűjtemény fogja tartalmazni az előre megadott tesztesetek futtatása során
     * keletkezett hibaüzeneteket, amiket a végén majd konzolra kiírunk
     */
    ArrayList<String> errors;
    /**
     * Mivel a kimeneti adatok összehasonlítása az előre megadott fájllal nem csak egy
     * helyen történik, ezért osztály szinten kell számon tartani hogy a fájl melyik sorában tartunk
     */
    Integer current_line;

    /**
     * Az SchParser külvilág felé elérhető egyetlen metódusa
     * Ez indítja be az osztály működését, ő az aki folyamatosan vár inputot
     * a konzolról.
     * Ha az megérkezett, akkor átadja a kapott sort a forwarder metódusnak, aki majd
     * tudni fogja, hogy az adott sor kapcsán milyen feldolgozó metódust kell meghívni.
     * Ha vége van a játéknak, akkor bezárja a kimeneti fájlt, ha volt
     */
    public void getInput() {
        while (gameon) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String line = "";
            try {
                line = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            forwarder(line);
        }
        if(ofile)
            pw.close();
    }

    /**
     * Ez a metódus veszi a getInputtól kapott sort, feldarabolja a space-ek mentén,
     * veszi az első "komponenst", ami nyilván mindig a parancs típusát határozza meg, és
     * az adott parancsnak megfelelő metódus felé továbbítja a már feldarabolt sort.
     * @param line a getInput által továbbított sor
     */
    private void forwarder(String line)
    {
        String parts[] = line.split("[ ]");
        switch (parts[0]) {
            case "test":
                test(parts);
                break;
            case "conf":
                conf(parts);
                break;
            case "create_game":
                create_game(parts);
                break;
            case "create_field":
                create_field(parts);
                break;
            case "create_char":
                create_char(parts);
                break;
            case "addstdtool":
                addstdtool(parts);
                break;
            case "addktool":
                addktool(parts);
                break;
            case "drowning":
                drowning(parts);
                break;
            case "add_neighbour":
                add_neighbour(parts);
                break;
            case "move":
                move(parts);
                break;
            case "tool":
                tool(parts);
                break;
            case "ability":
                ability(parts);
                break;
            case "dump_all":
                dump_all(parts);
                break;
            case "char_info":
                char_info(parts);
                break;
            case "field_info":
                field_info(parts);
                break;
            case "exit":
                exit();
                break;
            case "skip":
                skip();
                break;
            case "obtain":
                obtain();
                break;
            case "dig":
                dig();
                break;
            default:
                break;
        }
    }

    /**
     * Megvizsgáljuk, hogy éppen ki van körön, és a kapott
     * FriendlyCharacter-t megkérjük, hogy dig-eljen a Fieldjén
     */
    private void dig() {
        FriendlyCharacter fc = g.getActiveChar();
        fc.digWithHands();
    }

    /**
     * Megvizsgáljuk, hogy éppen ki van körön, és a kapott
     * FriendlyCharacter-t megkérjük, hogy obtain-eljen a Fieldjéről
     */
    private void obtain() {
        FriendlyCharacter fc = g.getActiveChar();
        fc.obtainTool();
    }

    /**
     * A skip-pel ekvivalens metódus létezik a Game-ben, az endTurn, ezért
     * csak ezt kell itt meghívni
     */
    private void skip() {
        g.endTurn();
    }


    /**
     * Mivel rengeteg metódusban ugyanúgy kell felbontani a parancs paramétereit, ezért
     * itt egységesítettem ezt a lekérdezést:
     * Felbontjuk az adott indexedik komponenst az egyenlőségjel mentén,
     * és a két részlet közül az egyenlőségjel utánit adjuk vissza
     * @param parts A (komponensekre bontott) sort tároló String-tömb
     * @param ind Hányadik komponenst akarjuk lekérdezni
     * @return Az adott komponenshez tartozó érték
     */
    private String get_relevant(String[] parts, Integer ind) {
        String[] str_arr = parts[ind].split("[=]");
        return str_arr[1];
    }

    /**
     * Itt még Integer-ré is parseoljuk a följebb ismertetett metódus visszatérési értékét,
     * hiszen sokszor az kell String helyett
     * @param parts A (komponensekre bontott) sort tároló String-tömb
     * @param ind Hányadik komponenst akarjuk lekérdezni
     * @return Az adott komponenshez tartozó érték
     */
    private Integer get_relevant_int(String[] parts, Integer ind) {
        return Integer.parseInt(get_relevant(parts, ind));
    }

    /**
     * Jelezzük a Parser felé, hogy álljon le a ciklussal, kilépünk a porgramból
     */
    private void exit()
    {
        gameon = false;
    }

    /**
     *Ez a metódus felel az előre megírt tesztesetek futtatásáért
     * Kezdetnek a test_mode flag-et true-ra kell állítani, hogy a kiírást kezelő
     * metódusok tudják, hogy majd elemezni is kell a kiírtakat
     * A paraméterként megkapott számot konkatenáljuk a fájlok helyéhez/nevéhez
     * Miután végigmentünk a bementi fájlon, kiírjuk a menet közben összegyűjtött hibákat
     * Ha nincs hiba, akkor örülünk
     * @param parts A (komponensekre bontott) sort tároló String-tömb
     */
    private void test(String[] parts)
    {
        test_mode = true;
        current_line = 0;
        Integer id = Integer.parseInt(parts[1]);
        String dir = "docs/test/";
        String infilename = dir + "test_" + ((id < 10) ? "0" + id : id) + "_in.txt";
        String cmpfilename = dir + "test_" + ((id < 10) ? "0" + id : id) + "_out.txt";
        errors = new ArrayList<String>();
        try {
            test_cmp = new BufferedReader(new FileReader(cmpfilename));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            String line;
            test_read = new BufferedReader(new FileReader(infilename));
            while ((line = test_read.readLine()) != null) {
                forwarder(line);
            }
            if(errors.size() == 0)
                System.out.println("Sikeres futtatás");
            else
                for(String s : errors)
                {
                    System.out.println(s);
                }
            test_cmp.close();
            test_read.close();
            //gameon = false;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Ez a metódus figyeli, hogy helyes adatokkal lett-e kiírva az adott
     * field mezeje.
     * A kapott stringeket hasonlítja össze úgy, hogy felbontja őket a > mentén,
     * majd mivel tudjuk a field_info adatainak sorrendjét, ezért az egyes komponenseken
     * elég végigmenni egyszerre, és ha találunk eltérést, akkor rögtön meg is tudjuk
     * állapítani, hogy melyik komponens a hibás, és ezt el is mentjük az errors-ba
     * @param chosen Ami kimenetet kaptunk
     * @param cmpline Az előre megadott kimenet
     */
    private void field_cmp(String chosen, String cmpline)
    {
        String[] cmp_arr = cmpline.split("[>]");
        String[] org_arr = chosen.split("[>]");
        for(Integer i = 0; i < cmp_arr.length && i < org_arr.length; i++)
        {
            if(!cmp_arr[i].equals(org_arr[i]))
            {
                String error = "A(z)" + current_line + ". sorban a ";
                if(i == 0)
                    error += "field_id ";
                else if(i == 1)
                    error += "capacity explored ";
                else if(i == 2)
                    error += "capacity ";
                else if(i == 3)
                    error += "standing_on ";
                else if(i == 4)
                    error += "neighbours ";
                else if(i == 5)
                    error += "frozen_tool ";
                else if(i == 6)
                    error += "building ";
                else if(i == 7)
                    error += "snow_level ";
                error += "erteke " + cmp_arr[i].replace("<", "") + " kene hogy legyen, " +
                        "de helyette " + org_arr[i].replace("<", "");
                errors.add(error);
            }
        }
    }

    /**
     *Ez a metódus írja ki a paraméterként kapott id-jú mező adatait
     *Formázni nem kell semmit, azt a showField intézi már automatikusan
     * Ha tesztelünk, akkor az elvárt kimenetet tartalmazó fájl következő
     * sorát beolvassuk, és azt összehasonlítjuk a kapott sorral
     * @param parts A (komponensekre bontott) sort tároló String-tömb
     */
    private void field_info(String[] parts) {
        Integer id = get_relevant_int(parts, 1);
        String chosen = g.showField(id);
        System.out.println(chosen);
        if(ofile)
            pw.println(chosen);
        if(test_mode)
        {
            try {
                current_line++;
                String cmpline = test_cmp.readLine();
                field_cmp(chosen, cmpline);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * Ez a metódus figyeli, hogy helyes adatokkal lett-e kiírva az adott
     * bear mezeje.
     * A kapott stringeket hasonlítja össze úgy, hogy felbontja őket a > mentén,
     * majd mivel tudjuk a field_info adatainak sorrendjét, ezért az egyes komponenseken
     * elég végigmenni egyszerre, és ha találunk eltérést, akkor rögtön meg is tudjuk
     * állapítani, hogy melyik komponens a hibás, és ezt el is mentjük az errors-ba
     * @param chosen Ami kimenetet kaptunk
     * @param cmpline Az előre megadott kimenet
     */
    private void bear_cmp(String chosen, String cmpline) {
        String[] cmp_arr = cmpline.split("[>]");
        String[] org_arr = chosen.split("[>]");
        for(Integer i = 0; i < cmp_arr.length; i++)
        {
            if(!cmp_arr[i].equals(org_arr[i]))
            {
                String error = "A(z) " + current_line + ". sorban a ";
                if(i == 0)
                    error += "char_id ";
                else if(i == 1)
                    error += "field_id ";
                else if(i == 2)
                    error += "type ";
                error += "erteke " + cmp_arr[i].replace("<", "") + " kene hogy legyen, " +
                        "de helyette " + org_arr[i].replace("<", "");
                errors.add(error);
            }
        }
    }
    /**
     * Ez a metódus figyeli, hogy helyes adatokkal lett-e kiírva az adott
     * character mezeje.
     * A kapott stringeket hasonlítja össze úgy, hogy felbontja őket a > mentén,
     * majd mivel tudjuk a field_info adatainak sorrendjét, ezért az egyes komponenseken
     * elég végigmenni egyszerre, és ha találunk eltérést, akkor rögtön meg is tudjuk
     * állapítani, hogy melyik komponens a hibás, és ezt el is mentjük az errors-ba
     * @param chosen Ami kimenetet kaptunk
     * @param cmpline Az előre megadott kimenet
     */
    private void char_cmp(String chosen, String cmpline)
    {
        String[] cmp_arr = cmpline.split("[>]");
        String[] org_arr = chosen.split("[>]");
        for(Integer i = 0; i < cmp_arr.length; i++)
        {
            if(!cmp_arr[i].equals(org_arr[i]))
            {
                String error = "A(z) " + current_line + ". sorban a ";
                //<char_id><field_id><health><actionpoints><tools [..]><char_type>
                if(i == 0)
                    error += "char_id ";
                else if(i == 1)
                    error += "field_id ";
                else if(i == 2)
                    error += "health ";
                else if(i == 3)
                    error += "actionpoints ";
                else if(i == 4)
                    error += "tools ";
                else if(i == 5)
                    error += "char_type ";
                error += "erteke " + cmp_arr[i].replace("<", "") + " kene hogy legyen, " +
                        "de helyette " + org_arr[i].replace("<", "");
                errors.add(error);
            }
        }
    }

    /**
     *Ez a metódus írja ki a paraméterként kapott id-jú character adatait
     *Formázni nem kell semmit, azt a showField intézi már automatikusan
     * Ha tesztelünk, akkor az elvárt kimenetet tartalmazó fájl következő
     * sorát beolvassuk, és azt összehasonlítjuk a kapott sorral
     * @param parts A (komponensekre bontott) sort tároló String-tömb
     */
    private void char_info(String[] parts) {
        Integer id = get_relevant_int(parts, 1);
        String chosen = g.showChar(id);
        System.out.println(chosen);
        if(ofile)
            pw.println(chosen);
        if(test_mode)
        {
            try {
                current_line++;
                String cmpline = test_cmp.readLine();
                if(chosen.split("[>]").length == 3)
                    bear_cmp(chosen, cmpline);
                else if(chosen.split("[>]").length == 6)
                    char_cmp(chosen, cmpline);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Itt kerül lekérdezésre, hogy melyik játékost mentsük meg a rope-pal.
     * "Free play" módban konzolról, előre megírt teszteset futtatása során az adott fájlból
     * olvassa be a választásunkat
     * @return a választott játékos id-ja
     */
    public Integer chooseRescued()
    {
        Integer rescued = -1;
        if(!test_mode)
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            try {
                String rescued_str = reader.readLine();
                rescued = Integer.parseInt(rescued_str);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            try {
                String rescued_str = test_read.readLine();
                rescued = Integer.parseInt(rescued_str);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return rescued;
    }

    /**
     * Ha a Game valamit ki szeretne írni, akkor azt ezen a metóduson keresztül
     * teheti meg. Azért így, hogy rögtön lekezelésre kerüljön az előre megírt
     * tesztesetet tartalmazó fájllal való összehasonlítás
     * @param printed ez a String kerül kiírásra
     */
    public void print(String printed)
    {
        System.out.println(printed);
        if(test_mode)
        {
            current_line++;
            String cmpline = "";
            try {
                cmpline = test_cmp.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(!cmpline.equals(printed))
            {
                String error = "A kovetkezo info uzenetet kellett volna kiirni: " + cmpline +
                                " helyette ez lett: " + printed;
                errors.add(error);
            }
        }
    }

    /**
     *A játékról ismert összes adat kiírását végzi ez a metódus
     * Mivel itt is elvégzésre kerül kiírás, így itt is kell összehasonlítgatni
     * az elvárt bemenetet tartalmazó fájllal
     * Az első hat sor összehasonlítása triviális, utána alapból nem lenne egyértelmű,
     * hogy pl éppen character vagy medve formátumú adat kerül kiírásra, de mivel ezek
     * más számú komponensből állnak, ezért ennek meghatározásával már könnyedén lehet továbbítani
     * a sorokat a megfelelő összehasonlító metódushoz
     * @param parts A (komponensekre bontott) sort tároló String-tömb
     */
    private void dump_all(String[] parts) {
        String everything = g.show();
        System.out.println(everything);
        if(ofile)
            pw.println(g.show());
        if(test_mode)
        {
            /*
            Mezők száma
            Játékosok száma [s]
            Soron következő játékos sorszáma [char_id]
            Vihar ennyi kör múlva várható: [t]
            A játék még tart / A játék végetért [c/w/l]
            A vízben lévő játékosok kilistázása <char [..]>
            minden karakterre: char_info
            minden mezőre: field_info
            */

            String[] split = everything.split("[\n]");
            for(int i = 0; i < split.length; i++)
            {
                try {
                    current_line++;
                    String cmpline = test_cmp.readLine();
                    if(!split[i].equals(cmpline))
                    {
                        if(i < 5)
                        {
                            String error = "";
                            error += "A(z)" + current_line + ". sorban a ";
                            if(i == 0)
                                error += "mezok szamanak ";
                            if(i == 1)
                                error += "jatekosok szamanak ";
                            else if(i == 2)
                                error += "soron kovetkezo jatekos sorszamanak ";
                            else if(i == 3)
                                error += "vihar varhato idejenek ";
                            else
                                error += "a jatek allapotanak ";
                            error += "erteke " + cmpline + " kene hogy legyen, " +
                                    " de helyette " + split[i];
                            errors.add(error);
                        }
                        else if (i == 5)
                        {
                            String error = "";
                            error += "A(z)" + current_line + ". sorban a vizben levo jatekosok listaja "
                                    + cmpline.substring(1, cmpline.length()-1) + " kene, hogy legyen, de helyette "
                                    + split[i].substring(1, split[i].length()-1);
                            errors.add(error);
                        }
                        else
                        {
                            if(split[i].split("[>]").length == 6)
                                char_cmp(split[i], cmpline);
                            else if(split[i].split("[>]").length == 8)
                                field_cmp(split[i], cmpline);
                            else if(split[i].split("[>]").length == 3)
                                bear_cmp(split[i], cmpline);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     *Lekérdezzük, hogy melyik mezőn akarjuk a képességet használni, majd
     *végrehajtjuk azt a FriendlyCharacter useAbility metódusán keresztül
     * @param parts A (komponensekre bontott) sort tároló String-tömb
     */
    private void ability(String[] parts) {
        Integer id = get_relevant_int(parts, 1);
        Field chosen = g.getField(id);
        FriendlyCharacter active = g.getActiveChar();
        active.useAbility(chosen);
    }

    /**
     *Lekérdezzük, hogy melyik Tool-t akarjuk használni, majd
     *használjuk azt a Tool useTool metódusán keresztül
     * @param parts A (komponensekre bontott) sort tároló String-tömb
     */
    private void tool(String[] parts) {
        Integer id = get_relevant_int(parts, 1);
        FriendlyCharacter active = g.getActiveChar();
        Tool chosen = active.getToolAt(id);
        active.useTool(chosen);
    }

    /**
     *Lekérdezzük, hogy melyik Field-re akarunk lépni, majd
     *rálépünk a Field stepOn metódusán keresztül
     * @param parts A (komponensekre bontott) sort tároló String-tömb
     */
    private void move(String[] parts) {
        String[] id_str = parts[1].split("[=]");
        Integer id = Integer.parseInt(id_str[1]);
        FriendlyCharacter active = g.getActiveChar();
        Field next = g.getField(id);
        active.stepOn(next);
    }

    /**
     * Ez a metódus visszaad az adott paramétereknek megfelelő Tool-t
     * @param tool_cat Lehet "k", "s" vagy más, ha "k", akkor valamilyen KeyTool-t várunk, ha "s", akkor StandardTool-t
     *                 Minden más esetben null-t adunk vissza
     * @param tool_type Keytool esetén egy 0-2 szám, StandardToolnál pedig a Tool neve
     * @param durability Ez csak a FragileShovel esetén releváns, annak a tartósságát adja meg
     * @return a paramétereknek megfelelő Tool
     */
    private Tool tool_returner(String tool_cat, String tool_type, Integer durability) {
        if(tool_cat.equals("k"))
            return new KeyTool(Integer.parseInt(tool_type), g);
        else if(tool_cat.equals("s"))
        {
            if(tool_type.equals("shovel"))
            {
                return new Shovel(g);
            }
            else if(tool_type.equals("fshovel"))
            {
                return new FragileShovel(g, durability);
            }
            else if(tool_type.equals("food"))
            {
                return new Food(g);
            }
            else if(tool_type.equals("dgear"))
            {
                return new DivingGear(g);
            }
            else if(tool_type.equals("tent"))
            {
                return new TentItem(g);
            }
            else if(tool_type.equals("rope"))
            {
                return new Rope(g);
            }
        }
        return null;
    }

    /**
     * Inicializál nekünk egy Field-et, aki a capacity-ja alapján lehet
     * DangerousField vagy StableField, mivel azt nem ellenőrizzük, hogy már használatban
     * levő id-val hozzuk-e létre az adott Field-et, ezért kiemelten kell figyelni
     * arra, hogy mindig különbözőt adjunk meg neki. A különböző if ágakban lényegében
     * ugyanaz történik: Létrehozunk egy Field objektumot a már lekérdezett building-gel és
     * tool-lal, majd hozzáadjuk a játékhoz a field-et
     * @param parts A (komponensekre bontott) sort tároló String-tömb
     */
    private void create_field(String[] parts) {
        Integer id = get_relevant_int(parts, 1);
        Integer capacity = get_relevant_int(parts, 2);
        Integer snow = get_relevant_int(parts, 3);
        Integer expl_int = get_relevant_int(parts, 4);
        Boolean expl = expl_int == 1 ? true : false;
        String building = get_relevant(parts, 5);
        String tool_cat = get_relevant(parts, 6);
        String tool_type = get_relevant(parts, 7);
        Integer durability = get_relevant_int(parts, 8);
        Building b;
        if(building.equals("igloo"))
            b = new Igloo();
        else if(building.equals("tent"))
            b = new DeployedTent();
        else
            b = null;
        Tool t = tool_returner(tool_cat, tool_type, durability);
        if(capacity == -1)
        {
            StableField sf = new StableField(id, capacity, snow, expl, b, t);
            g.addField(sf);
        }
        else
        {
            DangerousField df = new DangerousField(id, capacity, snow, expl, b, t);
            g.addField(df);
        }
    }

    /**
     *Lekérdezzük, hogy melyik két Field-et akarjuk szomszéddá tenni, majd
     * szomszédosítjuk őket a Game addneighbour metódusán keresztül
     * @param parts A (komponensekre bontott) sort tároló String-tömb
     */
    private void add_neighbour(String[] parts) {
        Integer id1 = get_relevant_int(parts, 1);
        Integer id2 = get_relevant_int(parts, 2);
        g.addNeighbour(id1, id2);
    }

    /**
     *Lekérdezzük, hogy ki fuldoklik, és mennyi ideje van hátra, majd ezt jelezzük
     * a Game-nek a drowning metódusán keresztül
     * @param parts A (komponensekre bontott) sort tároló String-tömb
     */
    private void drowning(String[] parts) {
        Integer char_id = get_relevant_int(parts, 1);
        Integer timeleft = get_relevant_int(parts, 2);
        g.drowning(char_id, timeleft);
    }

    /**
     *Lekérdezzük a paramtérként kapott character id-t és tool-típust,
     *majd a type-nak megfelelő típusú keytool-t létrehozunk a
     *Game-ben a megadott karakternél
     * @param parts A (komponensekre bontott) sort tároló String-tömb
     */
    private void addktool(String[] parts) {
        Integer loc_id = get_relevant_int(parts, 1);
        Integer type = get_relevant_int(parts, 2);
        g.addktool(new KeyTool(type, g), loc_id);
    }

    /**
     * Lekérdezzük a paramtérként kapott character id-t, tool-típust, illetve
     * eszköz-tartósságot, majd a type-nak megfelelő típusú standardtool-t létrehozunk a
     * Game-ben a megadott karakternél
     * @param parts A (komponensekre bontott) sort tároló String-tömb
     */
    private void addstdtool(String[] parts) {
        Integer loc_id = get_relevant_int(parts, 1);
        Integer durability = get_relevant_int(parts, 2);
        String type = get_relevant(parts, 3);
        if(type.equals("shovel"))
        {
            g.addstdtool(new Shovel(g), loc_id);
        }
        else if(type.equals("rope"))
        {
            g.addstdtool(new Rope(g), loc_id);
        }
        else if(type.equals("food"))
        {
            g.addstdtool(new Food(g), loc_id);
        }
        else if(type.equals("fshovel"))
        {
            g.addstdtool(new FragileShovel(g, durability), loc_id);
        }
        else if(type.equals("tent"))
        {
            g.addstdtool(new TentItem(g), loc_id);
        }
        else if(type.equals("dgear"))
        {
            g.addstdtool(new DivingGear(g), loc_id);
        }
    }

    /**
     * Inicializál nekünk egy karaktert, aki a type-ja alapján lehet
     * Eskimo Explorer vagy Bear, mivel azt nem ellenőrizzük, hogy már használatban
     * levő id-val hozzuk-e létre az adott Charactert, ezért kiemelten kell figyelni
     * arra, hogy mindig különbözőt adjunk meg neki. A különböző if ágakban lényegében
     * ugyanaz történik: Létrehozunk egy Character objektumot, hozzáadjuk a játékhoz,
     * majd a megadott Fieldhez.
     * @param parts A (komponensekre bontott) sort tároló String-tömb
     */
    private void create_char(String[] parts) {
        Integer id = get_relevant_int(parts, 1);
        String type = get_relevant(parts, 2);
        Integer health = get_relevant_int(parts, 3);
        Integer field_id = get_relevant_int(parts, 4);
        Field f = g.getField(field_id);
        if(type.equals("eskimo"))
        {
            Eskimo e = new Eskimo(id, g, health);
            g.addChar(e);
            e.initField(f);
        }
        else if(type.equals("explorer"))
        {
            Explorer e = new Explorer(id, g, health);
            g.addChar(e);
            e.initField(f);
        }
        else if(type.equals("bear"))
        {
            Bear b = new Bear(id, g);
            g.addBear(b);
            b.initField(f);
        }
    }

    /**
     * Ő inicializálja az osztály szintű Game objektumunkat, a parts-tól
     * megkapott blizzard-mérő illetve random érték szerint
     * @param parts A (komponensekre bontott) sort tároló String-tömb
     */
    private void create_game(String[] parts) {
        Integer blizzard = get_relevant_int(parts, 1);
        Integer rand = get_relevant_int(parts, 2);
        Boolean random = rand == 1 ? true : false;
        g = new Game(blizzard, random, this);
    }

    /**
     * Ha "freeplay" módban akarunk fájlból vagy fájlba dolgozni, akkor azt ez a metódus
     * állítja be.
     * Ha o-t kapunk az io paraméterre, akkor inicializáljuk az osztály szintű PrintWriter-ünket,
     * ha i-t kapunk, akkor rögtön el is kezdjük feldolgozni a fájlt, amihez itt is tökéletesen
     * működőképes a sorokat váró forwarder metódus
     * @param parts A (komponensekre bontott) sort tároló String-tömb
     */
    private void conf(String[] parts)
    {
        String in_out = get_relevant(parts, 1);
        String file = get_relevant(parts, 2);
        if(in_out.equals("0"))
        {
            ofile = true;
            FileWriter fw = null;
            try {
                fw = new FileWriter(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(pw != null)
                pw.close();
            pw = new PrintWriter(fw);
        }
        else
        {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    forwarder(line);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
