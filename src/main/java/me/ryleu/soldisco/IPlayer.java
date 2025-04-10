package me.ryleu.soldisco;

import me.ryleu.soldisco.component.IFoodHistory;

public interface IPlayer {
    IFoodHistory soldisco$getFoodHistory();
    int getNextMilestone();
}
