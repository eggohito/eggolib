#> eggolib:random_critical_hits/show_indicator
#
#@internal


#   Spawn an item from the `eggolib:random_critical_hits/indicator` loot table
loot spawn ~ ~ ~ loot eggolib:random_critical_hits/indicator


#   Set the data of the item entity spawned from the loot table
execute as @e[type = minecraft:item, nbt = {Item: {tag: {eggolib: {critical_indicator: 1b}}}}, limit = 1] run function eggolib:random_critical_hits/set_indicator_data
