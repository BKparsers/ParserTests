package contentclasses;

/**
 * Created by retor on 17.03.2015.
 */
public class CategoryTree extends Tree<Event, CategoryTree> {

    public CategoryTree() {
    }

    public CategoryTree(String name, int id, String href) {
        super(name, id, href);
    }
}
