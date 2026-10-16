# Dependency maintenance

Dependabot checks Maven and GitHub Actions weekly and groups routine minor/patch
updates into PRs targeting `dev`. Dependabot reads this configuration from the
default branch, so changes to `.github/dependabot.yml` become active after normal
integration into `main`.

After Maven CI succeeds, a separate job squash-merges eligible same-repository
Dependabot PRs into `dev`, rechecking the current target and tested head. Stable
numeric Maven versions (including two-part versions) and stable Actions versions
are supported; Guava may retain its `-jre` or `-android` variant. Major updates,
`0.x` minor updates, unsupported version formats, `io.allurx:maven-parent`, and
`allurx/allurx-build` updates require review. Ordinary internal library updates
follow the same version policy as other dependencies.

The merge job does not check out PR code or run project dependencies. Its scoped
write permissions are separate from the read-only verification job. A merge using
`GITHUB_TOKEN` does not trigger another push workflow; the later `dev` to `main`
integration still follows the repository's normal validation and release process.
No release tag or Maven Central publication is created by dependency auto-merging.

If concurrent changes to a workflow file cause GitHub to reject a merge for missing
`workflows` permission, refresh the PR branch and rerun verification. Do not bypass
the tested-head check or add unrelated Actions permissions. See the
[GitHub CLI maintainer explanation](https://github.com/cli/cli/issues/11493).
