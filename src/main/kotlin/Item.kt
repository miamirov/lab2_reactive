import org.bson.Document

data class Item(
    val id: String,
    val value: Double,
    val currency: Currency,
) {
    fun toDocument(): Document = Document("id", id)
        .append("value", value)
        .append("currency", currency.toString())

    fun toOtherCur(other: Currency): Item =
        Item(id, value * other.amount / this.currency.amount, other)
}

fun Document.toItem(): Item = Item(
    getString("id"),
    getDouble("value"),
    getString("currency").toCurrency(),
)
