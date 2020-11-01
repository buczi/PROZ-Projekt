# PROZ-Projekt
Projekt zaliczeniowy - gra Zarządca ruchu

MODEL_V_0_CODE

ZAWARTOŚĆ:
ALGORITHM - zawiera algorytm a* dostosowany do planszy składającej się z węzłów. Dla każdego samochodu losuje punkt startowy oraz końcowy. Następnie wyszukuje najepszej drogi pomiędzy tymi punktami.

MVC - zawiera klasę główną w tej iteracji służy jako miejsce wywołania funkcji main. Zawiera klasę Controller będącą wątkiem w którym wykonuje się główna pętla gry. Inicjuję mapę.

MAP - reprezentacja wszystkich obiektów nieprzemieszczających się. Klasa Map reprezentuję mapę po której przemieszczają się samochody, w jej skład wchodzi siatka węzłów (skrzyżowań).

UTILS - klasy pomocnicze oraz klasa i interface odpowiedzialny za upływ czasu w grze.

VEHICLE - reprezentacja obiektów przemieszczających się po mapie. Zestaw eventów dla samochodów w przypadku zmiany ich stanów ( np. z w drodze do na miejscu).

PLIKI .TXT - odpowiednio przygotowane pliki tekstowe reprezentujace rozne mapy na ktorych bedzie prowadzona rozgrywka.

ALGORITHM:
-A_star - wyszukanie ścieżki między dwoma punktami w grafie, algorytm a*
-EndPointFinder - pseudolosowy wybór punktów startowych i końcowych, samochód zawsze zaczyna na czerwonym świetle

MVC:
-Controller - główna klasa, dziedzicząca z Thread. Wątek przez nią reprezentowany ukazuje główną pętlę gry, która aktualizuje wszystkie elementy.

MAP:
- Coords - reprezentacja położenia w siatce mapy ( tablica 2D) lewy górny róg 0,0, prawy dolny róg n,m. n - wiersz, m - kolumna.
- Direction - reprezentacja kieruków świata.
- GridNode - węzeł reprezentujący skrzyżowanie. Ma 4 wejścia na ulice i 4 wyjścia, połączony z innymi węzłami ulicami (klasą street). Zarządza zmianą światła na swoim skrzyżowaniu.
- IntersectionType - rodzaj skrzyżowania określający czy skrzyżowanie ma światła czy nie. Wczytuje informacje z pliku i tworzy odpowiedni węzeł.
- Map - Reprezentacja siatki skrzyżowań połączonych ulicami. Zarządza wszystkimi węzłami oraz tworzy lub niszczy samochody w zależności od wydarzeń.
- Street - obiekt łączący dwa skrzyżowania. Może mieć załączone światła na końcu wchodzącym do skrzyżowania.
- TrafficLights - światła, które mogą byc zielone lub czerwone. Zawierają kolejke samochodów reprezentującą pojazdy czekające na przejazd przez skrzyżowanie.

UTILS:
- CreateMap - klasa tworząca bazową mapę do zadanego pliku (mapa z wszystkimi możliwymi ulicami).
- Pair - para dwoch wartosci.
- SPair - para dwoch wartości tego samego rodzaju.
- Time - inteface, który implementują wszystkie obiekty zmienne w czasie (np. Samochód, Światła).
- TimeController - singleton bedący zarządcą czasu, zawiera funkcje odświeżającą wszystkie obiekty z interfacem Time.

VEHICLE:
- Car - odpowiednik samochodu. Przemieszcza się po drogach po określonej ścieżce. Znajduje odpowiedni wyjazd ze skrzyżowania.
- CarEvents - interface wydarzen związane z samochodem. Poprawne zakończenie podrózy, Zagubienie świeżki, Inny błąd.
