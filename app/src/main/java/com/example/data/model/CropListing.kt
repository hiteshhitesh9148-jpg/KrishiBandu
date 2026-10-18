package com.example.data.model

enum class StockStatus(val kannadaLabel: String, val englishLabel: String) {
    IN_STOCK("ಲಭ್ಯವಿದೆ", "Ready in Stock"),
    LOW_STOCK("ಕಡಿಮೆ ದಾಸ್ತಾನು", "Low Stock"),
    COMING_SOON("ಶೀಘ್ರದಲ್ಲಿ ಲಭ್ಯ", "In 7-10 Days"),
    SOLD_OUT("ಖಾಲಿಯಾಗಿದೆ", "Direct Sold Out")
}

enum class FarmingMethod(val kannada: String, val english: String) {
    ORGANIC("ಸಾವಯವ", "Certified Organic"),
    NATURAL("ನೈಸರ್ಗಿಕ ಕೃಷಿ", "ZBNF / Natural"),
    CONVENTIONAL("ಸಾಂಪ್ರದಾಯಿಕ", "Conventional")
}

enum class CropCategory(val kannada: String, val english: String, val iconName: String) {
    GRAINS("ಧಾನ್ಯಗಳು", "Grains", "grain"),
    PULSES("ಬೇಳೆಕಾಳುಗಳು", "Pulses", "spa"),
    VEGETABLES("ತರಕಾರಿಗಳು", "Vegetables", "nutrition"),
    CASH_CROPS("ತೆಂಗು-ಅಡಿಕೆ", "Cash Crops", "yard"),
    FRUITS("ಹಣ್ಣುಗಳು", "Fruits", "eco")
}

data class CropListing(
    val id: String,
    val nameKannada: String,
    val nameEnglish: String,
    val variety: String,
    val price: Int,
    val unitKannada: String,
    val unitEnglish: String,
    val quantity: Int,
    val quantityUnit: String,
    val inquiriesCount: Int,
    val stockStatus: StockStatus,
    val imageUrl: String,
    val farmingMethod: FarmingMethod = FarmingMethod.ORGANIC,
    val mandiPrice: Int = 0,
    val mandiAdvantageText: String? = null,
    val harvestDate: String = "12 Nov 2024",
    val location: String = "ಶಿವಳ್ಳಿ ಗ್ರಾಮ, ಮಂಡ್ಯ (Shivalli, Mandya)"
)

data class BuyerInquiry(
    val id: String,
    val buyerNameKannada: String,
    val buyerNameEnglish: String,
    val location: String,
    val timeAgo: String,
    val message: String,
    val cropName: String,
    val avatarInitial: String,
    val phoneNumber: String = "+91 98450 12345"
)

data class VehicleOption(
    val id: String,
    val nameKannada: String,
    val nameEnglish: String,
    val capacity: String,
    val ratePerKm: Int,
    val iconType: String
)

data class MandiComparison(
    val cropKannada: String,
    val cropEnglish: String,
    val mandiRateText: String,
    val directRateText: String,
    val profitPercentageText: String,
    val isMassiveProfit: Boolean = false
)
