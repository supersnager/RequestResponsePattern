# RequestResponse
Request - response pattern implementation for Scala, based on Akka and Futures

Jakie elementy potrzebuję:

1. Promisa, typu, którego message będzie zwracany
2. Tymczasowy aktor komunikacyjny ResponseHandler
3. Metoda do wysyłania requestu, najlepiej by było, żeby to była jedna funkcja i żeby nie trzeba było
    tworzyć instancji obiektu (czyli całość może powinna być jako trait?)
4. Możliwość podpięcia metody onSuccess, onFailure (nie jako parametry, bo to bez sensu)
5. Czy chcę mieć na pewno opcjonalnie jakieś callbacki? - TAK, ale nie jako core funkcjonalność
   wszytko musi być złożone z małych klouszków
   
   No i zajebiście bo podstawowy mechanizm już jest
   Teraz wystarczy dodać funkcje callbacków i jesteśmy w domciu :)
   