package sh.scrap.scraplet;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsync;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.Table;
import io.vertx.core.Vertx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import sh.scrap.scraplet.verticle.ManagerVerticle;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class Application {

    @Autowired ManagerVerticle manager;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }

    @PostConstruct
    public void deployVerticle() {
        Vertx.vertx().deployVerticle(manager);
    }

    @Bean AmazonDynamoDBAsync dynamoDB() {
        AmazonDynamoDBAsyncClient client = new AmazonDynamoDBAsyncClient();
        client.setRegion(Region.getRegion(Regions.US_WEST_2));
        return client;
    }


    /*@Bean Ignite ignite(IgniteConfiguration config) {
        return Ignition.start(config);
    }

    @Bean IgniteConfiguration config(TcpDiscoveryIpFinder ipFinder, AddressResolver addressResolver) {
        IgniteConfiguration config = new IgniteConfiguration();
        TcpDiscoverySpi tcpDiscoverySpi = new TcpDiscoverySpi();
        tcpDiscoverySpi.setIpFinder(ipFinder);
        config.setDiscoverySpi(tcpDiscoverySpi);
        TcpCommunicationSpi communicationSpi = new TcpCommunicationSpi();
        communicationSpi.setAddressResolver(addressResolver);
        config.setCommunicationSpi(communicationSpi);
        config.setAddressResolver(addressResolver);
        return config;
    }

    @Bean @Profile("development")
    TcpDiscoveryIpFinder multicastIpFinder() {
        return new TcpDiscoveryMulticastIpFinder();
    }

    @Bean @Profile("production")
    TcpDiscoveryIpFinder awsIpFinder(
            @Value("${aws.accessKeyId}") String accessKeyId,
            @Value("${aws.secretAccessKey}") String secretAccessKey) {
        AWSCredentialsProvider provider = new InstanceProfileCredentialsProvider(false);
        TcpDiscoveryS3IpFinder s3IpFinder = new TcpDiscoveryS3IpFinder();
        s3IpFinder.setAwsCredentials(new BasicAWSCredentials(accessKeyId, secretAccessKey));
        s3IpFinder.setBucketName("scrap.sh-cluster");
        return s3IpFinder;
    }

    @Bean @Profile("development")
    AddressResolver localAddressResolver() {
        return Collections::singletonList;
    }

    @Bean @Profile("production")
    AddressResolver awsAddressResolver() throws UnknownHostException {
        InetAddress privateIpAddress = InetAddress.getByName(EC2MetadataUtils.getPrivateIpAddress());
        return (addr) -> singletonList(new InetSocketAddress(privateIpAddress, addr.getPort()));
    }



    @Bean IgniteCache<DataScrapperCacheKey, DataScrapperBuilder> builderCache(Ignite ignite,
            CacheConfiguration<DataScrapperCacheKey, DataScrapperBuilder> builderCacheConfig) {
        return ignite.getOrCreateCache(builderCacheConfig);
    }

    @Bean CacheConfiguration<DataScrapperCacheKey, DataScrapperBuilder> builderCacheConfig() {
        CacheConfiguration<DataScrapperCacheKey, DataScrapperBuilder> config = new CacheConfiguration<>();
        config.setName("scraplet");
        config.setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL);
        config.setReadThrough(true);
        config.setCacheStoreFactory(DataScrapperCacheStore::new);
        config.setCacheMode(CacheMode.PARTITIONED);
        config.setBackups(1);
        return config;
    }*/
}
