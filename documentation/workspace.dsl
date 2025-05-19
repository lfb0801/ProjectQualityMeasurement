workspace "Project Quality Measurement" "Docs, ADRs and C4-diagrams" {

    model {

        local = group local {
            user = person "User"

            targetProject = softwareSystem "Target project" "This project is the target of the quality measurement" "git/fs" {
                tags target
            }
        }

        sonar = softwareSystem "SonarQube" "Static code analysis tool" "SonarQube-Server:Community" {
            sonarServer = container "SonarServer" {
                tags external component
            }
            sonarDatabase = container "Postgress" {
                tags external db
            }

            sonarServer -> sonarDatabase "stores data in"

            tags external api
        }

        pqm = softwareSystem "Project Quality Measurement" "This project uses many different ways of collecting metrics and analysees about quality of a given project and generates a way to measure change in quality over time" {
            database = container "db" "Stores different Scanner and aggregates, (potentially even diffs)" {
                tags internal db
            }

            frontend = container "simple web-app" "Offers a way to compare the compare metrics between given git tags" "NOT IMPLEMENTED" {
                tags webBrowser notImplemented
            }

            backend = container "Quality Measurement Server" "Creates several metrics based on the target project" "java/spring" {

                sonarScanner = component "Sonar scanner" {
                    tags internal component notImplemented
                }

                gitScanner = component "Git scanner" "This component can perform git operations on a clone of the target project" {
                    tags internal component
                }

                fsScanner = component "File system scanner" {
                    tags internal component
                }

                aggregator = component "Scan aggregator" "aggregates results from the different scanners" {
                    tags internal component
                }

                diffScanner = component "diffScanner" "Attempts to report the differences between multiple aggregates" {
                    tags internal component notImplemented
                }

                sonarScanner -> sonarServer "retrieves scan results from"

                gitScanner -> targetProject "analyzes git behaviour"
                fsScanner -> targetProject "looks at the file system/folder structure and file content in"

                aggregator -> gitScanner "uses"
                aggregator -> fsScanner "uses"
                aggregator -> sonarScanner "uses"

                diffScanner -> aggregator "retrieves multiple aggregates"

                !docs docs/api
                tags internal api
            }

            frontend -> diffScanner "ask for comparison of 2 git tags"
            backend -> database "persists in"

            tags internal
        }

        user -> targetProject "commits and works on"
        user -> pqm "uses"
        user -> frontend "Interacts with"

        sonarServer -> targetProject "analyzes"
        pqm -> sonarServer "retrieves analysees"
    }

    !docs docs
    !adrs decisions

    views {
        systemLandscape pqm {
            include *
            autolayout lr
        }

        systemContext targetProject context-targetProject {
            include *
            exclude user
            autolayout lr
        }

        container pqm container-pqm {
            include *
            autolayout lr
        }

        container sonar {
            include *
            autolayout lr
        }

        component backend backend-component {
            include *
            autolayout lr
        }

        styles {
            element Element {
                color #ffffff
                shape RoundedBox
                fontSize 16
            }

            element Person {
                background #1168bd
                shape person
            }

            element internal {
                background #4A90E2
            }

            element component {
                shape component
            }

            element api {
                shape hexagon
            }

            element db {
                shape cylinder
            }

            element webBrowser {
                shape webBrowser
            }

            element notImplemented {
                background #ff0800
            }

            element target {
                shape folder
                background #007f5c
            }

            element external {
                background #696969
            }
        }
    }

}
