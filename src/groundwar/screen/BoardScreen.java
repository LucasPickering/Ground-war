package groundwar.screen;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.util.LinkedList;
import java.util.List;

import groundwar.Board;
import groundwar.Point;
import groundwar.constants.Constants;
import groundwar.screen.event.KeyEvent;
import groundwar.screen.event.MouseButtonEvent;
import groundwar.screen.tileoverlay.SpawningUnitTileOverlay;
import groundwar.screen.tileoverlay.TileOverlay;
import groundwar.tile.Tile;
import groundwar.unit.Unit;
import groundwar.unit.UnitType;

public class BoardScreen extends MainScreen {


  private final Board board;

  public BoardScreen(long window, Board board) {
    super(window);
    this.board = board;

    TextureHandler.loadTexture(Constants.TILE_BG_NAME);
    TextureHandler.loadTexture(Constants.TILE_OUTLINE_NAME);
  }

  @Override
  public void draw(Point mousePos) {
    super.draw(mousePos);
    final int color = board.getCurrentPlayer().primaryColor & 0xcccccc;
    GL11.glClearColor((color >> 16 & 0xff) / 255.0f, (color >> 8 & 0xff) / 255.0f,
                      (color & 0xff) / 255.0f, 1.0f);

    // Draw each tile
    for (Tile tile : board.getTiles().values()) {
      final Tile selectedTile = board.getSelectedTile();
      final List<TileOverlay> overlays = new LinkedList<>();

      // If the tile is selected, add the selected overlay
      if (tile == selectedTile) {
        overlays.add(TileOverlay.selected);
      } else if (selectedTile != null && board.canMoveTo(selectedTile, tile)) {
        overlays.add(TileOverlay.movable);
      }

      if (tile.contains(mousePos)) {
        final Unit spawningUnit = board.getSpawningUnit();
        if (spawningUnit != null) {
          overlays.add(new SpawningUnitTileOverlay(spawningUnit, tile.isSpawnable(spawningUnit)));
        } else {
          overlays.add(TileOverlay.mouseOver);
        }
      }
      drawTile(tile, overlays);
    }
  }

  private void drawTile(Tile tile, List<TileOverlay> overlays) {
    final int x = tile.getScreenPos().getX();
    final int y = tile.getScreenPos().getY();
    final int width = Constants.TILE_WIDTH;
    final int height = Constants.TILE_HEIGHT;

    TextureHandler.startDrawingTextures(); // Set up the texture-drawing environment

    // Draw the regular background
    TextureHandler.draw(Constants.TILE_BG_NAME, x, y, width, height, tile.getBackgroundColor());

    // Draw the regular foreground
    TextureHandler.draw(Constants.TILE_OUTLINE_NAME, x, y, width, height, tile.getOutlineColor());

    // Draw the unit on top
    Unit unit = tile.getUnit();
    if (unit != null) {
      TextureHandler.draw(unit.getName(), x, y, width, height, unit.getOwner().primaryColor);
    }

    // Draw tile overlays
    drawOverlays(overlays, x, y, width, height);

    TextureHandler.stopDrawingTextures(); // Tear down the texture-drawing environment
  }

  private void drawOverlays(List<TileOverlay> overlays, int x, int y, int width, int height) {
    overlays.stream().forEach(overlay -> overlay.draw(x, y, width, height));
  }

  @Override
  public void onKey(KeyEvent event) {
    switch (event.key) {
      case GLFW.GLFW_KEY_M:
        board.prepareToSpawn(UnitType.MARINES);
        break;
      case GLFW.GLFW_KEY_A:
        board.prepareToSpawn(UnitType.ANTITANK);
        break;
      case GLFW.GLFW_KEY_T:
        board.prepareToSpawn(UnitType.TANK);
        break;
      case GLFW.GLFW_KEY_ESCAPE:
        board.prepareToSpawn(null);
        break;
      case GLFW.GLFW_KEY_SPACE:
        board.nextTurn();
        break;
    }
  }

  @Override
  public void onClick(MouseButtonEvent event) {
    super.onClick(event);
    for (Tile tile : board.getTiles().values()) {
      if (tile.contains(event.mousePos)) {
        board.onTileClicked(tile);
        break;
      }
    }
  }
}
