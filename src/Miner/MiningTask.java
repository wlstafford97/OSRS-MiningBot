package Miner;

import java.util.List;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.world.World;
import org.dreambot.api.script.TaskNode;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.Player;

public class MiningTask extends TaskNode {
	Tile tile1 = new Tile(3288,3365);
	Tile tile2 = new Tile(3287,3366);
	Area mine = new Area(3287,3366,3288,3365);
	Area wHop = new Area(3286,3367,3289,3364);
	World w = getWorlds().getRandomWorld(w -> w != null && !w.isPVP() && !w.isHighRisk() && w.isF2P() && w.isNormal());

	@Override
	public boolean accept() {
		return !getInventory().isFull() && !isMining();
	}

	@Override
	public int execute() {
		GameObject rock = getClosestRock();
		if(!mine.contains(getLocalPlayer())) {
			int rNum = Calculations.random(1,2);
			if(rNum == 1) {
				if(tile1.distance(getLocalPlayer()) < 10) {
					getWalking().walkOnScreen(tile1);
				} else {
					getWalking().walk(tile1);
				}
			} else {
				if(tile2.distance(getLocalPlayer()) < 10) {
					getWalking().walkOnScreen(tile2);
				} else {
					getWalking().walk(tile2);
				}
			}
			sleepUntil(() -> mine.contains(getLocalPlayer()), Calculations.random(3200, 4000));
		} else {
			List<Player> playersInArea = Players.all(p -> p != null && wHop.contains(p));
			int amountInArea = playersInArea.size();
			if(amountInArea > 1) {
				getWorldHopper().hopWorld(w);
			} else {
				log(amountInArea); 
				if(!getTabs().isOpen(Tab.INVENTORY)) {
					sleep(Calculations.random(2000,5000));
				    getTabs().openWithMouse(Tab.INVENTORY);
				    sleep(Calculations.random(1000,3500));
				}
				rock.interactForceLeft("Mine");
				sleepUntil(this::isMining, 2500);
			}
		}

        return Calculations.random(500, 1000);
	}
	
	private GameObject getClosestRock() {
        return GameObjects.closest(object -> object.getName().equalsIgnoreCase("Rocks")
        		&& object.hasAction("Mine") && object.getModelColors() != null);
    }
	
	private boolean isMining() {
        return getLocalPlayer().isAnimating();
    }

}
