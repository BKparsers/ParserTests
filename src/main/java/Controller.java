import contentclasses.SportTree;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import tests.interfaceTest.IListenerTest;

import java.time.ZoneId;
import java.util.*;
import java.util.function.Consumer;

public class Controller implements IListenerTest {

    @FXML
    javafx.scene.control.TextArea text;
    @FXML
    javafx.scene.control.TextArea text1;
    @FXML
    javafx.scene.control.TextArea text2;
    @FXML
    javafx.scene.control.TextArea text3;

    private TextArea[] textAreas;

    @FXML
    public void initialize() {
        textAreas = new TextArea[]{text, text1, text2, text3};
        TestWorker worker = new TestWorker();
//        //MainWorker worker = new MainWorker();
        worker.registerListener(this);
        worker.updateData();
        Arrays.asList(textAreas).stream().parallel().forEach(textArea -> textArea.setText(" Wait a minute, loading Events information"));
    }

    @Override
    public void update(HashMap<String, ArrayList<SportTree>> results) {
        results.entrySet().stream().forEach(new Consumer<Map.Entry<String, ArrayList<SportTree>>>() {
            int i = 0;

            @Override
            public void accept(Map.Entry<String, ArrayList<SportTree>> stringArrayListEntry) {
                TextArea tmp = textAreas[i];
                tmp.clear();
                tmp.autosize();
                tmp.setText(stringArrayListEntry.getKey() + '\n');
                fiilText(tmp, stringArrayListEntry.getValue());
                i++;
            }
        });
    }

    private void fiilText(TextArea txt, ArrayList<SportTree> trees) {
        txt.appendText(Calendar.getInstance().toInstant().atZone(ZoneId.systemDefault()).toString() + '\n');
        trees.parallelStream().forEach(sportTree -> Platform.runLater(() -> {
            txt.appendText(sportTree.getName() + '\n');
            sportTree.forEach(categoryTree -> {
                txt.appendText(categoryTree.getName() + '\n');
                categoryTree.forEach(event -> txt.appendText(event.toString() + '\n'));
            });
            txt.positionCaret(0);
        }));
    }
}
