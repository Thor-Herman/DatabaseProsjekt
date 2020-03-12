import java.util.Scanner;

public class Main {
    private static ActorCtrl actorCtrl = new ActorCtrl();
    private static FilmCtrl filmCtrl = new FilmCtrl();
    private static Insertion DBinserter = new Insertion();

    // Antar at for hver main kan man bare opprette én bruker
    private boolean hasAlreadyRegisteredUser = false;
    private int userID;

    // Runs a text interface in the console
    public static void main(String[] args) {
        Main main = new Main(); // To avoid static
        String inputString = "";
        System.out.println("Velkommen til vår IMDB, vennligst velg et av alternative under: ");
        Scanner lineScanner = new Scanner(System.in);
        while (!inputString.equals("q")) {
            printMenu(lineScanner);
            inputString = lineScanner.nextLine();

            if (inputString.equals("a") || inputString.equals("b")) {
                printActorInfo(inputString, lineScanner);
            }
            if (inputString.equals("c")) {
                filmCtrl.printCompaniesMostFilmsPerGenre();
            }
            if (inputString.equals("d")) {
                insertMovieorSeriesorEpisode(lineScanner);
            }
            if (inputString.equals("f")) {
                main.createUser(lineScanner);
            }
            if (inputString.equals("g")) {
                main.insertRating(lineScanner);
            }
        }
        lineScanner.close();
    }

    private static void insertMovieorSeriesorEpisode(Scanner scanner) {
        System.out.println("Skriv 'a' for å sette inn en film, 'b' for serie og 'c' for episode");
        String answer = scanner.nextLine();
        if (answer.equals("a")) {
            int videoID = insertVideo(scanner);
            insertFilm(videoID, scanner);
        } else if (answer.equals("b")) {
            int videoID = insertVideo(scanner);
            insertSeries(videoID);
        } else if (answer.equals("c")) {
            int videoID = insertVideo(scanner);
            insertEpisode(videoID, scanner);
        }
    }

    private static void printActorInfo(String choiceString, Scanner scanner) {
        System.out.println("Vennligst skriv inn the fulle navnet til skuespilleren du vil ha info om: ");
        String actorName = scanner.nextLine(); // Tar navn input fra brukeren
        if (validateStringInput(actorName)) {
            if (choiceString.equals("a")) {
                System.out.println(actorCtrl.getActorRoles(actorName));
            } else {
                System.out.println(actorCtrl.getActorFilms(actorName));
            }
        }
        else {
            System.out.println("Vennligst skriv inn et ordentlig navn");
        }
    }

    private static void printMenu(Scanner scanner) {
        System.out.println("a: Finne navnet på alle rollene en gitt skuespiller har ");
        System.out.println("b: Finne hvilke filmer som en gitt skuespiller opptrer i ");
        System.out.println("c: Finne hvilke filmselskap som lager flest filmer inne hver sjanger ");
        System.out.println("d: Sette inn en ny film ");
        System.out.println("e: Sette inn ny anmeldelse av en episode av en serie ");
        System.out.println("q: Exit programmet");
        System.out.println("Skriv inn bokstaven foran det valget du ønsker å ta:  ");
    }

    private static int insertVideo(Scanner scanner) {
        System.out.println("Tittel: ");
        String title = scanner.nextLine();
        System.out.println("Beskrivelse: ");
        String description = scanner.nextLine();
        System.out.println("Lanseringsdato: ");
        String releaseDate = scanner.nextLine();
        System.out.println("Id til selskapet");
        String companyIDString = scanner.nextLine();
        System.out.println("Skriv inn hva slags type video dette er: ");
        String videoType = scanner.nextLine();
        int companyIDInteger = stringInputToInteger(companyIDString);
        if (companyIDInteger > 0) {
            return DBinserter.insertVideoIntoDB(title, description, releaseDate, companyIDInteger, videoType);
        }
        return 0;
    }

    private static void insertCompany(Scanner scanner) {
        System.out.println("Skriv inn landet til selskapet: ");
        String country = scanner.nextLine();
        System.out.println("Skriv inn adressem: ");
        String address = scanner.nextLine();
        System.out.println("Skriv inn urlen til nettsiden: ");
        String url = scanner.nextLine();
        DBinserter.insertCompanyIntoDb(country, address, url);
    }

    private static void insertFilm(int videoID, Scanner scanner) {
        System.out.println("Skriv inn lengden til filmen som et heltall: ");
        int length = stringInputToInteger(scanner.nextLine());
        System.out.println("Skriv inn utgivelsesåret: ");
        int releaseYear = stringInputToInteger(scanner.nextLine());
        if (length > 0 && releaseYear > 0 && videoID > 0) {
            DBinserter.insertFilmIntoDB(length, releaseYear, videoID);
        }
    }

    private static void insertPerson(Scanner scanner) {
        System.out.println("Skriv inn navnet til personen du vil legge inn: ");
        String personName = scanner.nextLine();
        DBinserter.insertPersonIntoDB(personName);
    }

    private static void insertSeries(int videoID) {
        DBinserter.insertSeriesIntoDB(videoID);
    }

    private static void insertEpisode(int videoID, Scanner scanner) {
        System.out.println("Skriv inn episodenummeret: ");
        int episodeNr = stringInputToInteger(scanner.nextLine());
        System.out.println("Skriv inn utgivelsesåret: ");
        int releaseYear = stringInputToInteger(scanner.nextLine());
        System.out.println("Skriv inn tittelen på episoden: ");
        String title = scanner.nextLine();
        System.out.println("Skriv inn sesongnummeret: ");
        int season = stringInputToInteger(scanner.nextLine());
        System.out.println("Skriv inn en beskrivelse av episoden: ");
        String description = scanner.nextLine();
        DBinserter.insertEpisodeIntoDB(episodeNr, releaseYear, season, title, description, videoID);
    }

    private void insertRating(Scanner scanner) {
        System.out.println("Skriv inn rating 1-10");
        int rating = stringInputToInteger(scanner.nextLine());
        if (rating < 11 && rating > 0) {
            System.out.println("Skriv inn episodeID");
            int epID = stringInputToInteger(scanner.nextLine());
            DBinserter.addRatingToEpisode(this.userID, epID, rating);
        }
        else {
            System.out.println("Rating var ikke mellom 1 og 10. Prøv igjen");
        }
    }

    private void createUser(Scanner scanner) {
        if (! this.hasAlreadyRegisteredUser) {
            System.out.println("Skriv inn ønsket brukernavn:");
            String name = scanner.nextLine();
            this.userID = DBinserter.insertUserIntoDB(name);
            this.hasAlreadyRegisteredUser = true;
        }
        else {
            System.out.println("Du har alt registrert en bruker");
        }
    }

    // Validates strings containing numbers, integers and underscores
    private static boolean validateStringInput(String s) {
        return s.matches("^[a-zA-Z0-9\\s_.-]+$");
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