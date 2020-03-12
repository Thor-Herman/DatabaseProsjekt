import java.util.Scanner;

public class Main {
    private static ActorCtrl actorCtrl = new ActorCtrl();
    private static FilmCtrl filmCtrl = new FilmCtrl();
    // Runs a text interface in the console
    public static void main(String[] args) {
        String inputString = "";
        System.out.println("Velkommen til vår IMDB, vennligst velg et av alternative under: ");

        while (!inputString.equals("q")) {
            printMenu();
            Scanner lineScanner = new Scanner(System.in);
            inputString = lineScanner.next();

            if (inputString.equals("a") || inputString.equals("b")) {
                printActorInfo(inputString);
            }
            if (inputString.equals("c")) {
                filmCtrl.printCompaniesMostFilmsPerGenre();
            }
        }



    }

    private static void printActorInfo(String choiceString) {
        Scanner actorScanner = new Scanner(System.in);
        System.out.println("Vennligst skriv inn the fulle navnet til skuespilleren du vil ha info om: ");
        String actorName = actorScanner.next(); // Tar navn input fra brukeren
        if (validateStringInput(actorName)) {
            if (choiceString.equals("a")) {
                actorCtrl.getActorRoles(actorName);
            } else {
                actorCtrl.getActorFilms(actorName);
            }
        } else {
            System.out.println("Vennligst skriv inn et ordentlig navn");
        }
    }

    private static void printMenu() {
        System.out.println("a: Finne navnet på alle rollene en gitt skuespiller har ");
        System.out.println("b: Finne hvilke filmer som en gitt skuespiller opptrer i ");
        System.out.println("c: Finne hvilke filmselskap som lager flest filmer inne hver sjanger ");
        System.out.println("d: Sette inn en ny film ");
        System.out.println("e: Sette inn ny anmeldelse av en episode av en serie ");
        System.out.println("q: Exit programmet");
        System.out.println("Skriv inn bokstaven foran det valget du ønsker å ta:  ");
    }

    // Validates strings containing numbers, integers and underscores
    private static boolean validateStringInput(String s) {
        return s.matches("^[a-zA-Z0-9_.-]+$");
    }

    // Validates all integers greater than zero
    private static boolean validateIntegerInput(String s) {
        return (s.matches("^[0-9]+$") && Integer.parseInt(s) > 0);
    }
}