workspace "Project Quality Measurement" "Docs, ADRs and C4-diagrams" {

    model {
        local = group local {
            user = person "User"

            targetProject = softwareSystem "Target project" "This project is the target of the quality measurement" "git/fs" {
                tags target
            }
        }

        aiModel = softwareSystem "AI Model" "This is the AI model that is used to generate reports based on the aggregated data" "openai/gpt-4" {
            tags external
        }

        pqm = softwareSystem "Project Quality Measurement" "This project uses many different ways of collecting metrics and analysees about quality of a given project and generates a way to measure change in quality over time" {
            database = container "db" "Stores different Scanner and aggregates, (potentially even diffs)" {
                tags internal db
            }

            frontend = container "simple web-app" "Offers a way to compare the compare metrics between given git tags" "NOT IMPLEMENTED" {
                tags webBrowser notImplemented
            }

            backend = container "Quality Measurement Server" "Creates several metrics based on the target project" "java/spring" {

                gitScanner = component "Git scanner" "This component can perform git operations on a clone of the target project" {
                    tags internal component
                }

                fsScanner = component "File system scanner" {
                    tags internal component
                }

                aggregator = component "Scan aggregator" "aggregates results from the different scanners and stores them as files" {
                    tags internal component
                }

                aiReporter = component "AI Reporter" "Generates a report based on the aggregated data" {
                    tags internal component
                }

                gitScanner -> targetProject "analyzes git behaviour"
                fsScanner -> targetProject "looks at the file system/folder structure and file content in"

                aggregator -> gitScanner "uses"
                aggregator -> fsScanner "uses"

                aiReporter -> aggregator "retrieves multiple aggregates"

                aiReporter -> aiModel "sends aggregates for analysis to"

                !docs docs/components
                tags internal api
            }

            frontend -> aiReporter "ask for comparison of 2 git tags"
            backend -> database "persists in"

            tags internal

            !docs docs/design
        }

        user -> targetProject "commits and works on"
        user -> pqm "uses"
        user -> frontend "Interacts with"

        research = softwareSystem "research" {
            container "research on churn" {
                !docs docs/research/measuring-churn.adoc
            }
            container "expert interview" {
                !docs docs/research/expert-interview.adoc
            }
            container "literature study" {
                !docs docs/research/literature-study.adoc
            }
            container "regels code per developer" {
                !docs docs/research/regels-code-per-developer.adoc
            }
            container "scope voor meetpunten" {
                !docs docs/research/scope-voor-meetpunten.adoc
            }
            container "proof-DaC-impl" {
                !docs docs/research/implementeren-van-docs-as-code.adoc
            }
        }
    }

    !adrs decisions dev.lfb0801.AsciiDocAdrImporter

    views {
        systemLandscape pqm {
            include *
            include aiModel
            autolayout lr
        }

        systemContext targetProject context-targetProject {
            include *
            include aiModel
            exclude user
            autolayout lr
        }

        container pqm container-pqm {
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
