import java.util.Scanner;

public class Main {
    private static ActorCtrl actorCtrl = new ActorCtrl();
    private static FilmCtrl filmCtrl = new FilmCtrl();
    private static Insertion DBinserter = new Insertion();
    // Runs a text interface in the console
    public static void main(String[] args) {
        String inputString = "";
        System.out.println("Velkommen til vår IMDB, vennligst velg et av alternative under: ");
        Scanner lineScanner = new Scanner(System.in);
        while (!inputString.equals("q")) {
            printMenu();
            inputString = lineScanner.nextLine();

            if (inputString.equals("a") || inputString.equals("b")) {
                printActorInfo(inputString);
            }
            if (inputString.equals("c")) {
                filmCtrl.printCompaniesMostFilmsPerGenre();
            }
            if (inputString.equals("d")) {
                insertMovieorSeriesorEpisode();
            }
        }
        lineScanner.close();
    }

    private static void insertMovieorSeriesorEpisode() {
        System.out.println("Skriv 'a' for å sette inn en film, 'b' for serie og 'c' for episode");
        Scanner answerScanner = new Scanner(System.in);
        String answer = answerScanner.nextLine();
        if (answer.equals("a")) {
            int videoID = insertVideo();
            insertFilm(videoID);
        } else if (answer.equals("b")) {
            int videoID = insertVideo();
            insertSeries(videoID);
        } else if (answer.equals("c")) {
            int videoID = insertVideo();
            insertEpisode(videoID);
        }
    }

    private static void printActorInfo(String choiceString) {
        Scanner actorScanner = new Scanner(System.in);
        System.out.println("Vennligst skriv inn the fulle navnet til skuespilleren du vil ha info om: ");
        String actorName = actorScanner.nextLine(); // Tar navn input fra brukeren
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

    private static int insertVideo() {
        Scanner videoInformationScanner = new Scanner(System.in);
        System.out.println("Tittel: ");
        String title = videoInformationScanner.nextLine();
        System.out.println("Beskrivelse: ");
        String description = videoInformationScanner.nextLine();
        System.out.println("Lanseringsdato: ");
        String releaseDate = videoInformationScanner.nextLine();
        System.out.println("Id til selskapet");
        String companyIDString = videoInformationScanner.nextLine();
        System.out.println("Skriv inn hva slags type video dette er: ");
        String videoType = videoInformationScanner.nextLine();
        int companyIDInteger = stringInputToInteger(companyIDString);
        if (companyIDInteger > 0) {
            return DBinserter.insertVideoIntoDB(title, description, releaseDate, companyIDInteger, videoType);
        }
        videoInformationScanner.close();
        return 0;
    }

    private static void insertCompany() {
        Scanner companyInformationScanner = new Scanner(System.in);
        System.out.println("Skriv inn landet til selskapet: ");
        String country = companyInformationScanner.nextLine();
        System.out.println("Skriv inn adressem: ");
        String address = companyInformationScanner.nextLine();
        System.out.println("Skriv inn urlen til nettsiden: ");
        String url = companyInformationScanner.nextLine();
        DBinserter.insertCompanyIntoDb(country, address, url);
        companyInformationScanner.close();
    }

    private static void insertFilm(int videoID) {
        Scanner companyInformationScanner = new Scanner(System.in);
        System.out.println("Skriv inn lengden til filmen som et heltall: ");
        int length = stringInputToInteger(companyInformationScanner.nextLine());
        System.out.println("Skriv inn utgivelsesåret: ");
        int releaseYear = stringInputToInteger(companyInformationScanner.nextLine());
        if (length > 0 && releaseYear > 0 && videoID > 0) {
            DBinserter.insertFilmIntoDB(length, releaseYear, videoID);
        }
        companyInformationScanner.close();
    }

    private static void insertPerson() {
        Scanner nameScanner = new Scanner(System.in);
        System.out.println("Skriv inn navnet til personen du vil legge inn: ");
        String personName = nameScanner.nextLine();
        DBinserter.insertPersonIntoDB(personName);
        nameScanner.close();
    }

    private static void insertSeries(int videoID) {
        DBinserter.insertSeriesIntoDB(videoID);
    }

    private static void insertEpisode(int videoID) {
        Scanner episodeInformationScanner = new Scanner(System.in);
        System.out.println("Skriv inn episodenummeret: ");
        int episodeNr = stringInputToInteger(episodeInformationScanner.nextLine());
        System.out.println("Skriv inn utgivelsesåret: ");
        int releaseYear = stringInputToInteger(episodeInformationScanner.nextLine());
        System.out.println("Skriv inn tittelen på episoden: ");
        String title = episodeInformationScanner.nextLine();
        System.out.println("Skriv inn sesongnummeret: ");
        int season = stringInputToInteger(episodeInformationScanner.nextLine());
        System.out.println("Skriv inn en beskrivelse av episoden: ");
        String description = episodeInformationScanner.nextLine();
        DBinserter.insertEpisodeIntoDB(episodeNr, releaseYear, season, title, description, videoID);
        episodeInformationScanner.close();
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