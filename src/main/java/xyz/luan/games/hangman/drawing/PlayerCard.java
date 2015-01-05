package xyz.luan.games.hangman.drawing;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import xyz.luan.games.hangman.game.Profile;
import xyz.luan.games.hangman.texture.FxHelper;
import xyz.luan.games.hangman.texture.TextType;
import xyz.luan.games.hangman.texture.PackManager;
import xyz.luan.games.hangman.texture.TileManager.TileType;

public class PlayerCard extends GridPane {

	public PlayerCard(Profile profile) {
		setProperties();
		createFields(profile);
	}

	private void createFields(Profile profile) {
		add(new ImageView(PackManager.pack().getAvatar(profile.getAvatar())), 0, 0, 1, 2);
		add(FxHelper.createRawLabel(TextType.PLAYER_CARD, profile.getUsername()), 1, 0);
		add(FxHelper.createRawLabel(TextType.PLAYER_CARD, String.valueOf(profile.getPoints())), 1, 1);
	}

	private void setProperties() {
		setStyle("-fx-border-color: red;");
		setAlignment(Pos.CENTER);
		setColumnWidths();
	}

	private void setColumnWidths() {
		int totalWidth = (TileType.AVATAR.getSize() + 10) * 3;
		setMinWidth(totalWidth);
		setMaxWidth(totalWidth);
		ColumnConstraints avatarColumn = new ColumnConstraints();
		avatarColumn.setPercentWidth(100 * 1 / 3);
		ColumnConstraints nameColumn = new ColumnConstraints();
		nameColumn.setPercentWidth(100 * 2 / 3);
		getColumnConstraints().addAll(avatarColumn, nameColumn);
	}
}
