package edu.hw7.task3;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertSame;

class InMemoryDataBaseTest {

    private final static Logger LOGGER = LogManager.getLogger();
    private final static String CAUGHT_EXCEPTION_MESSAGE_TEMPLATE = "Caught exception: %s";
    private final static int NUM_OF_THREADS = Runtime.getRuntime().availableProcessors();

    private static AbstractDataBase[] provideDataBases() {
        return new AbstractDataBase[] {
            SyncInMemoryDataBase.getDataBaseInstance(),
            LockMemoryDataBase.getDataBaseInstance()
        };
    }

    @Test
    @DisplayName("Test db singleton.")
    void testSingleton() {
        try (ExecutorService service = Executors.newFixedThreadPool(NUM_OF_THREADS)){
            Future<SyncInMemoryDataBase> syncDbInstanceRef1 =
                service.submit(SyncInMemoryDataBase::getDataBaseInstance);
            Future<SyncInMemoryDataBase> syncDbInstanceRef2 =
                service.submit(SyncInMemoryDataBase::getDataBaseInstance);
            Future<LockMemoryDataBase> lockDbInstanceRef1 =
                service.submit(LockMemoryDataBase::getDataBaseInstance);
            Future<LockMemoryDataBase> lockDbInstanceRef2 =
                service.submit(LockMemoryDataBase::getDataBaseInstance);
            service.shutdown();
            while (!service.isTerminated()) {

            }
            Assertions.assertAll(
                () -> assertSame(syncDbInstanceRef1.get(), syncDbInstanceRef2.get()),
                () -> assertSame(lockDbInstanceRef1.get(), lockDbInstanceRef2.get())
            );
        }
    }

    @ParameterizedTest
    @DisplayName("Test atomicity of adding and deleting.")
    @MethodSource("provideDataBases")
    void testAddAndDelete(AbstractDataBase db) {
        Person person1 = new Person(1, "person1", "address1", "111-11-11");
        Person person2 = new Person(2, "person2", "address2", "222-22-22");
        Person person3 = new Person(3, "person3", "address1", "333-33-33");
        //To be deleted
        Person person4 = new Person(4, "person1", "address1", "222-22-22");

        //When
        Future<List<Person>> futureFindByName;
        Future<List<Person>> futureFindByAddress;
        Future<List<Person>> futureFindByPhone;
        CountDownLatch latch = new CountDownLatch(4);
        try (ExecutorService service = Executors.newFixedThreadPool(NUM_OF_THREADS)) {
            service.execute(() -> {
                db.add(person1);
                latch.countDown();
            });
            service.execute(() -> {
                db.add(person2);
                latch.countDown();
            });
            service.execute(() -> {
                db.add(person3);
                latch.countDown();
            });
            service.execute(() -> {
                db.add(person4);
                latch.countDown();
            });
            latch.await();
            service.execute(() -> db.delete(4));
            futureFindByName = service.submit(() -> db.findByName("person1"));
            futureFindByAddress = service.submit(() -> db.findByAddress("address1"));
            futureFindByPhone = service.submit(() -> db.findByPhone("222-22-22"));
            service.shutdown();
            while (!service.isTerminated()) {

            }
            //Then
            Assertions.assertAll(
                () -> assertThat(futureFindByName.get()).containsOnly(person1),
                () -> assertThat(futureFindByAddress.get()).contains(person1, person3),
                () -> assertThat(futureFindByPhone.get()).containsOnly(person2)
            );
        } catch (InterruptedException e) {
            LOGGER.error(String.format(CAUGHT_EXCEPTION_MESSAGE_TEMPLATE, e.getMessage()));
        }
    }

}
