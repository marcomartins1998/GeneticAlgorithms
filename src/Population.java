import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Population {
    private ArrayList<Entity> populace;
    private double CROSS_PROB;
    private double MUTATION_PROB;
    private Crossover cType;
    private Mutation mType;
    private Random rand;

    public Population(ArrayList<Entity> arr, double CROSS_PROB, double MUTATION_PROB, Crossover cType, Mutation mType){
        this.populace = arr;
        this.CROSS_PROB = CROSS_PROB;
        this.MUTATION_PROB = MUTATION_PROB;
        this.cType = cType;
        this.mType = mType;
        this.rand = new Random();
    }

    public ArrayList<Entity> getPopulace() {
        return populace;
    }

    public void next(){
        System.out.println();
        System.out.println("---(START)---");
        List<Integer> auxprint = populace.stream().map(Entity::getFitness).sorted().collect(Collectors.toList());
        System.out.println(auxprint);

        /*List<Integer> fitnessList = this.populace.stream().map(Entity::getFitness).collect(Collectors.toList());
        Integer sum = fitnessList.stream().reduce(Integer::sum).get();
        List<Double> probSelectList = fitnessList.stream().map(i -> i.doubleValue()/sum).collect(Collectors.toList());
        double[] elem = {0};
        List<Double> rouletteList = probSelectList.stream().map(d -> {
            elem[0]+=d;
            return elem[0];
        }).collect(Collectors.toList());*/

        ArrayList<Entity> mutantGeneration = new ArrayList<>();
        for(int k = 0; k < populace.size(); k++){
            if(rand.nextDouble() < MUTATION_PROB){ populace.get(k).doMutation(mType); }
            mutantGeneration.add(populace.get(k));
        }
        /*for(int i = 0; i < populace.size(); i++){
            double val = rand.nextDouble();
            Entity ent = populace.get(rouletteList.indexOf(rouletteList.stream().filter(p -> p > val).findFirst().get())).newInstance();
            if(rand.nextDouble() < MUTATION_PROB){ ent.doMutation(mType); }
            mutantGeneration.add(ent);
        }*/
        List<Integer> auxmutantprint = mutantGeneration.stream().map(Entity::getFitness).sorted().collect(Collectors.toList());
        System.out.println(auxmutantprint);

        ArrayList<Entity> newGeneration = new ArrayList<>();
        int i = 0;
        while(i < mutantGeneration.size()){
            if(rand.nextDouble() < CROSS_PROB && mutantGeneration.size() > 1){
                int index = rand.nextInt(mutantGeneration.size());
                while(index == i) index = rand.nextInt(mutantGeneration.size());
                Pair<Entity, Entity> pair = mutantGeneration.get(i).doCrossOver(cType, mutantGeneration.get(index));
                mutantGeneration.remove(i);
                if(i > index) mutantGeneration.remove(index); else mutantGeneration.remove(index - 1);
                newGeneration.add(pair.getKey());
                newGeneration.add(pair.getValue());
            }
            else i++;
        }
        newGeneration.addAll(mutantGeneration);

        List<Integer> auxnewprint = newGeneration.stream().map(Entity::getFitness).sorted().collect(Collectors.toList());
        System.out.println(auxnewprint);

        List<Integer> fitnessList = newGeneration.stream().map(Entity::getFitness).collect(Collectors.toList());
        Integer sum = fitnessList.stream().reduce(Integer::sum).get();
        List<Double> probSelectList = fitnessList.stream().map(j -> j.doubleValue()/sum).collect(Collectors.toList());
        double[] elem = {0};
        List<Double> rouletteList = probSelectList.stream().map(d -> {
            elem[0]+=d;
            return elem[0];
        }).collect(Collectors.toList());

        ArrayList<Entity> finalGeneration = new ArrayList<>();
        for(int l = 0; l < populace.size(); l++){
            double val = rand.nextDouble();
            Entity ent = newGeneration.get(rouletteList.indexOf(rouletteList.stream().filter(p -> p > val).findFirst().get())).newInstance();
            finalGeneration.add(ent);
        }

        this.populace = finalGeneration;

        List<Integer> auxfinalprint = finalGeneration.stream().map(Entity::getFitness).sorted().collect(Collectors.toList());
        System.out.println(auxfinalprint);
        System.out.println("---(END)---");
    }

}
