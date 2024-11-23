package ie.setu.helpers

import ie.setu.domain.Tracker
import ie.setu.domain.User

val nonExistingEmail = "112233445566778testUser@xxxxx.xx"
val validName = "Test User 1"
val validEmail = "testuser1@test.com"

val users = arrayListOf<User>(
    User(name = "Alice Wonderland", email = "alice@wonderland.com", id = 1),
    User(name = "Bob Cat", email = "bob@cat.ie", id = 2),
    User(name = "Mary Contrary", email = "mary@contrary.com", id = 3),
    User(name = "Carol Singer", email = "carol@singer.com", id = 4)
)

val trackers = arrayListOf<Tracker>(
    Tracker(userId = 1, calories = 100.0, drinking = 5.3, walkHours = 3.5, id = 1),
    Tracker(userId = 1, calories = 80.0, drinking = 6.3, walkHours = 0.2, id = 2),
    Tracker(userId = 1, calories = 150.0, drinking = 4.8, walkHours = 4.0, id = 3),
    Tracker(userId = 1, calories = 185.0, drinking = 3.5, walkHours = 3.2, id = 4),

    Tracker(userId = 2, calories = 133.0, drinking = 3.8, walkHours = 12.5, id = 5),
    Tracker(userId = 2, calories = 122.0, drinking = 7.5, walkHours = 5.6, id = 6),

    Tracker(userId = 3, calories = 101.0, drinking = 2.8, walkHours = 8.0, id = 7),
    Tracker(userId = 3, calories = 105.0, drinking = 3.5, walkHours = 6.2, id = 8),

    Tracker(userId = 4, calories = 200.0, drinking = 6.8, walkHours = 13.0, id = 9),
    Tracker(userId = 4, calories = 205.0, drinking = 6.5, walkHours = 5.2, id = 10),

)