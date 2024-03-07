/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tournaments;

import ai.RandomBiasedAI;
import ai.abstraction.partialobservability.POLightRush;
import ai.abstraction.partialobservability.POWorkerRush;
import ai.competition.Aggrobot.Aggrobot;
import ai.competition.Bezjak.Predator;
import ai.competition.COAC.CoacAI;
import ai.competition.MyMicroRtsBot.MyMicroRtsBot;
import ai.competition.NIlSiBot.NIlSiBot;
import ai.competition.ObiBotKenobi.ObiBotKenobi;
import ai.competition.Ragnar.Ragnar;
import ai.competition.SaveTheBeesV4.SaveTheBeesV4;
import ai.competition.bRHEAdBot.RHEA.bRHEAdBot;
import ai.competition.mayariBot.mayari;
import ai.competition.myBotJonaEnis.myBot;
import ai.competition.sophia.sophia;
import ai.core.AI;
import ai.mcts.naivemcts.NaiveMCTS;
import ai.rai.RAISocketAI;
import ai.synthesis.ComplexDSL.LS_CFG.FactoryLS;
import ai.synthesis.ComplexDSL.LS_CFG.Node_LS;
import ai.synthesis.ComplexDSL.Synthesis_Base.AIs.Interpreter;
import ai.synthesis.ComplexDSL.Synthesis_Base.CFG.Control;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import rts.units.UnitTypeTable;

/**
 *
 * @author rubens
 */
public class performFixedTournamentRR {

    public static void main(String[] args) throws Exception {
        FactoryLS f = new FactoryLS();
        UnitTypeTable utt = new UnitTypeTable(2);        
        List<AI> selectedAIs = new ArrayList<>();
        List<String> maps = new ArrayList<>();
        
        //load classes baselines
        //selectedAIs.add((AI) new POWorkerRush(utt));
        //selectedAIs.add((AI) new POLightRush(utt));
        //selectedAIs.add((AI) new NaiveMCTS(utt));
        //selectedAIs.add((AI) new RandomBiasedAI());
        selectedAIs.add((AI) new CoacAI(utt));
        selectedAIs.add((AI) new mayari(utt));  
        selectedAIs.add((AI) new RAISocketAI(utt));
        Node_LS node = (Node_LS) Control.load("S;For_S;S;S_S;S;S_S;S;C;Train;Worker;Right;2;S;C;Idle;S;S_S;S;S_S;S;C;Harvest;10;S;S_S;S;For_S;S;C;Idle;S;S_S;S;S_S;S;C;Train;Heavy;Down;7;S;If_B_then_S;B;HasUnitInOpponentRange;S;S_S;S;"
                + "S_S;S;C;MoveToUnit;Enemy;MostHealthy;S;S_S;S;S_S;S;For_S;S;S_S;S;C;Harvest;1;S;S_S;S;C;Train;Light;EnemyDir;2;S;C;Train;Worker;"
                + "Right;2;S;C;Attack;Farthest;S;C;MoveToUnit;Enemy;Closest;S;S_S;S;S_S;S;For_S;S;If_B_then_S;B;HasNumberOfWorkersHarvesting;100;S;C;MoveToUnit;"
                + "Ally;Closest;S;S_S;S;For_S;S;S_S;S;C;MoveToUnit;Enemy;LessHealthy;S;S_S;S;C;MoveToUnit;Ally;LessHealthy;S;C;MoveAway;S;S_S;S;C;MoveToUnit;Enemy;"
                + "Strongest;S;If_B_then_S;B;OpponentHasUnitThatKillsUnitInOneAttack;S;For_S;S;C;MoveToUnit;Ally;Closest;S;C;MoveToUnit;Enemy;Closest;S;C;Attack;Weakest;S;For_S;S;C;"
                + "Build;Barracks;EnemyDir;9", f);
        selectedAIs.add(new Interpreter(utt,node, "2L")); //2L"S;For_S;S;S_S;S;S_S;S;C;Train;Worker;Right;2;S;C;Idle;S;S_S;S;S_S;S;C;Harvest;10;S;S_S;S;For_S;S;C;Idle;S;S_S;S;S_S;S;C;Train;Heavy;Down;7;S;If_B_then_S;B;HasUnitInOpponentRange;S;S_S;S;S
        //load classes opponents
//        selectedAIs.add((AI) new Predator(utt));//Bezjak - ok
//        selectedAIs.add((AI) new NIlSiBot(utt)); //ok
//        selectedAIs.add((AI) new ObiBotKenobi(utt)); //ok
//        selectedAIs.add((AI) new Ragnar(utt)); //ok
//        selectedAIs.add((AI) new myBot(utt)); //jonaBeckmann and Enis Solad        
//        selectedAIs.add((AI) new MyMicroRtsBot(utt));
//        selectedAIs.add((AI) new SaveTheBeesV4(utt));
//        selectedAIs.add((AI) new sophia(utt));
//        selectedAIs.add((AI) new bRHEAdBot(utt));
//        selectedAIs.add((AI) new Aggrobot(utt));
        
        maps.add("maps/chambers32x32.xml");

        int iterations = 4;
        int maxGameLength = 6000;
        int timeBudget = 100;
        int iterationsBudget = -1;
        int preAnalysisBudget = 3600;

        boolean fullObservability = true;
        boolean selfMatches = false;
        boolean timeOutCheck = false;
        boolean gcCheck = true;
        boolean preGameAnalysis = true;

        String prefix = "tournament_";
        int idx = 0;
//                    String sufix = ".tsv";
        File file;
        do {
            idx++;
            file = new File(prefix + idx);
        } while (file.exists());
        file.mkdir();
        String tournamentfolder = file.getName();
        final File fileToUse = new File(tournamentfolder + "/tournament"+String.valueOf(idx)+".csv");
        final File fileToWrite = new File("tournament"+String.valueOf(idx)+".txt");
        final String tracesFolder = "traces_"+String.valueOf(idx);

        try {
            Runnable r = new Runnable() {
                public void run() {
                    try {
                        Writer writer = new FileWriter(fileToUse);
                        Writer writerMonitor = new FileWriter(fileToWrite);
                        new RoundRobinTournament(selectedAIs).runTournament(-1, maps,
                                iterations, maxGameLength, timeBudget, iterationsBudget,
                                preAnalysisBudget, 1000, // 1000 is just to give 1 second to the AIs to load their read/write folder saved content
                                fullObservability, selfMatches, timeOutCheck, gcCheck, preGameAnalysis,
                                utt, tracesFolder,
                                writer, writerMonitor,
                                tournamentfolder);
                        writer.close();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            };
            (new Thread(r)).start();
        } catch (Exception e3) {
            e3.printStackTrace();
        }

    }
}
