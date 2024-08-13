package bg.sofia.uni.fmi.mjt.commands;

import java.util.List;

public abstract class Command {
    private final List<String> attributes;

    protected Command(List<String> attributes) {
        this.attributes = attributes;
    }

    public List<String> getAttributes() {
        return attributes; // to-do make it immutable
    }

    //to-do write documentation
    public abstract String execute();
}
