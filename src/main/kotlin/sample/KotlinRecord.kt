package sample

import io.andrewohara.dynamokt.DynamoKtPartitionKey
import io.andrewohara.dynamokt.DynamoKtSortKey

data class KotlinRecord(
    @DynamoKtPartitionKey val partitionKey: String,
    @DynamoKtSortKey val sortKey: Int,
    val stringAttribute: String?,
)
