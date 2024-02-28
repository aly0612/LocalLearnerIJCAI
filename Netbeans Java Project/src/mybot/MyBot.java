/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mybot;

import ai.core.AI;
import ai.core.AIWithComputationBudget;
import ai.core.ParameterSpecification;
import ai.synthesis.ComplexDSL.IAs2.Algoritmo1;
import ai.synthesis.ComplexDSL.IAs2.AvaliadorPadrao;
import ai.synthesis.ComplexDSL.IAs2.HC;
import ai.synthesis.ComplexDSL.LS_CFG.Node_LS;
import ai.synthesis.ComplexDSL.Synthesis_Base.AIs.Interpreter;
import java.util.ArrayList;
import java.util.List;
import rts.GameState;
import rts.PlayerAction;
import rts.units.UnitTypeTable;
/**
 *
 * @author austinly
 */
public class MyBot extends AIWithComputationBudget {
        
    private Algoritmo1 algorithm;
    private Interpreter interpreter;
    
    
    UnitTypeTable m_utt = null;
    
    public MyBot(UnitTypeTable utt) {
        
    super(-1, -1);
    m_utt = utt; // Initialize m_utt first
    this.algorithm = new Algoritmo1(new HC(2000), new AvaliadorPadrao(1));
    this.interpreter = new Interpreter(m_utt); 

    }

    @Override
    public void reset() {
             
    }

    @Override
    public PlayerAction getAction(int player, GameState gs) throws Exception {
        this.algorithm.run(gs, 10);
        
        Node_LS optimizedStrategy = this.algorithm.getLastOptimizedStrategy();
        
       this.interpreter.setNode(optimizedStrategy);
       
       return this.interpreter.getAction(player, gs);
    }


    public AI clone() {
        return new MyBot(m_utt);
    }

    @Override
    public List<ParameterSpecification> getParameters() {
        return new ArrayList<>();
    }
     
}
