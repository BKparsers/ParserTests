import contentclasses.SportTree;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;
import tests.exceptions.WorkerException;
import tests.exceptions.loaderEx.LoaderNotFoundException;
import tests.exceptions.parserEx.ParserNotFoundException;
import tests.fabrics.Constants;
import tests.fabrics.LoadersFabric;
import tests.fabrics.ParsersFabric;
import tests.interfaceTest.IListenerTest;
import tests.interfaceTest.ITestLoader;
import tests.interfaceTest.IWorkerTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by retor on 06.04.2015.
 */
public class TestWorker implements IWorkerTest {

    private ArrayList<IListenerTest> listeners = new ArrayList<>();
    private ArrayList<ITestLoader> loaders = new ArrayList<>();
    private int updTimer = 0;
    private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    private HashMap<String, ArrayList<SportTree>> results = new HashMap<>();
    private boolean inWork = false;

    public TestWorker() {
        fillLoaders();
        scheduleUpdates(30);
    }

    public TestWorker(IListenerTest listener, int schedule) {
        this.listeners.add(listener);
        this.updTimer = schedule;
        fillLoaders();
        scheduleUpdates(30);
    }

    @Override
    public IWorkerTest registerListener(IListenerTest listener) {
        this.listeners.add(listener);
        return this;
    }

    @Override
    public void unregisterListener(IListenerTest listener) {
        this.listeners.remove(listener);
    }

    @Override
    public void updateInformation(HashMap<String, ArrayList<SportTree>> results) {
        if (!results.isEmpty() && results.size() > 0) {
            updateListeners();
        } else {
            updateData();
        }
    }

    @Override
    public void updateData() {
        if (!inWork) {
            inWork = true;
            if (loaders.size() > 0)
                doLoad();
        }
    }

    @Override
    public void scheduleUpdates(int seconds) {
        if (seconds > 0) {
            this.updTimer = seconds;
            this.executor.scheduleAtFixedRate(this::updateData, 0, updTimer, TimeUnit.SECONDS);
        } else {
            executor.shutdownNow();
        }
    }

    @Override
    public ArrayList<IListenerTest> getListeners() {
        return listeners;
    }

    @Override
    public ScheduledExecutorService getUpdater() {
        return executor;
    }

    @Override
    public void deleteWorker() {
        executor.shutdownNow();
        listeners.clear();
        loaders.clear();
    }

    private void fillLoaders(){
        try {
            loaders.add(LoadersFabric.getLoader(Constants.XBET, ParsersFabric.getParser(Constants.XBET)));
//            loaders.add(LoadersFabric.getLoader(Constants.MARATHON, ParsersFabric.getParser(Constants.MARATHON)));
//            loaders.add(LoadersFabric.getLoader(Constants.BKFONBET, ParsersFabric.getParser(Constants.BKFONBET)));
        } catch (LoaderNotFoundException | ParserNotFoundException e) {
            showError(e);
        }
/*        loaders.add(new Test1loader(new Test1parser()));
        loaders.add(new Test2Loader(new Test2Parser()));
        loaders.add(new XBetLoader(new XBetParser()));*/
    }

    private void doLoad() {
        createResult()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.immediate())
                .subscribe(TestWorker.this.results::putAll,
                        throwable -> showError(throwable).setVisible(true),
                        () -> updateInformation(TestWorker.this.results));
    }

    private void updateListeners() {
        if (results.size() > 0)
            Observable.from(listeners)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .forEach(iListenerTest -> iListenerTest.update(results),
                            throwable -> showError(throwable).setVisible(true),
                            () -> inWork = false);
    }

    private Error showError(Throwable throwable) {
        Error out = new Error();
        out.setSize(400, 200);
        out.setAlwaysOnTop(true);
        out.setLocation(300, 200);
        out.setTitle(throwable.getMessage());
        if (throwable.getCause() != null)
            out.setTextArea1(throwable.toString() + " " + throwable.getCause().getMessage());
        else
            out.setTextArea1(throwable.toString());
        return out;
    }

    private void combine(HashMap<String, ArrayList<SportTree>> results) {
/*        Combiner comb = new Combiner();
        comb.combineCategories();
        List<Observable<ArrayList<SportTree>>> observables = new ArrayList<>();
        for (ITestLoader load:loaders){
            observables.add(load.getData());
        }*/
    }

    private Observable<HashMap<String, ArrayList<SportTree>>> createResult() {
        HashMap<String, ArrayList<SportTree>> out = new HashMap<>();
        return Observable.create(new Observable.OnSubscribe<HashMap<String, ArrayList<SportTree>>>() {
            @Override
            public void call(Subscriber<? super HashMap<String, ArrayList<SportTree>>> subscriber) {
                        Observable.from(loaders)
                                .forEach(iTestLoader -> iTestLoader.getData()
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(Schedulers.immediate())
                                        .subscribe(sportTrees -> {
                                                    try {
                                                        out.put(iTestLoader.getParserClassName(), sportTrees);
                                                    } catch (ParserNotFoundException e) {
                                                        subscriber.onError(e);
                                                    }
                                                }, throwable -> subscriber.onError(new WorkerException(iTestLoader.getClass().getName()).initCause(throwable)),
                                                () -> subscriber.onNext(out)));
            }
        }).onErrorReturn(throwable -> {
            showError(throwable).setVisible(true);
            return out;
        });
    }
}
