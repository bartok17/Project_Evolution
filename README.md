# Projekt_Ewolucja

## How to run the project with Gradle (English)

1. Make sure you have Java 17 installed.
2. Open a terminal in the project directory.
3. Build the project:
   ```
   ./gradlew build
   ```
4. Run the application:
   ```
   ./gradlew run
   ```
   or
   ```
   java -jar build/libs/Projekt_Ewolucja-1.0-SNAPSHOT.jar
   ```
5. The main class is `agh.ics.oop.WorldGUI`. The simulation GUI will start.

Saved simulation configurations are stored in the `/saves` folder.

## Jak uruchomić projekt za pomocą Gradle (Polski)

1. Upewnij się, że masz zainstalowaną Javę 17.
2. Otwórz terminal w katalogu projektu.
3. Zbuduj projekt:
   ```
   ./gradlew build
   ```
4. Uruchom aplikację:
   ```
   ./gradlew run
   ```
   lub
   ```
   java -jar build/libs/Projekt_Ewolucja-1.0-SNAPSHOT.jar
   ```
5. Główna klasa to `agh.ics.oop.WorldGUI`. Uruchomi się graficzny interfejs symulacji.

Zapisane konfiguracje symulacji znajdują się w folderze `/saves`.

---

Projekt wykonuje wszystkie polecenia oprócz generowania pliku csv przebiegu całej symulacji, jako że projekt robiłem jednoosobowo nie zdążyłem tego zaimplementować.

Testy Jednostkowe Zaimplementowałem tylko do poodstawowych klas. Dla reszty było by to bardzo czasochłonne, a i tak dużo prościej kożysta się z działającej symulacji i ją obserwuje. (Zakładając że funkcje bazowe działają)
Część funkcji było też tak prosta, że do do momentu do którego nie pojawia się problem, testy nigdy nie będą potrzebne
