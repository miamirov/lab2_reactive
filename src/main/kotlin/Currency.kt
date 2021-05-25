enum class Currency(val amount: Double) {
    EUR(80.0),
    RUB(1.0),
    USD(70.0);

    override fun toString() = when (this) {
        EUR -> "EUR"
        RUB -> "RUB"
        USD -> "USD"
    }

}

fun String.toCurrency() = when (this) {
    "EUR" -> Currency.EUR
    "RUB" -> Currency.RUB
    "USD" -> Currency.USD
    else -> error("Unknown currency")
}
