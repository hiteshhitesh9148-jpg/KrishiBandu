package com.example.data.repository

import com.example.data.model.BuyerInquiry
import com.example.data.model.CropCategory
import com.example.data.model.CropListing
import com.example.data.model.FarmingMethod
import com.example.data.model.MandiComparison
import com.example.data.model.StockStatus
import com.example.data.model.VehicleOption
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class KrishiRepository {

    companion object {
        const val KRISHI_LOGO_URL = "https://lh3.googleusercontent.com/aida/AEtjO1X-n6ezsINmrcW5s1Py0IkPzFsI7NQ6aZwG5hC8NENTRvfrgjk0NpNta8L4qA_jjptCuGhqHxpM3ORvCA-gZIBtZFmOVkKOH6I2uWVvYe9i6E5kCfJli-qES9lnDCbzCzgvaCKS59bAqPCkLtrStQewu2am2SH_0nbOSXw-tgqNtTiEYb8LaJiexNzxEj9xxt30B1MGaMaLSWyE7HtK-2fCvIG_lvT5b9EDCAtAh0WiexclNRraGWLxxNEu"
        const val FARMER_AVATAR_URL = "https://lh3.googleusercontent.com/aida-public/AB6AXuAVHH1HUAJ9c2qAMRk9IPkO7TgFTwQm9-vUf0KMl8MwCbzThVxjbOUa_eXhTpo53LEzKxme9eRr00OtFBU30o--xBLmVehD1RlpuLTSQVVqS26WbpwarmFIfteScICCuteoi7oG3keTeufeEGGtlGwv8uKBL4_yH4SAeizJLqFKGxvhxSzeY4Ey76CnPxvHfWvp1yjUJLZ-W1v4dJquC6ogpfW3EW9eSv7mP6hxqNXagu5GWxgR7N8vuw"
        const val RAGI_IMG_URL = "https://lh3.googleusercontent.com/aida-public/AB6AXuBnaizZNMFOze02AuZA-nRs1CrnMBNKDAv6ZPvDXDu-3z1LDCnwoKjjD-j2BCbnH7X7SzoKJgMLwNQLGweEgJNTfAlUg3jDb0Mx5Gi-DFY2oQkN7EBBNQj8yZJ4H29ka_8Dxs5cY2YzZM_MIBE-nnKwtJot9SJiXYZ8KduxhMQc3cZoD9YD1mKmLYsg-p7iwlTSmNZaGbQKcLRtH9Jv5ZjZnZCYBOmSSFcWmssDQxg3m-2Zn2QlMfI2eQ"
        const val PADDY_IMG_URL = "https://lh3.googleusercontent.com/aida-public/AB6AXuDesMV5oJFJxh480e17l1gIPk59_y5cbX2So2eHWT8U66iBi5KeJAWoibSLkRTESr4SxURsbsnagbNizflqGnFbx08CKBJ6wAJYvXkpQdkcx21QfEFXBHtsBSimF7ulMX_yAApqDFa54zkuqVtaOjAMtif1IvG9Dg89bmXwsWEHjhg2p4cVEl059j2qLw-iErrqTEeeQYotLxhbCojsjrRU7o-l2Gy5_yJfy1pZ0Ou20x-tkXYxm-Fq7g"
        const val TOMATO_IMG_URL = "https://lh3.googleusercontent.com/aida-public/AB6AXuCpWlYFEbdG2kYolThYkcKWm3ZZtYbOcCsvih1YBcotgtMrTQpArXvKlJBQ-3BOXQQuqZHMENqToZJetcevdAp9qdj0SWyXbwKnlZgNVOpM0dH511DoXTpLWpGhmBxaAxPetmORTpekLudEI4v61bTne57QRiTrUbptaUTqwXjIffpz4eAxeo3ejTFRN1bejmQ4lME0B8XYjoJdrBbG24Hspo1P0G2BBBYVgpM0G0clTEaQX1mIQ_WgFg"
        const val SACKS_PHOTO_URL = "https://lh3.googleusercontent.com/aida-public/AB6AXuAyIhjUSQQm74ARujbj_V64CajffRCqU0KgLooajQjV0jyrELtHJWXj8Vl6UHdI_oOYG_sqWwA96GmhOUkydkqwHJwzMUuOljyqY_3Tj1v49qcbwAYA4ANrizuGVNby0TTYkKpra--55J6b7m8xe_W2yfd7vCDKlnhrjoBuxtccAhsVbJx9J1cBgREIicl1_NWyaew47fG7tZpviOgQ3RjExiCjCZXNj2BRjcK8GKLtfiRD4WxO56lEUw"
        const val HANDS_PHOTO_URL = "https://lh3.googleusercontent.com/aida-public/AB6AXuC_dpiU9UWkHd31buNML-8f-vP_A2aaNIuj7YfwUVr9s6JIpCTa6kaQTHrRJ1VuCji4d5XSKT4xoV2z0DXveAMxavKtyyHoaZW1S4-w-7ixyyXMp6-nnLP_N3agn2BT0rJqxzd5oaQ_l6fSgWzTlR9BsZ0hMYt_2WL_dEVZHVopnrG_DL5zuWhyuia9m8PKBskN2YwvVB8w4araZmFSROD0AccvgE_HWys-A6ca_wjrocnI2R6EZOkZ5w"
        const val MAP_PREVIEW_URL = "https://lh3.googleusercontent.com/aida-public/AB6AXuAUHMQp0LkTfC4vClHJjOQJnBmXVgk8sjibx0miCbZFaE7rHMz7l8NGkKP0SBMfEl7NSKQvpjz3I_e4bRQbPeIkMhK2qOrzmRkq_KP3ocutfZ6INdFf1J_QuPDrVtO8I2ni0jTdWCq8Ha7Rj6CTYamXNS8Glr0TE6BZ79OpjaGn4I-W_nl5zOQW_JV5chy0Jpv0gj4eOGtNz6WWKUk6NW2QN4JkL8wVjJg_kSlf8WnA8jx4ks0rMwEt0A"
    }

    private val _crops = MutableStateFlow<List<CropListing>>(
        listOf(
            CropListing(
                id = "1",
                nameKannada = "ರಾಗಿ",
                nameEnglish = "Finger Millet",
                variety = "MR-1 ಸಾವಯವ ತಳಿ • Fresh Harvest",
                price = 3400,
                unitKannada = "ಕ್ವಿಂಟಾಲ್",
                unitEnglish = "Quintal",
                quantity = 45,
                quantityUnit = "ಕ್ವಿಂಟಾಲ್ (Qtl)",
                inquiriesCount = 7,
                stockStatus = StockStatus.IN_STOCK,
                imageUrl = RAGI_IMG_URL,
                farmingMethod = FarmingMethod.ORGANIC,
                mandiPrice = 3150,
                mandiAdvantageText = "ಮಂಡಿ ದರಕ್ಕಿಂತ +₹250 ಹೆಚ್ಚು ಸಿಗುತ್ತಿದೆ (vs APMC ₹3,150) • +8% ಲಾಭ",
                harvestDate = "12 Nov 2024"
            ),
            CropListing(
                id = "2",
                nameKannada = "ಸೋನಾ ಮಸೂರಿ ಭತ್ತ",
                nameEnglish = "Sona Masuri Paddy",
                variety = "Sona Masuri Paddy • 75kg ಚೀಲ",
                price = 2250,
                unitKannada = "75kg ಚೀಲ",
                unitEnglish = "Bag",
                quantity = 80,
                quantityUnit = "ಚೀಲಗಳು (Bags)",
                inquiriesCount = 11,
                stockStatus = StockStatus.LOW_STOCK,
                imageUrl = PADDY_IMG_URL,
                farmingMethod = FarmingMethod.ORGANIC,
                mandiPrice = 2050,
                mandiAdvantageText = "ಮಂಡಿ ದರಕ್ಕಿಂತ +₹200 ಹೆಚ್ಚು • +10% ಲಾಭ",
                harvestDate = "05 Nov 2024"
            ),
            CropListing(
                id = "3",
                nameKannada = "ನಾಟಿ ಟೊಮೆಟೊ",
                nameEnglish = "Country Tomato",
                variety = "Direct Garden Pick • 15 Crates",
                price = 32,
                unitKannada = "ಕೆ.ಜಿ",
                unitEnglish = "kg",
                quantity = 0,
                quantityUnit = "ಕ್ರೇಟ್ (Crates)",
                inquiriesCount = 14,
                stockStatus = StockStatus.SOLD_OUT,
                imageUrl = TOMATO_IMG_URL,
                farmingMethod = FarmingMethod.NATURAL,
                mandiPrice = 22,
                mandiAdvantageText = "ನೇರ ಮಾರುಕಟ್ಟೆ ಮಾರಾಟದಿಂದ +45% ಲಾಭ ದಾಖಲಾಗಿದೆ",
                harvestDate = "28 Oct 2024"
            )
        )
    )
    val crops: StateFlow<List<CropListing>> = _crops.asStateFlow()

    private val _inquiries = MutableStateFlow<List<BuyerInquiry>>(
        listOf(
            BuyerInquiry(
                id = "inq1",
                buyerNameKannada = "ಸುರೇಶ್ ಕುಮಾರ್",
                buyerNameEnglish = "Suresh Kumar",
                location = "ಜೆಪಿ ನಗರ, ಬೆಂಗಳೂರು (JP Nagar, Blr)",
                timeAgo = "10 ನಿಮಿಷದ ಹಿಂದೆ",
                message = "\"5 ಕ್ವಿಂಟಾಲ್ ರಾಗಿ ಬೇಕು. ಬೆಂಗಳೂರಿಗೆ ಸರಕು ಸಾಗಾಟ ವ್ಯವಸ್ಥೆ ಮಾಡಿಕೊಡಲು ಸಾಧ್ಯವೇ? (Looking for 5 quintals Ragi)\"",
                cropName = "ಸಾವಯವ ರಾಗಿ (MR-1)",
                avatarInitial = "S",
                phoneNumber = "+91 98450 12345"
            ),
            BuyerInquiry(
                id = "inq2",
                buyerNameKannada = "ಅನಿತಾ ರಾವ್",
                buyerNameEnglish = "Anita Rao",
                location = "ವಿಜಯನಗರ, ಮೈಸೂರು (Mysuru)",
                timeAgo = "1 ಗಂಟೆ ಹಿಂದೆ",
                message = "\"2 ಚೀಲ ಸೋನಾ ಮಸೂರಿ ಅಕ್ಕಿ ಬೇಕಾಗಿದೆ. ಮನೆಗೆ ತಲುಪಿಸಲು ಎಷ್ಟು ವೆಚ್ಚವಾಗುತ್ತದೆ? (Need 2 bags Sona Masuri)\"",
                cropName = "ಸೋನಾ ಮಸೂರಿ ಭತ್ತ",
                avatarInitial = "A",
                phoneNumber = "+91 94801 88765"
            ),
            BuyerInquiry(
                id = "inq3",
                buyerNameKannada = "ಮಹೇಶ್ ಗೌಡ",
                buyerNameEnglish = "Mahesh Gowda",
                location = "ಮದ್ದೂರು, ಮಂಡ್ಯ (Maddur)",
                timeAgo = "3 ಗಂಟೆ ಹಿಂದೆ",
                message = "\"ನಾಟಿ ಟೊಮೆಟೊ ಮುಂದಿನ ವಾರ ಕೊಯ್ಲು ಯಾವಾಗ ಲಭ್ಯವಾಗಲಿದೆ? 10 ಕ್ರೇಟ್ ಬೇಕಾಗಿದೆ.\"",
                cropName = "ನಾಟಿ ಟೊಮೆಟೊ (Country)",
                avatarInitial = "M",
                phoneNumber = "+91 99002 33411"
            )
        )
    )
    val inquiries: StateFlow<List<BuyerInquiry>> = _inquiries.asStateFlow()

    val mandiComparisons = listOf(
        MandiComparison(
            cropKannada = "ರಾಗಿ",
            cropEnglish = "Ragi / kg",
            mandiRateText = "ಮಂಡಿ ದರ: ₹31.50/kg",
            directRateText = "ನಿಮ್ಮ ನೇರ ದರ: ₹34.00",
            profitPercentageText = "+8% ನೇರ ಆದಾಯ",
            isMassiveProfit = false
        ),
        MandiComparison(
            cropKannada = "ನಾಟಿ ಟೊಮೆಟೊ",
            cropEnglish = "Tomato / kg",
            mandiRateText = "ಮಂಡಿ ದರ: ₹22.00/kg",
            directRateText = "ನಿಮ್ಮ ನೇರ ದರ: ₹32.00",
            profitPercentageText = "+45% ಭರ್ಜರಿ ಲಾಭ!",
            isMassiveProfit = true
        )
    )

    val vehicleOptions = listOf(
        VehicleOption(
            id = "v1",
            nameKannada = "ಟಾಟಾ ಏಸ್",
            nameEnglish = "Tata Ace",
            capacity = "750 kg ಸಾಮರ್ಥ್ಯ",
            ratePerKm = 14,
            iconType = "local_shipping"
        ),
        VehicleOption(
            id = "v2",
            nameKannada = "ಬೊಲೆರೋ ಪಿಕಪ್",
            nameEnglish = "Bolero Pickup",
            capacity = "1.5 Ton ಸಾಮರ್ಥ್ಯ",
            ratePerKm = 20,
            iconType = "fire_truck"
        ),
        VehicleOption(
            id = "v3",
            nameKannada = "ಮಿನಿ ಟ್ರಕ್",
            nameEnglish = "Mini Truck",
            capacity = "3.0 Ton ಸಾಮರ್ಥ್ಯ",
            ratePerKm = 28,
            iconType = "airport_shuttle"
        )
    )

    fun addCrop(crop: CropListing) {
        _crops.update { current ->
            listOf(crop) + current
        }
    }

    fun updatePrice(cropId: String, newPrice: Int) {
        _crops.update { list ->
            list.map {
                if (it.id == cropId) it.copy(price = newPrice) else it
            }
        }
    }

    fun relistCrop(cropId: String, quantity: Int = 20) {
        _crops.update { list ->
            list.map {
                if (it.id == cropId) it.copy(
                    stockStatus = StockStatus.IN_STOCK,
                    quantity = quantity,
                    quantityUnit = "ಕ್ರೇಟ್ (Crates)"
                ) else it
            }
        }
    }
}
