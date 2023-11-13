package complex

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.arbitrary.next
import io.kotest.property.checkAll
import io.kotest.property.random

class TestdataSpec : StringSpec({

    "java record arb is deterministic" {
        checkAll<Long> { seed ->
            val rs1 = seed.random()
            val rs2 = seed.random()

            val a = aJavaRecord.next(rs1)
            val b = aJavaRecord.next(rs2)

            a shouldBe b
        }
    }

    "lombok value record arb is deterministic" {
        checkAll<Long> { seed ->
            val rs1 = seed.random()
            val rs2 = seed.random()

            val a = anImmutableLombokRecord.next(rs1)
            val b = anImmutableLombokRecord.next(rs2)

            a shouldBe b
        }
    }
})
