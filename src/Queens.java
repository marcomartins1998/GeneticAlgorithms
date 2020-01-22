import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class Queens implements Entity {
    private ArrayList<Integer> chromossome;
    private Random rand;

    public Queens(ArrayList<Integer> chromossome){
        this.chromossome = chromossome;
        this.rand = new Random();
    }

    public ArrayList<Integer> getChromossome(){
        return chromossome;
    }

    public static Queens generateBadEntity(){
        ArrayList<Integer> aux = new ArrayList<>(Arrays.asList(0,1,2,3,4,5,6,7));
        return new Queens(aux);
    }

    public static Queens generateEntity(){
        ArrayList<Integer> aux = new ArrayList<>(Arrays.asList(0,1,2,3,4,5,6,7));
        Collections.shuffle(aux);
        return new Queens(aux);
    }

    public Entity newInstance() {
        return new Queens(new ArrayList<>(this.chromossome));
    }

    private Integer notAttack(ArrayList<Integer> arr){
        int sum = 0;
        for(int k = 0; k < arr.size(); k++){
            if(!arr.get(0).equals(arr.get(k)) && (arr.get(k)-arr.get(0))!=(0-k) && (arr.get(k)-arr.get(0))!=(k-0)){
                sum++;
            }
        }
        if(arr.size() <= 2) return sum;
        else return sum + notAttack(new ArrayList<>(arr.subList(1, arr.size())));
    }

    public int getFitness() {
        int i = notAttack(chromossome);
        return i;
    }

    public Pair<Entity, Entity> doCrossOver(Crossover c, Entity other){
        Queens q = (Queens) other;
        int p1 = rand.nextInt(chromossome.size());
        int p2 = rand.nextInt(chromossome.size());
        while(p2 == p1) p2 = rand.nextInt(chromossome.size());
        if(p1 > p2){
            int val = p2;
            p2 = p1;
            p1 = val;
        }

        if(c == Crossover.OX){
            ArrayList<Integer> d1vals = new ArrayList<>(this.chromossome.subList(p2 + 1, this.chromossome.size()));
            d1vals.addAll(this.chromossome.subList(0, p2 + 1));
            ArrayList<Integer> d2vals = new ArrayList<>(q.chromossome.subList(p2 + 1, q.chromossome.size()));
            d2vals.addAll(q.chromossome.subList(0, p2 + 1));

            ArrayList<Integer> d1 = new ArrayList<>(this.chromossome.subList(p1, p2 + 1));
            ArrayList<Integer> d2 = new ArrayList<>(q.chromossome.subList(p1, p2 + 1));

            for(int k = p2 + 1; k != p1; k++){
                //System.out.print(".");
                if(k == this.chromossome.size()){ k = -1; continue; }
                int nextval = d2vals.remove(0);
                while(d1.contains(nextval)) nextval = d2vals.remove(0);
                this.chromossome.set(k, nextval);
                //if(k == this.chromossome.size() - 1) k = -1;
            }

            for(int k = p2 + 1; k != p1; k++){
                //System.out.print(":");
                if(k == this.chromossome.size()){ k = -1; continue; }
                int nextval = d1vals.remove(0);
                while(d2.contains(nextval)) nextval = d1vals.remove(0);
                q.chromossome.set(k, nextval);
                //if(k == q.chromossome.size() - 1) k = -1;
            }
        }

        return new Pair<>(this, q);
    }

    public Entity doMutation(Mutation m) {
        System.out.println("Mutation");
        int p1 = rand.nextInt(chromossome.size());
        int p2 = rand.nextInt(chromossome.size());
        while(p2 == p1) p2 = rand.nextInt(chromossome.size());

        if(m == Mutation.INVERSION){
            if(p1 > p2){
                int val = p2;
                p2 = p1;
                p1 = val;
            }
            ArrayList<Integer> aux = new ArrayList<>(chromossome.subList(p1, p2 + 1));
            Collections.reverse(aux);
            for(int k = p1; k <= p2; k++) chromossome.set(k, aux.get(k - p1));
        }

        else if(m == Mutation.INSERTION){
            int aux = chromossome.remove(p1);
            chromossome.add(p2, aux);
        }

        else if(m == Mutation.EXCHANGE){
            int aux1 = chromossome.get(p1);
            chromossome.set(p1, chromossome.get(p2));
            chromossome.set(p2, aux1);
        }

        else if(m == Mutation.RANDOM){
            return doMutation(Mutation.values()[rand.nextInt(Mutation.values().length - 1)]);
        }

        return this;
    }
}
