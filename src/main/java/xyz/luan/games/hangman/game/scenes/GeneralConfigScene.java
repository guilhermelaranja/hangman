package xyz.luan.games.hangman.game.scenes;

import xyz.luan.games.hangman.game.ConfigManager;
import xyz.luan.games.hangman.game.GameStatus;
import xyz.luan.games.hangman.game.I18n;
import xyz.luan.games.hangman.game.forms.FormUtils;
import xyz.luan.games.hangman.game.forms.InvalidFormException;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class GeneralConfigScene extends DefaultScene {

    private TextField[] fields;

    private static final String[] FIELD_NAMES = { "width", "height", "locale" };

    private static String getFieldName(int field) {
        return I18n.t("options." + FIELD_NAMES[field]);
    }

    @Override
    protected Pane generatePane() {
        GridPane grid = FormUtils.defaultGrid();

        Text sceneTitle = new Text(I18n.t("main.menu.options"));
        sceneTitle.getStyleClass().add("title");
        grid.add(sceneTitle, 0, 0, 2, 1);

        fields = new TextField[FIELD_NAMES.length];
        for (int i = 0; i < fields.length; i++) {
            grid.add(new Label(getFieldName(i)), 0, i + 1);
            fields[i] = new TextField();
            fields[i].setText(ConfigManager.general.config().get(FIELD_NAMES[i]));
            grid.add(fields[i], 1, i + 1);
        }

        grid.add(new StateChangeButton("common.cancel", GameStatus.MAIN, mainRef), 0, FIELD_NAMES.length + 1);
        grid.add(new StateChangeButton("common.save", event -> {
            boolean ok = true;
            for (int i = 0; i < fields.length; i++) {
                try {
                    ConfigManager.general.config().set(FIELD_NAMES[i], fields[i].getText());
                } catch (InvalidFormException ex) {
                    ok = false;
                    /* TODO put on screen */
                    System.out.println(ex.getErrorMessage(getFieldName(i)));
                }
            }
            if (ok) {
                mainRef.setStatus(GameStatus.MAIN);
            }
        }), 1, FIELD_NAMES.length + 1);

        return grid;
    }

}