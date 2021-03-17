package storage;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class StudentsWishesStorage implements Serializable {
    private Map<String, ArrayList<String>> studentsWishes;
    private static final Gson gson = new Gson();
    private final String FILE_LOCATION = "wishesDB";


    /**
     * @param studentsWishes
     */
    public StudentsWishesStorage(Map<String, ArrayList<String>> studentsWishes) {
        this.studentsWishes = studentsWishes;

    }

    /**
     * @param location
     */
    public StudentsWishesStorage(String location) {
        String json = readJsonFromFile(location);
        this.studentsWishes = jsonToMap(json);
    }

    /**
     *
     */
    public StudentsWishesStorage() {
        this.studentsWishes = new HashMap<>();
    }

    public Map<String, ArrayList<String>> getMap() {
        return this.studentsWishes;
    }
    public void saveStudents(){
        String json = mapTOJson(studentsWishes);
        try(Writer writer = new FileWriter(FILE_LOCATION)) {
            writer.write(json);
            writer.flush();
        } catch (IOException e) {
            System.err.println("A problem occurred, while saving the data from the vault in the DB.");
            e.printStackTrace();
        }
    }

    private String readJsonFromFile(String location) {
        try (BufferedReader reader = new BufferedReader(new FileReader(location))) {
            String line;
            StringBuilder builder = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            return builder.toString();

        } catch (FileNotFoundException e) {
            System.err.println("The file where the data about the students is being stored can not be found.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("The procedure of reading from the storage file was not successful.");
            e.printStackTrace();
        }
        return "";
    }

    private Map<String, ArrayList<String>> jsonToMap(String json) {
        Map<String, ArrayList<String>> retrievedMap;
        retrievedMap = gson.fromJson(json, Map.class);
        return retrievedMap;
    }

    private String mapTOJson(Map<String, ArrayList<String>> studentsWishes) {
        Type gsonType = new TypeToken<HashMap>() {}.getType();
        String jsonString = gson.toJson(studentsWishes, gsonType);
        return jsonString;
    }


}
