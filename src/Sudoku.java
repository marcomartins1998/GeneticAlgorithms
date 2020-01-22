import javafx.util.Pair;

import java.util.*;

public class Sudoku implements Entity{
    private Integer[][] chromossome;
    private Random rand;
    private ArrayList<Pair<Integer,Integer>> staticFields;

    public Sudoku(Integer[][] chromossome){
        this.chromossome = chromossome;
        this.rand = new Random();
        this.staticFields = new ArrayList<>();

        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                if(chromossome[i][j] != -1) staticFields.add(new Pair<>(i, j));
            }
        }
    }

    public Sudoku(Integer[][] chromossome, ArrayList<Pair<Integer,Integer>> staticFields){
        this.chromossome = chromossome;
        this.rand = new Random();
        this.staticFields = staticFields;
    }

    //Use this set when needing to modify chromossome
    public boolean set(int x, int y, int value){
        boolean[] canChange = { true };
        staticFields.forEach(p -> canChange[0] = !(p.getKey() == x && p.getValue() == y));
        if(canChange[0]) { chromossome[x][y] = value; }
        return canChange[0];
    }

    public Integer[][] getChromossome(){
        return chromossome;
    }

    public Sudoku generateEntity(){
        Integer[][] matrix = new Integer[9][9];
        staticFields.forEach(p -> matrix[p.getKey()][p.getValue()] = chromossome[p.getKey()][p.getValue()]);
        ArrayList<Integer> aux = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8,9));

        for(int i = 0; i < 9; i++){
            Collections.shuffle(aux);
            for(int j = 0; j < 9; j++){
                if(matrix[i][j] == null) matrix[i][j] = aux.get(j);
            }
        }
        return new Sudoku(matrix, staticFields);
    }

    public static <T> T[] getColumn(T[][] array, T[] emptyarray, int index){
        for(int i=0; i<emptyarray.length; i++){
            emptyarray[i] = array[i][index];
        }
        return emptyarray;
    }

    public static int getLineFitness(Integer[] arr){
        int totalFitness = 10;
        Integer[] zeroArr = {0,0,0,0,0,0,0,0,0};
        ArrayList<Integer> howManyDups = new ArrayList<>(Arrays.asList(zeroArr));
        List<Integer> aux = Arrays.asList(arr);
        aux.forEach(i -> howManyDups.set(i - 1, howManyDups.get(i - 1) + 1));
        return 10 - howManyDups.stream().map(i -> {
            if(i != 0) i--;
            return i;
        }).reduce(Integer::sum).get();
    }

    //xi, yi -> inclusive, xf, yf -> exclusive
    public static int get3By3Fitness(Integer[][] matrix, int xi, int xf, int yi, int yf){
        int totalFitness = 10;
        Integer[] zeroArr = {0,0,0,0,0,0,0,0,0};
        ArrayList<Integer> howManyDups = new ArrayList<>(Arrays.asList(zeroArr));
        for(int x = xi; x < xf; x++){
            for(int y = yi; y < yf; y++){
                howManyDups.set(matrix[x][y] - 1, howManyDups.get(matrix[x][y] - 1) + 1);
            }
        }
        return 10 - howManyDups.stream().map(i -> {
            if(i != 0) i--;
            return i;
        }).reduce(Integer::sum).get();
    }

    @Override
    public int getFitness() {
        int linesum = 0;
        int columnsum = 0;
        int cubesum = 0;
        for(int k = 0; k < 9; k++){
            linesum += getLineFitness(chromossome[k]);
            columnsum += getLineFitness(getColumn(chromossome, new Integer[9], k));
        }
        for(int i = 0; i < 9; i+=3){
            for(int j = 0; j < 9; j+=3){
                cubesum += get3By3Fitness(chromossome, i, i + 3, j, j + 3);
            }
        }
        return linesum + columnsum + cubesum;
    }

    @Override
    public Entity newInstance() {
        Integer[][] newMatrix = new Integer[9][9];
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                newMatrix[i][j] = chromossome[i][j];
            }
        }
        return new Sudoku(newMatrix, this.staticFields);
    }

    public static Integer[][] generateMask(){
        Random rand = new Random();
        Integer[][] matrix = new Integer[9][9];

        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                double aux = rand.nextDouble();
                matrix[i][j] = (aux >= 0.5) ? 1 : 0;
            }
        }
        return matrix;
    }

    @Override
    public Pair<Entity, Entity> doCrossOver(Crossover c, Entity other) {
        if(c == Crossover.UNIFORM){
            Integer[][] mask = generateMask();
            Integer[][] matrix1 = ((Sudoku) newInstance()).getChromossome();
            Integer[][] matrix2 = ((Sudoku) other.newInstance()).getChromossome();

            for(int i = 0; i < 9; i++){
                for(int j = 0; j < 9; j++){
                    if(mask[i][j] == 1){
                        set(i, j, matrix2[i][j]);
                        ((Sudoku) other).set(i, j, matrix1[i][j]);
                    }
                }
            }
        }
        return new Pair<>(this, other);
    }

    @Override
    public Entity doMutation(Mutation m) {
        if(m == Mutation.INSERTION){
            //Mutate only 4 values
            for(int i = 0; i < 3; i++){
                int aux = rand.nextInt(9) + 1;
                set(rand.nextInt(9), rand.nextInt(9), aux);
            }
            /*Integer[][] mask = generateMask();

            for(int i = 0; i < 9; i++){
                for(int j = 0; j < 9; j++){
                    if(mask[i][j] == 1){
                        int aux = rand.nextInt(9) + 1;
                        set(i, j, aux);
                    }
                }
            }*/
        }
        return this;
    }
}
