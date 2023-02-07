loot spawn ~ ~ ~ loot eggolib:random_critical_hits/indicator


data modify entity @e[type = minecraft:item, nbt = {Item: {tag: {eggolib: {critical_indicator: 1b}}}}, limit = 1] Age set value 5990

data modify entity @e[type = minecraft:item, nbt = {Item: {tag: {eggolib: {critical_indicator: 1b}}}}, limit = 1] PickupDelay set value -32767

data modify entity @e[type = minecraft:item, nbt = {Item: {tag: {eggolib: {critical_indicator: 1b}}}}, limit = 1] CustomName set value '{"text": "CRIT!", "color": "red", "bold": true}'

data modify entity @e[type = minecraft:item, nbt = {Item: {tag: {eggolib: {critical_indicator: 1b}}}}, limit = 1] CustomNameVisible set value 1b

data remove entity @e[type = minecraft:item, nbt = {Item: {tag: {eggolib: {critical_indicator: 1b}}}}, limit = 1] Item.tag