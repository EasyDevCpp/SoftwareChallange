package sc.player2018.logic.Parts;

import sc.plugin2018.*;
import sc.plugin2018.util.GameRuleLogic;

import java.util.ArrayList;

public abstract class Part {
    private Move m;
    private ArrayList<Action> actions;
    private int newTask;
    private int[] karrotCosts = {0,1,3,6,10,15,21,28,36,45,55,66,78,91,105,120,136,153,171,190,210,231,253,276,300,325,351,378,406,435,465,496,528,561,595,630,666,703,741,780,820,861,903,946,990};
    private GameState gs;
    private Player player;
    private Player enemy;

    public Part(){}
    public Part(boolean b){
        actions = new ArrayList<>();
    }

    public void processAI(){}

    public void update(GameState gs){
        actions.clear();
        this.gs = gs;
        player = gs.getCurrentPlayer();
        enemy = gs.getOtherPlayer();

        if(newTask != 0 || (GameRuleLogic.isValidToEat(gs) && gs.fieldOfCurrentPlayer() == FieldType.SALAD)) {
            if (newTask == 1 || (GameRuleLogic.isValidToEat(gs) && gs.fieldOfCurrentPlayer() == FieldType.SALAD)) {
                actions.add(new EatSalad(0));
            } else if (newTask == 2 || gs.getNextFieldByType(FieldType.CARROT, player.getFieldIndex()) == 0){
                actions.add(new ExchangeCarrots(+10));
            } else if (newTask == 3){
                actions.add(new ExchangeCarrots(-10));
            }
            newTask = 0;
        } else{
            processAI();
        }
    }

    public boolean enemyOnNextFieldType(FieldType type){
        return gs.getBoard().getNextFieldByType(type, player.getFieldIndex()) == enemy.getFieldIndex();
    }
    public boolean enemyOnPreviousFieldType(FieldType type){
        return gs.getBoard().getPreviousFieldByType(type, player.getFieldIndex()) == enemy.getFieldIndex();
    }
    public int getDistance(FieldType f){
        return gs.getNextFieldByType(f, player.getFieldIndex()) - player.getFieldIndex();
    }
    public boolean isMovePlayable(int direction, FieldType f){
        if(direction == 0 && gs.getBoard().getPreviousFieldByType(f, player.getFieldIndex()) > -1 && gs.getBoard().getPreviousFieldByType(f, player.getFieldIndex()) != enemy.getFieldIndex()) return true; //previousField
        else if(direction == 1 && gs.getBoard().getNextFieldByType(f, player.getFieldIndex()) > -1 && gs.getBoard().getNextFieldByType(f, player.getFieldIndex()) != enemy.getFieldIndex() && karrotCosts[gs.getBoard().getNextFieldByType(f, player.getFieldIndex()) - player.getFieldIndex()] <= player.getCarrots()) return true; //nextField
        return false;
    }

    public Move getM() {
        return m;
    }
    public void setM(Move m) {
        this.m = m;
    }
    public ArrayList<Action> getActions() {
        return actions;
    }
    public void setActions(ArrayList<Action> actions) {
        this.actions = actions;
    }
    public int getNewTask() {
        return newTask;
    }
    public void setNewTask(int newTask) {
        this.newTask = newTask;
    }
    public int[] getKarrotCosts() {
        return karrotCosts;
    }
    public void setKarrotCosts(int[] karrotCosts) {
        this.karrotCosts = karrotCosts;
    }
    public GameState getGameState() {
        return gs;
    }
    public void setGameState(GameState gs) {
        this.gs = gs;
    }
    public Player getPlayer() {
        return player;
    }
    public void setPlayer(Player player) {
        this.player = player;
    }
    public Player getEnemy() {
        return enemy;
    }
    public void setEnemy(Player enemy) {
        this.enemy = enemy;
    }
}
