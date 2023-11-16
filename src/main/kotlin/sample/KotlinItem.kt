package sample

import io.andrewohara.dynamokt.DynamoKtPartitionKey
import io.andrewohara.dynamokt.DynamoKtSortKey

// [START example]
data class KotlinItem(
    @DynamoKtPartitionKey val partitionKey: String,
    @DynamoKtSortKey val sortKey: Int,
    val stringAttribute: String?,
)
// [END example]
