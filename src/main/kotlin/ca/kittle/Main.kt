package ca.kittle

import ca.kittle.network.*
import ca.kittle.storage.containerRepository
import com.pulumi.Context
import com.pulumi.kotlin.Pulumi
import kotlinx.coroutines.runBlocking

fun main() {
    Pulumi.run(::run)
}


fun run(ctx: Context) {
    runBlocking {
        val env = Stack.valueOf(ctx.stackName().replaceFirstChar { it.uppercase() })

        // VPC
        val vpc = createVpc(env)
        val publicSubnet = publicSubnet(env, vpc)
        val publicSubnet2 = publicSubnet2(env, vpc)
        val privateSubnet = privateSubnet(env, vpc)
        val privateSubnet2 = privateSubnet2(env, vpc)
        val publicSubnetGroup = publicSubnetGroup(env, publicSubnet, publicSubnet2)
        val privateSubnetGroup = privateSubnetGroup(env, privateSubnet, privateSubnet2)
        val igw = createInternetGateway(env, vpc)
        updateRouteTable(env, vpc, igw)

        // ECR
        val containerRepository = containerRepository(env)

        val publicSubnet1Id = publicSubnet.id.applyValue(fun(name: String): String { return name })
        val publicSubnet2Id = publicSubnet2.id.applyValue(fun(name: String): String { return name })
        val privateSubnet1Id = privateSubnet.id.applyValue(fun(name: String): String { return name })
        val privateSubnet2Id = privateSubnet2.id.applyValue(fun(name: String): String { return name })
        val publicSGId = publicSubnetGroup.id.applyValue(fun(name: String): String { return name })
        val privateSGId = privateSubnetGroup.id.applyValue(fun(name: String): String { return name })
        val vpcId = vpc.id.applyValue(fun(name: String): String { return name })
        val igwId = igw.id.applyValue(fun(name: String): String { return name })
        val repositoryUrl = containerRepository.repositoryUrl.applyValue(fun(name: String): String { return name })

        ctx.export("publicSubnet1Id", publicSubnet1Id)
        ctx.export("publicSubnet2Id", publicSubnet2Id)
        ctx.export("privateSubnet1Id", privateSubnet1Id)
        ctx.export("privateSubnet2Id", privateSubnet2Id)
        ctx.export("publicSubnetGroupId", publicSGId)
        ctx.export("privateSubnetGroupId", privateSGId)
        ctx.export("vpcId", vpcId)
        ctx.export("igwId", igwId)
        ctx.export("repositoryUrl", repositoryUrl)
    }

}

enum class Stack {
    Dev,
    Staging,
    Prod;

    val stackName: String = name.lowercase()
}

fun envTags(env: Stack, resource: String): Map<String, String> = mapOf(
    "Name" to "${env.stackName}-$resource",
    "Env" to env.name
)
