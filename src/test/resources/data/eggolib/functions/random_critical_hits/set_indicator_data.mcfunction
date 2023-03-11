#> eggolib:random_critical_hits/set_indicator_data
#
#@within function eggolib:random_critical_hits/show_indicator


data modify entity @s Age set value 5990

data modify entity @s PickupDelay set value -32767

data modify entity @s CustomName set value '{"text": "CRIT!", "color": "red", "bold": true}'

data modify entity @s CustomNameVisible set value 1b

data remove entity @s Item.tag
