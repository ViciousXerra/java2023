package edu.hw8.task1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThat;

class Task1Test {

    private final static int PORT;
    private final static String LINE_SEPARATOR = System.lineSeparator();

    static {
        try {
            PORT = Utils.getFreePort();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static class ClientTask implements Callable<String> {

        private final String message;

        private ClientTask(String message) {
            this.message = message;
        }

        @Override
        public String call() {
            Client client = new Client(PORT, message);
            return client.getResponse();
        }
    }

    @Test
    @DisplayName("Test response retrieving.")
    void testResponseRetrieving() {
        //Given
        String expectedResponse1 = "Твоим языком хорошо было бы железо на морозе лизать."
                                   + LINE_SEPARATOR +
                                   "Разумеется, мы найдём общий язык. Если ты прикусишь свой...";
        String expectedResponse2 = "Зависть - добровольное признание себя ничтожеством.";
        String expectedResponse3 = "Не ищи во мне плюсы и минусы - я не батарейка!";
        String expectedResponse4 = "Ваше право на собственное мнение ещё не обязывает меня слушать бред.";
        String expectedResponse5 = "Твоё \"обещаю\", как моё \"я на диете\".";
        String expectedResponse6 = "There is no existing suitable quotes.";
        Runnable serverTask = () -> {
            Server server = new Server(PORT, 1000, 3);
            server.listen();
        };
        //When
        try (ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())) {
            service.execute(serverTask);
            Thread.sleep(1000);
            Future<String> actualResponse1 = service.submit(new ClientTask("язык"));
            Future<String> actualResponse2 = service.submit(new ClientTask("зависть"));
            Future<String> actualResponse3 = service.submit(new ClientTask("минус"));
            Future<String> actualResponse4 = service.submit(new ClientTask("мнение"));
            Future<String> actualResponse5 = service.submit(new ClientTask("обещаю"));
            Future<String> actualResponse6 = service.submit(new ClientTask("слон"));
            //Then
            assertThat(actualResponse1.get()).isEqualTo(expectedResponse1);
            assertThat(actualResponse2.get()).isEqualTo(expectedResponse2);
            assertThat(actualResponse3.get()).isEqualTo(expectedResponse3);
            assertThat(actualResponse4.get()).isEqualTo(expectedResponse4);
            assertThat(actualResponse5.get()).isEqualTo(expectedResponse5);
            assertThat(actualResponse6.get()).isEqualTo(expectedResponse6);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

}
