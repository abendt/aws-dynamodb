package basic

import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.orNull
import io.kotest.property.arbitrary.string
import io.kotest.property.arbitrary.uuid
import sample.KotlinRecord

// [START example]
private val aShortString = Arb.string(1..10)

// partition key may not be empty
private val aPartitionKey = Arb.uuid().map { it.toString() }

private val aSortKey = Arb.int()

val aJavaRecord =
    arbitrary {
        JavaRecord().apply {
            partitionKey = aPartitionKey.bind()
            sortKey = aSortKey.bind()

            // attributes may be null
            stringAttribute = aShortString.orNull().bind()
        }
    }
// [END example]

val aMutableLombokRecord =
    arbitrary {
        LombokMutableRecord().apply {
            partitionKey = aPartitionKey.bind()
            sortKey = aSortKey.bind()
            stringAttribute = aShortString.orNull().bind()
        }
    }

val anImmutableLombokRecord =
    arbitrary {
        LombokImmutableRecord.builder()
            .partitionKey(aPartitionKey.bind())
            .sortKey(aSortKey.bind())
            // scalars
            .stringAttribute(aShortString.orNull().bind())
            .build()
    }

val aKotlinRecord =
    arbitrary {
        KotlinRecord(
            aPartitionKey.bind(),
            aSortKey.bind(),
            aShortString.orNull().bind(),
        )
    }
