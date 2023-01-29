#> eggolib:punch_coal_block/marker/summon
#
#@internal


#   Summon an Area Effect Cloud with a randomized name and a temporary tag
execute positioned ~ ~0.1 ~ unless entity @e[type = minecraft:area_effect_cloud, tag = talking_coal_block.marker, distance = ..0.5] run summon minecraft:area_effect_cloud ~ ~ ~ {Tags: ["talking_coal_block.marker", "talking_coal_block.marker.tmp"], CustomName: '{"text": ""}', CustomNameVisible: 1b, Duration: 30}


#   Set the name of the Area Effect Cloud randomly
execute as @e[tag = talking_coal_block.marker.tmp] run function eggolib:punch_coal_block/marker/set_name_randomly


#   Remove the temporary tag from the summoned Area Effect Cloud
tag @e remove talking_coal_block.marker.tmp
