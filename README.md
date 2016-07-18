# Pivotaltrackermirror
Mirror a pivotal tracker project to an another tracker.

Only github is currently supported for now.

Can be ran under Cloud Foundry (see [manifest.yml](/manifest.yml)).

## Use it

1. clone this repo and `cd` to the new directory
2. run `mvn clean install``
3. set env var (see below)
4. if you have a cloud foundry run create a database service and add this service to the manifest and do `cf push` otherwise run `java -jar ./target/pivotaltrackermirror-1.0.0.war
5. go to url provided by cloud foundry or http://localhost:8080

## Necessary env var

| Key                          | Default value      |
| ---------------------------- |:------------------:|
| admin_password               | password           |
| admin_username               | admin              |
| github_token                 | *NONE*             |
| pivotaltracker_token         | *NONE*             |
| refresh_mirror_after_minutes | 120                |

