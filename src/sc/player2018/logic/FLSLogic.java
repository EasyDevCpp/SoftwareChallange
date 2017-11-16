package sc.player2018.logic;

import sc.player2018.logic.Parts.FirstPart;
import sc.player2018.logic.Parts.SecondPart;
import sc.player2018.logic.Parts.ThirdPart;
import sc.plugin2018.*;
import sc.plugin2018.util.GameRuleLogic;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FLSLogic {

    protected int newTask = 0; //0 = Nothing, 1 = Salad, 2 = Carrot+, 3 = Carrot-
    protected GameState gs;
    protected Player player;
    protected Player enemy;
    protected int step = 0;
    protected Move m;
    private BufferedWriter fout;
    protected boolean step0WasAtHare = false;
    protected int[] karottenVerbrauch = {0,1,3,6,10,15,21,28,36,45,55,66,78,91,105,120,136,153,171,190,210,231,253,276,300,325,351,378,406,435,465,496,528,561,595,630,666,703,741,780,820,861,903,946,990};
    protected ArrayList<Action> actions = new ArrayList<>();

    private FirstPart first;
    private SecondPart second;
    private ThirdPart third;

    public FLSLogic() {
        logMessage("init flslogic", true);
    }

    public void onUpdate(Player p, Player e){
        player = p;
        enemy = e;
    }

    public void onUpdate(GameState gameState){
        gs = gameState;
    }

    public void update() {
        if(newTask != 0){
            if(newTask == 1 || (GameRuleLogic.isValidToEat(gs) && gs.fieldOfCurrentPlayer() == FieldType.SALAD)){
                actions.add(new EatSalad(0));
                logMessage("eat salad", false);
            } else if(newTask == 2){
                actions.add(new ExchangeCarrots(+10));
                logMessage("eat carrots", false);
            } else if(newTask == 3){
                actions.add(new ExchangeCarrots(-10));
                logMessage("remove carrots", false);
            }
            newTask = 0;
        } else{
            if(player.getFieldIndex()<23) {
                first.processAI();
                m = first.getMove();
            } else if(player.getFieldIndex()<43) {
                second.processAI();
                m = second.getMove();
            } else if(player.getFieldIndex()<=65) {
                third.processAI();
                m = third.getMove();
            }
        }
    }

    protected void logMessage(String msg, boolean newLine) {
        try {
            if (newLine) msg = "\n" + msg;
            fout.write(msg);
            fout.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected boolean enemyOnNextFieldType(FieldType type){
        return gs.getBoard().getNextFieldByType(type, player.getFieldIndex()) == enemy.getFieldIndex();
    }

    protected boolean enemyOnPreviousFieldType(FieldType type){
        return gs.getBoard().getPreviousFieldByType(type, player.getFieldIndex()) == enemy.getFieldIndex();
    }

    protected boolean isMovePlayable(int actionId, FieldType f){
        if(actionId == 0 && gs.getBoard().getPreviousFieldByType(f, player.getFieldIndex()) > -1 && gs.getBoard().getPreviousFieldByType(f, player.getFieldIndex()) != enemy.getFieldIndex()) return true; //previousField
        else if(actionId == 1 && gs.getBoard().getNextFieldByType(f, player.getFieldIndex()) > -1 && gs.getBoard().getNextFieldByType(f, player.getFieldIndex()) != enemy.getFieldIndex() && karottenVerbrauch[gs.getBoard().getNextFieldByType(f, player.getFieldIndex()) - player.getFieldIndex()] <= player.getCarrots()) return true; //nextField
        return false;
    }

    protected int getDistance(FieldType f){
        return gs.getNextFieldByType(f, player.getFieldIndex()) - player.getFieldIndex();
    }

    public Move getMove(){
        return m;
    }
}