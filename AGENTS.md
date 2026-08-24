# AGENTS.md

Guidance for AI agents and automation working in this repository.

## Project overview

Apache MyFaces Tobago is a multi-module Maven project for a JSF component framework. The main language is Java, with
TypeScript and SCSS assets in the theme modules.

Top-level modules:

- `tobago-tool` - annotation processors, config helpers, and test tooling.
- `tobago-core` - core Tobago Java APIs, components, renderers, config, and unit tests.
- `tobago-theme` - TypeScript, SCSS, CSS, JavaScript, and theme Maven modules.
- `tobago-example` - demo and example applications plus integration tests.
- `tobago-assembly` - assembly/release packaging.

## Component declarations

Tobago components are declared in `*TagDeclaration` source files. These declarations define the component tag,
attributes, supported behaviors, markup, and component metadata used by annotation processing. For example, the
`<tc:textarea>` component is declared by
`tobago-core/src/main/java/org/apache/myfaces/tobago/internal/taglib/component/TextareaTagDeclaration.java`.

Each declaration generates a `UI[uiComponent]` class during `mvn clean install`; for example,
`TextareaTagDeclaration` generates `UITextarea`. The generated UI component extends its corresponding
`AbstractUI[uiComponent]` base class, such as `UITextarea extends AbstractUITextarea`. Every UI component also has a
matching `*Renderer` class; for example, `TextareaRenderer` renders `UITextarea`.

When adding or changing a component, begin with its corresponding `*TagDeclaration` file and check the generated tag
metadata, implementation, renderer, documentation, and examples or tests for related updates.

Declare component-specific attributes and their behavior in the `*TagDeclaration` file or a shared declaration
interface it extends (for example, `IsVisual`). Use the annotation metadata there to control the generated component
code, including types, default values, and default code. Do not change `tobago-tool-apt` to implement behavior for a
single component attribute; change that module only when the annotation-processing or code-generation mechanism itself
must change for all applicable components. Add a renderer test when an attribute affects rendered output.

## Environment

- Use JDK 21 or newer for builds. Maven is configured to compile with Java release 17, but the enforcer requires Java 21+.
- Use Maven 3.
- The `frontend` Maven profile installs its own Node.js/npm through `frontend-maven-plugin`; do not assume a system Node
  version is required for Maven builds.

## Build and verification

Common commands from the repository root:

```sh
mvn clean install
```

Use the frontend profile when TypeScript, SCSS, CSS, JavaScript, Bootstrap assets, or theme resources change:

```sh
mvn clean install -Pfrontend
```

Closer CI-equivalent verification:

```sh
mvn clean install checkstyle:check apache-rat:check dependency-check:check -DautoUpdate=false
```

To build the Maven project, use in the root directory:

```sh
mvn clean install -T1C
```

## Frontend/theme work

Frontend sources live mainly below `tobago-theme/tobago-theme-standard/src/main` directory. Do not edit JavaScript or
CSS files. Edit TypeScript or SCSS files instead.

To rebuild the theme (CSS/JS), use the following command:

```sh
mvn clean install -Pfrontend -T1C
```

## Tests

- Java unit tests use Maven/JUnit and normally run through `mvn test` or `mvn clean install`.
- TypeScript tests use Jest via `npm run ts-test` or as part of `npm run ts`.
- Demo integration tests are under `tobago-example/tobago-example-demo`.
  - Jasmine/docker flow: `mvn clean verify -Pdocker -Pintegration-tests`
  - Playwright flow: go to `tobago-example/tobago-example-demo` and start the demo with
    `mvn clean package -Pdev -Pjetty jetty:run`, then run `npx playwright test` from
    `tobago-example/tobago-example-demo`.

## Code style

- Follow `.editorconfig`: UTF-8, LF endings, final newline, trim trailing whitespace, 2-space visual tab width, and
  120-character max line length.
- Java uses project Checkstyle rules configured through Maven.
- Preserve Apache license headers where present and add them to new source files when appropriate for this ASF project.
- Keep Java imports consistent with the configured layout: general imports, then `jakarta.*`, `javax.*`, `java.*`, then
  static imports.

## Change guidelines

- Prefer minimal, targeted changes. Avoid unrelated formatting churn.
- Do not edit generated/bundled theme outputs by hand unless the source is unavailable; update the source and regenerate.
- When changing public components, tags, renderers, config files, or examples, check whether documentation, generated
  tag metadata, tests, or examples also need updates.
- Be careful with server/container profiles in `tobago-example`; many profiles target specific runtimes such as Jetty,
  Tomcat, TomEE, Open Liberty, Quarkus, and Spring Boot.

## Commit messages

Follow `CONTRIBUTING.md` semantic commit format:

```text
<type>(<scope>): <subject>
```

Allowed types include `feat`, `fix`, `refactor`, `style`, `test`, `docs`, `build`, `ci`, and `chore`. Use imperative,
present-tense subjects, no initial capital, no trailing period, and keep lines at or below 100 characters. Add the
related Apache JIRA issue as a footer, using the format `Issue: TOBAGO-<number>`. Tickets are available at
`https://issues.apache.org/jira/browse/TOBAGO-<number>`.
