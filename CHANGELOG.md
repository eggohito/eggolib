##  Changes
* Updated to Minecraft 1.20.2

##  Changes
* Removed some power/condition/action types that are now available in Apoli.
* The end bound for the linear-interpolated FOV multiplier is no longer affected by the `affected_by_fov_effect_scale` field of the `modify_fov` power type.
* Changed the object at which the **NBT operation/operator** data types operate on.
* Improved object-based packet implementation.
* Added `should_resolve` boolean field to `modify_label_render` power type, with a default value of `true` (set this to false if you don't plan on using [text components which require resolution.](https://minecraft.wiki/w/Raw_JSON_text_format#Component_resolution))

##  Fixes
* Fixed `open_inventory` entity action type not opening the inventory of the entity that invoked the action if no `power` is specified (for real this time.)

##  Additions
* New power type: `pose`, which sets the pose of the entity that has the power (may not work for certain poses for certain entities). It supports a `pose` field, which then supports these values: `standing`, `fall_flying`, `sleeping`, `swimming`, `spin_attack`, `crouching`, `long_jumping`, `dying`, `croaking`, `using_tongue`, `sitting`, `roaring`, `sniffing`, `emerging`, `digging`.
* New entity condition type: `in_pose`, which checks the entity's current pose. It supports a `pose` field.
* New data type: **message filter**, used for executing an entity action if a message matches the specified filter. It supports these fields:
  * `filter`; the string used as the filter for matching the message. Supports regular expressions.
  * `entity_action`; an optional entity action field, which will be executed upon the message matching the filter.
* New data type: **message consumer**, used for executing entity actions if a message matches the specified filter. It supports these fields:
  * `filter`; the string used as the filter for matching the message. Supports regular expressions.
  * `before_action`/`after_action`; an optional entity action field. When this entity action is invoked will depend on the power type.
* New data type: **message replacer**, used for replacing a message and executing entity actions if the message matches the specified filter. It supports these fields:
  * `filter`; the string used as the filter for matching the message. Supports regular expressions.
  * `before_action`; an optional entity action field, which will be executed *before* the message is replaced.
  * `after_action`; an optional entity action field, which will be executed *after* the message has been replaced successfully (e.g: the message is no longer as it was.)
* New power type: `action_on_sending_message`, which executes an action upon sending a message. It supports these fields:
  * `message_type`; determines whether the actions should be executed if the message is of this type. [See here for a list of vanilla message types](https://minecraft.wiki/w/Chat_type)
  * `filter`/`filters`; optional **message consumer(s)** fields. Invokes `before_action` before the message is prevented from being sent.
  * `priority`; determines the execution priority of the power. Higher values means the power will be executed earlier.
* New power type: `prevent_sending_message`, which prevents a message from being sent. It supports these fields:
  * `message_type`; determines whether the actions should be executed if the message is of this type. [See here for a list of vanilla message types](https://minecraft.wiki/w/Chat_type)
  * `filter`/`filters`; optional **message filter(s)** fields.
  * `priority`; determines the execution priority of the power. Higher values means the power will be executed earlier.
* New item action type: `modify_item_cooldown`, which modifies the item's cooldown. It supports these fields:
  * `modifier`/`modifiers`; these modifiers will be applied to the item's previous cooldown.
* New item condition type: `item_cooldown`, which compares the cooldown progress (ranging from 0.0 to 1.0) of the item to a specific value. It supports these fields:
  * `comparison`; determines how the cooldown progress value of the item is compared to the specified value.
  * `compare_to`; the value to compare the cooldown progress value of the item to.
* New power types, `modify_riding_position` and `modify_passenger_position`, which modifies the mounting position of the entity that has the power, and the passenger(s) of the entity that has the power respectively. It supports these fields:
  * `bientity_condition`; an optional bi-entity condition field, which if present, checks whether either or both the actor (the passenger(s)) and the target (the entity being ridden) fulfills this bi-entity condition.
  * `x_modifier`; an optional modifier field, which will be applied to the passenger's X mounting position.
  * `y_modifier`; an optional modifier field, which will be applied to the passenger's Y mounting position.
  * `z_modifier`; an optional modifier field, which will be applied to the passenger's Z mounting position.
* New damage condition type: `attacker`; similar to Apoli's `attacker` damage condition type, except it has a `bientity_condition` field.

### Full changelog: [`v1.9.1-1.20...v1.10.0-1.20.2`](https://github.com/eggohito/eggolib/compare/v1.9.1-1.20...v1.10.0-1.20.2)
