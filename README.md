<img alt="Logo" src="http://coderslab.pl/svg/logo-coderslab.svg" width="400">

# Egzamin - Spring - Hibernate.

Przed przystąpieniem do rozwiązywania zadań przeczytaj poniższe wskazówki

## Jak zacząć?

1. Stwórz [*fork*](https://guides.github.com/activities/forking/) repozytorium z zadaniami.
2. Sklonuj repozytorium na swój komputer. Użyj do tego komendy `git clone adres_repozytorium`.

Adres repozytorium możesz znaleźć na stronie repozytorium po naciśnięciu w guzik "Clone or download".
3. Zaimportuj projekt jako projekt `Maven`, wg poniższych wskazówek:

	* W `IntelliJ` wybieramy: `File –> Open`
	* Wskazujemy lokalizację katalogu ze sklonowanym projektem i zatwierdzamy.

4. Rozwiąż zadania i zakomituj zmiany do swojego repozytorium. Użyj do tego komend `git add nazwa_pliku`.
Jeżeli chcesz dodać wszystkie zmienione pliki użyj `git add .` 
Pamiętaj że kropka na końcu jest ważna!
Następnie zakomituj zmiany komendą `git commit -m "nazwa_commita"`
5. Wypchnij zmiany do swojego repozytorium na GitHubie.  Użyj do tego komendy `git push origin master`
6. Stwórz [*pull request*](https://help.github.com/articles/creating-a-pull-request) do oryginalnego repozytorium, gdy skończysz wszystkie zadania.
**Zwróć szczególną uwagę na adres repozytorium oraz nazwę folderu.**

7. Rozwiąż zadania i skomituj zmiany do swojego repozytorium. Użyj do tego komend `git add nazwa_pliku`.
Jeżeli chcesz dodać wszystkie zmienione pliki użyj `git add .` 
Pamiętaj że kropka na końcu jest ważna!
Następnie skommituj zmiany komendą `git commit -m "nazwa_commita"`
    **Repozytorium może zawierać jedynie katalog `src`, pliki: `pom.xml`, `.gitignore`, `README.md`**
8. Wypchnij zmiany do swojego repozytorium na GitHubie.  Użyj do tego komendy `git push origin master`
9. Stwórz [*pull request*](https://help.github.com/articles/creating-a-pull-request) do oryginalnego repozytorium, gdy skończysz wszystkie zadania.

#### Pamiętaj, że pull request musi być stworzony, aby wykładowca dostał Twoje odpowiedzi.

* podczas egzaminu **możesz** korzystać z notatek, kodu napisanego wcześniej, internetu i prezentacji,
* zabroniona jest jakakolwiek komunikacja z innymi kursantami oraz osobami na zewnątrz.

**Powodzenia!**

----------------------------------------------------------------------------------------
### Zadanie 1 (4pkt)

Nazwa bazy danych to **exam6**.

1. Utwórz następujące encje i określ walidację:

- `Tag` (nazwa tabeli `tags`):
	- id – klucz główny,
	- active (Boolean) – pole wymagane,
	- name – pole wymagane.

- `Post` (nazwa tabeli `posts`):
	- id – klucz główny,
	- title – minimalna długość 5 znaków, maksymalna długość 100, pole wymagane,
	- content – pole tekstowe.

2. Nazwy kolumn mają pozostać domyślne.
3. Połącz encje `Post` oraz `Tag` relacją wiele do wielu (dwukierunkową – pamiętaj o odpowiednim określeniu atrybutu `mappedBy`).

### Zadanie 2 (3 pkt)

1. Utwórz repozytorium dla encji `Post` i `Tag`.
2. Uzupełnij repozytorium o poniższe metody umożliwiające pobieranie:
- wszystkich postów dla zadanego obiektu `Tag`,
- wszystkich postów dla zadanej nazwy tagu – typ danych `String`,


### Zadanie 3 (5 pkt)

1. Utwórz kontrolery: dla encji `Post` z mapowaniem `/post` oraz dla encji `Tag` z mapowaniem `/tag`.
2. Utwórz akcję oraz formularz umożliwiające dodanie tagu. Formularz ma być dostępny pod adresem `/tag/add`. 
3. Utwórz akcję oraz formularz umożliwiające dodanie postu. Formularz ma być dostępny pod adresem `/post/add` 
(formularz ma posiadać możliwość wyboru jednego lub wielu tagów).
4. Poprawność wprowadzonych danych musi być sprawdzana. Wyświetlaj komunikat o błędach.
5. Po dodaniu postu oraz tagu wyświetlaj dane właśnie dodanego elementu.

### Zadanie 4 (4 pkt)

1. Utwórz akcję dostępną pod adresem `/tag/all`, która wyświetli (w formie tabeli) listę wszystkich tagów. 
2. Dodaj możliwość zmiany aktywności tagu – link do zmiany umieść w tabeli html (`Aktywuj/Dezaktywuj`) dla każdego tagu. 
Akcja ma być dostępna pod adresem: `/tag/{id}/change`.
Jeżeli tag ma aktywność ustawioną na `true` ma się ona zmienić na `false` i odwrotnie. 
3. Dodaj akcję oraz widok wyświetlający wszystkie informacje z bazy dla zadanego tagu. 
Akcja ma być dostępna pod adresem: `/tag/{id}/show` – link: `Pokaż tag` umieść w tabeli html. 


### Zadanie 5 (4 pkt)

1. Utwórz akcję dostępną pod adresem `/tag/{id}/deletePosts` - usuwającą wszystkie posty dla zadanego tagu.
2. Dodaj możliwość edycji tagów - umieść w tabeli html link do edycji dla każdego tagu: `Edytuj tag`.
Akcja ma być dostępna pod adresem `/tag/{id}/edit`
3. Dodaj akcję oraz widok wyświetlający wszystkie posty dla określonego tagu. 
Akcja ma być dostępna pod adresem: `/tag/{id}/posts` – link: `Pokaż posty` umieść w tabeli html.

## Przykład interfejsu dla zadań 4 i 5:

| Nazwa     | Aktywność | 					Akcja	   				                           |
| --------- |:---------:| :-------------------------------------------------------------------:|
| java      | TAK       | Aktywuj/Dezaktywuj, Pokaż posty, Pokaż tag, Edytuj tag, Usuń posty   |
