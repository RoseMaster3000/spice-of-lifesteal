# Spice of Life(steal)

Spice of Lifesteal is a spin on the Spice of Life: Discovery, but for lifestealSMP server. 
It tracks food history, and when you hit certain milestones, you are rewarded a heart-consumable which you can consume to gain a heart.
Ensure you use recipe mods for those milestones to actually be met (we have a S curve that tops out +27 hearts:413 foods)

## Configuration

### Default

By default, SOL:D starts the player at 6 HP (3 hearts) and grants an additional 2 HP (1 heart) for each 5 new food items
eaten. That formula is completely configurable in `.minecraft/config/soldisco.json5`. Here's what it looks
like by default:



The formula is parsed by exp4j. You can find which functions are available [here](https://www.objecthunter.net/exp4j/#Built-in_functions).

### Suggestions

Here are a few interesting or weird ideas to get you started.

This formula will require an increasing amount of new food items for each additional heart:

```json5
{
    "formula": "20 + 2 * floor(sqrt(foodsEaten))",
    "minHp": 20.0
}
```

This formula punishes you for each new food you eat:

```json5
{
    "formula": "40 - foodsEaten",
    "minHp": 1.0
}
```

Start at 3 hearts and gain a heart for every five new foods you eat (this is the default):

```json5
{
    "formula": "6 + 2 * floor(foodsEaten / 5)",
    "minHp": 6.0
}
```

## Commands

- `/food` tells you the next food milestone until you gain a heart
- `/food history [<player>]` gets the full food history of a player
- `/food add <food> [<players>]` adds a food to the food history of `players`
- `/food remove <food> [<players>]` removes a food from the food history of `players`
- `/food query <food> [<players>]` check if `players` have a food

## Credits

- [Siphalor's Spice of Fabric mod](https://github.com/Siphalor/spiceoffabric), for inspiration and a code reference.
- [Twemoji](https://github.com/twitter/twemoji), for the magnifying glass in the mod icon.
