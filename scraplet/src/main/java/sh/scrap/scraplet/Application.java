package sh.scrap.scraplet;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsync;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.util.EC2MetadataUtils;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.metrics.MetricsOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Async;
import sh.scrap.scraplet.verticle.ManagerVerticle;

import javax.annotation.PostConstruct;
import java.util.concurrent.Future;

@SpringBootApplication
public class Application {

    @Autowired ManagerVerticle manager;
    @Autowired Future<Vertx> vertx;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }

    @PostConstruct
    public void deployVerticle() throws Exception {
        vertx.get().deployVerticle(manager);
    }

    @Bean AmazonDynamoDBAsync dynamoDB() {
        AmazonDynamoDBAsyncClient client = new AmazonDynamoDBAsyncClient();
        client.setRegion(Region.getRegion(Regions.US_WEST_2));
        return client;
    }

    @Bean Future<Vertx> vertx(VertxOptions options) {
        if (options.isClustered()) {
            SettableFuture<Vertx> future = SettableFuture.create();
            Vertx.clusteredVertx(options, (create) -> {
                if (create.failed())
                    future.setException(create.cause());
                else
                    future.set(create.result());
            });
            return future;
        }
        return Futures.immediateFuture(Vertx.vertx(options));
    }

    @Bean @Profile("development")
    VertxOptions developmentOptions() {
        return new VertxOptions();
    }

    @Bean @Profile("production")
    VertxOptions productionOptions() {
        return new VertxOptions()
                .setClustered(true)
                .setHAEnabled(true);
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
