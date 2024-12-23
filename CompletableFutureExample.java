package pr_5_11;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class CompletableFutureExample {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // завдання 1 Отрим дан з кільк джер і оброб їх післ заверш всіх завд
        // ств асинхр завд для отриман дан з трьох джерел
        CompletableFuture<String> dataFromSource1 = CompletableFuture.supplyAsync(() -> getDataFromSource("Джерело 1"));
        CompletableFuture<String> dataFromSource2 = CompletableFuture.supplyAsync(() -> getDataFromSource("Джерело 2"));
        CompletableFuture<String> dataFromSource3 = CompletableFuture.supplyAsync(() -> getDataFromSource("Джерело 3"));

        // вик всіх асинхр завд та очік їх заверш
        CompletableFuture<Void> allOf = CompletableFuture.allOf(dataFromSource1, dataFromSource2, dataFromSource3);

        // оброб резул післ заверш всіх завд
        allOf.thenRun(() -> {
            try {
                String result1 = dataFromSource1.get();
                String result2 = dataFromSource2.get();
                String result3 = dataFromSource3.get();
                System.out.println("\n=== Результати отримання даних з джерел ===");
                System.out.println("Джерело 1: " + result1);
                System.out.println("Джерело 2: " + result2);
                System.out.println("Джерело 3: " + result3);
            } catch (InterruptedException | ExecutionException e) {
                System.err.println("Помилка при отриманні даних з джерел: " + e.getMessage());
            }
        }).join(); // очік заверш всіх завдань

        // завд 2: Отрим оптим марш
        // ств асинхр завд для отрим цін на різн вид транспор
        CompletableFuture<Double> trainPrice = CompletableFuture.supplyAsync(() -> getPrice("Поїзд"));
        CompletableFuture<Double> busPrice = CompletableFuture.supplyAsync(() -> getPrice("Автобус"));
        CompletableFuture<Double> flightPrice = CompletableFuture.supplyAsync(() -> getPrice("Літак"));

        // обробк результ післ заверш всіх завд
        CompletableFuture<Void> bestPrice = CompletableFuture.allOf(trainPrice, busPrice, flightPrice).thenRun(() -> {
            try {
                double train = trainPrice.get();
                double bus = busPrice.get();
                double flight = flightPrice.get();

                System.out.println("\n=== Порівняння цін на транспорт ===");
                System.out.println("Поїзд: " + train + " $");
                System.out.println("Автобус: " + bus + " $");
                System.out.println("Літак: " + flight + " $");

                // пош найменш цін
                double minPrice = Math.min(train, Math.min(bus, flight));
                String bestTransport = (minPrice == train) ? "Поїзд" : (minPrice == bus) ? "Автобус" : "Літак";

                System.out.println("\nНайкращий варіант: " + bestTransport + " (ціна: " + minPrice + " $)");
            } catch (InterruptedException | ExecutionException e) {
                System.err.println("Помилка при обчисленні найкращої ціни: " + e.getMessage());
            }
        });

        bestPrice.join(); // очік заверш всіх завд
    }

    private static String getDataFromSource(String source) {
        // імітац отрим дан з джер
        simulateDelay(); // імітац затримк
        return "Дані з " + source;
    }

    private static Double getPrice(String transport) {
        // імітац отрим цін для певн вид транс
        simulateDelay(); // імітац затр
        switch (transport) {
            case "Поїзд": return 50.0;
            case "Автобус": return 30.0;
            case "Літак": return 100.0;
            default: return 0.0;
        }
    }

    private static void simulateDelay() {
        try {
            Thread.sleep(1000); // іміт затр 1 сек
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
