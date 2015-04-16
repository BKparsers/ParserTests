package tests.interfaceTest;

import contentclasses.SportTree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by retor on 30.03.2015.
 */
public interface IWorkerTest {

    IWorkerTest registerListener(IListenerTest listener);

    void unregisterListener(IListenerTest listener);

    void updateInformation(HashMap<String, ArrayList<SportTree>> results);

    void updateData();

    void scheduleUpdates(int seconds);

    void deleteWorker();

    ArrayList<IListenerTest> getListeners();

    ScheduledExecutorService getUpdater();
}
