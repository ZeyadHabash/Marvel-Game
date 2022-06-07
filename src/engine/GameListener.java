package engine;

import model.world.Champion;

import java.util.ArrayList;

public interface GameListener {
    void onBoardUpdated();
    void onGameOver(Player winner);
    void onTurnEnd();
    void onPlayerTeamsUpdated();
    void onAbilityCast();
    void onAttackHit();
}
