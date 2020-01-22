import javafx.util.Pair;

public interface Entity {
    int getFitness();
    Entity newInstance();
    Pair<Entity, Entity> doCrossOver(Crossover c, Entity other);
    Entity doMutation(Mutation m);
}
