package commands;

import storage.StudentsWishesStorage;

import java.util.*;

public class StudentsWishes {

    private StudentsWishesStorage storage;
    private final Map<String, ArrayList<String>> studentsWishes;
    private final Random RANDOM;
    private final Vault vault;

    public StudentsWishes(Vault vault, StudentsWishesStorage storage){
        this.storage = storage;
        this.studentsWishes = storage.getMap();
        this.RANDOM = new Random();
        this.vault = vault;
    }

    public String postWish(String arguments) {

        String[] argumentsArray = arguments.split(" " );
        String username = argumentsArray[0];
        String gift = argumentsArray[1];
        String client = argumentsArray[2];


        if(!vault.userExists(client)){
            return String.format("[ Student with username %s is not registered, so they have no permission to post wishes ] " ,client);
        }

        if (!vault.userIsLoggedIn(client)){
            return String.format("[ Student with username %s is not logged in, so they have no permission to post wishes ] " ,client);
        }

        if(!vault.userExists(username)){
            return String.format("[ Student with username %s is not registered ] " ,username);
        }

        if (!studentsWishes.containsKey(username)) {
            studentsWishes.put(username, new ArrayList<>());
        } else if (studentsWishes.get(username).contains(gift)) {
            return "[ The same gift for student " + username + " was already submitted ]";
        }

        studentsWishes.get(username).add(gift);

        return "[ Gift " + gift + " for student " + username + " submitted successfully ]";
    }

    public String getWish(String arguments) {
        String name = arguments.trim();
        List<String> students = new ArrayList<>(studentsWishes.keySet());
        List<String> studentsCopy = new ArrayList<>(students);
        studentsCopy.remove(name);
        if (studentsCopy.isEmpty()) {
            return "[ There are no other students present in the wish list]";
        }

        String randomStudent = studentsCopy.get(RANDOM.nextInt(studentsCopy.size()));
        String randomStudentWishes = studentsWishes.get(randomStudent).toString();
        studentsWishes.remove(randomStudent);

        return "[ " + randomStudent + ": " + randomStudentWishes + " ]";
    }
}
