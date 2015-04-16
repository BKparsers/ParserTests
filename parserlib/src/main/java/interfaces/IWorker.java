package interfaces;

import contentclasses.SportTree;

import java.util.ArrayList;

/**
 * Created by retor on 30.03.2015.
 */
public interface IWorker {

    IWorker registerListener(IListener listener);

    void unregisterListener(IListener listener);

    void updateInformation(ArrayList<SportTree> trees, String parserName);

    void updateData();

    void scheduleUpdates(int seconds);

    void deleteWorker();
}
