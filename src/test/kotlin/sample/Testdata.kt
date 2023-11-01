package sample

import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.boolean
import io.kotest.property.arbitrary.byte
import io.kotest.property.arbitrary.byteArray
import io.kotest.property.arbitrary.float
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.long
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.orNull
import io.kotest.property.arbitrary.set
import io.kotest.property.arbitrary.short
import io.kotest.property.arbitrary.string
import io.kotest.property.arbitrary.uuid

private val aShortString = Arb.string(1..10)
private val anEmptyOrShortString = Arb.string(0..10)

// partition key may not be empty
private val aPartitionKey = Arb.uuid().map { it.toString() }

private val aSortKey = Arb.int()

private fun nested(level: Int): Arb<Nested> =
    arbitrary {
        Nested().apply {
            stringAttribute = aShortString.bind()
            nestedList = if (level == 0) emptyList() else Arb.list(nested(level - 1), 0..3).bind()
        }
    }

private fun nestedLombok(level: Int): Arb<NestedLombok> =
    arbitrary {
        NestedLombok.builder()
            .stringAttribute(aShortString.bind())
            .nestedList(if (level == 0) emptyList() else Arb.list(nestedLombok(level - 1), 0..3).bind())
            .build()
    }

val collectionMaxSize = 5

val aJavaRecord = arbitrary {
    JavaRecord().apply {
        partitionKey = aPartitionKey.bind()
        sortKey = aSortKey.bind()

        // attributes may be null
        stringAttribute = aShortString.orNull().bind()

        nestedList = Arb.list(nested(3), 0..collectionMaxSize).bind()
    }
}

val aLombokRecord = arbitrary {
    LombokRecord.builder()
        .partitionKey(aPartitionKey.bind())
        .sortKey(aSortKey.bind())
        // scalars
        .stringAttribute(aShortString.orNull().bind())
        .intAttribute(Arb.int().orNull().bind())
        .booleanAttribute(Arb.boolean().orNull().bind())
        .booleanPrimitiveAttribute(Arb.boolean().bind())
        .longAttribute(Arb.long().orNull().bind())
        // Numbers can have up to 38 digits of precision. Exceeding this results in an exception. If you need greater precision than 38 digits, you can use strings.
        // .doubleAttribute(Arb.double(includeNonFiniteEdgeCases = false).orNull().bind())
        .floatAttribute(Arb.float(includeNonFiniteEdgeCases = false).orNull().bind())
        .shortAttribute(Arb.short().orNull().bind())
        .byteAttribute(Arb.byteArray(Arb.int(0..10), Arb.byte()).bind())
        // lists may be empty
        .stringList(Arb.list(anEmptyOrShortString, 0..collectionMaxSize).bind())
        // map may be empty, map keys may not be empty
        .stringStringMap(Arb.map(aShortString, anEmptyOrShortString, maxSize = collectionMaxSize).bind())
        // set may not be empty, entries may be empty
        .stringSet(Arb.set(anEmptyOrShortString, 1..collectionMaxSize).bind())
        // lists may be empty
        .nestedList(Arb.list(nested(3), 0..collectionMaxSize).bind())
        .nestedLombokList(Arb.list(nestedLombok(3), 0..collectionMaxSize).bind())
        // map keys may not be empty
        .nestedMap(Arb.map(aShortString, nested(3), maxSize = collectionMaxSize).bind())
        .build()
}

val aKotlinRecord = arbitrary {
    KotlinRecord(
        aPartitionKey.bind(),
        aSortKey.bind(),
        aShortString.orNull().bind(),
    )
}
