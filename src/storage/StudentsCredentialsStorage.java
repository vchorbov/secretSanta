package storage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class StudentsCredentialsStorage implements Serializable {
    private Map<String, ArrayList<String>> vault;
    private static final Gson gson = new Gson();
    private final String FILE_LOCATION = "credentialsDB";

    /**
     * The StudentsCredentialStorage can be instantiated by injecting
     * @vault - Map as a dependency.
     * The @vault contain Students represented by usernames and passwords.
     * The List contains the password corresponding to the key (username) at position 0,
     * and a key word (true/false) which is used for identifying if the user is logged in
     * or not.
     */
    public StudentsCredentialsStorage(Map<String, ArrayList<String>> vault) {
        this.vault = vault;
    }

    /**
     * The StudentsCredentialStorage can be instantiated by passing
     * @param location , which is the place where the added students'
     * credentials from previous interactions with the server
     * were saved. A state is being created.
     *
     */

    public StudentsCredentialsStorage(String location) {
        String json = readJsonFromFile(location);
        this.vault = jsonToMap(json);
    }


    /**
     * This constructor creates an instance with an empty map,
     * which is yet to be filled and modified.
     */
    public StudentsCredentialsStorage(){
        this.vault = new HashMap<>();
    }

    public Map<String, ArrayList<String>> getMap(){
        return this.vault;
    }
    public void saveCredentials(){
        String json = mapTOJson(vault);
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

    private Map<String, ArrayList<String>> jsonToMap(String json){
        Map<String, ArrayList<String>> retrievedMap;
        retrievedMap = gson.fromJson(json, Map.class);
        return retrievedMap;
    }

    private String mapTOJson(Map<String, ArrayList<String>> vault){
        Type gsonType = new TypeToken<HashMap>(){}.getType();
        String jsonString = gson.toJson(vault, gsonType);
        return jsonString;
    }
}

