package bg.sofia.uni.fmi.mjt.commands;

import bg.sofia.uni.fmi.mjt.database.DatabaseManager;
import bg.sofia.uni.fmi.mjt.exceptions.NoSuchElementException;
import bg.sofia.uni.fmi.mjt.foods.Foods;

import java.util.List;

public class GetFoodCommand extends Command {
    public static final String DELIMITER = " ";

    public GetFoodCommand(List<String> attributes) {
        super(attributes);
    }

    //to-do write documentation
    @Override
    public String execute() throws NoSuchElementException {
        String foodName = String.join(DELIMITER, getAttributes());

        Foods result = DatabaseManager.getInstance().getFoodsByName(foodName);

        return result.toString();
    }
}
