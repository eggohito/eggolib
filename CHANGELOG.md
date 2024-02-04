#   Changelog

##  Changes

* The current screen and perspective is now only synced after a certain amount of ticks (controlled by the `syncTickRate` field in the client config.)
* Simplified the attack distance scaling factor implementation of the `eggolib:invisibility` power type.
* Optimized implementation of syncing command tags.

##  Fixes

* Fixed performance issue with setting the item stack's holder.
* Fixed `open_inventory` entity action type not opening the entity's inventory if no `power` is specified.
