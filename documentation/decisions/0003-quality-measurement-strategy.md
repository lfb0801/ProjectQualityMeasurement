# quality measurement strategy

## Status

Proposed

## Context

The scope for this project is analyzing codebases that are so large that a developer can't use his/her own reasoning to
determine where the quality of their application is declining.
This causes the scope of our quality scan to be large from the offset.

We also need to consider the **GAP-analysis** (found in the Justification) and create a clear
distinction between ourselves and our two closest 'competitors'.

## Decision

We will use JGit to analyze the evolution of a repository over time, focusing on the following aspects:

- **Code Hotspots**: Identify areas of the codebase that are frequently modified.
- **Modules/Directories over files**: Analyze the evolution of modules or directories rather than individual files to
  capture architectural changes.
- **Greater comparison scope**: Compare the evolution of modules or directories across multiple releases to identify
  long-term trends and patterns.
- **Metric Collection**: Gather metrics about:
    - Commit frequency and size
    - Directory structure changes
    - File movement patterns
    - Code churn rates
    - Developer interaction patterns

## Consequences

### Positive

- ✅ Unique focus on temporal analysis sets us apart from SonarQube and CodeClimate
- ✅ Analysis of directory-level changes provides architectural insights
- ✅ Git-based analysis requires no special tooling or setup from users
- ✅ Can work offline and analyze private repositories easily

### Negative

- ❌ No traditional static code analysis capabilities
- ❌ Cannot detect security vulnerabilities
- ❌ Limited insights into code quality at a specific point in time
- ❌ May require significant processing power for large repositories with long histories

## Alternatives Considered, GAP-analysis

### Objectives

* **Assess** how each tool addresses key code quality and maintainability requirements.
* **Identify** gaps in features, integrations, and usability.
* **Recommend** areas for improvement or supplementing with other tools.

### Evaluation Criteria

1. **Static Analysis & Rule Coverage**: Depth and breadth of coding rules (security, style, bugs).
2. **Maintainability & Technical Debt**: Metrics for complexity, duplication, debt estimation.
3. **Code Hotspots & Temporal Analysis**: Identification of evolving risky areas using historical data.
4. **Security Scanning**: Support for vulnerability detection (SAST).
5. **Integrations & CI/CD**: Plugins, cloud services, and pipeline compatibility.
6. **Reporting & Visualization**: Dashboards, trend charts, and actionable insights.
7. **Usability & Setup**: Installation complexity, documentation, learning curve.
8. **Pricing & Licensing**: Open-source vs commercial licensing, cost models.

### Feature Matrix

| Criterion                         | CodeScene                              | SonarQube                                     | CodeClimate                             |
|-----------------------------------|----------------------------------------|-----------------------------------------------|-----------------------------------------|
| Static Analysis & Rule Coverage   | Moderate set; custom rules via plugin  | Extensive rule sets (1000+); language support | Good core rules; limited security rules |
| Maintainability & Technical Debt  | Advanced debt estimation; XY metric    | Strong debt calculation; SQALE method         | Basic Maintainability score             |
| Code Hotspots & Temporal Analysis | **Yes**: Hotspot detection, trends     | No temporal analytics                         | No temporal analytics                   |
| Security Scanning                 | Limited SAST via plugin                | Strong SAST (OWASP, CWE)                      | SAST add-ons available                  |
| Reporting & Visualization         | Interactive hotspot maps, trend graphs | Rich dashboards, customizable widgets         | Simple dashboards, PR comments          |

### Gap Summary

* **Temporal Analytics**: SonarQube and CodeClimate lack built-in code hotspot and evolution analysis, which CodeScene
  provides.
* **Security Depth**: SonarQube leads in security rule coverage; CodeClimate and CodeScene rely on plugins or add-ons.
* **Rule Coverage**: SonarQube offers the most extensive rule library; CodeClimate has fewer built-in rules.
* **Visualization**: CodeScene excels in trend and hotspot visualization; SonarQube offers flexible dashboards;
  CodeClimate is more lightweight.
* **Cost Structure**: SonarQube offers a free core; CodeScene and CodeClimate are fully commercial with per-seat or
  subscription fees.

### What sets this project apart

Unlike traditional tools, our application zeroes in exclusively on the evolution of a repository over time. Rather than
simply reporting issues at a single point in time, it continually tracks how code and architectural smells emerge,
persist, or regress across successive commits and releases. This temporal focus reveals whether engineers are truly
resolving root causes or merely applying superficial fixes to satisfy static-analysis gates like SonarQube’s rules. By
highlighting long‑lived hotspots and patterns of “quick fixes,” our tool encourages teams to prioritize genuine
architectural improvements over tactical workarounds, fostering sustainable code health and guiding strategic
refactoring initiatives.

