package ca.kittle.storage

import ca.kittle.Stack
import ca.kittle.envTags
import com.pulumi.aws.ecr.kotlin.Repository
import com.pulumi.aws.ecr.kotlin.repository

suspend fun containerRepository(env: Stack): Repository =
    repository("${env.name.lowercase()}-container-repository") {
        args {
            tags(envTags(env, "container-repository"))
        }
    }
