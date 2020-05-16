package sample.Configuration;


import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

@Configuration
public class MongoConfiguration extends AbstractMongoClientConfiguration {
    private MongoClient mongoClient = MongoClients.create(
            new ConnectionString(
                    "mongodb://admin:admin@cluster0-shard-00-00-gxsls.mongodb.net:27017,cluster0-shard-00-01-gxsls.mongodb.net:27017,cluster0-shard-00-02-gxsls.mongodb.net:27017/test?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin&retryWrites=true&w=majority"
            ));
    private MongoDatabase database = mongoClient.getDatabase("mydatabase");

    @Override
    public MongoClient mongoClient() {
        return mongoClient;
    }

    @Override
    protected String getDatabaseName() {
        return database.getName();
    }
}