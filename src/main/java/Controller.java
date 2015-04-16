import contentclasses.CategoryTree;
import contentclasses.Event;
import contentclasses.SportTree;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import tests.interfaceTest.IListenerTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Controller implements IListenerTest {

    @FXML
    javafx.scene.control.TextArea text;
    @FXML
    javafx.scene.control.TextArea text1;
    @FXML
    javafx.scene.control.TextArea text2;
    @FXML
    javafx.scene.control.TextArea text3;

    private ArrayList<TextArea> texts = new ArrayList<>();

    @FXML
    public void initialize() {
        TestWorker worker = new TestWorker();
//        //MainWorker worker = new MainWorker();
        worker.registerListener(this);
        worker.updateData();
        initTexts();
        for (TextArea t:texts){
            t.setText(" Wait a minute, loading Events information");
        }
    }

    @Override
    public void update(HashMap<String, ArrayList<SportTree>> results) {
        int i = 0;
        for (Map.Entry<String, ArrayList<SportTree>> entry : results.entrySet()) {
            TextArea tmp = texts.get(i);
            tmp.clear();
            tmp.autosize();
            tmp.setText(entry.getKey()+'\n');
            fiilText(tmp, entry.getValue());
            i++;
        }
    }

    private void fiilText(TextArea txt, ArrayList<SportTree> trees){
        Platform.runLater(() -> {
            for (SportTree tree : trees) {
                txt.appendText(tree.getName() + '\n');
                for (CategoryTree categoryTree : tree) {
                    txt.appendText(categoryTree.getName() + '\n');
                    for (Event event : categoryTree) {
                        if (event != null && event.toString().length() > 0)
                            txt.appendText(event.toString() + '\n');
                    }
                }
            }
            txt.positionCaret(0);
        });
    }

    private void initTexts(){
        texts.add(text);
        texts.add(text1);
        texts.add(text2);
        texts.add(text3);
    }
}
