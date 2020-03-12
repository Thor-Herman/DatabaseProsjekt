import java.util.Scanner;

public class Main {
    private static ActorCtrl actorCtrl = new ActorCtrl();
    private static FilmCtrl filmCtrl = new FilmCtrl();
    private static Insertion DBinserter = new Insertion();
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
            lineScanner.close();
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
        actorScanner.close();
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

    private static void insertVideo() {
        Scanner videoInformationScanner = new Scanner(System.in);
        System.out.println("Tittel: ");
        String title = videoInformationScanner.next();
        System.out.println("Beskrivelse: ");
        String description = videoInformationScanner.next();
        System.out.println("Lanseringsdato: ");
        String releaseDate = videoInformationScanner.next();
        System.out.println("Id til selskapet");
        String companyIDString = videoInformationScanner.next();
        System.out.println("Skriv inn hva slags type video dette er: ");
        String videoType = videoInformationScanner.next();
        int companyIDInteger = stringInputToInteger(companyIDString);
        if (companyIDInteger > 0) {
            DBinserter.insertVideoIntoDB(title, description, releaseDate, companyIDInteger, videoType);
        }
        videoInformationScanner.close();
    }

    private static void insertCompany() {
        Scanner companyInformationScanner = new Scanner(System.in);
        String country = companyInformationScanner.next();
        String address = companyInformationScanner.next();
        String url = companyInformationScanner.next();
        DBinserter.insertCompanyIntoDb(country, address, url);
        companyInformationScanner.close();
    }

    private static void insertFilm() {
        Scanner companyInformationScanner = new Scanner(System.in);
        int length = stringInputToInteger(companyInformationScanner.next());
        int releaseYear = stringInputToInteger(companyInformationScanner.next());
        int videoID = stringInputToInteger(companyInformationScanner.next());
        if (length > 0 && releaseYear > 0 && videoID > 0) {
            DBinserter.insertFilmIntoDB(length, releaseYear, videoID);
        }
        companyInformationScanner.close();
    }

    private static void insertPerson() {
        Scanner nameScanner = new Scanner(System.in);
        String personName = nameScanner.next();
        DBinserter.insertPersonIntoDB(personName);
        nameScanner.close();
    }

    // Validates strings containing numbers, integers and underscores
    private static boolean validateStringInput(String s) {
        return s.matches("^[a-zA-Z0-9_.-]+$");
    }

    // Converts string input to an integer greater than zero
    private static int stringInputToInteger(String s) {
        if (validateIntegerInput(s)) {
            return Integer.parseInt(s);
        } else {
            System.out.println("Du har skrevet inn noe som ikke er et tall større enn null");
        }
        return -1;
    }


    // Validates all integers greater than zero
    private static boolean validateIntegerInput(String s) {
        return (s.matches("^[0-9]+$") && Integer.parseInt(s) > 0);
    }
}