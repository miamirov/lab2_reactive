import org.bson.Document

data class User(
    val id: String,
    val currency: Currency,
) {
    fun toDocument(): Document = Document("id", id)
        .append("currency", currency.toString())
}

fun Document.toUser() = User(
    getString("id"),
    getString("currency").toCurrency(),
)
