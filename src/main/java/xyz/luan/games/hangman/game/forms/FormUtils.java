package xyz.luan.games.hangman.game.forms;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;

public final class FormUtils {

    private FormUtils() {
        throw new RuntimeException("Should not be instanciated.");
    }

    public static GridPane defaultGrid() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        return grid;
    }
}