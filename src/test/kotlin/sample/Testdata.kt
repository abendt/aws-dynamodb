package sample

import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.orNull
import io.kotest.property.arbitrary.set
import io.kotest.property.arbitrary.string
import io.kotest.property.arbitrary.uuid

private val shortString = Arb.string(1..10)

private fun nestedGen(level: Int): Arb<Nested> =
    arbitrary {
        Nested().apply {
            stringAttribute = shortString.bind()
            nestedList = if (level == 0) emptyList() else Arb.list(nestedGen(level - 1), 0..3).bind()
        }
    }

val javaRecordArb = arbitrary {
    JavaRecord().apply {
        // partition key may not be empty
        partitionKey = Arb.uuid().map { it.toString() }.bind()
        sortKey = Arb.int().bind()

        stringAttribute = shortString.orNull().bind()

        // lists may be empty
        stringList = Arb.list(shortString, 0..3).bind()

        // map keys may not be empty
        stringStringMap = Arb.map(Arb.string(1..3), Arb.string(), maxSize = 3).bind()

        // set may not be empty
        stringSet = Arb.set(shortString, 1..3).bind()

        // lists may be empty
        nestedList = Arb.list(nestedGen(3), 0..3).bind()

        // map keys may not be empty
        nestedMap = Arb.map(shortString, nestedGen(3), maxSize = 3).bind()
    }
}

val lombokRecordArb = arbitrary {
    LombokRecord.builder()
        // partition key may not be empty
        .partitionKey(Arb.uuid().map { it.toString() }.bind())
        .sortKey(Arb.int().bind())
        .stringAttribute(Arb.string().orNull().bind())
        .build()
}

val kotlinRecordArb = arbitrary {
    KotlinRecord(Arb.uuid().map { it.toString() }.bind(), Arb.int().bind(), shortString.orNull().bind())
}
