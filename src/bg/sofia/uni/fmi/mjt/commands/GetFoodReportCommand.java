package bg.sofia.uni.fmi.mjt.commands;

import bg.sofia.uni.fmi.mjt.database.DatabaseManager;
import bg.sofia.uni.fmi.mjt.exceptions.NoSuchElementException;
import bg.sofia.uni.fmi.mjt.foods.FoodReport;

import java.util.List;

public class GetFoodReportCommand extends Command {
    public GetFoodReportCommand(List<String> attributes) {
        super(attributes);
    }

    //to-do write documentation
    @Override
    public String execute() throws NoSuchElementException {
        long fdcId = Long.parseLong(getAttributes().getFirst());

        FoodReport report = DatabaseManager.getInstance().getFoodReportBy(fdcId);

        return report.toString();
    }
}
