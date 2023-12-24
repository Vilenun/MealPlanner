package mealplanner;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class ShowPlan {
    static void show(int showPoint, Statement statement, Scanner sc) throws SQLException {
    boolean check = false;
    if(showPoint != 0){
        String showCategory = "";
        System.out.println("Which category do you want to print (breakfast, lunch, dinner)?");

        while (!check){
            showCategory = sc.nextLine();
            if (!(showCategory.equals("breakfast")||showCategory.equals("lunch")||showCategory.equals("dinner"))){
                System.out.println("Wrong meal category! Choose from: breakfast, lunch, dinner.");
            } else {
                check = true;
            }
        }


        if (!isEmpty(statement,showCategory)) {
            ResultSet rs1 = statement.executeQuery(String.format("select * from meals " +
                    "left join ingredients  " +
                    "on meals.meal_id = ingredients.meal_id where meals.category = '%s'",showCategory));
            System.out.printf("Category: %s\n",showCategory);
            while (rs1.next()) {

                if (showPoint != rs1.getInt("meal_id") || rs1.isFirst()) {
                    System.out.println("Name: " + rs1.getString("meal"));
                    System.out.println("Ingredients: " + rs1.getString("ingredient"));

                } else {
                    System.out.println(rs1.getString("ingredient"));

                }
                showPoint = rs1.getInt("meal_id");
            }
        } else System.out.println("No meals found.");
                        /*ResultSet rs1 = statement.executeQuery("select * from meals " +
                               "left join ingredients  " +
                                "on meals.meal_id = ingredients.meal_id");
                        while (rs1.next()){

                            if (showPoint != rs1.getInt("meal_id") || rs1.isFirst()) {
                                System.out.println("Category: " + rs1.getString("category"));
                                System.out.println("Name: " + rs1.getString("meal"));
                                System.out.println("Ingredients: " + rs1.getString("ingredient"));

                            }else {
                                System.out.println(rs1.getString("ingredient"));

                            }
                            showPoint = rs1.getInt("meal_id");
                        }*/
    } else System.out.println("No meals saved. Add a meal first.");

    }
    public static boolean isEmpty(Statement statement, String cat) {
        try {
            int numberOfRow;
            ResultSet checkIsEmpty = statement.executeQuery(String.format("SELECT COUNT(*) FROM meals where category ='%s'",cat));
            checkIsEmpty.next();
            numberOfRow = checkIsEmpty.getInt(1);
            checkIsEmpty.close();
            return numberOfRow == 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

