package bg.sofia.uni.fmi.mjt.commands;

import bg.sofia.uni.fmi.mjt.exceptions.NoSuchElementException;

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
    public abstract String execute() throws NoSuchElementException;
}
