/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rubensbot;

import ai.core.AI;
import ai.core.AIWithComputationBudget;
import ai.core.ParameterSpecification;
import ai.synthesis.ComplexDSL.LS_CFG.FactoryLS;
import ai.synthesis.ComplexDSL.LS_CFG.Node_LS;
import ai.synthesis.ComplexDSL.Synthesis_Base.AIs.Interpreter;
import ai.synthesis.ComplexDSL.Synthesis_Base.CFG.Control;
import ai.synthesis.ComplexDSL.Synthesis_Base.CFG.Node;
import java.util.ArrayList;
import java.util.List;
import rts.GameState;
import rts.PlayerAction;
import rts.units.UnitTypeTable;
/**
 *
 * @author austinly
 */
public class RubensBot extends AIWithComputationBudget {
        
    UnitTypeTable m_utt = null;
    Interpreter strategyInterpreter;
    
    public RubensBot(UnitTypeTable utt) {    
        
    super(-1, -1);
    this.m_utt = utt;
    FactoryLS f = new FactoryLS();
    Node_LS node = (Node_LS) Control.load("S;For_S;S;S_S;S;If_B_then_S;B;HasUnitInOpponentRange;S;S_S;S;C;Attack;LessHealthy;S;For_S;S;C;Idle;S;S_S;S;For_S;S;S_S;S;C;Harvest;1;S;For_S;S;S_S;S;C;Train;Worker;Left;3;S;For_S;S;C;Train;Worker;Down;10;S;S_S;S;C;Attack;Strongest;S;C;MoveToUnit;Enemy;LessHealthy", f);
    
    this.strategyInterpreter = new Interpreter(m_utt,node, "2L");
    
    }

    @Override
    public void reset() {
             
    }

    @Override
    public PlayerAction getAction(int player, GameState gs) throws Exception {
        
        return this.strategyInterpreter.getAction(player, gs);
        
    }


    public AI clone() {
       
        return new RubensBot(m_utt);
    }

    @Override
    public List<ParameterSpecification> getParameters() {
        return new ArrayList<>();
    }
     
}
