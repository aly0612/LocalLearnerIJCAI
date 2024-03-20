 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;

import ai.core.AI;
import ai.*;
import ai.abstraction.HeavyRush;
import ai.abstraction.LightRush;
import ai.abstraction.WorkerRush;
import ai.abstraction.pathfinding.BFSPathFinding;
import ai.ahtn.AHTNAI;
import ai.asymmetric.PGS.PGSmRTS;
import ai.asymmetric.SSS.SSSmRTS;
import ai.competition.dropletGNS.Droplet;
import ai.competition.newBotsEval.botEmptyBase;
import ai.mcts.naivemcts.NaiveMCTS;
import ai.scv.SCV;
import ai.synthesis.ComplexDSL.LS_CFG.FactoryLS;
import ai.synthesis.ComplexDSL.LS_CFG.Node_LS;
import ai.synthesis.ComplexDSL.Synthesis_Base.CFG.Control;
import gui.PhysicalGameStatePanel;
import java.io.OutputStreamWriter;
import javax.swing.JFrame;
import rubensbot.RubensBot;
import rts.GameState;
import rts.PhysicalGameState;
import rts.PlayerAction;
import rts.units.UnitTypeTable;
import util.XMLWriter;

/**
 *
 * santi
 */
public class GameVisualSimulationTest {
    public static void main(String args[]) throws Exception {
        UnitTypeTable utt = new UnitTypeTable();
        PhysicalGameState pgs = PhysicalGameState.load("maps/8x8/basesWorkers8x8A.xml", utt);
//        PhysicalGameState pgs = MapGenerator.basesWorkers8x8Obstacle();

        GameState gs = new GameState(pgs, utt);
        int MAXCYCLES = 5000;
        int PERIOD = 20;
        boolean gameover = false;
        
        AI ai2 = new RubensBot(utt);
        //AI ai1 = new PGSmRTS(utt);
        //AI ai2 = new Droplet(utt);        
        AI ai1 = new LightRush(utt);
        //AI ai2 = new AHTNAI(utt);

        JFrame w = PhysicalGameStatePanel.newVisualizer(gs,640,640,false,PhysicalGameStatePanel.COLORSCHEME_BLACK);
//        JFrame w = PhysicalGameStatePanel.newVisualizer(gs,640,640,false,PhysicalGameStatePanel.COLORSCHEME_WHITE);

        long nextTimeToUpdate = System.currentTimeMillis() + PERIOD;
        do{
            if (System.currentTimeMillis()>=nextTimeToUpdate) {
                PlayerAction pa1 = ai1.getAction(0, gs);
                PlayerAction pa2 = ai2.getAction(1, gs);
                gs.issueSafe(pa1);
                gs.issueSafe(pa2);

                // simulate:
                gameover = gs.cycle();
                w.repaint();
                nextTimeToUpdate+=PERIOD;
            } else {
                try {
                    Thread.sleep(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }while(!gameover && gs.getTime()<MAXCYCLES);
        ai1.gameOver(gs.winner());
        ai2.gameOver(gs.winner());
        
        System.out.println("Game Over");
    }    
}
