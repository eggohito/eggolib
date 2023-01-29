#> eggolib:punch_coal_block/marker/set_name_randomly
#
#@within function eggolib:punch_coal_block/marker/summon


#   Create the list of strings to choose randomly from
data modify storage random:input List set value ["That hurts :(", "My eye!", "..zzZZZ", "Why?!", "I'll be back!", "You didn't ....", "Oof", "(&%!@*#&%!@$#", "Ouch", "Hi there!", "That tickles", "Who's there!", "I thought we were friends", "You're mean!", "Whiiii", "Not dropping anything!", "I like you!", "I HATE you!", "YOU NO TAKE.. COBBLE!", "I'm diamond", "You punched me", "I'm dirt", "Mummy?", "SSSSSSST", "KABOOM", "BULLY!", "I'm telling", "Creeper behind you!", "Did I do bad?", "Hello"]


#   Choose a string from the list randomly
function random:choose


#   Use the randomly chosen string as the name of the Area Effect Cloud
execute in minecraft:overworld run data modify block -30000000 0 1603 Text1 set value '{"storage": "random:output", "nbt": "Tag"}'

execute in minecraft:overworld run data modify entity @s CustomName set from block -30000000 0 1603 Text1
