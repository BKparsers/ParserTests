import contentclasses.SportTree;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tests.interfaceTest.IListenerTest;
import tests.interfaceTest.IWorkerTest;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by retor on 16.04.2015.
 */
public class TestWorkerTest {
    IWorkerTest w;
    IListenerTest listener;

    @Before
    public void setUp() throws Exception {
        w = new TestWorker();
        listener = new IListenerTest() {
            @Override
            public void update(HashMap<String, ArrayList<SportTree>> results) {

            }
        };
    }

    @Test
    public void testRegisterListener() throws Exception {
        w.registerListener(listener);
        Assert.assertNotNull(w.getListeners());
    }

    @Test
    public void testUnregisterListener() throws Exception {
        w.unregisterListener(listener);
        Assert.assertTrue(w.getListeners().isEmpty());
    }

    @Test
    public void testUpdateInformation() throws Exception {
        w.updateInformation(new HashMap<>());
        Assert.fail();
    }

    @Test
    public void testUpdateData() throws Exception {
        w.updateData();
        Assert.fail();
    }

    @Test
    public void testScheduleUpdates() throws Exception {
        w.scheduleUpdates(40);
        Assert.assertNotNull(w.getUpdater());
        w.scheduleUpdates(0);
        Assert.assertTrue(w.getUpdater().isShutdown());
    }

    @Test
    public void testDeleteWorker() throws Exception {
        w.deleteWorker();
        Assert.assertTrue(w.getUpdater().isShutdown());
        Assert.assertTrue(w.getListeners().isEmpty());
    }
}