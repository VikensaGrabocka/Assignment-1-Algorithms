import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter the path of the file: ");
        String path = input.nextLine();
        BigWeatherConfiguration weather = new BigWeatherConfiguration();
        weather.readFile(path);
        weather.computeStatistics();
    }
}
