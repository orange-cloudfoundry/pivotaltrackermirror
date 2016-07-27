# Pivotaltrackermirror

Mirror a pivotal tracker (PT) project to an another tracker system (only github is currently supported for now as a mirror target):
* Stories in the PT are mirrored into github issues.
* Comments in stories are mirrored into github issue comments


# Why mirroring a pivotal tracker project ?

Mirroring a PT projet stories into github issues provides the following benefits:
* leverage the [watching notifications](https://help.github.com/articles/about-notifications/#types-of-notifications) github feature to track progress on public pivotal trackers which lacks such self-service update notification feature
* leverage [github search features](https://help.github.com/articles/searching-github) to search PT content (e.g. accross multiple  mirrored PT projects, or along with other github repositories)
* leverage [github @mentions](https://help.github.com/articles/basic-writing-and-formatting-syntax/#mentioning-users-and-teams) to contact github accounts associated with PT public projects contributors, in the context with a specific mirrored story

## Use it

1. clone this repo and `cd` to the new directory
2. run `mvn clean install``
3. set env var (see below)
4. if you have a cloud foundry instance, create a database service and add this service to the  [manifest.yml](/manifest.yml) and do `cf push` otherwise run `java -jar ./target/pivotaltrackermirror-1.0.0.war
5. go to url provided by cloud foundry or http://localhost:8080

### Necessary env vars

| Key                          | Default value      |
| ---------------------------- |:------------------:|
| admin_password               | password           |
| admin_username               | admin              |
| github_token                 | *NONE*             |
| pivotaltracker_token         | *NONE*             |
| refresh_mirror_after_minutes | 120                |

## Project history and relations to cloud foundry community

This project originated as a POC to help the Cf community track progress of the CF pivotal tracker stories used to drive the cloudfoundry backlog and features (see [full list](https://github.com/cloudfoundry-community/cf-docs-contrib/wiki) in the sidebar). 

Currently there is no way for non project members to subscribe to PT story updates, see related [cf-dev thread](http://cf-dev.70369.x6.nabble.com/cf-dev-FW-issue-tracker-permissions-tt2763.html#a5014) or https://github.com/cloudfoundry/cli/issues/560#issuecomment-156846815 where CLI team PM tried to add non-team members to provide ways to check story status), by leveraging github watch features to selectively subscribe to existing mirrored stories, or up-to-come future stories.

Thsi project is also trying to make it easier for the cloudfoundry community to interact with cloud foundry core contributors and suggest product ideas or comment current progresses/decisions/investigations.
