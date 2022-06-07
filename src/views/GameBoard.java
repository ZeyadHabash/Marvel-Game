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
import model.effects.Disarm;
import model.effects.Effect;
import model.effects.Root;
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
    VBox p1ChampionInfo;
    VBox p2ChampionInfo;
    Label ch1 = new Label();
    Button[] ch1Abilities = new Button[3];
    Label ch2 = new Label();
    Button[] ch2Abilities = new Button[3];
    Button up;
    Button down;
    Button left;
    Button right;
    Button atk;
    Button move;
    Button punch;

    int xCoordinate;
    int yCoordinate;

    boolean currChampDisarmed = false;
    boolean currChampRooted = false;

    public Scene scene(Game game) {
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
        p1.getChildren().addAll(p1Name, p1Champs, p1LeaderAbility);
        p1.setAlignment(Pos.CENTER_LEFT);

        p2 = new VBox();
        p2.getChildren().addAll(p2Name, p2Champs, p2LeaderAbility);
        p2.setAlignment(Pos.CENTER_RIGHT);

        turns = new Label();
        turns.setText(game.getTurnOrder().toString());
        turns.setAlignment(Pos.CENTER);

        HBox top = new HBox();
        top.setAlignment(Pos.CENTER);
        HBox.setHgrow(p1, Priority.ALWAYS);
        HBox.setHgrow(p2, Priority.ALWAYS);
        top.getChildren().addAll(p1, turns, p2);

        container.setTop(top);


        //////////////////////////////////////
        //Bottoommm
        /////////////////////////////////////
        up = new Button("UP");
        up.setDisable(true);
        down = new Button("DOWN");
        down.setDisable(true);
        left = new Button("LEFT");
        left.setDisable(true);
        right = new Button("RIGHT");
        right.setDisable(true);

        atk = new Button("ATK");
        move = new Button("MOVE");
        VBox command = new VBox();
        command.getChildren().addAll(atk, move);
        HBox bottom = new HBox();

        bottom.setMinHeight(100); //negarab 3ady w neshoof


        key = new HBox();
        key.getChildren().addAll(left, down, right);

        key2 = new VBox();
        key2.getChildren().addAll(up, key);
        up.setAlignment(Pos.TOP_CENTER);
        key.setAlignment(Pos.BOTTOM_CENTER);
        key2.setAlignment(Pos.BOTTOM_RIGHT);

        Button endTurn = new Button("END TURN");

        endTurn.setAlignment(Pos.BOTTOM_LEFT);
        bottom.getChildren().addAll(key2, command, endTurn);
        HBox.setHgrow(key2, Priority.ALWAYS);
        HBox.setHgrow(endTurn, Priority.ALWAYS);
        HBox.setHgrow(command, Priority.ALWAYS);
        //HBox.setHgrow(move, Priority.ALWAYS);
        container.setBottom(bottom);


        //////////////////////////////////
        //    Button Functions
        /////////////////////////////////
        //TODO find a way to make it even cooler, eshtat?
        //LA buttons
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

        //Direction buttons

        //Atk button
        atk.setOnAction(e -> {
            if (!currChampDisarmed)
                up.setDisable(false);
            down.setDisable(false);
            left.setDisable(false);
            right.setDisable(false);
            up.setOnAction(l -> {
                try {
                    up.setDisable(true);
                    down.setDisable(true);
                    left.setDisable(true);
                    right.setDisable(true);
                    game.attack(Direction.UP);
                } catch (NotEnoughResourcesException | ChampionDisarmedException exception) {
                    AlertBox.display("Can't attack", exception.getMessage());
                } catch (CloneNotSupportedException exception) {
                    exception.printStackTrace();
                }

            });
            down.setOnAction(l -> {
                try {
                    up.setDisable(true);
                    down.setDisable(true);
                    left.setDisable(true);
                    right.setDisable(true);
                    game.attack(Direction.DOWN);
                } catch (NotEnoughResourcesException | ChampionDisarmedException exception) {
                    AlertBox.display("Can't attack", exception.getMessage());
                } catch (CloneNotSupportedException exception) {
                    exception.printStackTrace();
                }
            });
            left.setOnAction(l -> {
                try {
                    up.setDisable(true);
                    down.setDisable(true);
                    left.setDisable(true);
                    right.setDisable(true);
                    game.attack(Direction.LEFT);
                } catch (NotEnoughResourcesException | ChampionDisarmedException exception) {
                    AlertBox.display("Can't attack", exception.getMessage());
                } catch (CloneNotSupportedException exception) {
                    exception.printStackTrace();
                }
            });
            right.setOnAction(l -> {
                try {
                    up.setDisable(true);
                    down.setDisable(true);
                    left.setDisable(true);
                    right.setDisable(true);
                    game.attack(Direction.RIGHT);
                } catch (NotEnoughResourcesException | ChampionDisarmedException exception) {
                    AlertBox.display("Can't attack", exception.getMessage());
                } catch (CloneNotSupportedException exception) {
                    exception.printStackTrace();
                }
            });

        });

        //move button
        move.setOnAction(e -> {
            up.setDisable(false);
            down.setDisable(false);
            left.setDisable(false);
            right.setDisable(false);
            up.setOnAction(l -> {
                try {
                    up.setDisable(true);
                    down.setDisable(true);
                    left.setDisable(true);
                    right.setDisable(true);
                    game.move(Direction.UP);
                } catch (UnallowedMovementException | NotEnoughResourcesException | ArrayIndexOutOfBoundsException exception) {
                    AlertBox.display("Can't move", exception.getMessage());
                }
            });
            down.setOnAction(l -> {
                try {
                    up.setDisable(true);
                    down.setDisable(true);
                    left.setDisable(true);
                    right.setDisable(true);
                    game.move(Direction.DOWN);
                } catch (UnallowedMovementException | NotEnoughResourcesException | ArrayIndexOutOfBoundsException exception) {
                    AlertBox.display("Can't move", exception.getMessage());
                }
            });
            left.setOnAction(l -> {
                try {
                    up.setDisable(true);
                    down.setDisable(true);
                    left.setDisable(true);
                    right.setDisable(true);
                    game.move(Direction.LEFT);
                } catch (UnallowedMovementException | NotEnoughResourcesException | ArrayIndexOutOfBoundsException exception) {
                    AlertBox.display("Can't move", exception.getMessage());
                }
            });
            right.setOnAction(l -> {
                try {
                    up.setDisable(true);
                    down.setDisable(true);
                    left.setDisable(true);
                    right.setDisable(true);
                    game.move(Direction.RIGHT);
                } catch (UnallowedMovementException | NotEnoughResourcesException | ArrayIndexOutOfBoundsException exception) {
                    AlertBox.display("Can't move", exception.getMessage());
                }
            });

        });

        //end turn button
        endTurn.setOnAction(e -> {
            try {
                game.endTurn();
            } catch (CloneNotSupportedException ex) {
                ex.printStackTrace();
            }
        });
        //////////////////////////////////////////
        // Player's and shit (Aka their boxes 3ala gamb 2ly 2na ba7awel 23mel-ha fy callCurrChamp
        ////////////////////////////////////////
        p1ChampionInfo = new VBox();
        p2ChampionInfo = new VBox();
        punch = new Button("PUNCH");
        punch.setDisable(true);
        punch.setVisible(false);
        for (int i = 0; i < 3; i++) {
            ch2Abilities[i] = new Button();
            ch1Abilities[i] = new Button();
        }

        callCurrChamp();
        p1ChampionInfo.getChildren().addAll(ch1, ch1Abilities[0], ch1Abilities[1], ch1Abilities[2], punch);
        p1ChampionInfo.setAlignment(Pos.CENTER);
        container.setLeft(p1ChampionInfo);
        p2ChampionInfo.getChildren().addAll(ch2, ch2Abilities[0], ch2Abilities[1], ch2Abilities[2], punch);
        p2ChampionInfo.setAlignment(Pos.CENTER);
        container.setRight(p2ChampionInfo);

        return new Scene(container, StartScreen.screenSize.getWidth(), StartScreen.screenSize.getHeight() - 40);
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
        boardGrid.setStyle("-fx-background-color: white ; -fx-grid-lines-visible: true");
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
                    String effects = "";
                    for (Effect e : thisChamp.getAppliedEffects()) {
                        effects += "\n" + e.getName() + "  " + e.getDuration() + " turns";
                    }
                    if (effects.equals(""))
                        effects = "none";
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
                            "\nHP: " + thisChamp.getCurrentHP() + "/" + thisChamp.getMaxHP() + "\nMana: " + thisChamp.getMana() +
                            "\nMax Action Points Per Turn: " + thisChamp.getMaxActionPointsPerTurn() +
                            "\nAttack Damage: " + thisChamp.getAttackDamage() + "\nAttack Range: " + thisChamp.getAttackRange() +
                            "\nSpeed: " + thisChamp.getSpeed() + "\nCondition: " + thisChamp.getCondition() +
                            "\nApplied effects: " + effects +
                            "\nAbilities: " + finalChampAbilities));
                    board[i][j].setText(((Champion) game.getBoard()[i][j]).toString());
                    if (((Champion) game.getBoard()[i][j]).equals(game.getCurrentChampion())) {
                        board[i][j].setTextFill(Color.TOMATO); //أسمى ماطم زى طماطم منغير ال"ط"

                    } else if (game.getFirstPlayer().getTeam().contains((Champion) game.getBoard()[i][j])) {
                        if (((Champion) game.getBoard()[i][j]).equals(game.getFirstPlayer().getLeader()))
                            board[i][j].setText(((Champion) game.getBoard()[i][j]).toString() + "\nLeader");
                        board[i][j].setTextFill(Color.BLUEVIOLET);
                    } else {
                        if (((Champion) game.getBoard()[i][j]).equals(game.getSecondPlayer().getLeader()))
                            board[i][j].setText(((Champion) game.getBoard()[i][j]).toString() + "\nLeader");
                        board[i][j].setTextFill(Color.CORNFLOWERBLUE);
                    }
                } else {
                    board[i][j].setText("Cover\nHP: " + ((Cover) game.getBoard()[i][j]).getCurrentHP());
                    board[i][j].setTextFill(Color.DEEPPINK);
                }
                int finalI = i;
                int finalJ = j;
                board[i][j].setOnMouseClicked(e -> {
                    xCoordinate = finalI;
                    yCoordinate = finalJ;
                });
                GridPane.setConstraints(board[i][j], j, Game.getBoardheight() - 1 - i);
                boardGrid.getChildren().add(board[i][j]);
            }
        }
        boardGrid.setAlignment(Pos.CENTER);
        container.setCenter(boardGrid);
    }

    private void setTeams() {
        String p1 = "";
        String p2 = "";
        for (int i = 0; i < game.getFirstPlayer().getTeam().size(); i++) {
            p1 += ((Champion) game.getFirstPlayer().getTeam().get(i)).toString() + "\n";
        }
        p1Champs.setText(p1);

        for (int j = 0; j < game.getSecondPlayer().getTeam().size(); j++) {
            p2 += ((Champion) game.getSecondPlayer().getTeam().get(j)).toString() + "\n";
        }
        p2Champs.setText(p2);
    }

    private void callCurrChamp() {
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
            p1ChampionInfo.setVisible(true);
            p1ChampionInfo.setStyle("-fx-background-color: blueviolet ;");
            p1ChampionInfo.setMaxHeight(700);
            p1ChampionInfo.setMinWidth(300);

            String effects = "";
            for (Effect e : curr.getAppliedEffects()) {
                if (e instanceof Disarm) {
                    currChampDisarmed = true;
                    DamagingAbility punchAb = (DamagingAbility) curr.getAbilities().get(3);
                    punch.setTooltip(new Tooltip(punchAb.getName() + "\nType: Damaging" + "\nMana Cost: " + punchAb.getManaCost() +
                            "\nAP Required: " + punchAb.getRequiredActionPoints() + "\nCurrent Cooldown: " + punchAb.getCurrentCooldown() + "\nBase Cooldown: " + punchAb.getBaseCooldown() +
                            "\nArea of Effect: " + punchAb.getCastArea() + "\nRange: " + punchAb.getCastRange() + "\nDamage Amount" + punchAb.getDamageAmount()));
                    punch.setOnAction(l -> {
                        try {
                            game.castAbility(punchAb, xCoordinate, yCoordinate);
                        } catch (AbilityUseException | NotEnoughResourcesException | InvalidTargetException exception) {
                            AlertBox.display("Can't use ability", exception.getMessage());
                        } catch (CloneNotSupportedException exception) {
                            exception.printStackTrace();
                        }
                    });
                }
                if (e instanceof Root)
                    currChampRooted = true;
                effects += "\n" + e.getName() + "  Duration: " + e.getDuration();
            }
            if (effects.equals(""))
                effects = "none";
            ch1.setText("Name: " + curr.toString() +
                    "\nType: " + type +
                    "\nHP: " + curr.getCurrentHP() + "/" + curr.getMaxHP() +
                    "\nMana: " + curr.getMana() +
                    "\nAP: " + curr.getCurrentActionPoints() +
                    "\nAttack Damage: " + curr.getAttackDamage() +
                    "\nAttack Range: " + curr.getAttackRange() +
                    "\nApplied effects: " + effects +
                    "\nAbilites: ");

            for (int currIndex = 0; currIndex < 3; currIndex++) {
                Ability a = curr.getAbilities().get(currIndex);
                ch1Abilities[currIndex].setText(a.getName());
                String extraInfo = "";
                String abilityType;
                if (a instanceof DamagingAbility) {
                    abilityType = "\nType: Damaging";
                    extraInfo += "\nDamage Amount: " + ((DamagingAbility) a).getDamageAmount();
                } else if (a instanceof HealingAbility) {
                    abilityType = "\nType: Healing";
                    extraInfo += "\nHeal Amount: " + ((HealingAbility) a).getHealAmount();
                } else {
                    abilityType = "\nType: Crowd Control";
                    extraInfo += "\nEffect: " + ((CrowdControlAbility) a).getEffect().getName() + "   Duration: " + ((CrowdControlAbility) a).getEffect().getDuration();
                }
                ch1Abilities[currIndex].setTooltip(new Tooltip(a.getName() + abilityType + "\nMana Cost: " + a.getManaCost() +
                        "\nAP Required: " + a.getRequiredActionPoints() + "\nCurrent Cooldown: " + a.getCurrentCooldown() + "\nBase Cooldown: " + a.getBaseCooldown() +
                        "\nArea of Effect: " + a.getCastArea() + "\nRange: " + a.getCastRange() + extraInfo));

                if (a.getCastArea() == AreaOfEffect.SELFTARGET || a.getCastArea() == AreaOfEffect.TEAMTARGET || a.getCastArea() == AreaOfEffect.SURROUND) {
                    ch1Abilities[currIndex].setOnAction(e -> {
                        try {
                            game.castAbility(a);
                        } catch (NotEnoughResourcesException | AbilityUseException exception) {
                            AlertBox.display("Can't use ability", exception.getMessage());
                        } catch (CloneNotSupportedException exception) {
                            exception.printStackTrace();
                        }
                    });
                } else if (a.getCastArea() == AreaOfEffect.SINGLETARGET) {
                    // TODO MAKE IT SO THAT YOU TARGET AFTER CLICKING THE BUTTON
                    ch1Abilities[currIndex].setOnAction(e -> {
                        //AlertBox.display("Single Target Ability", "Choose a target from the board");
                        try {
                            game.castAbility(a, xCoordinate, yCoordinate);
                        } catch (AbilityUseException | NotEnoughResourcesException | InvalidTargetException exception) {
                            AlertBox.display("Can't use ability", exception.getMessage());
                        } catch (CloneNotSupportedException exception) {
                            exception.printStackTrace();
                        }
                    });
                } else if (a.getCastArea() == AreaOfEffect.DIRECTIONAL) {
                    ch1Abilities[currIndex].setOnAction(e -> {
                        up.setDisable(false);
                        down.setDisable(false);
                        left.setDisable(false);
                        right.setDisable(false);
                        up.setOnAction(l -> {
                            try {
                                up.setDisable(true);
                                down.setDisable(true);
                                left.setDisable(true);
                                right.setDisable(true);
                                game.castAbility(a, Direction.UP);
                            } catch (NotEnoughResourcesException | AbilityUseException exception) {
                                AlertBox.display("Can't attack", exception.getMessage());
                            } catch (CloneNotSupportedException exception) {
                                exception.printStackTrace();
                            }

                        });
                        down.setOnAction(l -> {
                            try {
                                up.setDisable(true);
                                down.setDisable(true);
                                left.setDisable(true);
                                right.setDisable(true);
                                game.castAbility(a, Direction.DOWN);
                            } catch (NotEnoughResourcesException | AbilityUseException exception) {
                                AlertBox.display("Can't attack", exception.getMessage());
                            } catch (CloneNotSupportedException exception) {
                                exception.printStackTrace();
                            }
                        });
                        left.setOnAction(l -> {
                            try {
                                up.setDisable(true);
                                down.setDisable(true);
                                left.setDisable(true);
                                right.setDisable(true);
                                game.castAbility(a, Direction.LEFT);
                            } catch (NotEnoughResourcesException | AbilityUseException exception) {
                                AlertBox.display("Can't attack", exception.getMessage());
                            } catch (CloneNotSupportedException exception) {
                                exception.printStackTrace();
                            }
                        });
                        right.setOnAction(l -> {
                            try {
                                up.setDisable(true);
                                down.setDisable(true);
                                left.setDisable(true);
                                right.setDisable(true);
                                game.castAbility(a, Direction.RIGHT);
                            } catch (NotEnoughResourcesException | AbilityUseException exception) {
                                AlertBox.display("Can't attack", exception.getMessage());
                            } catch (CloneNotSupportedException exception) {
                                exception.printStackTrace();
                            }
                        });

                    });
                }
                if (currChampDisarmed) {
                    punch.setDisable(false);
                    punch.setVisible(true);
                } else {
                    punch.setDisable(true);
                    punch.setVisible(false);
                }

            }

        } else {
            p1ChampionInfo.setVisible(false);
            p2ChampionInfo.setVisible(true);
            p2ChampionInfo.setMinWidth(300);
            p2ChampionInfo.setMaxHeight(700);
            p2ChampionInfo.setStyle("-fx-background-color: cornflowerblue ;");

            String effects = "";
            for (Effect e : curr.getAppliedEffects()) {
                if (e instanceof Disarm) {
                    currChampDisarmed = true;
                    DamagingAbility punchAb = (DamagingAbility) curr.getAbilities().get(3);
                    punch.setTooltip(new Tooltip(punchAb.getName() + "\nType: Damaging" + "\nMana Cost: " + punchAb.getManaCost() +
                            "\nAP Required: " + punchAb.getRequiredActionPoints() + "\nCurrent Cooldown: " + punchAb.getCurrentCooldown() + "\nBase Cooldown: " + punchAb.getBaseCooldown() +
                            "\nArea of Effect: " + punchAb.getCastArea() + "\nRange: " + punchAb.getCastRange() + "\nDamage Amount" + punchAb.getDamageAmount()));
                    punch.setOnAction(l -> {
                        try {
                            game.castAbility(punchAb, xCoordinate, yCoordinate);
                        } catch (AbilityUseException | NotEnoughResourcesException | InvalidTargetException exception) {
                            AlertBox.display("Can't use ability", exception.getMessage());
                        } catch (CloneNotSupportedException exception) {
                            exception.printStackTrace();
                        }
                    });
                }
                if (e instanceof Root)
                    currChampRooted = true;
                effects += "\n" + e.getName() + "  Duration: " + e.getDuration();
            }
            if (effects.equals(""))
                effects = "none";
            ch2.setText("Name: " + curr.toString() +
                    "\nType: " + type +
                    "\nHP: " + curr.getCurrentHP() + "/" + curr.getMaxHP()+
                    "\nMana: " + curr.getMana() +
                    "\nAction Points: " + curr.getCurrentActionPoints() +
                    "\nAttack Damage: " + curr.getAttackDamage() +
                    "\nAttack Range: " + curr.getAttackRange() +
                    "\nApplied effects: " + effects +
                    "\nAbilites: ");
            for (int currIndex = 0; currIndex < 3; currIndex++) {
                Ability a = curr.getAbilities().get(currIndex);
                ch2Abilities[currIndex].setText(a.getName());
                String extraInfo = "";
                String abilityType;
                if (a instanceof DamagingAbility) {
                    abilityType = "\nType: Damaging";
                    extraInfo += "\nDamage Amount: " + ((DamagingAbility) a).getDamageAmount();
                } else if (a instanceof HealingAbility) {
                    abilityType = "\nType: Healing";
                    extraInfo += "\nHeal Amount: " + ((HealingAbility) a).getHealAmount();
                } else {
                    abilityType = "\nType: Crowd Control";
                    extraInfo += "\nEffect: " + ((CrowdControlAbility) a).getEffect().getName() + "   Duration: " + ((CrowdControlAbility) a).getEffect().getDuration();
                }
                ch2Abilities[currIndex].setTooltip(new Tooltip(a.getName() + abilityType + "\nMana Cost: " + a.getManaCost() +
                        "\nAP Required: " + a.getRequiredActionPoints() + "\nCurrent Cooldown: " + a.getCurrentCooldown() + "\nBase Cooldown: " + a.getBaseCooldown() +
                        "\nArea of Effect: " + a.getCastArea() + "\nRange: " + a.getCastRange() + extraInfo));

                if (a.getCastArea() == AreaOfEffect.SELFTARGET || a.getCastArea() == AreaOfEffect.TEAMTARGET || a.getCastArea() == AreaOfEffect.SURROUND) {
                    ch2Abilities[currIndex].setOnAction(e -> {
                        try {
                            game.castAbility(a);
                        } catch (NotEnoughResourcesException | AbilityUseException exception) {
                            AlertBox.display("Can't use ability", exception.getMessage());
                        } catch (CloneNotSupportedException exception) {
                            exception.printStackTrace();
                        }
                    });
                } else if (a.getCastArea() == AreaOfEffect.SINGLETARGET) {
                    // TODO MAKE IT SO THAT YOU TARGET AFTER CLICKING THE BUTTON
                    ch2Abilities[currIndex].setOnAction(e -> {
                        //AlertBox.display("Single Target Ability", "Choose a target from the board");
                        try {
                            game.castAbility(a, xCoordinate, yCoordinate);
                        } catch (AbilityUseException | NotEnoughResourcesException | InvalidTargetException exception) {
                            AlertBox.display("Can't use ability", exception.getMessage());
                        } catch (CloneNotSupportedException exception) {
                            exception.printStackTrace();
                        }
                    });
                } else if (a.getCastArea() == AreaOfEffect.DIRECTIONAL) {
                    ch2Abilities[currIndex].setOnAction(e -> {
                        up.setDisable(false);
                        down.setDisable(false);
                        left.setDisable(false);
                        right.setDisable(false);
                        up.setOnAction(l -> {
                            try {
                                up.setDisable(true);
                                down.setDisable(true);
                                left.setDisable(true);
                                right.setDisable(true);
                                game.castAbility(a, Direction.UP);
                            } catch (NotEnoughResourcesException | AbilityUseException exception) {
                                AlertBox.display("Can't attack", exception.getMessage());
                            } catch (CloneNotSupportedException exception) {
                                exception.printStackTrace();
                            }

                        });
                        down.setOnAction(l -> {
                            try {
                                up.setDisable(true);
                                down.setDisable(true);
                                left.setDisable(true);
                                right.setDisable(true);
                                game.castAbility(a, Direction.DOWN);
                            } catch (NotEnoughResourcesException | AbilityUseException exception) {
                                AlertBox.display("Can't attack", exception.getMessage());
                            } catch (CloneNotSupportedException exception) {
                                exception.printStackTrace();
                            }
                        });
                        left.setOnAction(l -> {
                            try {
                                up.setDisable(true);
                                down.setDisable(true);
                                left.setDisable(true);
                                right.setDisable(true);
                                game.castAbility(a, Direction.LEFT);
                            } catch (NotEnoughResourcesException | AbilityUseException exception) {
                                AlertBox.display("Can't attack", exception.getMessage());
                            } catch (CloneNotSupportedException exception) {
                                exception.printStackTrace();
                            }
                        });
                        right.setOnAction(l -> {
                            try {
                                up.setDisable(true);
                                down.setDisable(true);
                                left.setDisable(true);
                                right.setDisable(true);
                                game.castAbility(a, Direction.RIGHT);
                            } catch (NotEnoughResourcesException | AbilityUseException exception) {
                                AlertBox.display("Can't attack", exception.getMessage());
                            } catch (CloneNotSupportedException exception) {
                                exception.printStackTrace();
                            }
                        });

                    });
                }
                if (currChampDisarmed) {
                    punch.setDisable(false);
                    punch.setVisible(true);
                } else {
                    punch.setDisable(true);
                    punch.setVisible(false);
                }

            }
            if (currChampDisarmed)
                atk.setDisable(true);
            else
                atk.setDisable(false);
            if (currChampRooted)
                move.setDisable(true);
            else
                move.setDisable(false);
        }
    }

    @Override
    public void onBoardUpdated() {
        makeBoard();
        callCurrChamp();
    }

    @Override
    public void onGameOver(Player winner) {
        // TODO show an alertbox with winning player name and send back to start screen?
        AlertBox.displayAndReturn("Game Over", "Winner: " + winner.getName());
    }

    @Override
    public void onTurnEnd() {
        turns.setText(game.getTurnOrder().toString());
        makeBoard();
        callCurrChamp();
    }

    @Override
    public void onPlayerTeamsUpdated() {
        // TODO redraw both teams
        setTeams();
    }

    @Override
    public void onAbilityCast() {
        // TODO update all champion details
        makeBoard();
        callCurrChamp();
    }

    @Override
    public void onAttackHit() {
        // TODO update all champion details
        makeBoard();
        callCurrChamp();
    }
}
