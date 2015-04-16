package contentclasses;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by retor on 17.03.2015.
 */
public class Tree<T, T1> implements Iterable<T> {
    private int parentId;
    private ArrayList<T> items = new ArrayList<>();
    private int id;
    private String name;
    private String href;

    public Tree() {
    }

    public Tree(String name, int id, String href) {
        this.name = name;
        this.id = id;
        this.href = href;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public T1 addItem(T item) {
        this.items.add(item);
        return (T1) this;
    }

    public T1 addItems(ArrayList<T> items) {
        this.items.addAll(items);
        return (T1) this;
    }

    public ArrayList<T> getItems() {
        return items;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public Iterator<T> iterator() {
        return items.iterator();
    }
}
