package com.example

import com.example.data.model.PrintProductId
import com.example.data.model.PrintSideOption
import com.example.ui.CatalogSelectionState
import org.junit.Assert.assertEquals
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun visiting_card_pricing_matches_catalog() {
        val state250 = CatalogSelectionState(
            activeProductId = PrintProductId.VISITING_CARD,
            visitingCardOptionId = "vc_250_gsm"
        ).toSelectedOrder()
        assertEquals(500, state250.priceInr)

        val stateNt = CatalogSelectionState(
            activeProductId = PrintProductId.VISITING_CARD,
            visitingCardOptionId = "vc_nt"
        ).toSelectedOrder()
        assertEquals(700, stateNt.priceInr)

        val state350 = CatalogSelectionState(
            activeProductId = PrintProductId.VISITING_CARD,
            visitingCardOptionId = "vc_350_gsm"
        ).toSelectedOrder()
        assertEquals(1000, state350.priceInr)
    }

    @Test
    fun pamphlet_pricing_matches_all_size_and_side_combinations() {
        assertEquals(
            1000,
            CatalogSelectionState(
                activeProductId = PrintProductId.PAMPHLET,
                pamphletSizeId = "pamphlet_a5_1000",
                pamphletSide = PrintSideOption.FRONT_ONLY
            ).toSelectedOrder().priceInr
        )
        assertEquals(
            1400,
            CatalogSelectionState(
                activeProductId = PrintProductId.PAMPHLET,
                pamphletSizeId = "pamphlet_a5_1000",
                pamphletSide = PrintSideOption.FRONT_BACK
            ).toSelectedOrder().priceInr
        )
        assertEquals(
            1800,
            CatalogSelectionState(
                activeProductId = PrintProductId.PAMPHLET,
                pamphletSizeId = "pamphlet_a5_2000",
                pamphletSide = PrintSideOption.FRONT_ONLY
            ).toSelectedOrder().priceInr
        )
        assertEquals(
            2200,
            CatalogSelectionState(
                activeProductId = PrintProductId.PAMPHLET,
                pamphletSizeId = "pamphlet_a5_2000",
                pamphletSide = PrintSideOption.FRONT_BACK
            ).toSelectedOrder().priceInr
        )
        assertEquals(
            1850,
            CatalogSelectionState(
                activeProductId = PrintProductId.PAMPHLET,
                pamphletSizeId = "pamphlet_a4_1000",
                pamphletSide = PrintSideOption.FRONT_ONLY
            ).toSelectedOrder().priceInr
        )
        assertEquals(
            2050,
            CatalogSelectionState(
                activeProductId = PrintProductId.PAMPHLET,
                pamphletSizeId = "pamphlet_a4_1000",
                pamphletSide = PrintSideOption.FRONT_BACK
            ).toSelectedOrder().priceInr
        )
    }

    @Test
    fun letterhead_and_envelope_pricing_match_catalog() {
        assertEquals(
            1500,
            CatalogSelectionState(
                activeProductId = PrintProductId.LETTERHEAD,
                letterheadOptionId = "lh_90_gsm"
            ).toSelectedOrder().priceInr
        )
        assertEquals(
            1800,
            CatalogSelectionState(
                activeProductId = PrintProductId.ENVELOPE,
                envelopeOptionId = "env_9x4"
            ).toSelectedOrder().priceInr
        )
        assertEquals(
            2550,
            CatalogSelectionState(
                activeProductId = PrintProductId.ENVELOPE,
                envelopeOptionId = "env_6x8"
            ).toSelectedOrder().priceInr
        )
    }
}
