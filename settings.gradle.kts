rootProject.name = "dusk-shared"

include(":network", ":utility")

project(":network").projectDir = file("network/")
project(":utility").projectDir = file("utility/")
