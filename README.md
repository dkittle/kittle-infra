# Kittle Infra

A project to manage infrastructure for all my little projects 😎

## Setup

Make sure Pulumi (https://pulumi.com) is installed and set up for your AWS region and credentials.

## Create the Pulumi Dev Stack

Create a stack for this project
`pulumi stack init`

Change to dev stack, if needed
`pulumi stack select dev`

Set aws region
`pulumi config set aws:region ca-central-1`

Create resources for your stack
`pulumi up`


## Destroy the Stack
To save money 🤣

`pulumi destroy`
# kittle-infra
