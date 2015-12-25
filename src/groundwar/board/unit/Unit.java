package groundwar.board.unit;

import java.util.Objects;

import groundwar.GroundWar;
import groundwar.board.Player;
import groundwar.render.TileOverlay;

public abstract class Unit {

  private final UnitType type;
  private Player owner;
  private int movesRemaining;
  private int health;

  /**
   * Constructs a new unit.
   *
   * @param type  the type of the unit (non-null)
   * @param owner the owner of the unit (non-null)
   * @throws NullPointerException if {@code type == null} or {@code owner == null}
   */
  protected Unit(UnitType type, Player owner) {
    Objects.requireNonNull(type);
    Objects.requireNonNull(owner);

    this.type = type;
    this.owner = owner;
    resetMoves();
    health = type.maxHealth;
    GroundWar.groundWar.getRenderer().loadTexture(type.textureName);
  }

  public final UnitCategory getCategory() {
    return type.category;
  }

  public final String getTextureName() {
    return type.textureName;
  }

  public final String getDisplayName() {
    return type.displayName;
  }

  public final TileOverlay getSpawningTexture() {
    return type.spawningTexture;
  }

  public final int getCost() {
    return type.cost;
  }

  public final int getMovesPerTurn() {
    return type.movesPerTurn;
  }

  public final int getMaxHealth() {
    return type.maxHealth;
  }

  public final int getCombatStrength() {
    return type.combatStrength;
  }

  public final boolean canCarryFlag() {
    return type.canCarryFlag;
  }

  public final Player getOwner() {
    return owner;
  }

  public final int getMovesRemaining() {
    return movesRemaining;
  }

  /**
   * Can this unit move the given distance? In other words, does this unit have enough movement points
   * remaining to move the given distance?
   *
   * @param distance the distance to move, in tiles (non-negative)
   * @return true if this unit has enough movement points remaining, false otherwise
   */
  public final boolean canMove(int distance) {
    if (distance < 0) {
      throw new IllegalArgumentException("distance must be non-negative");
    }
    return distance <= movesRemaining;
  }

  /**
   * Subtracts the given distance from {@link #movesRemaining}.
   */
  public final void useMoves(int distance) {
    if (!canMove(distance)) {
      throw new IllegalStateException("Not enough movement points to move!");
    }
    movesRemaining -= distance;
  }

  /**
   * Uses up the rest of this unit's moves.
   */
  public final void exhaustMoves() {
    movesRemaining = 0;
  }

  public final int getHealth() {
    return health;
  }

  /**
   * Inflicts the given amount of damage to this unit.
   *
   * @param damage the amount of damage to inflict (non-negative)
   * @return true if the unit is still alive, false if it is now dead
   * @throws IllegalArgumentException if {@code damage < 0}
   */
  public final boolean inflictDamage(int damage) {
    if (damage < 0) {
      throw new IllegalArgumentException("Can't inflict negative damage!");
    }
    health -= damage;
    if (isDead()) {
      onKilled();
      return false;
    }
    return true;
  }

  /**
   * Is this unit dead?
   *
   * @return {@code health <= 0}
   */
  public final boolean isDead() {
    return health <= 0;
  }

  /**
   * Resets {@link #movesRemaining}. Should be called at the end of each turn.
   */
  public final void resetMoves() {
    movesRemaining = type.movesPerTurn;
  }


  /**
   * Gets the strength of this unit relative to a unit of the given category, as a float.
   *
   * <ul> <li>< 1 means this unit is weak to the given category</li> <li>1 means this unit is neutral
   * to the given category</li> <li>> 1 means this unit is strong against the given category</li>
   * </ul>
   *
   * This should always be called on the <i>attacking</i> unit, as it may provide bias towards the
   * attacking unit.
   *
   * @param category the category of the defending unit
   * @return a float, with 1 being neutral
   */
  public abstract float getStrengthVs(UnitCategory category);

  // Events
  protected void onKilled() {
  }
}
