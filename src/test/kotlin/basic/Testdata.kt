package basic

import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.orNull
import io.kotest.property.arbitrary.string
import io.kotest.property.arbitrary.uuid
import sample.KotlinItem

// [START example]
private val aShortString = Arb.string(1..10)

// partition key may not be empty
private val aPartitionKey = Arb.uuid().map { it.toString() }

private val aSortKey = Arb.int()

val aJavaItem =
    arbitrary {
        // this block is evaluated to produce a new testdata value
        JavaItem().apply {
            // we use the existing generators to populate our item with random values
            partitionKey = aPartitionKey.bind()
            sortKey = aSortKey.bind()

            // attributes may be null
            stringAttribute = aShortString.orNull().bind()
        }
    }
// [END example]

val aMutableLombokItem =
    arbitrary {
        LombokMutableItem().apply {
            partitionKey = aPartitionKey.bind()
            sortKey = aSortKey.bind()
            stringAttribute = aShortString.orNull().bind()
        }
    }

val anImmutableLombokItem =
    arbitrary {
        LombokImmutableItem.builder()
            .partitionKey(aPartitionKey.bind())
            .sortKey(aSortKey.bind())
            .stringAttribute(aShortString.orNull().bind())
            .build()
    }

val aKotlinItem =
    arbitrary {
        KotlinItem(
            aPartitionKey.bind(),
            aSortKey.bind(),
            aShortString.orNull().bind(),
        )
    }
