package commands;

public class CommandExecutor {
    StudentsWishes studentsWishes;
    public CommandExecutor(StudentsWishes studentsWishes){

        this.studentsWishes = studentsWishes;
    }

    public String execute(String command, String arguments){
        String response = null;
        switch (command) {
            case "post-wish":
                response = studentsWishes.postWish(arguments);
                break;
            case "get-wish":
                response = studentsWishes.getWish(arguments);
                break;
            default:
                response = "[ Unknown command ]";
        }
        return response;
    }
}
