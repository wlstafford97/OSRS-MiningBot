package Miner;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.script.TaskNode;

public class BankTask extends TaskNode {

	Area AREA1 = new Area(3251,3422,3254,3420);
	Tile bankT = new Tile(3253,3420);
	
	@Override
	public boolean accept() {
		return getInventory().isFull();
	}

	@Override
	public int execute() {
		log("Walking to bank");
		walkToBank();
		log("Banking");
		bankInteractions();
		return 0;
	}
	
	public void walkToBank() {
		while (!AREA1.contains(getLocalPlayer())) {
			if(bankT.distance(getLocalPlayer()) < 10) {
				getWalking().walkOnScreen(AREA1.getRandomTile());
			} else {
				getWalking().walk(AREA1.getRandomTile());
			}
			sleepUntil(() -> !getLocalPlayer().isMoving(), Calculations.random(3000, 5200));
		}
	}

	public void bankInteractions() {
		if(!getBank().isOpen()) {
			getBank().open();
			log("Sleeping after bank opens");
			sleep(Calculations.random(2000, 7500));
			getBank().depositAllItems();
			log("Sleeping after items deposited");
			sleep(Calculations.random(2000, 5500));
			getBank().close();
			sleepUntil(() -> !getBank().isOpen(), Calculations.random(3000, 7500));
		} else {
			getBank().depositAllItems();
			log("Sleeping after items deposited");
			sleep(Calculations.random(2000, 5500));
			getBank().close();
			sleepUntil(() -> !getBank().isOpen(), Calculations.random(3000, 7500));
		}
	}
}
