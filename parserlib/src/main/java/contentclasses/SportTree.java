package contentclasses;

/**
 * Created by retor on 17.03.2015.
 */
public class SportTree extends Tree<CategoryTree, SportTree> {

    public SportTree() {
    }

    public SportTree(String name, int id, String href) {
        super(name, id, href);
    }

}
