#> eggolib:name_label_prefix/apply
#
#   >   Change the prefix for the name label of the entity
#
#@user


#   Get the entry of the entity
function #rx.playerdb:api/v2/get/self


#   If the `prefix` NBT of the `eggolib:name_label` storage is not empty, use the stringified JSON text component from that NBT as the new prefix
execute if data storage eggolib:name_label prefix run data modify storage rx.playerdb:io player.data.eggolib.name_label.prefix set from storage eggolib:name_label prefix


#   Save the changes made to the prefix of the name label of the entity
execute if data storage eggolib:name_label prefix run function #rx.playerdb:api/v2/save/self


#   Do some clean up
data remove storage eggolib:name_label prefix
