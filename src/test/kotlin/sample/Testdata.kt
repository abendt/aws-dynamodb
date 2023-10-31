package sample

import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.boolean
import io.kotest.property.arbitrary.byte
import io.kotest.property.arbitrary.byteArray
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.orNull
import io.kotest.property.arbitrary.set
import io.kotest.property.arbitrary.string
import io.kotest.property.arbitrary.uuid

private val shortString = Arb.string(1..10)
private val emptyOrShortString = Arb.string(0..10)

// partition key may not be empty
private val partitionKeyArb = Arb.uuid().map { it.toString() }

private val sortKeyArb = Arb.int()

private fun nestedGen(level: Int): Arb<Nested> =
    arbitrary {
        Nested().apply {
            stringAttribute = shortString.bind()
            nestedList = if (level == 0) emptyList() else Arb.list(nestedGen(level - 1), 0..3).bind()
        }
    }

val collectionMaxSize = 5

val javaRecordArb = arbitrary {
    JavaRecord().apply {
        partitionKey = partitionKeyArb.bind()
        sortKey = sortKeyArb.bind()

        // attributes may be null
        stringAttribute = shortString.orNull().bind()

        // other scalars
        intAttribute = Arb.int().bind()

        isBooleanAttribute = Arb.boolean().bind()

        byteAttribute = Arb.byteArray(Arb.int(0..10), Arb.byte()).bind()

        // lists may be empty
        stringList = Arb.list(emptyOrShortString, 0..collectionMaxSize).bind()

        // map may be empty, map keys may not be empty
        stringStringMap = Arb.map(shortString, Arb.string(), maxSize = collectionMaxSize).bind()

        // set may not be empty, entries may be empty
        stringSet = Arb.set(emptyOrShortString, 1..collectionMaxSize).bind()

        // lists may be empty
        nestedList = Arb.list(nestedGen(3), 0..collectionMaxSize).bind()

        // map keys may not be empty
        nestedMap = Arb.map(shortString, nestedGen(3), maxSize = collectionMaxSize).bind()
    }
}

val lombokRecordArb = arbitrary {
    LombokRecord.builder()
        .partitionKey(partitionKeyArb.bind())
        .sortKey(sortKeyArb.bind())
        .stringAttribute(Arb.string().orNull().bind())
        .build()
}

val kotlinRecordArb = arbitrary {
    KotlinRecord(
        partitionKeyArb.bind(),
        sortKeyArb.bind(),
        shortString.orNull().bind(),
    )
}
