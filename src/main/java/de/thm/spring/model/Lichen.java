package de.thm.spring.model;

import org.ini4j.Wini;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Loads and serves the list of lichen for the gui from the file ARTENLISE.INI
 */
public class Lichen {
    private File initfile;

    private Map<String, List<String>> lichen;
    private static Lichen instance;

    /**
     * Returns a map of super-group as key and the species names as a list of Strings
     *
     * @return
     */
    public Map<String, List<String>> getLichen() {
        return lichen;
    }

    public static Lichen getInstance(){
        if(instance == null)
            instance = new Lichen();
        return instance;
    }

    private Lichen(){
        lichen = new TreeMap<>();

        if(System.getenv("HOME").contains("menzel")){
            initfile = new File("ARTENLISTE.csv");
        } else{
            initfile = new File("/home/mmnz21/ARTENLISTE.csv");
        }

        init(initfile);
    }

    /**
     * Load INI File
     * Old method for the old ini file
     * @param initfile
     */
    @Deprecated
    private void init_old(File initfile) {
        try {
            Wini ini  = new Wini(initfile);

            Set<String> sections = ini.keySet();

            for(String section: sections){

                if(!ini.get(section).containsKey("Anzahl"))
                    continue; // jump over "programm"

                int anzahl = Integer.parseInt(ini.get(section, "Anzahl"));
                List<String> art = new ArrayList<>();


                for(int i = 1; i <= anzahl; i++){
                    art.add(ini.get(section, Integer.toString(i)));
                }

                lichen.put(section, art);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void init(File initfile){

        try (BufferedReader br = new BufferedReader(new FileReader(initfile))) {

            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("#")) continue;
                String[] parts = line.split(",");

                String section = parts[1];

                if(!lichen.containsKey(section))
                    lichen.put(section, new ArrayList<>());

                if (parts.length < 3)
                    lichen.get(section).add(parts[0]);
                else
                    lichen.get(section).add(parts[2] + "," + parts[0]);
            }

        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            System.err.println("Error loading lichen csv file");
        }
    }
}
