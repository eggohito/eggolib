##  Changelog

* Added the `modify_fov` power type
* Updated the `modify_label_render` power type
    * Now parses the specified JSON text component in the `text` field
    * Now has a `before_parse_action` and `after_parse_action` entity action fields
    * Now has a `tick_rate` positive integer field
* Added the `exposed_to_weather` entity condition type
* Updated the `scoreboard` entity condition type
    * Now accepts a score holder in its `name` field
* Updated the `inventory` entity condition type
    * Now has a `comparison` and `compare_to` fields
