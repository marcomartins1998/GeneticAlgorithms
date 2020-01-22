import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        MQueens();
        //MSudoku();
    }

    public static void MQueens(){
        ArrayList<Entity> elist = new ArrayList<>(8);
        for(int i = 0; i < 50; i++) elist.add(Queens.generateEntity());
        Population p = new Population(elist, 0.2, 0.01, Crossover.OX, Mutation.INSERTION);
        for(int i = 0; i < 1000; i++){
            p.next();
            if(p.getPopulace().stream().map(Entity::getFitness).max(Integer::compareTo).get() == 28) {
                Queens q = (Queens) p.getPopulace().stream().filter(e -> e.getFitness() == 28).findFirst().get();
                System.out.println(q.getChromossome());
                System.out.println(i);
                break;
            }
            System.out.println(i);
        }
    }

    public static void MSudoku(){
        ArrayList<Entity> elist = new ArrayList<>(8);
        Integer[][] board = new Integer[][]{
                {5,3,-1,-1,7,-1,-1,-1,-1},
                {6,-1,-1,1,9,5,-1,-1,-1},
                {-1,9,8,-1,-1,-1,-1,6,-1},
                {8,-1,-1,-1,6,-1,-1,-1,3},
                {4,-1,-1,8,-1,3,-1,-1,1},
                {7,-1,-1,-1,2,-1,-1,-1,6},
                {-1,6,-1,-1,-1,-1,2,8,-1},
                {-1,-1,-1,4,1,9,-1,-1,5},
                {-1,-1,-1,-1,8,-1,-1,7,9},
        };
        Sudoku sudoku = new Sudoku(board);
        for(int i = 0; i < 300; i++) elist.add(sudoku.generateEntity());
        Population p = new Population(elist, 0.6, 0.003, Crossover.UNIFORM, Mutation.INSERTION);
        Entity bestEntity = p.getPopulace().stream().reduce((e1, e2) -> (e1.getFitness() > e2.getFitness()) ? e1 : e2).get();
        for(int i = 0; i < 100000; i++){
        //while(bestEntity.getFitness() != 270){
            p.next();
            System.out.println("Generation: " + i);
            System.out.println("Max fitness: " + p.getPopulace().stream().map(Entity::getFitness).max(Integer::compareTo).get());
            Entity bestThisGeneration = p.getPopulace().stream().reduce((e1, e2) -> (e1.getFitness() > e2.getFitness()) ? e1 : e2).get();
            if(bestThisGeneration.getFitness() > bestEntity.getFitness()) bestEntity = bestThisGeneration;
        }

        System.out.println("\nBest fitness: " + bestEntity.getFitness());
        String end = "";
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                if(j == 0 || j == 3 || j == 6) end = end + "(" + ((Sudoku) bestEntity).getChromossome()[i][j] + ")-";
                else if(j == 2 || j == 5) end = end + "(" + ((Sudoku) bestEntity).getChromossome()[i][j] + ")  ";
                else if(j == 8) end = end + "(" + ((Sudoku) bestEntity).getChromossome()[i][j] + ")";
                else end = end + "(" + ((Sudoku) bestEntity).getChromossome()[i][j] + ")-";
            }
            if(i == 2 || i == 5) end = end + "\n\n";
            else end = end + "\n";
        }
        System.out.println(end);
    }
}
