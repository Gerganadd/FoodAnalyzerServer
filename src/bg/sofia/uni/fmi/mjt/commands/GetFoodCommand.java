package bg.sofia.uni.fmi.mjt.commands;

import bg.sofia.uni.fmi.mjt.database.DatabaseManager;
import bg.sofia.uni.fmi.mjt.foods.Foods;

import java.util.List;

public class GetFoodCommand extends Command {
    public GetFoodCommand(List<String> attributes) {
        super(attributes);
    }

    //to-do write documentation
    @Override
    public String execute() {
        String foodName = String.join(" ", getAttributes());

        Foods result = DatabaseManager.getInstance().getFoodsByName(foodName);

        return result.toString();
    }
}
