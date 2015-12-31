package groundwar.render.screen;

import org.lwjgl.opengl.GL11;

import groundwar.GroundWar;
import groundwar.board.Board;
import groundwar.render.HorizAlignment;
import groundwar.render.VertAlignment;
import groundwar.render.event.MouseButtonEvent;
import groundwar.render.screen.gui.Button;
import groundwar.render.screen.gui.GuiElement;
import groundwar.util.Constants;
import groundwar.util.Point;

public class VictoryScreen extends MainScreen {

  private static final int MESSAGE_Y = 350;
  private static final int MAIN_MENU_BUTTON_Y = 1000;
  private static final int EXIT_BUTTON_Y = 1200;

  private final Board board;
  private final Button menuButton;
  private final Button exitButton;
  private boolean backToMenu;

  public VictoryScreen(Board board) {
    this.board = board;
    addGuiElement(menuButton = new Button("Main Menu", new Point(center.getX(),
                                                                 MAIN_MENU_BUTTON_Y),
                                          HorizAlignment.CENTER, VertAlignment.TOP));
    addGuiElement(exitButton = new Button("Exit Game", new Point(center.getX(),
                                                                 EXIT_BUTTON_Y),
                                          HorizAlignment.CENTER, VertAlignment.TOP));
  }

  @Override
  public void draw(Point mousePos) {
    super.draw(mousePos);
    GL11.glEnable(GL11.GL_BLEND);
    GL11.glEnable(GL11.GL_TEXTURE_2D);
    renderer().drawString(
        Constants.FONT_SIZE_TITLE,
        String.format("%s wins\nin %d turns!", board.getWinner().getName(), board.getTurnCount()),
        center.getX(), MESSAGE_Y, board.getWinner().getPrimaryColor(),
        HorizAlignment.CENTER, VertAlignment.CENTER);
    GL11.glDisable(GL11.GL_TEXTURE_2D);
    GL11.glDisable(GL11.GL_BLEND);
  }

  @Override
  public MainScreen nextScreen() {
    if (backToMenu) {
      return new MainMenuScreen();
    }
    return this;
  }

  @Override
  public void onElementClicked(MouseButtonEvent event, GuiElement element) {
    if (element == menuButton) {
      backToMenu = true;
    } else if (element == exitButton) {
      GroundWar.groundWar.exitGame();
    }
  }
}
