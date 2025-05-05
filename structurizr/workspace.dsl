workspace "Project Quality Measurement" "Docs, ADRs and C4-diagrams" {
    model {

        user = person "User"

        pgm = softwareSystem "Project Quality Measurement" {
            frontend = container "simple web-app" "Offers a way to compare the compare metrics between given Tags"
            backend = container "api" "Creates several metrics based on a given project" "java/spring" {

            }
        }

        sourceControl = softwareSystem "External source control system"

        user -> frontend "Interacts with"

        frontend -> backend "send requests to"
        backend -> sourceControl "analyzes"
    }

    !docs docs
    !adrs decisions
}
