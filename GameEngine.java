package io.github.some_example_name;

import java.util.ArrayList;

import static io.github.some_example_name.Bird.State.FLYING;
import static io.github.some_example_name.Bird.State.START;

public class GameEngine {
    ArrayList<Bird>birdslist;
    MoveEngine moveEngine;
    int current_number_of_birds;
    int current_number_of_pigs;

    public GameEngine(ArrayList<Bird> birdslist , MoveEngine moveEngine, int numpigs){
        this.birdslist = birdslist;
        current_number_of_birds = birdslist.size();
        this.moveEngine = moveEngine;
        this.current_number_of_pigs = numpigs;

    }
    private boolean Catapultisempty(){
        if(moveEngine.getActive_bird().getCurrentState()==FLYING && moveEngine.getActive_bird().getPreviousState()==START){
            current_number_of_birds--;
            return false;
        }
        return true;
    }

    public void FillingtheCatapult(){
        if (!this.Catapultisempty()) {
            Bird nextBird = birdslist.get(birdslist.size() - current_number_of_birds);
            moveEngine.setActive_bird(nextBird);
            nextBird.setCurrentState(START);
            nextBird.setPreviousState(START);
        }
    }







}
