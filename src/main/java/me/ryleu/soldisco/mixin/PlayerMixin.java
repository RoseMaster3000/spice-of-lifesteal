package me.ryleu.soldisco.mixin;

import com.mojang.authlib.GameProfile;
import me.ryleu.soldisco.IPlayer;
import me.ryleu.soldisco.SOLDisco;
import me.ryleu.soldisco.component.IFoodHistory;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import mc.mian.lifesteal.api.PlayerImpl;

import java.util.Arrays;

import static me.ryleu.soldisco.component.SOLDiscoComponents.FOOD_HISTORY_COMPONENT_KEY;

@Mixin(Player.class)
public class PlayerMixin implements IPlayer {
    @Unique
    private static final int[] TARGET_MILESTONES = {2,10,25,50,75,100,135,165,190,215,240,265,290,315,340,365,390,400,402,404,406,408,409,410,411,412,413};

    @Inject(at = @At("HEAD"), method = "eat(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/food/FoodProperties;)Lnet/minecraft/world/item/ItemStack;")
    private void onEat(Level level, ItemStack itemStack, FoodProperties foodProperties, CallbackInfoReturnable<ItemStack> cir) {
        if ((Object)this instanceof Player player) {
            Item foodItem = itemStack.getItem();

            if (soldisco$getFoodHistory().add(foodItem)) {
                SOLDisco.LOGGER.debug("{} ate a {} (new food!)", player.getStringUUID(), foodItem);
                heartRewardCheck();
            }
        }
    }

    @Unique
    public int getNextMilestone() {
        int foodCount = soldisco$getFoodHistory().size();
        return Arrays.stream(TARGET_MILESTONES)
                    .filter(n -> n > foodCount)
                    .min()
                    .orElse(-1);
    }

    @Unique
    private void heartRewardCheck(){
        Object thisObj = this;
        if (thisObj instanceof Player player) {
            if (player instanceof ServerPlayer serverPlayer) {
                if (player instanceof PlayerImpl lsPlayer) {
                    int foodCount = soldisco$getFoodHistory().size();
                    boolean milestoneReached = Arrays.stream(TARGET_MILESTONES)
                            .anyMatch(milestone -> milestone == foodCount);
                    if (milestoneReached){
                        lsPlayer.gainHeart(
                                "Food Milestone!",
                                foodCount + " foods eaten, gain a ♥!");

                    }
                }
            }
        }
    }


    @Unique
    public IFoodHistory soldisco$getFoodHistory() {
        return FOOD_HISTORY_COMPONENT_KEY.get(this);
    }
}
