#> eggolib:load
#
#@within tag/function minecraft:load


#   Forceload MinecraftPhi chunk
forceload remove -30000000 1600

forceload add -30000000 1600


#   Setup burrow for MinecraftPhi blocks
#alias vector minecraftPhi.sign -30000000 0 1603
execute unless block -30000000 0 1603 minecraft:oak_wall_sign run setblock -30000000 0 1603 minecraft:oak_wall_sign[facing = south]


#   Add scoreboard objective(s)
scoreboard objectives add eggolib dummy


#   Re-initialize the tick function
schedule function eggolib:tick 1t replace
