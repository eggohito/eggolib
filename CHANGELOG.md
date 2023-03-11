##  Changelog

* Updated testdata
* Tweaked the `eggolib:loop` meta action type
  * The action specified in the `before_action` field is now executed before getting the score from the specified scoreboard
* Updated the `eggolib:invisibility` power type
  * Now has the new fields from the `apoli:invisibility` power type
* Removed the `eggolib:action_on_item_use` power type
* Removed the `eggolib:clear_key_cache` entity action type
* Added an `eggolib:equal` bi-entity condition type
* Tweaked the `eggolib:game_event_listener` power type
  * Now has a `show_particle` boolean field, which determines whether it should spawn the vibration particle
  * Now can listen to the game events emitted by the power holder
* Added an `eggolib:selector_action` entity action type
* Made the `SCOREBOARD` data type accept score holders
  * e.g: `scoreHolderName` and `@e[tag = test, limit = 1]` are valid inputs
* Added an `eggolib:leash` bi-entity action type
* Added an `eggolib:moon_phase` entity condition type
* Optimized the `eggolib:in_screen` entity condition type
  * Now it shouldn't lag the server exponentially if many players are invoking it
* Added an `eggolib:crawling` power type
* Added an `eggolib:crawling` entity condition type