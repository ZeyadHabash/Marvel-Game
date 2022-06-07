package views;

import engine.Game;
import engine.GameListener;
import engine.Player;
import engine.PriorityQueue;
import exceptions.*;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import model.abilities.*;
import model.world.*;

/**
 * Not sure if I should implement {@link ChampionListener} and {@link AbilityListener} here, in {@link Game}
 * or somewhere completely different
 */
public class GameBoard implements GameListener {

    GridPane boardGrid;
    Game game;
    Label[][] board = new Label[Game.getBoardheight()][Game.getBoardwidth()];
    BorderPane container;
    Label turns;
    VBox p1;
    VBox p2;
    Label p1Name;
    Label p2Name;
    Label p1Champs;
    Label p2Champs;
    HBox key;
    VBox key2;
    public Scene scene(double width, double height, Game game) {
        // setting the listener of the game, each champion and each ability to this class
        game.setListener(this);
        this.game = game;
        container = new BorderPane();
        makeBoard();

        //////////////////////////////////////
        //     Top of the board
        /////////////////////////////////////
        p1Name = new Label(game.getFirstPlayer().getName());
        p1Name.setAlignment(Pos.TOP_RIGHT);
        p1Name.setStyle("-fx-font: 24 arial;");
        p2Name = new Label(game.getSecondPlayer().getName());
        p2Name.setAlignment(Pos.TOP_RIGHT);
        p2Name.setStyle("-fx-font: 24 arial;");
        p1Champs = new Label();
        p1Champs.setAlignment(Pos.TOP_LEFT);
        p2Champs = new Label();
        p2Champs.setAlignment(Pos.TOP_RIGHT);
        setTeams();

        Button p1LeaderAbility = new Button("Use " + ((Champion) game.getFirstPlayer().getLeader()).toString() + " Leader Ability");
        Button p2LeaderAbility = new Button("Use " + ((Champion) game.getSecondPlayer().getLeader()).toString() + " Leader Ability");

        p1 = new VBox();
        p1.getChildren().addAll(p1Name, p1Champs, p1LeaderAbility );
        p1.setAlignment(Pos.CENTER_LEFT);

        p2 = new VBox();
        p2.getChildren().addAll(p2Name, p2Champs, p2LeaderAbility);
        p2.setAlignment(Pos.CENTER_RIGHT);

        turns = new Label();
        turns.setText(game.getTurnOrder().toString());
        turns.setAlignment(Pos.CENTER);

        HBox top = new HBox();
        top.setAlignment(Pos.CENTER);
        HBox.setHgrow(p1,Priority.ALWAYS);
        HBox.setHgrow(p2, Priority.ALWAYS);
        top.getChildren().addAll(p1,turns,p2);

        container.setTop(top);

        //////////////////////////////////////////
        // Player's and shit (Aka their boxes 3ala gamb 2ly 2na ba7awel 23mel-ha fy callCurrChamp
        ////////////////////////////////////////
        callCurrChamp();


        //////////////////////////////////////
        //Bottoommm
        /////////////////////////////////////
        Button up = new Button("UP");
        Button down = new Button("DOWN");
        Button left = new Button("lEFT");
        Button right = new Button("RIGHT");
        HBox bottom = new HBox();
        key = new HBox();
        key.getChildren().addAll(left,down,right);
        key.setAlignment(Pos.BOTTOM_CENTER);
        key2= new VBox();
        key2.getChildren().addAll(up,key);
        key2.setAlignment(Pos.BOTTOM_RIGHT);

        //key.setAlignment(Pos.BOTTOM_CENTER);
       // key2.setAlignment(Pos.BOTTOM_RIGHT);
        bottom.getChildren().addAll();
        container.setBottom(bottom);
        //////////////////////////////////
        //    Button Functions
        /////////////////////////////////
        //TODO find a way to make it even cooler, eshtat?
        p1LeaderAbility.setOnAction(e -> {
            try {
                game.useLeaderAbility();
                p1LeaderAbility.setDisable(!p1LeaderAbility.isDisable());
            } catch (LeaderNotCurrentException | LeaderAbilityAlreadyUsedException exception) {
                AlertBox.display("Can't use leader ability", exception.getMessage());
            } catch (CloneNotSupportedException exception) {
                exception.printStackTrace();
            }

        });
        p2LeaderAbility.setOnAction(e -> {
            try {
                game.useLeaderAbility();
                p2LeaderAbility.setDisable(!p2LeaderAbility.isDisable());
            } catch (LeaderNotCurrentException | LeaderAbilityAlreadyUsedException exception) {
                AlertBox.display("Can't use leader ability", exception.getMessage());
            } catch (CloneNotSupportedException exception) {
                exception.printStackTrace();
            }
        });
        up.setOnAction(e -> {
            try{
                game.move(Direction.UP);
            }
            catch (UnallowedMovementException | NotEnoughResourcesException | ArrayIndexOutOfBoundsException exception){
                AlertBox.display("Can't move", exception.getMessage());
            }
        });
        down.setOnAction(e -> {
            try{
                game.move(Direction.DOWN);
            }
            catch (UnallowedMovementException | NotEnoughResourcesException | ArrayIndexOutOfBoundsException exception){
                AlertBox.display("Can't move", exception.getMessage());
            }
        });
        left.setOnAction(e -> {
            try{
                game.move(Direction.LEFT);
            }
            catch (UnallowedMovementException | NotEnoughResourcesException | ArrayIndexOutOfBoundsException exception){
                AlertBox.display("Can't move", exception.getMessage());
            }
        });
        right.setOnAction(e -> {
            try{
                game.move(Direction.RIGHT);
            }
            catch (UnallowedMovementException | NotEnoughResourcesException | ArrayIndexOutOfBoundsException exception){
                AlertBox.display("Can't move", exception.getMessage());
            }
        });


        return new Scene(container, width, height);
    }

    private void makeBoard() {
        boardGrid = new GridPane();
        for (int i = 0; i < 5; i++) {
            ColumnConstraints columnConstraints = new ColumnConstraints(100);
            boardGrid.getColumnConstraints().add(columnConstraints);
        }
        for (int i = 0; i < 5; i++) {
            RowConstraints rowConstraints = new RowConstraints(100);
            boardGrid.getRowConstraints().add(rowConstraints);
        }
        boardGrid.setStyle("-fx-background-color: #D9DEEC ; -fx-grid-lines-visible: true");
        for (int i = 0; i < Game.getBoardheight(); i++) {
            for (int j = 0; j < Game.getBoardwidth(); j++) {
                board[i][j] = new Label();
                if (game.getBoard()[i][j] == null) { //TODO play bel border thickness
                    board[i][j].setText("empty");
                    board[i][j].setTextFill(Color.DARKSLATEGRAY);
                } else if (game.getBoard()[i][j] instanceof Champion) {

                    Champion thisChamp = (Champion) game.getBoard()[i][j];
                    String champAbilities = "";
                    for (Ability a : thisChamp.getAbilities()) {
                        // TODO add the rest of the ability details
                        champAbilities += ("\n" + a.getName());
                    }
                    String thisChampType;
                    if (thisChamp instanceof Hero)
                        thisChampType = "Hero";
                    else if (thisChamp instanceof Villain)
                        thisChampType = "Villain";
                    else
                        thisChampType = "AntiHero";
                    String finalChampAbilities = champAbilities;
                    Tooltip.install(board[i][j], new Tooltip(thisChamp.getName() +
                            "\nType: " + thisChampType +
                            "\nMax HP: " + thisChamp.getMaxHP() + "\nMana: " + thisChamp.getMana() +
                            "\nMax Action Points Per Turn: " + thisChamp.getMaxActionPointsPerTurn() +
                            "\nAttack Damage: " + thisChamp.getAttackDamage() + "\nAttack Range: " + thisChamp.getAttackRange()
                            + "\nSpeed: " + thisChamp.getSpeed() + "\nAbilities: " + finalChampAbilities));

                    board[i][j].setText(((Champion) game.getBoard()[i][j]).toString());
                    if (((Champion) game.getBoard()[i][j]).equals(game.getCurrentChampion())) {
                        board[i][j].setTextFill(Color.TOMATO); //أسمى ماطم زى طماطم منغير ال"ط"

                    } else if (game.getFirstPlayer().getTeam().contains((Champion) game.getBoard()[i][j])) {
                        board[i][j].setTextFill(Color.BLUEVIOLET);
                    } else {
                        board[i][j].setTextFill(Color.CORNFLOWERBLUE);
                    }
                } else {
                    board[i][j].setText("Cover HP:\n" + ((Cover) game.getBoard()[i][j]).getCurrentHP());
                    board[i][j].setTextFill(Color.DEEPPINK);
                                   }
                GridPane.setConstraints(board[i][j], j, Game.getBoardheight() - 1 - i);
                boardGrid.getChildren().add(board[i][j]); //Please run badal ma haneekak.
            }
        }
        boardGrid.setAlignment(Pos.CENTER);
        container.setCenter(boardGrid);
    }

    private void setTeams(){
        String p1 = "";
        String p2 = "";
        for (int i =0; i< game.getFirstPlayer().getTeam().size(); i++){
            p1+= ((Champion) game.getFirstPlayer().getTeam().get(i)).toString() + "\n";
        }
        p1Champs.setText(p1);

        for (int j =0; j< game.getSecondPlayer().getTeam().size(); j++){
            p2+= ((Champion) game.getSecondPlayer().getTeam().get(j)).toString() + "\n";
        }
        p2Champs.setText(p2);
    }

    private void callCurrChamp() { //TODO find a way to link b 2wel kol door
        VBox p1ChampionInfo = new VBox();
        p1ChampionInfo.setStyle("-fx-background-color: blueviolet ;");
        p1ChampionInfo.setMinWidth(300);
//        p1ChampionInfo.setMaxHeight(700);
        VBox p2ChampionInfo = new VBox();
        p2ChampionInfo.setMinWidth(300);
//        p2ChampionInfo.setMaxHeight(700);
        p2ChampionInfo.setStyle("-fx-background-color: cornflowerblue ;");
        String type;
        Champion curr = game.getCurrentChampion();
        if (curr instanceof AntiHero) {
            type = "AntiHero";
        } else if (curr instanceof Hero) {
            type = "Hero";
        } else {
            type = "Villain";
        }
        if ((game.getFirstPlayer().getTeam()).contains(curr)) {
            p2ChampionInfo.setVisible(false);
            Label ch1 = new Label();
            ch1.setText("Name: " + curr.toString() +
                    "\nType: " + type +
                    "\nHP: " + curr.getCurrentHP() +
                    "\nMana: " + curr.getMana() +
                    "\nAP: " + curr.getCurrentActionPoints() +
                    "\nAttack Damage: " + curr.getAttackDamage() +
                    "\nAttack Range: " + curr.getAttackRange()+
                    "\nAbilites: "); //TODO find a way to update Mana and AP... with each turn
            Label ch1Ability1 = new Label();
            ch1Ability1.setText("1) " + curr.getAbilities().get(0).getName()); //TODO zabaty 2l display
            Label ch1Ability2 = new Label();
            ch1Ability2.setText("2) " +curr.getAbilities().get(1).getName());
            Label ch1Ability3 = new Label();
            ch1Ability3.setText("3) " + curr.getAbilities().get(2).getName());
            /*for (Ability a : curr.getAbilities()) {
                String extraInfo = "";
                String type;
                if (a instanceof DamagingAbility) {
                    type = "\nType: Damaging";
                    extraInfo += "\nDamage Amount: " + ((DamagingAbility) a).getDamageAmount();
                } else if (a instanceof HealingAbility) {
                    type = "\nType: Healing";
                    extraInfo += "\nHeal Amount: " + ((HealingAbility) a).getHealAmount();
                } else {
                    type = "\nType: Crowd Control";
                    extraInfo += "\nEffect: " + ((CrowdControlAbility) a).getEffect().getName() + "   Duration: " + ((CrowdControlAbility) a).getEffect().getDuration();
                }
                int currIndex = curr.getAbilities().indexOf(a);
                abilityInfo.get(currIndex).setText(a.getName());
                abilityInfo.get(currIndex).setTooltip(new Tooltip(a.getName() + type + "\nMana Cost: " + a.getManaCost() +
                        "\nAP Required: " + a.getRequiredActionPoints() + "\nBase Cooldown: " + a.getBaseCooldown() +
                        "\nArea of Effect: " + a.getCastArea() + "\nRange: " + a.getCastRange() + extraInfo));
            }
  /*          Tooltip.install(curr.getAbilities().get(0), new Tooltip("Name: " + curr.getAbilities().get(0).getName() +
                    "\nType: " + thisChampType +
                    "\nEffect Area: " + thisChamp.getMaxHP() +
                    "\nCast Range: " + thisChamp.getMana() +
                    "\nMane: " + thisChamp.getMaxActionPointsPerTurn() +
                    "\nAction Cost: " + thisChamp.getAttackDamage() + "\nAttack Range: " + thisChamp.getAttackRange()
                    + "\nCooldown: " + thisChamp.getSpeed() +
                    "\nAbilities: " + finalChampAbilities));
*/
        p1ChampionInfo.getChildren().addAll(ch1, ch1Ability1, ch1Ability2, ch1Ability3);
        p1ChampionInfo.setAlignment(Pos.CENTER);
        container.setLeft(p1ChampionInfo);
        }
        else{
            p1ChampionInfo.setVisible(false);
            Label ch2 = new Label();
            ch2.setText("Name: " + curr.toString() +
                    "\nType: " + type +
                    "\nHP: " + curr.getCurrentHP() +
                    "\nMana: " + curr.getMana() +
                    "\nAP: " + curr.getCurrentActionPoints() +
                    "\nAttack Damage: " + curr.getAttackDamage() +
                    "\nAttack Range: " + curr.getAttackRange()+
                    "\nAbilites: "); //TODO find a way to update Mana and AP... with each turn
            Label ch2Ability1 = new Label();
            ch2Ability1.setText("1) " + curr.getAbilities().get(0).getName()); //TODO zabaty 2l display
            Label ch2Ability2 = new Label();
            ch2Ability2.setText("2) " + curr.getAbilities().get(1).getName());
            Label ch2Ability3 = new Label();
            ch2Ability3.setText("3) " + curr.getAbilities().get(2).getName());
            p2ChampionInfo.getChildren().addAll(ch2, ch2Ability1, ch2Ability2, ch2Ability3);
            p2ChampionInfo.setAlignment(Pos.CENTER);
            container.setRight(p2ChampionInfo);
        }


        }

    @Override
    public void onBoardUpdated() {
        makeBoard();
    }

    @Override
    public void onGameOver(Player winner) {
        // TODO show an alertbox with winning player name and send back to start screen?

    }

    @Override
    public void onTurnOrderUpdated(PriorityQueue turnOrder) {
        turns.setText(game.getTurnOrder().toString());
    }

    @Override
    public void onPlayerTeamsUpdated() {
        // TODO redraw both teams
        setTeams();
    }

    @Override
    public void onAbilityCast() {
        // TODO update all champion details
    }

    @Override
    public void onAttackHit() {
        // TODO update all champion details
    }
}
