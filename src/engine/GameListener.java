package engine;

import model.world.Champion;

import java.util.ArrayList;

public interface GameListener {
    void onBoardUpdated();
    void onGameOver(Player winner);
    void onTurnOrderUpdated(PriorityQueue turnOrder);
    void onPlayerTeamsUpdated();
    void onAbilityCast();
    void onAttackHit();
}
