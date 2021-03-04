package storage;

import java.util.*;

public class StudentsWishes {

    private final Map<String, Set<String>> studentsWishes;
    private final Random RANDOM;
    private final Vault vault;

    public StudentsWishes(Vault vault){
        this.studentsWishes = new HashMap<>();
        this.RANDOM = new Random();
        this.vault = vault;
    }

    public String postWish(String arguments) {

        String[] argumentsArray = arguments.split(" " );
        String username = argumentsArray[0];
        String gift = argumentsArray[1];
        String logged = argumentsArray[2];

        if(logged.equals("f")){
            return "[ You are not logged in ]";
        }
        if(!vault.userExists(username)){
            return String.format("[ Student with username %s is not registered ] " ,username);
        }


        if (!studentsWishes.containsKey(username)) {
            studentsWishes.put(username, new HashSet<>());
        } else if (studentsWishes.get(username).contains(gift)) {
            return "[ The same gift for student " + username + " was already submitted ]";
        }

        studentsWishes.get(username).add(gift);

        return "[ Gift " + gift + " for student " + username + " submitted successfully ]";
    }

    public String getWish() {
        if (studentsWishes.isEmpty()) {
            return "[ There are no students present in the wish list ]";
        }

        List<String> students = new ArrayList<>(studentsWishes.keySet());
        String randomStudent = students.get(RANDOM.nextInt(students.size()));
        String randomStudentWishes = studentsWishes.get(randomStudent).toString();
        studentsWishes.remove(randomStudent);

        return "[ " + randomStudent + ": " + randomStudentWishes + " ]";
    }
}
