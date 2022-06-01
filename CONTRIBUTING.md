# (WIP) Contributing Info

## Semantic Commit Messages

Inspired by [Git Commit Msg][git-commit-msg], [Angular Commit Message Format][angular-commit-message-format] and [Conventional Commits][conventional-commits]


### <a name="commit-message-format"></a> Commit Message Format

Each commit message consists of a **header**, a **body** and a **footer**. The header has a special
format that includes a **type**, a **scope**, and a **subject**:

```html
<type>(<scope>): <subject>
<BLANK LINE>
<body>
<BLANK LINE>
<footer>
```

> Any line of the commit message cannot be longer 100 characters!<br/>
This allows the message to be easier to read on GitHub as well as in various Git tools.

##### Type

Must be one of the following:

* **feat**: A new feature
* **fix**: A bug fix
* **refactor**: A code change that neither fixes a bug nor adds a feature
* **style**: Changes that do not affect the meaning of the code (white-space, formatting, missing semi-colons, etc)
* **test**: Changes tests only
* **docs**: Changes to the documentation
* **build**: Changes to the dependencies, devDependencies, or build tooling
* **ci**: Changes to our Continuous Integration configuration
* **chore**: Changes to the auxiliary tools such as release scripts

##### Scope

The scope could be anything that helps specify the scope (or feature) that is changing.

Examples
- fix(select):
- docs(menu):

The `(<scope>)` field is optional.

##### Subject

The subject contains a succinct description of the change:

* use the imperative, present tense: "change" not "changed" nor "changes"
* don't capitalize first letter
* no period (.) at the end

##### Body

Just as in the **subject**, use the imperative, present tense: "change" not "changed" nor "changes".
The body should include the motivation for the change and contrast this with previous behavior.

##### Footer

The footer should contain any information about **Breaking Changes** and is also the place to
reference Jira issues that this commit **Issue: TOBAGO-XXXX**.

[git-commit-msg]: http://karma-runner.github.io/6.1/dev/git-commit-msg.html

[angular-commit-message-format]: https://github.com/angular/material/blob/master/.github/CONTRIBUTING.md#-commit-message-format

[conventional-commits]: https://www.conventionalcommits.org/en/v1.0.0/
