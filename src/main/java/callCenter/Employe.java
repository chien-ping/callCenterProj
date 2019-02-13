package callCenter;

import java.util.Random;

public class Employe extends CustomerService {

    public Employe(String name, int rank, int size) {
        super(name, rank, size);
    }

    @Override
    protected boolean resolve(Call call) {
        System.out.printf("%s is being processed by %s.%n", call.toString(), this.getName());
        call.setLevel(this.getRank());
        return rollDice();
    }

    private boolean rollDice() {
        Random diceRoll = new Random();
        return diceRoll.nextBoolean();
    }
}
