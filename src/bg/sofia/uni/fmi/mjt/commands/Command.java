package bg.sofia.uni.fmi.mjt.commands;

import bg.sofia.uni.fmi.mjt.exceptions.NoSuchElementException;

import java.util.Collections;
import java.util.List;

public abstract class Command {
    private final List<String> attributes;

    protected Command(List<String> attributes) {
        this.attributes = attributes;
    }

    public List<String> getAttributes() {
        return Collections.unmodifiableList(attributes);
    }

    public abstract String execute() throws NoSuchElementException;
}
