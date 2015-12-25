package groundwar.render;

import org.lwjgl.opengl.GL11;

import groundwar.board.Board;
import groundwar.util.Constants;
import groundwar.util.Point;

public class VictoryScreen extends MainScreen {

  private final Board board;

  public VictoryScreen(long window, Board board) {
    super(window);
    this.board = board;
    GL11.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
  }

  @Override
  public void draw(Point mousePos) {
    super.draw(mousePos);
    GL11.glEnable(GL11.GL_BLEND);
    renderer.drawText(Constants.FONT_SIZE_TITLE,
                      String.format("%s wins\nin %d turns!", board.getWinner(), board.getTurnCount()),
                      Constants.NATIVE_WINDOW_WIDTH / 2, Constants.NATIVE_WINDOW_HEIGHT / 2,
                      board.getWinner().getPrimaryColor(), TextAlignment.CENTER);
    renderer.drawText(Constants.FONT_SIZE_TITLE,
                      "a b c d e f g h i j k l m n o p q r s t u v w x y z", 50,
                      50,
                      0xffff0000);
    GL11.glDisable(GL11.GL_BLEND);
  }

  @Override
  public MainScreen nextScreen() {
    return this;
  }
}
