# Pivotaltrackermirror

Mirror a pivotal tracker (PT) project to an another tracker system (only github is currently supported for now as a mirror target):
* Stories in the PT are mirrored into github issues.
* Comments in stories are mirrored into github issue comments


## Why mirroring a pivotal tracker project ?

Mirroring a PT projet stories into github issues provides the following benefits:
* ~~leverage the [watching notifications](https://help.github.com/articles/about-notifications/#types-of-notifications) github feature to track progress on public pivotal trackers which lacks such self-service update notification feature~~ EDIT: Pivotal tracker supports this as of March 2017, see [Viewers can follow](https://www.pivotaltracker.com/blog/project-permissions/) in pivotal tracker blog. 
* leverage [github search features](https://help.github.com/articles/searching-github) to search PT content (e.g. accross multiple  mirrored PT projects, or along with other github repositories), including permalink URLs that return search results (e.g. for [identity and refresh tokens](https://github.com/cf-tm-bot/identity/search?q=refresh&type=Issues&utf8=%E2%9C%93))
* leverage [github @mentions](https://help.github.com/articles/basic-writing-and-formatting-syntax/#mentioning-users-and-teams) to contact github accounts associated with PT public projects contributors, in the context with a specific mirrored story
* mirrored content becomes discoverable: search engines would index it, making it easier to find mirrored PT such as a stack trace

## Deploy it

1. clone this repo and `cd` to the new directory
2. run `mvn clean install``
3. set env var (see below)
4. if you have a cloud foundry instance, create a database service and add this service to the  [manifest.yml](/manifest.yml) and do `cf push` otherwise run `java -jar ./target/pivotaltrackermirror-1.0.0.war

### Necessary env vars

| Key                          | Default value      | Description       
| ---------------------------- |:------------------:|:------------------:|
| admin_password               | password           | mirror UI login    |
| admin_username               | admin              | mirror UI pwd |
| pivotaltracker_token         | *NONE*             | used to pull pivotal tracker data | 
| refresh_mirror_after_minutes | 120                | periodicity of refreshes
| story_filtered_labels        | "CVE"              | exclude some stories assigned some labels (comma-separated, case insensitive)        |

## Use it

### From Api

This application provides an api more than a frontend. Developers or users can use this api.

[Click here to see the api documentation](/src/docs/asciidoc/generated/api.adoc)

You can also test this api through [swagger-ui](http://swagger.io/swagger-ui/), to do so, you have to deploy the app and browse to this endpoint `/swagger-ui.html`.

### From small frontend

Go to url of the app deployed on cloud foundry or http://localhost:8080 for local deployments and manage mirrors through the UI:

![screenshot210](https://cloud.githubusercontent.com/assets/4748380/17184566/3d044c22-542d-11e6-90c7-0863a7cab0d7.png)

In the "Converter token", specify a [github personal access token](https://github.com/blog/1509-personal-api-tokens) with the  ``repo Full control of private repositories`` scope. The account associated to this token needs owner permission on the target github repo, in order for the trackermirror to set up issue labels.


Then a regular job mirrors PT stories (in the non-accepted state) into GH issues.
![screenshot247](https://cloud.githubusercontent.com/assets/4748380/17997053/7cbee95c-6b6c-11e6-94a6-2199f626a894.png)


See sample partial attempt with the [](https://www.pivotaltracker.com/n/projects/997278) mirrored into https://github.com/cf-tm-bot/buildpacks 

List of mirrored stories

![screenshot212](https://cloud.githubusercontent.com/assets/4748380/17189865/c546f448-5443-11e6-8a14-12f19eefc592.png)

A sample mirrored story with comments

![screenshot211](https://cloud.githubusercontent.com/assets/4748380/17189863/c3ddb42a-5443-11e6-8cbe-fe389181a813.png)

Note that screenshots may be out of date w.r.t. latest code improvements.

When upgrading pivotaltrackermirror (e.g. improving mapping markup), you may request to reimport all mirrored stories in the UI.
There are additional [REST endpoints](src/docs/asciidoc/generated/api.adoc) that can provide additional details beyond what the UI displays, e.g. 

```
curl -k -u login:pwd https://trackerapp.org/api/tasks/15

{"jobStatus":"COMPLETE","dateStartTime":"2016-08-12T13:09Z","nextFireTime":"2016-08-25T15:09Z","previousFireTime":"2016-08-25T13:09Z"}
```

## Project history and relations to cloud foundry community

This project originated as a POC to help the Cf community track progress of the CF pivotal tracker stories used to drive the cloudfoundry backlog and features (see [full list](https://github.com/cloudfoundry-community/cf-docs-contrib/wiki) in the sidebar). 

Currently there is no way for non project members to subscribe to PT story updates, see related [cf-dev thread](http://cf-dev.70369.x6.nabble.com/cf-dev-FW-issue-tracker-permissions-tt2763.html#a5014) or https://github.com/cloudfoundry/cli/issues/560#issuecomment-156846815 where CLI team PM tried to add non-team members to provide ways to check story status), or to comment on stories.

This project is generally trying to make it easier for the cloudfoundry community to interact with cloud foundry core contributors and suggest product ideas or comment current progresses/decisions/investigations.

## Roadmap and future evolutions

Feedback and contributions are welcome. See issues for current ideas of future improvement. We use milestones for tracking  relative priority, have a look at https://huboard.com/orange-cloudfoundry/pivotaltrackermirror#/milestones for a visual display.
