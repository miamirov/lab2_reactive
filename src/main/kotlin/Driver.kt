import com.mongodb.client.model.Filters
import com.mongodb.rx.client.MongoClient
import com.mongodb.rx.client.MongoClients
import com.mongodb.rx.client.MongoDatabase
import com.mongodb.rx.client.Success
import org.bson.Document
import rx.Observable

object Driver {

    private val client: MongoClient = MongoClients.create()
    private val db: MongoDatabase = client.getDatabase("db")

    fun addUser(user: User): Observable<Success> = db
        .getCollection("users")
        .insertOne(user.toDocument())

    fun addItem(product: Item): Observable<Success> = db
        .getCollection("items")
        .insertOne(product.toDocument())

    fun getUserById(id: String): Observable<User> = db
        .getCollection("users")
        .find(Filters.eq("id", id))
        .toObservable()
        .map(Document::toUser)

    fun getItems(): Observable<Item> = db
        .getCollection("items")
        .find()
        .toObservable()
        .map(Document::toItem)
}
