import contentclasses.SportTree;
import interfaces.IListener;
import interfaces.ILoader;
import interfaces.IWorker;
import loader.SimpleJsonLoader;
import parser.MarathonParser;
import rx.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by retor on 03.04.2015.
 */
public class MainWorker implements IWorker {
    private ArrayList<SportTree> sports = new ArrayList<>();
    private ArrayList<IListener> listeners = new ArrayList<>();
    private ArrayList<ILoader> loaders = new ArrayList<>();
    private int updTimer = 0;
    private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    private HashMap<String, ArrayList<SportTree>> results = new HashMap<>();

    public MainWorker() {
        fillLoaders();
        scheduleUpdates(30);
    }

    public MainWorker(IListener listener, int schedule) {
        this.listeners.add(listener);
        this.updTimer = schedule;
        fillLoaders();
        scheduleUpdates(30);
    }

    private void fillLoaders() {
        loaders.add(new SimpleJsonLoader(new MarathonParser()));
    }

    @Override
    public IWorker registerListener(IListener listener) {
        this.listeners.add(listener);
        return this;
    }

    @Override
    public void unregisterListener(IListener listener) {
        this.listeners.remove(listener);
    }

    @Override
    public void updateInformation(ArrayList<SportTree> trees, String parserName) {
        if (!trees.isEmpty() && trees.size() > 0) {
            sports.clear();
            sports.addAll(trees);
            results.put(parserName, trees);
            updateListeners();
        } else {
            updateData();
        }
    }

    @Override
    public void updateData() {
        if (loaders.size() > 0)
            for (ILoader load : loaders) {
                load.getData()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(Schedulers.immediate())
                        .subscribe(sportTrees -> {
                            updateInformation(sportTrees, load.getParserClassName());
                        });
            }
    }

    @Override
    public void scheduleUpdates(int seconds) {
        this.updTimer = seconds;
        this.executor.scheduleAtFixedRate(() -> updateData(), 0, updTimer, TimeUnit.SECONDS);
    }

    private void updateListeners() {
        for (IListener listener : listeners) {
            listener.update(sports);
        }
    }

    @Override
    public void deleteWorker() {
        executor.shutdownNow();
        listeners.clear();
        loaders.clear();
    }
}
