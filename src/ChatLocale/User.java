package ChatLocale;

public class User {
    public String name;
    public ChatThread thread;

    public User(String name, ChatThread thread) {
        this.name = name;
        this.thread = thread;
    }
}