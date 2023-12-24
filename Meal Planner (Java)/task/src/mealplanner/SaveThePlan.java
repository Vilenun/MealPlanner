package mealplanner;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.HashMap;
import java.util.Scanner;

public class SaveThePlan {
    static void save(Statement statement, Scanner sc, Connection connection) throws SQLException {
        DatabaseMetaData databaseMetaData = connection.getMetaData();
        HashMap<String,Integer> toBuy = new HashMap<>();
        ResultSet resultSet = databaseMetaData.getTables(null, null, "plan", new String[] {"TABLE"});
        boolean planReady = resultSet.next();
        String ingredient;
        resultSet.close();
        if (planReady) {
            System.out.println("Input a filename:");
            String fileName = sc.nextLine();
            File file = new File(fileName);
            ResultSet resultSet1 = statement.executeQuery("select ingredients.ingredient from plan " +
                    "left join ingredients  " +
                    "on plan.meal_id = ingredients.meal_id");
            while (resultSet1.next()){
                ingredient = resultSet1.getString("ingredient");
                if (toBuy.containsKey(ingredient)){
                    toBuy.put(ingredient,toBuy.get(ingredient)+1);
                } else toBuy.put(ingredient,1);
            }
            try (PrintWriter printWriter = new PrintWriter(file)) {
                toBuy.keySet().forEach(x-> printWriter.println(x+(toBuy.get(x)==1 ? "" : " x"+toBuy.get(x))));
            } catch (IOException e) {
                System.out.printf("An exception occurred %s", e.getMessage());
            }
            System.out.println("Saved!");

        } else System.out.println("Unable to save. Plan your meals first.");

    }
}
