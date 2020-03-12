import java.util.Scanner;

public class Main {

    // Runs a text interface in the console
    public static void main(String[] args) {
        String inputString = "";

        while (!inputString.equals("q")) {
            System.out.println("Velkommen til vår IMDB, vennligst velg et av alternative under: ");
            System.out.println("a: Finne navnet på alle rollene en gitt skuespiller har ");
            System.out.println("b: Finne hvilke filmer som en gitt skuespiller opptrer i ");
            System.out.println("c: Finne hvilke filmselskap som lager flest filmer inne hver sjanger ");
            System.out.println("d: Sette inn en ny film ");
            System.out.println("e: Sette inn ny anmeldelse av en episode av en serie ");
            System.out.println("q: Exit programmet");

            Scanner lineScanner = new Scanner(System.in);
            inputString = lineScanner.next();

        }



    }

    // Validates strings containing numbers, integers and underscores
    public static boolean validateStringInput(String s) {
        return s.matches("^[a-zA-Z0-9_.-]+$");
    }

    // Validates all integers greater than zero
    public static boolean validateIntegerInput(String s) {
        return (s.matches("^[0-9]+$") && Integer.parseInt(s) > 0);
    }
}
