package mealplanner;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.TreeMap;

public class PlanThePlan {
    static String[] week = new String[]{"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
    static TreeMap<String,Integer> breakfastMap=new TreeMap<>();
    static TreeMap<String,Integer> lunchMap=new TreeMap<>();
    static TreeMap<String,Integer> dinnerMap=new TreeMap<>();

    static void plan(Statement statement, Scanner sc) throws SQLException {
        statement.executeUpdate("DROP TABLE IF EXISTS plan");
        statement.executeUpdate("CREATE TABLE if not exists plan (" +
                "meal_option varchar(1024)," +
                "category varchar(1024)," +
                "day varchar(1024)," +
                "meal_id integer references meals(meal_id) NOT NULL)");

        ResultSet rs = statement.executeQuery("select * from meals " +
                "where meals.category = 'breakfast'" +
                "order by meal");
        while(rs.next()){
            breakfastMap.put(rs.getString("meal"),rs.getInt("meal_id"));
        }
        rs.close();
        ResultSet rs1 = statement.executeQuery("select * from meals " +
                "where meals.category = 'lunch'" +
                "order by meal");
        while(rs1.next()){
            lunchMap.put(rs1.getString("meal"),rs1.getInt("meal_id"));
        }
        rs1.close();
        ResultSet rs2 = statement.executeQuery("select * from meals " +
                "where meals.category = 'dinner'" +
                "order by meal");
        while(rs2.next()){
            dinnerMap.put(rs2.getString("meal"),rs2.getInt("meal_id"));
        }
        rs2.close();
        int point = 0;


        for (String day: week) {
            System.out.println(day);
            breakfastMap.keySet().forEach(System.out::println);
            System.out.printf("Choose the breakfast for %s from the list above:",day);

            boolean check = false;
            while (!check){
                String chosenBreakfast = sc.nextLine();
                if(breakfastMap.containsKey(chosenBreakfast)){
                    check=true;
                    statement.executeUpdate(String.format("insert into plan  values ('%s','%s', '%s', '%d')", chosenBreakfast,"breakfast",day,breakfastMap.get(chosenBreakfast)));
                } else System.out.println("This meal doesn’t exist. Choose a meal from the list above.");
            }

            lunchMap.keySet().forEach(System.out::println);
            System.out.printf("Choose the lunch for %s from the list above:",day);

            check = false;
            while (!check){
                String chosenLunch = sc.nextLine();
                if(lunchMap.containsKey(chosenLunch)){
                    check=true;
                    statement.executeUpdate(String.format("insert into plan  values ('%s','%s', '%s', '%d')", chosenLunch,"lunch",day,lunchMap.get(chosenLunch)));
                } else System.out.println("This meal doesn’t exist. Choose a meal from the list above.");
            }
            dinnerMap.keySet().forEach(System.out::println);
            System.out.printf("Choose the dinner for %s from the list above:",day);

            check = false;
            while (!check){
                String chosenDinner = sc.nextLine();
                if(dinnerMap.containsKey(chosenDinner)){
                    check=true;
                    statement.executeUpdate(String.format("insert into plan  values ('%s','%s', '%s', '%d')", chosenDinner,"dinner",day,dinnerMap.get(chosenDinner)));
                } else System.out.println("This meal doesn’t exist. Choose a meal from the list above.");
            }
            System.out.printf("Yeah! We planned the meals for %s.\n",day);

        }
        ResultSet rsPlan = statement.executeQuery("select * from plan ");
        for (String day: week) {
            System.out.println(day);
            rsPlan.next();
            System.out.println("Breakfast: " + rsPlan.getString("meal_option"));
            rsPlan.next();
            System.out.println("Lunch: " + rsPlan.getString("meal_option"));
            rsPlan.next();
            System.out.println("Dinner: " + rsPlan.getString("meal_option"));
            System.out.println();

        }


    }
}
