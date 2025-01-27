package agh.ics.oop.model;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class NameGenerator {
    Random random = new Random(); // modyfikator dostępu?
    List<String> polishNames = Arrays.asList( // modyfikator dostępu?
            "Adam", "Adrian", "Albert", "Aleksander", "Andrzej", "Antoni", "Arkadiusz", "Artur",
            "Bartosz", "Błażej", "Bogdan", "Bogumił", "Bolesław", "Bronisław", "Cezary", "Damian",
            "Daniel", "Dawid", "Dominik", "Edward", "Emil", "Ernest", "Filip", "Franciszek",
            "Gabriel", "Grzegorz", "Henryk", "Hubert", "Igor", "Jakub", "Jan", "Jacek", "Jarosław",
            "Jerzy", "Józef", "Kacper", "Kamil", "Karol", "Krzysztof", "Łukasz", "Maciej", "Marcin",
            "Marek", "Marian", "Mariusz", "Mateusz", "Michał", "Mikołaj", "Norbert", "Olaf", "Paweł",
            "Piotr", "Przemysław", "Rafał", "Robert", "Roman", "Radosław", "Sebastian", "Stanisław",
            "Stefan", "Tomasz", "Waldemar", "Włodzimierz", "Wojciech", "Zbigniew", "Zdzisław",
            "Agata", "Aleksandra", "Alicja", "Alina", "Amelia", "Anastazja", "Aneta", "Anna",
            "Antonina", "Barbara", "Beata", "Bogna", "Bożena", "Celina", "Dagmara", "Danuta",
            "Dominika", "Dorota", "Edyta", "Elżbieta", "Emilia", "Ewa", "Gabriela", "Grażyna",
            "Halina", "Hanna", "Helena", "Iga", "Irena", "Izabela", "Jadwiga", "Janina", "Joanna",
            "Jolanta", "Julia", "Justyna", "Karolina", "Katarzyna", "Kinga", "Klaudia", "Krystyna",
            "Lidia", "Liliana", "Lucyna", "Magdalena", "Małgorzata", "Maria", "Marta", "Martyna",
            "Michalina", "Monika", "Natalia", "Nikola", "Olga", "Paulina", "Patrycja", "Renata",
            "Roksana", "Rozalia", "Sabina", "Sandra", "Sonia", "Stefania", "Sylwia", "Teresa",
            "Weronika", "Wiktoria", "Wioletta", "Zofia", "Zuzanna",
            "Barnaba", "Bernard", "Czesław", "Dariusz", "Dionizy", "Eugeniusz", "Gustaw", "Ignacy",
            "Jeremi", "Klemens", "Konrad", "Leon", "Ludwik", "Maksymilian", "Marceli", "Marceliusz",
            "Marian", "Mariusz", "Martian", "Mieczysław", "Nikodem", "Patryk", "Remigiusz",
            "Ryszard", "Sławomir", "Tadeusz", "Teodor", "Tymoteusz", "Walenty", "Witold", "Zygmunt",
            "Adela", "Agnieszka", "Aldona", "Amelia", "Apolonia", "Aurelia", "Beatrycze", "Cecylia",
            "Diana", "Eliza", "Elwira", "Gabriela", "Gertruda", "Honorata", "Irena", "Izolda",
            "Jadwiga", "Kalina", "Karina", "Klara", "Kornelia", "Krystyna", "Leokadia", "Liliana",
            "Ludwika", "Magdalena", "Malwina", "Marzena", "Mirosława", "Natalia", "Oliwia",
            "Patrycja", "Roksana", "Sabina", "Salomea", "Sara", "Tatiana", "Teodora", "Urszula",
            "Weronika", "Wiktoria", "Wiola", "Zofia", "Zuzanna",
            "Alojzy", "Ambroży", "Anatol", "Apolinary", "Arnold", "Baltazar", "Barnaba", "Bartłomiej",
            "Bazyli", "Benedykt", "Bernard", "Błażej", "Bonifacy", "Borys", "Brunon", "Cezary",
            "Czesław", "Cyprian", "Cyryl", "Dariusz", "Dawid", "Dionizy", "Dominik", "Edmund",
            "Edward", "Emil", "Ernest", "Eryk", "Eugeniusz", "Fabian", "Feliks", "Ferdynand",
            "Filip", "Florian", "Franciszek", "Fryderyk", "Gabriel", "Gerard", "Grzegorz",
            "Gustaw", "Henryk", "Herbert", "Hieronim", "Hilary", "Hipolit", "Honorat", "Hubert",
            "Hugo", "Ignacy", "Igor", "Ireneusz", "Iwan", "Iwo", "Izajasz", "Izydor", "Jacek",
            "Jakub", "Jan", "Janusz", "Jaromir", "Jarosław", "Jeremi", "Jerzy", "Jędrzej",
            "Joachim", "Józef", "Julian", "Juliusz", "Juri", "Justyn", "Kacper", "Kajetan",
            "Kamil", "Karol", "Kazimierz", "Klaudiusz", "Klemens", "Konrad", "Konstanty",
            "Kornel", "Korneliusz", "Kosma", "Kryspyn", "Krystian", "Krzysztof", "Ksawery",
            "Laurencjusz", "Lech", "Lechosław", "Leo", "Leon", "Leokadiusz", "Leonard",
            "Leopold", "Lesław", "Leszek", "Lew", "Longin", "Lubomił", "Lubomir", "Luborad",
            "Lubosław", "Lucjan", "Ludomił", "Ludomir", "Ludwik", "Łukasz", "Maciej",
            "Makary", "Maksymilian", "Malachiasz", "Manfred", "Marcel", "Marceli", "Marcin",
            "Marek", "Marian", "Mariusz", "Martian", "Mateusz", "Maurycy", "Michał",
            "Mieczysław", "Mieszko", "Mikołaj", "Milan", "Miloš", "Mirosław", "Mściwoj",
            "Mstislav", "Natan", "Nikodem", "Norbert", "Olaf", "Oleg", "Oliwier", "Olgierd",
            "Oskar", "Paweł", "Piotr", "Przemysław", "Rajmund", "Rafał", "Radosław",
            "Rajmund", "Roman", "Radosław", "Remigiusz", "Robert", "Roman", "Radosław",
            "Ryszard", "Sambor", "Samuel", "Sebastian", "Seweryn", "Sławomir", "Sławoj",
            "Sławosz", "Sobiesław", "Stanisław", "Stefan", "Szymon", "Tadeusz", "Teodor",
            "Teofil", "Teodor", "Tymoteusz", "Tytus", "Waldemar", "Walenty", "Waldemar",
            "Walerian", "Walenty", "Władysław", "Włodzimierz", "Wojciech", "Witold",
            "Włodzimierz", "Wojciech", "Witold", "Zbigniew", "Zdzisław", "Zenon",
            "Zenon", "Zygfryd", "Zygmunt", "Zygmunt");

    public NameGenerator() {
        random = new Random();
    }

    public String generateName() {
        return polishNames.get(random.nextInt(polishNames.size()));
    }
}
