#> eggolib:name_label_prefix/prep
#
#   >   Prepare the default prefix of the entity's name label
#
#@within eggolib:name_label_prefix


#   Get or create the entry for the entity executing this function
function #rx.playerdb:api/v2/get/self


#   Set the default prefix for the name label of the entity
data modify storage rx.playerdb:io player.data.eggolib.name_label.prefix set value '{"text": "IMPOSTOR", "color": "red", "bold": true}'


#   Save the changes made to the entry for the entity
function #rx.playerdb:api/v2/save/self
